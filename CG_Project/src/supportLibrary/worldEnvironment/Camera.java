package supportLibrary.worldEnvironment;


import templateModels.Point;
import templateModels.Vector;

/**
 * It represents a camera object and it position
 * Rotation of the object is accomplished by rotating camera around the object.
 * This gives an effect that object is rotated.
 * This is accomplished by rotating the camera around a center point of our object.
 * As the objects  is 128, 128, 128 . We can take center point as (64,64,64)
 * Camera is rotated in spherical motion, making it equivocally distant to the center point
 *
 */
public class Camera {

    private static final float MOTION_VALUE = 10f;  // Constant which define amount of rotation
    private static final float ZOOM_IN = -10;       // Constant which define amount of zoom in
    private static final float ZOOM_OUT = 10;       // Constant which define amount of zoom out

    public enum RotationEvent{
        LEFT, RIGHT, UP, DOWN
    }
    private Point pointVrp;         // Camera's Reference Point
    private Vector vectorVpn;       // Plane Normal Vector
    private Vector vectorVup;       // Camera's Up vector
    private Point objPosition= new Point(64f,64f,64f);      // A point around which camera is rotated
    private float rotation=1f;      // initial rotation
    private float elevation=90f;    // initial elevation
    private float zoomValue=1;
    private Vector prevVUP;
    
    public Camera(Point pointVrp, Vector vectorVpn, Vector vectorVup) {
        this.pointVrp = pointVrp;
        this.vectorVpn = vectorVpn;
        this.vectorVup = vectorVup;
        this.prevVUP= vectorVup;

    }

    public Point getPointVrp() {
        return pointVrp;
    }

    public void setPointVrp(Point pointVrp) {
        this.pointVrp = pointVrp;
    }

    public Vector getVectorVpn() {
        return vectorVpn;
    }

    public void setVectorVpn(Vector vectorVpn) {
        this.vectorVpn = vectorVpn;
    }

    public Vector getVectorVup() {
        return vectorVup;
    }

    public void setVectorVup(Vector vectorVup) {
        this.vectorVup = vectorVup;
    }

    /**
     * Calculates VPN, VUP according to the new VRP (Reference point)
     */
    public void cameraUpdate(){
        // VPN is calculating by subtracting Obj Position and VRP
        this.vectorVpn=pointVrp.minus(objPosition);
        // An arbitrary Vector is calculated to find the VUP
        Vector anotherVector= new Point(63.9f, 64f, 64f).minus(pointVrp);
        // cross product between VPN and the arbitrary Vector helps to compute a Up Vector
        this.vectorVup= anotherVector.crossProduct(vectorVpn).getUnitVector();
        
        if((Math.abs(prevVUP.getY()-vectorVup.getY())>.01 ) && (Math.abs(prevVUP.getY())== Math.abs(vectorVup.getY()))){
            this.vectorVup= vectorVpn.crossProduct(anotherVector).getUnitVector();
        }
        
        vectorVup= vectorVup.getUnitVector();
  //      System.out.println(String.format("x-%s,y- %s,z- %s",vectorVup.getX(),vectorVup.getY(),vectorVup.getZ()));
        prevVUP=vectorVup;
    }

    /**
     * Takes an event and rotate camera according to the event.
     * Spherical Co-ordinate system is used to calculate position of the camera
     * @param event - Takes LEFT, RIGHT, TOP, BOTTOM as input
     */
    public void orbitCamera(RotationEvent event){
        switch (event){
            case RIGHT: rotation+= MOTION_VALUE;

                break;
            case LEFT: rotation-= MOTION_VALUE;

                break;
            case UP:
                elevation+=MOTION_VALUE;
                break;
            case DOWN: elevation-= MOTION_VALUE;
                break;
            default:
        }
        // Calculate new position of camera using equation
        // x= r*Sin(rotation)*sin(elevation)
        // y= r*cos(elevation)
        // x= r*Sin(elevation)*cos(rotation)
        // these lengths added to objPosition
        double z= objPosition.getZ()+ vectorVpn.getMagnitude()*
                Math.sin(toRad(elevation))*Math.cos(toRad(rotation));
        double x= objPosition.getX()+ vectorVpn.getMagnitude()*
                Math.sin(toRad(rotation))*Math.sin(toRad(elevation));
        double y= objPosition.getY()+ vectorVpn.getMagnitude()* Math.cos(toRad(elevation));
        pointVrp.setX((float) x);
        pointVrp.setY((float) y);
        pointVrp.setZ((float) z);

        // refresh camera to update VPN and VUP
        cameraUpdate();

    }


    /**
     * Function used for rotation using Mouse drag or touch drag(For touchscreen laptops)
     * @param rot  rotation angle
     * @param elev elevation angle
     */
    public void rotateCamera(float rot,float elev){
        this.rotation+=rot;
        this.elevation+=elev;
        
        // Calculate new position of camera using equation
        // x= r*Sin(rotation)*sin(elevation)
        // y= r*cos(elevation)
        // x= r*Sin(elevation)*cos(rotation)
        // these lengths added to objPosition
        double z= objPosition.getZ()+ vectorVpn.getMagnitude()*
                Math.sin(toRad(elevation))*Math.cos(toRad(rotation));
        double x= objPosition.getX()+ vectorVpn.getMagnitude()*
                Math.sin(toRad(rotation))*Math.sin(toRad(elevation));
        double y= objPosition.getY()+ vectorVpn.getMagnitude()* Math.cos(toRad(elevation));
        pointVrp.setX((float) x);
        pointVrp.setY((float) y);
        pointVrp.setZ((float) z);
        //System.out.println(String.format("x-%s,y- %s,z- %s",x,y,z));
        // refresh camera to update VPN and VUP
        cameraUpdate();
    }

    public double toRad(double degrees){
        return Math.toRadians(degrees);
    }

    /**
     * Zooms in or out
     * @param isZoom if true then zoom in els zoom out
     */
    public void zoomInOutCamera(boolean isZoom){
        zoomValue= isZoom?ZOOM_IN:ZOOM_OUT;
        // find unit vector directed towards object
        Vector unitVector = vectorVpn.getUnitVector();
        // update zoom value
        Vector zoomVector = unitVector.multiply(zoomValue);
        // re calculate Reference Point using the zoom vector
        pointVrp= pointVrp.addVector(zoomVector);
        cameraUpdate();
    }
}