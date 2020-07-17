package supportLibrary.worldEnvironment;


import templateModels.Point;
import templateModels.Vector;
import utils.Constants;

/**
 * This class represents a ray object
 * A ray is essentially a line. It is also like a "vector" because it has a starting point and
 * points a certain direction
 * The parametric representation for line is PO+ VO*t
 */
public class Ray {

    private Vector V0;      // Ray vector
    private Point P0;       // Point of Origin of ray
    private float t=1;      // a variable to compute different lengths of Ray

    public Ray() {
    }

    public Ray(Vector v0, Point p0) {
        V0 = v0;
        P0 = p0;
    }

    public Vector getV0() {
        return V0;
    }

    public void setV0(Vector v0) {
        V0 = v0;
    }

    public Point getP0() {
        return P0;
    }

    public void setP0(Point p0) {
        P0 = p0;
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    /**
     * Get a Point for a particular value of t
     * @param t0 the value of t for which point is to be calculated
     * @return Point
     */
    public Point getPointWithT(float t0) {
        float x = P0.getX() + t0*V0.getX();
        float y = P0.getY() + t0*V0.getY();
        float z = P0.getZ() + t0*V0.getZ();
        return new Point(x,y,z);
    }

    /**
     * Calculates value of t using ray equation
     * P(t)= P0+ t*VO
     * so for Px(t) we can say
     * tx= (Px(t)-P0x)/V0x
     * @param plane name of plane, could be x, y or z
     * @param value value of the plane, we use value 0 or 127 as this will be plane for now
     * @return value of t on the plane
     */
    public float getTvalueFor(String plane,float value){
        float point = 0;
        float rayVectorComponent = 0;
        // check plane and assign point and rayVectorComponent values corresponding to given plane
        switch (plane){
            case "x":
                point= P0.getX();
                rayVectorComponent= V0.getX();
                break;
            case "y":
                point= P0.getY();
                rayVectorComponent= V0.getY();
                break;
            case "z":
                point= P0.getZ();
                rayVectorComponent= V0.getZ();
                break;

        }

        // use ray equation to value of t
        t= (value-point)/rayVectorComponent;
        return t;
    }

    /**
     * Calculates a value of t for which ray is intersecting a face of the cube
     * @param plane name of plane, could be x, y or z
     * @param value value of the plane, we use value 0 or 127 as this will be plane for now
     * @return
     */
    public float getTPointForPlane(String plane,float value){
        // we use ray equationg for finding value of t
        // this t gives us the value for which ray intersects plane of cube
        float t= getTvalueFor(plane,value);
        float x=0,y=0,z=0;
        // we check whether this t value is for a point which lies within range of our cube
        switch (plane){
            case "x":
                x=value;
                y= P0.getY() + t*V0.getY();
                z= P0.getZ() + t*V0.getZ();
                // checks if the point lies in x face
                if((y>0 && y< Constants.SIZE-1 ) && (z>0 && z < Constants.SIZE-1) ){
                    return  t;
                }
                break;
            case "y":
                y=value;
                x= P0.getX() + t*V0.getX();
                z= P0.getZ() + t*V0.getZ();
                // checks if the point lies in y face
                if((x>0 && x< Constants.SIZE-1 ) && (z>0 && z < Constants.SIZE-1) ){
                    return t;
                }
                break;
            case "z":
                z=value;
                x= P0.getX() + t*V0.getX();
                y= P0.getY() + t*V0.getY();
                // checks if the point lies in z face
                if((x>0 && x< Constants.SIZE-1 ) && (y>0 && y < Constants.SIZE-1) ){
                    return  t;
                }
                break;

        }

        return Float.NEGATIVE_INFINITY;
    }
}
