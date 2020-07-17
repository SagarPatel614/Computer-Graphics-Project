package supportLibrary.functions;


import templateModels.Vector;
import utils.MatrixHelper;

/**
 * RotationMatrixComp is used to calculate
 * 1. U unit Vector
 * 2. V unit Vector
 * 3. W unit Vector
 * It takes v1 and v2 vector as input and calls calculateUVN() to calculate three vectors
 * A 4x4 matrix is returned using getRotationMatrix()
 */
public class RotationMatrixComp {

    // Output Unit vector
    private Vector uVector;                // u unit vector
    private Vector vVector;                // v unit vector
    private Vector nVector;                // n unit vector

    // Input Vector
    private Vector v1Vector;               // input vector v1
    private Vector v2Vector;               // input vector v2


    /**
     * It calls calculateUVN and all U,V,N unit vectors are computed
     * @param v1Vector Vector 1
     * @param v2Vector Vector 2
     */
    public RotationMatrixComp(Vector v1Vector, Vector v2Vector) {
        this.v1Vector = v1Vector;
        this.v2Vector = v2Vector;
        calculateUVN();
    }

    /**
     * The core method which is used to calculate U,V and N vector
     * 
     */
    private void calculateUVN(){
        if(v1Vector==null||v2Vector==null){
            System.err.print("Please Enter V1 and V2 Vector!!!!");
        }
        
        // V1 vector is along n unit vector
        // So we can easily say that
        // N(unit Vector) = unit vector of V1
        nVector = v1Vector.getUnitVector();
        
        // As U unit vector is perpendicular to V1 vector and V2 vector
        // We can say that their cross product(crossV21) will be along U unit Vector
        Vector crossV21 = v2Vector.crossProduct(v1Vector);
        
        // If we take a unit vector of this crossV21(Cross product of V2 and V1) it will be
        // Unit Vector U
        uVector=crossV21.getUnitVector();
        
        // as V unit vector is perpendicular to N unit vector and U unit vector
        // NxU that is cross product of N unit vector and U unit vector will be V unit Vector
        vVector= nVector.crossProduct(uVector);
    }

    /**
     * This method is used to construct a 4x4 matrix from U,V & N unit vectors.
     * This will be an orthogonal matrix
     * @return 4x4 Rotation Matrix
     */
    public float[][] getRotationMatrix(){
        float[][] rotationArray = new float[4][];
        rotationArray[0]= uVector.getArrayNotation();
        rotationArray[1]= vVector.getArrayNotation();
        rotationArray[2]= nVector.getArrayNotation();
        rotationArray[3]=new float[]{0f,0f,0f,1f};
        return rotationArray;
    }

    /**
     *
     * @return Inverse of Rotation Matrix
     */

    public float[][] getInverseRotationMatrix(){
        // returns a 4x4 rotation matrix
        float[][] rotationMatrix= getRotationMatrix();
        // As rotationMatrix is orthogonal matrix
        // so R inverse = R transpose
        return MatrixHelper.transpose(rotationMatrix);
    }

    public Vector getuVector() {
        return uVector;
    }

    public void setuVector(Vector uVector) {
        this.uVector = uVector;
    }

    public Vector getvVector() {
        return vVector;
    }

    public void setvVector(Vector vVector) {
        this.vVector = vVector;
    }

    public Vector getnVector() {
        return nVector;
    }

    public void setnVector(Vector nVector) {
        this.nVector = nVector;
    }

    public Vector getV1Vector() {
        return v1Vector;
    }

    public void setV1Vector(Vector v1Vector) {
        this.v1Vector = v1Vector;
    }

    public Vector getV2Vector() {
        return v2Vector;
    }

    public void setV2Vector(Vector v2Vector) {
        this.v2Vector = v2Vector;
    }


    @Override
    public String toString() {

        if(uVector==null||vVector==null||nVector==null){
            return "Please call calculateUVN";
        }
        return uVector.getString("U")+"\n"+vVector.getString("V")+"\n"+nVector.getString("N");
    }

}
