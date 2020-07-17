package supportLibrary.functions;

import templateModels.Point;
import templateModels.Vector;
import supportLibrary.worldEnvironment.Camera;
import supportLibrary.worldEnvironment.Ray;
import rayHelper.RayConstruction;
import utils.Constants;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class VolumeRendering {

    private static final int SIZE = 128;
    public float ctData[][][];
    public float shadingData[][][] = new float[SIZE][SIZE][SIZE];
    private Vector lightVector;
    private RayConstruction rayConstruction;
    int sizecounter;
    public VolumeRendering(float[][][] ctData, Camera camera, Vector lightVector) {
        this.ctData = ctData;
        this.lightVector = lightVector;
        this.rayConstruction = new RayConstruction(camera);


    }

    /**
     * Function used to compute shading value for each voxel,
     * It can be calculating by calculating gradient vector which tells the
     * change in the values among different voxel.
     * Once the gradient vector is calculated it can be used to
     * calculate shading value
     * We here use Phong Illumination Model
     * I = Ia*ka + Ip*kd*(N.L) + Ip*ks*(R.V)^n
     * Here we only consider diffusion shading so
     * I= Ip*kd*(N.L);
     * where N is the gradient Vector
     */
    private void computeShadingData() {
        Vector normal= new Vector();
        for (int i = 1; i < SIZE - 1; i++) {
            for (int j = 1; j < SIZE - 1; j++) {
                for (int k = 1; k < SIZE - 1; k++) {

                    // x,y,z component for gradient vector is calculated
                    float x = .5f *(ctData[i][j][k + 1] - ctData[i][j][k - 1]);
                    float y = .5f *(ctData[i][j + 1][k] - ctData[i][j - 1][k]);
                    float z = .5f *(ctData[i + 1][j][k] - ctData[i - 1][j][k]);
                    // normal vector is initialized
                    normal.setX(x);
                    normal.setY(y);
                    normal.setZ(z);
                    /*if(normal.getMagnitude()<.005f ){
                        shadingData[i][j][k] =  0;
                        continue;
                    }*/
                    // Unit vector is calculated
                    normal= normal.getUnitVector();
                    // shading is calculated
                    float v = Constants.INTENSITY_OF_POINT_SOURCE * Constants.KD * normal.dotProduct(lightVector);
                    // if shading value is negative than we have to ignore those values as
                    // in that case normal will be in opposite direction to light
                    int value = (int) Math.max(0,v);
                    shadingData[i][j][k] =  value;
                }
            }
        }
    }

    /**
     * This function is used to render frame
     */
    public BufferedImage renderFrame()  {
        // firstly shading value for each voxel is calculated
        computeShadingData();
        // rows and columns are initialized for the camera
        int rows = Constants.NO_OF_ROWS;
        int columns = Constants.NO_OF_COLS;
        int[][] imageBuffer = new int[rows][columns];
        System.out.println("Rendering... ");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Ray is calculated for each point on 3.5 mm film
                Ray ray = rayConstruction.constructRayVector(i, j);

                List<Float> tValues = rayboxintersection(ray);
                if (tValues == null) {      // it means no intersection
                    continue;
                }
                // we arrange t0 and t1 such that t0<t1
                float t0 = Math.min(tValues.get(0),tValues.get(1));
                float t1 = Math.max(tValues.get(0),tValues.get(1));

                float dt = Constants.SAMPLING_INTERVAL; // the interval for sampling along the ray
                float C = 0.0f;   // for accumulating the shading value
                float T = 1.0f;   // for accumulating the transparency

                // we start traversing in the cube and find integrating the values of color and shading
                // over different values of t for a particular ray
                // this shading value is stored in a pixel in camera
                for (float t = t0; t <= t1; t += dt) {
                  //  System.out.print(".");
                    // Get a point in the cube, this point will be surrounded by other values
                    Point point = ray.getPointWithT(t);
                    /* Obtain the shading value C and opacity value A
                    from the shading volume and CT volume, respectively,
                    by using tri-linear interpolation. */

                    float opacity=0;
                    try {
                        // calculate opacity using tri-linear interpolation
                        opacity = get3dInterpolatedValue(ctData,point);

                    } catch (IndexOutOfBoundsException e) {
                      //  throw e;
                    }

                    float shading=0;
                    try {
                        // calculate shading using tri-linear interpolation
                        shading = get3dInterpolatedValue(shadingData,point);

                    } catch (IndexOutOfBoundsException e) {
                        //
                    }

                   /* if(opacity<0||shading<0){
                        System.err.println("Opacity or shading is negative");
                    }*/
                    /* Accumulate the shading values in the front-to-back order.
                    Note: You will accumulate the transparency. This value
                    can be used in the for-loop for early termination.
                    */

                    if(T<.01f){         // if value of T goes beyond certain value we can terminate loop
                        break;
                    }
                    C+= opacity*shading*T;      // value of color and shading is accumulated
                    // For the next loop transparency will be updated as transparency decreases
                    // each time we keep on penetrating the cube
                    T *= (1-opacity);

                }
              //  System.out.println( C);
                /*if(C<0){
                    System.err.println("C is negative "+ C);
                }*/
                // value is saved in image pixel
                imageBuffer[i][j]= (int) C;

            }


        }

    //    System.out.println("Z error is "+zerr);
      return  saveImageInPng(imageBuffer);

    }

    /**
     * Converts int pixel array to a png image
     * @param arr Pixel Array
     */
    private BufferedImage saveImageInPng(int[][] arr) {
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

        return b;
    }

    /**
     * Calculates Trilinear interpolation of point. It gives value at a particular point inside a cube
     * by using linear interpolation multiple times
     * @param data data with values
     * @param point point for which Tri-linear interpolation is required
     * @return
     * @throws IndexOutOfBoundsException
     */
    private float get3dInterpolatedValue(float[][][] data,Point point) throws IndexOutOfBoundsException {

        int xIndex = (int) Math.floor(point.getX());
        int yIndex = (int) Math.floor(point.getY());
        int zIndex = (int) Math.floor(point.getZ());

        float f1= data[zIndex][yIndex][xIndex];
        float f2= 0;
        try {
            f2 = data[zIndex][yIndex][xIndex+1];
        } catch (Exception e) {
            /*String x = "%s , %s , %s";
            x=String.format(x,point.getX(),yIndex,zIndex);
            System.out.println(x);*/
        }
        float s1 = oneDInterpolation(f1, f2, point.getX());

        f1= data[zIndex][yIndex+1][xIndex];
        f2= data[zIndex][yIndex+1][xIndex+1];
        float s2 = oneDInterpolation(f1, f2, point.getX());

        f1= data[zIndex+1][yIndex][xIndex];
        f2= data[zIndex+1][yIndex][xIndex+1];
        float s3 = oneDInterpolation(f1, f2, point.getX());

        f1= data[zIndex+1][yIndex+1][xIndex];
        f2= data[zIndex+1][yIndex+1][xIndex+1];
        float s4 = oneDInterpolation(f1, f2, point.getX());


        float s5 = oneDInterpolation(s1,s3,point.getZ());
        float s6 = oneDInterpolation(s2,s4,point.getZ());

        float v = oneDInterpolation(s5, s6, point.getY());
       

        return v;
    }

    /*private float get3dInterpolatedValue(int[][][] data,Point point) throws IndexOutOfBoundsException {

        int xIndex = (int) Math.floor(point.getZ());
        int yIndex = (int) Math.floor(point.getY());
        int zIndex = (int) Math.floor(point.getX());

        float f1= data[xIndex][yIndex][zIndex];
        float f2= 0;
        try {
            f2 = data[xIndex][yIndex][zIndex+1];
        } catch (Exception e) {
            String x = "%s , %s , %s";
            x=String.format(x,point.getX(),yIndex,zIndex);
            System.out.println(x);
        }
        float s1 = oneDInterpolation(f1, f2, point.getZ());

        f1= data[xIndex][yIndex+1][zIndex];
        f2= data[xIndex][yIndex+1][zIndex+1];
        float s2 = oneDInterpolation(f1, f2, point.getZ());

        f1= data[xIndex+1][yIndex][zIndex];
        f2= data[xIndex+1][yIndex][zIndex+1];
        float s3 = oneDInterpolation(f1, f2, point.getZ());

        f1= data[xIndex+1][yIndex+1][zIndex];
        f2= data[xIndex+1][yIndex+1][zIndex+1];
        float s4 = oneDInterpolation(f1, f2, point.getZ());


        float s5 = oneDInterpolation(s1,s3,point.getX());
        float s6 = oneDInterpolation(s2,s4,point.getX());

        float v = oneDInterpolation(s5, s6, point.getY());
        if(v>0){
            try {
                String str = "\nPoint %s Value at corners is %s,%s,%s,%s,%s,%s,%s,%s" + data[zIndex][yIndex][xIndex];
                String format = String.format(str,point, data[zIndex][yIndex][xIndex],
                        data[zIndex][yIndex][xIndex+1],
                        data[zIndex][yIndex+1][xIndex],
                        data[zIndex][yIndex+1][xIndex+1],
                        data[zIndex+1][yIndex][xIndex],
                        data[zIndex+1][yIndex][xIndex+1],
                        data[zIndex+1][yIndex+1][xIndex],
                        data[zIndex+1][yIndex+1][xIndex+1]);
                writer.write(format);
                writer.write("\nInterpolated value is " + v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return v;
    }*/

    /**
     * Checks whether a ray is intersecting the given cube or not
     * if it is intersecting we have to find number of intersection
     * For now we are just trying to find those rays which have 2 intersections
     * as rays with intersection =3 will be very less and can be ignored
     * @param ray
     * @return
     */
    private List<Float> rayboxintersection(Ray ray) {

        List<Float> tList = new ArrayList<>();
        // x minimum bound check - 0
        float tXMin = ray.getTPointForPlane("x", 0);
        if (tXMin != Float.NEGATIVE_INFINITY) {
            tList.add(tXMin);
        }
        // x maximum bound check 127
        float tXMax = ray.getTPointForPlane("x", Constants.SIZE - 1);
        if (tXMax != Float.NEGATIVE_INFINITY) {
            tList.add(tXMax);
        }
        // y minimum bound check -0
        float tYMin = ray.getTPointForPlane("y", 0);
        if (tYMin != Float.NEGATIVE_INFINITY) {
            tList.add(tYMin);
        }

        // y maximum bound check-127

        float tYMax = ray.getTPointForPlane("y", Constants.SIZE - 1);
        if (tYMax != Float.NEGATIVE_INFINITY) {
            tList.add(tYMax);
        }

        // z minimum bound check-0
        float tZMin = ray.getTPointForPlane("z", 0);
        if (tZMin != Float.NEGATIVE_INFINITY) {
            tList.add(tZMin);
        }

        // z maximum bound check- 127
        float tZMax = ray.getTPointForPlane("z", Constants.SIZE - 1);
        if (tZMax != Float.NEGATIVE_INFINITY) {
            tList.add(tZMax);
        }

        /*StringBuilder stringBuilder= new StringBuilder();

        if(tList.size()>2){
            stringBuilder.append("\nT size");
            for (float element :
                    tList) {
                stringBuilder.append(element).append(",");
            }
            System.out.println(stringBuilder.toString());
        }*/
        if (tList.size() == 2) {
            return tList;
        }

        return null;
    }

    /**
     * A method used to find linear interpolationg
     * @param f1 value at first corner point
     * @param f2 value at second corner point
     * @param x
     * @return
     */
    private float oneDInterpolation(float f1,float f2, float x){
        // find the floor of the x
        // for example if point is 4.5
        // we will get first point x0= 4
        double x0= Math.floor(x);
        // similarly second point will x1=5
        double x1= x0+1; //Math.ceil(x);
        // This formula helps in interpolating value between two points
        return (float) (f1* (x1-x) + f2*(x-x0));
    }

    public  float min(float a, float b) {

        return (a <= b) ? a : b;
    }

    public  float max(float a, float b) {

        return (a >= b) ? a : b;
    }
}
