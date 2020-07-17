package supportLibrary.worldEnvironment;

import supportLibrary.interfaces.PhysicalObject;
import templateModels.Point;
import worldObjects.Polygon;

import worldObjects.Sphere;
import rayHelper.RayConstruction;
import utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * A top level class used for showing a scene which would consist of
 * 1. camera
 * 2. Objects (Sphere and Polygon for now)
 * 3. RayConstruction
 * 4. Position of Light
 */
public class Scene {

    enum  ObjType{
        NO_OBJ,SPHERE,POLYGON
    }
    private static final int NUM_BANDS = 3;
    private Sphere sphere;
    private Polygon polygon;
    private Camera camera;
    private RayConstruction rayConstruction;
    private Point lightPosition;

    public Scene() {
        try {
            /**
             * Objects are read from file in src/object directory and
             * cast into specific object
             */
       /*     Gson gson= new Gson();
            sphere = gson.fromJson(Constants.sphereJson,Sphere.class);//(Sphere) Utility.readFromFile(Sphere.class, "sphere.json");
            polygon = gson.fromJson(Constants.polygonJson,Polygon.class);//(Polygon) Utility.readFromFile(Polygon.class, "polygon.json");
            camera = gson.fromJson(Constants.cameraJson,Camera.class);//(Camera) Utility.readFromFile(Camera.class, "camera.json");
       */ } catch (Exception e) {
            e.printStackTrace();
        }
        // ray is constructed using camera object
        rayConstruction = new RayConstruction(camera);
        // hard coded position of light object (It is hardcoded according to model.h file)
        lightPosition = new Point(-10.0f, 10.0f, 2.0f);

    }

    /**
     * It renders sphere and polygon object in the scen
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void renderFrame() throws FileNotFoundException, UnsupportedEncodingException {
        int rows = Constants.NO_OF_ROWS;
        int columns = Constants.NO_OF_COLS;
        int[][] imageBuffer = new int[rows][columns];
        byte[] imageByteArray= new byte[rows*columns];
        int[] pixels= new int[rows*columns];
        int k=0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Ray is calculated for each point on 3.5 mm film
                Ray ray = rayConstruction.constructRayVector(i, j);
                // Point of intersection of sphere
                Point sphereIntrsctPt = sphere.checkIntersection(ray);
                // Point of intersection of Polygon
                Point polyIntrstPt= polygon.checkIntersection(ray);
                Point intersectPt = null;
                PhysicalObject object;
                if(sphereIntrsctPt==null && polyIntrstPt==null){  // There is no intersection
                    object= null;
                    intersectPt=null;
                }else if(polyIntrstPt==null){ // Ray is intersecting with sphere
                    object= sphere;
                    intersectPt= sphereIntrsctPt;
                }else if(sphereIntrsctPt==null){    // Polygon is intersecting with the ray
                    object= polygon;
                    intersectPt= polyIntrstPt;
                }else if(Point.comparePoints(ray.getP0(),sphereIntrsctPt,polyIntrstPt)){ // calculate which point is nearest, sphere or polygon
                    object=sphere;
                    intersectPt= sphereIntrsctPt;
                }else {
                    object= polygon;
                    intersectPt= polyIntrstPt;
                }

                int value = 0;
                if (intersectPt != null) {
                    // shading value for the point is calculated for a point of intersection
                    value = object.shading(ray, intersectPt, lightPosition);
                }

                imageBuffer[i][j] = value;
                // pixel array to store image it as raw image
                pixels[k++]= value%256;

            }

        }
        saveImageInPng(imageBuffer);
        saveRawImage(imageByteArray);
    }

    /**
     * Saves image in raw format
     * @param imageByteArray pixel array
     * @throws FileNotFoundException
     */
    private void saveRawImage(byte[] imageByteArray) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream("image.raw");

        try {
            fos.write(imageByteArray);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Converts int pixel array to a png image
     * @param arr Pixel Array
     */
    private void saveImageInPng(int[][] arr) {
        int xLength = arr.length;
        int yLength = arr[0].length;
        BufferedImage b = new BufferedImage(yLength, xLength, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yLength; y++) {
                int pixel = arr[y][x];
                pixel= pixel%256;
                // a 32 bit integer is created with the pixel value
                int rgb = 0xff000000 |pixel << 16 | pixel << 8 | pixel;
                b.setRGB(x, y, rgb);
            }
        }

        try {
            // write image to Output.png
            File output = new File( "Output.png");
            ImageIO.write(b, "PNG", output);
            try {
                Desktop dt = Desktop.getDesktop();
                dt.open(output);
            } catch (Exception e) {
                System.out.println("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon imageIcon = new ImageIcon(b);
        JLabel jLabel = new JLabel();
        jLabel.setBounds(0, 0, yLength, xLength);
        jLabel.setIcon(imageIcon);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.add(jLabel);
    }


   /* private void displayBufferedImage(int[] pixels){
        int h= Constants.NO_OF_ROWS;
        int w= Constants.NO_OF_COL;
        int[] matrix = new int[w * h];

// ...manipulate the matrix...

        DataBufferInt buffer = new DataBufferInt(pixels, matrix.length);

        int[] bandMasks = {0xFF0000, 0xFF00, 0xFF, 0xFF000000}; // ARGB (yes, ARGB, as the masks are R, G, B, A always) order
        WritableRaster raster = Raster.createPackedRaster(buffer, w, h, w, bandMasks, null);

        System.out.println("raster: " + raster);

        ColorModel cm = ColorModel.getRGBdefault();
        BufferedImage image = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);

        System.err.println("image: " + image);
        try {
            ImageIO.write(image, "PNG", new File("./src/objects/" + "Rasterway.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
