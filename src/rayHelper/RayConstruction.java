package rayHelper;


import supportLibrary.functions.TransformationMatrix;
import templateModels.Point;
import templateModels.Vector;
import supportLibrary.worldEnvironment.Camera;
import supportLibrary.worldEnvironment.Ray;
import utils.Constants;


/**
 * It is a helper class for construction of ray
 */
public class RayConstruction {

    private float[][] Mwc,Mcw;
    private TransformationMatrix transformationMatrix;
    private Point worldCameraPoint;
    private Camera camera;

    public RayConstruction(Camera camera) {
        // We initialize a transformation matrix for camera to world coordinates conversion
        transformationMatrix= new TransformationMatrix(camera.getVectorVpn(),
                camera.getVectorVup(),
                camera.getPointVrp());
        Mwc= transformationMatrix.getWorldToCamera();
        Mcw= transformationMatrix.getCameraToWorld();
        this.camera= camera;
    }

    /**
     * We transform each ray from camera to world co-ordinate system. This
     * is easier in computation
     *
     * @param i
     * @param j
     * @return
     */
    public Ray constructRayVector(int i, int j){
        transformationMatrix= new TransformationMatrix(camera.getVectorVpn(),
                camera.getVectorVup(),
                camera.getPointVrp());
        Mwc= transformationMatrix.getWorldToCamera();
        Mcw= transformationMatrix.getCameraToWorld();
        // map i,j to screen co-ordinates Xc, Yc
        Ray ray= new Ray();
        // initialize camera point to co-ordinate
        Point cameraPoint = new Point(0,0,0);
        // convert camera point to World Co-ordinate System
        worldCameraPoint = transformationMatrix.getPointInReferenceToWorldCoordinate(cameraPoint);
        // initialize the point of origin to ray start point
        ray.setP0(worldCameraPoint);
        float x= ((Constants.XMAX-Constants.XMIN)*j/(Constants.NO_OF_COLS-1)) + Constants.XMIN ;
        float y=  ((Constants.YMAX-Constants.YMIN)*i/(Constants.NO_OF_ROWS-1))+Constants.YMIN;
        float f= Constants.FOCAL_LENGTH;
        Point point= new Point(x,y,f);          // Point in camera co-ordinate system
        Point worldCoordinatePoint = transformationMatrix.      // Convert camera point in Camera co-ordinate system to world co-ordinate
                getPointInReferenceToWorldCoordinate(point);
        // Ray vector VO is calculated
        Vector V0= worldCoordinatePoint.minus(ray.getP0());
        // VO vector is normalized
        V0= V0.getUnitVector();
        // ray vector is set in the Ray Object
        ray.setV0(V0);
        return  ray;
    }
}