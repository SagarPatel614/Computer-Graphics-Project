package templateModels;


import utils.Utility;

/**
 * Class represents a vector.
 * It can hold values for x, y & z component.
 * It also provides methods like
 * a.) Determinant of Vector
 * b.) Unit Vector of Vector
 * c.) Cross Product of Vector with another Vector
 * d.) Dot Product of Vector with another Vector
 * e.) Check if vector is orthogonal with another Vector
 */
public class Vector {

    // x,y,z values of vector
    private float x;
    private float y;
    private float z;

    public Vector(){

    }
    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Calculates the determinant of the vector
     * @return value of determinant
     */
    public float getMagnitude(){
        return (float) Math.sqrt(x*x+y*y+z*z);
    }

    /**
     * Calculate the unit vector of the given vector
     * @return Unit Vector for the given Vector
     */
    public Vector getUnitVector(){
        Vector unitVector= new Vector();
        // calculate determinant
        float determinant = getMagnitude();
        // calculate x,y,z for unit vector
        unitVector.x= x/ determinant;
        unitVector.y= y/ determinant;
        unitVector.z= z/ determinant;
        return unitVector;
    }

    /**
     * Calculate the Cross Product of given vector with other vector
     * @param v2 vector with which cross product should be take
     * @return result of cross product
     */
    public Vector crossProduct(Vector v2){
        Vector crossVector= new Vector();
        crossVector.x= this.y*v2.z - this.z*v2.y;
        crossVector.y= this.z*v2.x - this.x*v2.z;
        crossVector.z= this.x*v2.y - this.y*v2.x;
        return crossVector;
    }

    @Override
    public String toString() {
        return fmt(x) + " "+fmt(y)+" "+fmt(z);
    }

    private String fmt(float x) {
        return String.format("%10.3f", x);
    }

    /**
     * Calculate the Dot Product of given vector with other vector
     * @param v2 vector with which Dot product should be take
     * @return result of Dot product
     */
    public float dotProduct(Vector v2){
        //
        return x * v2.x + y * v2.y + z * v2.z;
    }


    /**
     * Calculate the Dot Product of given vector with other Point
     * @param v2 point with which Dot product should be take
     * @return result of Dot product
     */
    public float dotProduct(Point v2){
        //
        return x * v2.getX() + y * v2.getY() + z * v2.getZ();
    }

    public Vector multiply(float magnitude){
        Vector vector=new Vector() ;
        vector.x= x*magnitude;
        vector.y= y*magnitude;
        vector.z= z*magnitude;
        return vector;
    }

    /**
     * Checks if Vector is orthogonal with another vector
     * @param v2 second vector
     * @return true if vector is orthogonal
     */
    public boolean isOrthogonal(Vector v2){
        return dotProduct(v2)==0;
    }

    public float[] getArrayNotation(){
        return new float[]{x,y,z,0f};
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    /**
     * Used for formatting output
     * @param name- vector name
     * @return Formatted output
     */
    public String getString(String name){
        String output= "%1$sx:%2$s  %1$sy:%3$s  %1$sz:%4$s\n";
        output= String.format(output,name, Utility.fmt(x)
                , Utility.fmt(y)
                        , Utility.fmt(z));
        return output;
    }

    /**
     * Method helps in finding Projection of a vector
     * it could be found by finding magnitude along which
     * axis is maximum
     * @return Projection of the Vector
     */
    public Point.Projection getProjectionOfTheVector(){
        if(x>=y && x>=z){
            return Point.Projection.X_AXIS;
        }else if(y>=x && y>=z){
            return Point.Projection.Y_AXIS;
        }else{
            return Point.Projection.Z_AXIS;
        }
    }

    public static Vector floatArrayToVector(float[] arr){
        if(arr.length<3){
            return null;
        }
        return new Vector(arr[0],arr[1],arr[2]);
    }

}
