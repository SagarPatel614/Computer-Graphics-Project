package templateModels;


public class Point {




    /**
     * Enum for different projection states
     */
    public enum Projection{
        X_AXIS,Y_AXIS,Z_AXIS
    }

    private float x;
    private float y;
    private float z;

    public Point(){

    }

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }
    /**
     * Computes 4x4 matrix of the point
     * @param isInverse if true- inverse of translation matrix is returned
     * @return
     */
    public float[][] getTranslationMatrix(boolean isInverse){
        // A multplier is created using isInverse
        // if isInverse is true then multiplier is 1 otherwise it is -1
        int multplier=isInverse?1:-1;
        float[][] translationMatrix = new float[4][];
        translationMatrix[0]= new float[]{1f,0f,0f,multplier*x};
        translationMatrix[1]= new float[]{0f,1f,0f,multplier*y};
        translationMatrix[2]= new float[]{0f,0f,1f,multplier*z};
        translationMatrix[3]=new float[]{0f,0f,0f,1f};
        return translationMatrix;
    }

    public float[][] getPointinMatrix(){
        float[][] point = new float[4][1];
        point[0][0]= x;
        point[1][0]= y;
        point[2][0]= z;
        point[3][0]= 1;
        return point;
    }

    /**
     * Convert a homogeneous co-ordinate system to x,y,z co-ordinate
     * @param matrix homogeneous co-ordinate point
     * @return A point
     */
    public static Point convertPointMatrixtoPoint(float[][] matrix){
        Point point = new Point();
        if (matrix.length < 3) {
            return null;
        }
        point.x= matrix[0][0];
        point.y= matrix[1][0];
        point.z= matrix[2][0];
        return point;
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

    public Vector minus(Point point) {
        return new Vector(x-point.getX(), y-point.getY(),z-point.getZ());
    }

    /**
     * Convert a point in <b>2d plane</b> according to the Projection enum
     * for example if Projection is along x axis
     * we will drop x point and resulting point will be (y,z)
     * @param projection Projection name
     * @return A point in 2d plane
     */
    public Point getProjectedPointAlong(Projection projection){
        switch (projection){
            case X_AXIS: // As projection is along x-axis
                         // we would drop x-axis
                            return new Point(y,z);
            case Y_AXIS: // As projection is along y-axis
                         // we would drop y-axis
                            return new Point(x,z);
            case Z_AXIS: // As projection is along z-axis
                         // we would drop Z-axis
                            return new Point(x,y);
            default:
                // This step would never be called
                return new Point(0,0);
        }
    }

    /**
     * Translate point x
     * @param xTranslate translation amount
     */
    public void translateX(float xTranslate){
        this.x+=xTranslate;
    }

    /**
     * Translate point y
     * @param yTranslate
     */
    public void translateY(float yTranslate){
        this.y+=yTranslate;
    }

    /**
     * Translate point z
     * @param zTranslate
     */
    public void translateZ(float zTranslate){
        this.z+=zTranslate;
    }

    /**
     * If first pt is near to origin then it will return true else false
     *
     * @param origin Origin from where we have to check
     * @param firstPt First Point
     * @param secondPt Second Point
     * @return If first pt is near to origin then it will return true else false
     */
    public static boolean comparePoints(Point origin, Point firstPt, Point secondPt) {
        Vector firstVector= firstPt.minus(origin);
        Vector secondVector= secondPt.minus(origin);
        return firstVector.getMagnitude()<secondVector.getMagnitude();
    }


    public static Point floatArrayToPoint(float[] arr) {
        if(arr.length<3){
            return null;
        }
        return new Point(arr[0],arr[1],arr[2]);
    }

    public void normalizeX(){
        x-=.2f;
    }

    public void normalizeY(){
        y-=.2f;
    }
    public void normalizeZ(){
        z-=.2f;
    }

    public void normalize() {
        if(x>=127){
            normalizeX();
        }

        if(y>=127){
            normalizeY();
        }
        if(z>=127){
            normalizeZ();
        }
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s",x,y,z);
    }

    public Point addVector(Vector zoomVector) {
        Point point= new Point();
        point.x= x+zoomVector.getX();
        point.y= y+zoomVector.getY();
        point.z= z+zoomVector.getZ();
        return point;
    }
}
