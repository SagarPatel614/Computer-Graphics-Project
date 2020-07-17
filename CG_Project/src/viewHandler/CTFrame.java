package viewHandler;

import eventHandler.BusProvider;
import eventHandler.Events;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class CTFrame extends JPanel implements MouseMotionListener,MouseListener,KeyListener{

    private BufferedImage bufferedImage;
    float xMousePressed=0,yMousePressed=0;

    public CTFrame() {
        super();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        grabFocus();
    }

    public CTFrame(BufferedImage bufferedImage){
        super(new BorderLayout());
        this.bufferedImage=bufferedImage;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, this);
    }


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        xMousePressed= e.getX();
        yMousePressed= e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // compute the change in x and y
        float xMotion= e.getX()- xMousePressed;
        float yMotion= e.getY()- yMousePressed;
        System.out.println(xMotion+","+ yMotion);
        // calculate rotation and elevation based on xMotion and yMotion
        float rotation = xMotion/10;
        float elevation= yMotion/10;
        // fire event to rotate camera
        BusProvider.getInstance().send(new Events.CameraRotate(rotation,elevation));
        xMousePressed=0;
        yMousePressed=0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                // handle up
                BusProvider.getInstance().send(new Events.KeyUp());
                break;
            case KeyEvent.VK_DOWN:
                // handle down
                BusProvider.getInstance().send(new Events.KeyDown());
                break;
            case KeyEvent.VK_LEFT:
                // handle left
                BusProvider.getInstance().send(new Events.KeyLeft());
                break;
            case KeyEvent.VK_RIGHT :
                // handle right
                BusProvider.getInstance().send(new Events.KeyRight());
                break;
            case KeyEvent.VK_Z :
                // handle right
                BusProvider.getInstance().send(new Events.ZoomIn());
                break;
            case KeyEvent.VK_X :
                // handle right
                BusProvider.getInstance().send(new Events.ZoomOut());
                break;
        }
    }
}
