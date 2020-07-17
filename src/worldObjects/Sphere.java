package worldObjects;


import supportLibrary.interfaces.PhysicalObject;
import templateModels.Point;
import templateModels.Vector;
import supportLibrary.worldEnvironment.Ray;
import utils.Constants;

/**
 * template class for a sphere
 */
public class Sphere implements PhysicalObject {

    private Point centerOfSphere;       // center of radius
    private float radius;               // radius of sphere
    private float kd;                   // diffuse reflection coefficient
    private Vector normalVector;
    public Sphere(Point centerOfSphere, float radius, float kd) {
        this.centerOfSphere = centerOfSphere;
        this.radius = radius;
        this.kd = kd;

    }


    @Override
    public Point checkIntersection(Ray ray) {

        // L vector is vector between Point P0(camera reference point) and Center of sphere
        Vector lVector = centerOfSphere.minus(ray.getP0());
        // Tca could be calculated  by dot product lVector and ray vector
        float Tca= ray.getV0().dotProduct(lVector);
    //    System.out.println(ray.getV0());
        if(Tca<0){
            return null;
        }
        // Distance between center of sphere and ray passing through sphere is
        float magnitude = lVector.getMagnitude();
        double d= Math.sqrt(magnitude*magnitude - Tca*Tca);

        // We can find Thc by applying pythagoras theorem as d is perpendicular to Thc and
        if(d>radius) {
   //         System.out.println("d was greater than radius");
            return null;
        }
        // Radius is hypotenuse
        double Thc = Math.sqrt(radius * radius - d * d);
        // To can be calculated by subtracting Tca and Thc
        double t0 = Tca-Thc;
        double t1= Tca+Thc;
        return ray.getPointWithT((float) t0);
    }

    @Override
    public int shading(Ray ray,Point objectPoint, Point lightPosition) {
        // Light vector is a vector from point on object to the source of light
        Vector lightVector= lightPosition.minus(objectPoint);
        // light vector is normalized
        lightVector = lightVector.getUnitVector();
        // normal vector is a vector from center of sphere to point
        normalVector = objectPoint.minus(centerOfSphere);
        // normalize normalvector
        normalVector = normalVector.getUnitVector();
        // a dot product of normal vector and light vector is calculated
        float v = normalVector.dotProduct(lightVector);
        // shadingvalue =  Ip *kd * (NormalVector*lightVector)
        int value = (int) (Constants.INTENSITY_OF_POINT_SOURCE * kd * v);
        // shading value is negative it means light is not reaching this point
        value= value<0?0:value;
        return value;
    }

    public Point getCenterOfSphere() {
        return centerOfSphere;
    }

    public void setCenterOfSphere(Point centerOfSphere) {
        this.centerOfSphere = centerOfSphere;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getKd() {
        return kd;
    }

    public void setKd(float kd) {
        this.kd = kd;
    }


}
