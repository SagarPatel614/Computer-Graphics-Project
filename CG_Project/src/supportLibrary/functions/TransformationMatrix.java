package supportLibrary.functions;


import templateModels.Point;
import templateModels.Vector;
import utils.MatrixHelper;

/**
 * TransformationMatrix helps in finding Transformation Matrix of world to point or Point to world
 */
public class TransformationMatrix {

    private final RotationMatrixComp rotationMatrixComp;
    float[][] rotationMatrix;
    float[][] translationMatrix;
    float[][] transformMatrix;
    float[][] Mwp,Mpw ;
    // Vector for View Plane and Up Direction
    Vector v1;
    Vector v2;
    Point referencePoint;               //Reference Point


    /**
     * Used to Initialize Transformation Matrix
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @param referencePoint Reference Point
     */
    public TransformationMatrix(Vector v1, Vector v2, Point referencePoint) {
        this.v1 = v1;
        this.v2 = v2;
        this.referencePoint = referencePoint;
        // RotationMatrix class helps in calculating U,V,N unit vectors
        rotationMatrixComp= new RotationMatrixComp(v1,v2);
        // A rotation matrix from the U,V,N unit vector
        rotationMatrix = rotationMatrixComp.getRotationMatrix();
        // Reference point in a 4x4 matrix
        translationMatrix= referencePoint.getTranslationMatrix(false);
        Mwp= getWorldToPoint();
        Mpw= getPointToWorld();
    }

    /**
     * Calculate transformation Matrix from World to Point.
     * We know Mwc = R*T
     * @return Transformation Matrix from World to Point
     */
    public float[][] getWorldToPoint(){
        float[][] Mwc ;
        try {
            // Multiply Rotation Matrix with the Translation Matrix
            Mwc = MatrixHelper.multiply(rotationMatrix, translationMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.print(e.getMessage());
            return new float[][]{};
        }
        return Mwc;
    }


    /**
     * Calculate transformation Matrix from World to Point.
     * Mcw= inverse of Translation Matrix * inverse of Rotation Matrix
     * @return transformation Matrix from World to Point
     */
    public float[][] getPointToWorld(){
        // get Inverse of TranslationMatrix
        float[][] inverseTranslationMatrix= referencePoint.getTranslationMatrix(true);
        // Because R is an Orthogonal Matrix
        // R inverse = R transpose
        float[][] inverseRotationMatrix = rotationMatrixComp.getInverseRotationMatrix();
        // Transformation Matrix is Initialized
        float[][] Mcw;
        try {
            // as Mcw = T inverse * R inverse
            // where T inverse is inverse of TranslationMatrix and R inverse is inverse of Rotation Matrix
            Mcw= MatrixHelper.multiply(inverseTranslationMatrix,inverseRotationMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.print(e.getMessage());
            return new float[][]{};
        }
        return  Mcw;
    }


    /**
     * Wrapper Method for WorldToCamera
     * @return Transformation Matrix
     */
    public float[][] getWorldToCamera(){
        return getWorldToPoint();
    }

    /**
     * Wrapper Method for CameraToWorld
     * @return Transformation Matrix
     */
    public float[][] getCameraToWorld(){
        return getPointToWorld();
    }
    /**
     * Wrapper Method for WorldToLight
     * @return Transformation Matrix
     */
    public float[][] getWorldToLight(){
        return getWorldToPoint();
    }

    /**
     * Wrapper Method for LightToWorld
     * @return Transformation Matrix
     */
    public float[][] getLightToWorld(){
        return getPointToWorld();
    }

    /**
     * Convert A point to World coordinate system
     * @param point
     * @return
     */
    public Point getPointInReferenceToWorldCoordinate(Point point){
        float[][] pointInMatrix= point.getPointinMatrix();
        float[][] resultMatrix = new float[0][];
        try {
            resultMatrix = MatrixHelper.multiply(Mpw, pointInMatrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Point.convertPointMatrixtoPoint(resultMatrix);
    }
}
