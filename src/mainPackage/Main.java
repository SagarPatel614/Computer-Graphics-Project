package mainPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import eventHandler.BusProvider;
import eventHandler.Events;
import templateModels.Point;
import templateModels.Vector;
import supportLibrary.worldEnvironment.Camera;
import utils.Constants;
import utils.Utility;
import viewHandler.CTFrame;
import supportLibrary.functions.VolumeRendering;
import rx.Subscriber;



public class Main {
	
	public static void main(String[] args) {

        // load CT DATA from the smallHead.den file
        float[][][] ctData= Utility.loadCTData();
        
        // VRP,VPN and VUP is loaded from Constants
        // These values can be updated in Constants.java
        Point vrp= Point.floatArrayToPoint(Constants.VRP);
        Vector vpn= Vector.floatArrayToVector(Constants.VPN);
        Vector vup= Vector.floatArrayToVector(Constants.VUP);
        
        // VRP,VPN and VUP is used to initialize the camera
        final Camera camera= new Camera(vrp,vpn,vup);
        
        // Light vector is initialized from the Constants
        Vector lightVector = Vector.floatArrayToVector(Constants.Light);
        
        // Volume Rendering class is initialized using CT data, camera and light vector
        final VolumeRendering volumeRendering= new VolumeRendering(ctData,camera,lightVector);
        
        // A Frame is defined
        final CTFrame ctFrame= new CTFrame();
        
        // camera is refreshed
        camera.cameraUpdate();
        
        // a frame is rendered and saved in buffered image
        BufferedImage bufferedImage = volumeRendering.renderFrame();

        ctFrame.setBufferedImage(bufferedImage);
        JFrame jFrame= new JFrame();
        jFrame.setVisible(true);
        
        // frame is initialized to our camera size
        jFrame.setSize(Constants.NO_OF_COLS,Constants.NO_OF_ROWS);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.add(ctFrame,BorderLayout.CENTER);
        jFrame.addKeyListener(ctFrame);
        ctFrame.repaint();
        
        // A help message is initialized to explain the controls
        JOptionPane.showMessageDialog(jFrame,  Constants.help,"Controls",
                JOptionPane.INFORMATION_MESSAGE);

        // An event based system created which will listen to the events and then take
        // appropriate action
        BusProvider.getInstance().toObserverable().subscribe(new Subscriber<Object>() {
            long prevTime= System.currentTimeMillis();

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                if(o instanceof Events.KeyRight){
                    camera.orbitCamera(Camera.RotationEvent.RIGHT);

                }else if(o instanceof Events.KeyLeft){
                    camera.orbitCamera(Camera.RotationEvent.LEFT);

                }else if(o instanceof Events.KeyUp){
                    camera.orbitCamera(Camera.RotationEvent.UP);

                }else if(o instanceof Events.KeyDown){
                    camera.orbitCamera(Camera.RotationEvent.DOWN);

                }else if(o instanceof Events.ZoomIn){
                    camera.zoomInOutCamera(true);

                }else if(o instanceof Events.ZoomOut){
                    camera.zoomInOutCamera(false);

                }else if(o instanceof Events.CameraRotate){
                    Events.CameraRotate cameraRotate= (Events.CameraRotate) o;
                    camera.rotateCamera(cameraRotate.rotation,cameraRotate.elevation);
                }
                
                // update the frame according to new camera values
                prevTime= System.currentTimeMillis();
                BufferedImage bi = volumeRendering.renderFrame();
                
                System.out.println("Frame duration is "+ (System.currentTimeMillis()- prevTime)/600 + "s");
                
                prevTime= System.currentTimeMillis();
                ctFrame.setBufferedImage(bi);
            }
        });
    }
}