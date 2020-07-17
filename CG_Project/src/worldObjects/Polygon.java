package worldObjects;



import supportLibrary.interfaces.PhysicalObject;
import templateModels.Point;
import templateModels.Vector;
import supportLibrary.worldEnvironment.Ray;
import utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * template class for a polygon
 */
public class Polygon implements PhysicalObject {

    private List<Point> vertices= new ArrayList<>();
    private List<Point> projectedVertices= new ArrayList<>();
    private Vector normalVector;
    float diffusionCoefficient;


    @Override
    public Point checkIntersection(Ray ray) {

        // First we have to Calculate Normal
        // For calculating normal we have to take
        // a cross product of first two vertices
        Point vertex0= vertices.get(0);
        Point vertex1= vertices.get(1);
        Point vertex2= vertices.get(2);
/*
        // for calculating Normal of the plane
        // we have to calculate two vectors L1 and L2
        // using the above three vertices
        // eg L1= vertex1 - vertex0
        Vector L1= vertex1.minus(vertex0);

        // and L2= vertex2- vertex0;
        Vector L2= vertex2.minus(vertex0);

        // Now Normal vector would be cross product
        // of L1 and L2
        normalVector= L1.crossProduct(L2);
        normalVector= normalVector.getUnitVector();*/
        // General plane equation is  Ax+Bx+Cx+D=0
        // We know, A,B and C form normal vector of the plane
        // that is N=[A,B,C]
        float A= normalVector.getX();
        float B= normalVector.getY();
        float C= normalVector.getZ();

        // D can be calculated using
        // equation
        // D = -( A*v0x + B * v0y + C * v0z )
        float D= -(A*vertex0.getX()+ B*vertex0.getY()+C*vertex0.getZ());

        // Ray equation is represented by equation
        // R(t) = O+ t*V , where O is origin and V is the ray vector
        // We know
        // N.P =-D
        // N.R(t)= N.(O+ t*V)=-D
        // t= -(N.O+D)/ N.V
        // Lets break into different steps
        // because we have to check if N.V!=0
        // as that would mean N vector and V vector are perpendicular
        float nvDot= normalVector.dotProduct(ray.getV0());
        if(nvDot==0){
            // N vector and V vector are perpendicular
            // ray would be parallel to the plane
            return null;
        }

        // t= -(N.O+D)/ N.V
        float t= -(normalVector.dotProduct(ray.getP0())+D)/nvDot;
        Point pointOnPlane = ray.getPointWithT(t);

        // This point On Plane could either be inside the polygon or outside
        // we would draw a ray from point
        // if number of intersections are odd then point is in Polygon else it is outside
        // But for it First we have to find the axis of projection
        // as it would be quite difficult finding it in 3d
        Point.Projection projection= normalVector.getProjectionOfTheVector();

        // As we are projecting for 2d we would get a 2d point
        Point projectedPoint=pointOnPlane.getProjectedPointAlong(projection);

        // Project all the vertices of polygon to 2d along Projection which we just
        // calculated
        projectedVertices.clear();
        for (Point vertex :
                vertices) {
            Point projectedPointAlong = vertex.getProjectedPointAlong(projection);
            projectedVertices.add(projectedPointAlong);
        }

        // Let us translate our point and polygon to origin
        // this will simply calculations
        //translate projectedPoint to Origin
        Point translatedProjectedPoint = new Point(-projectedPoint.getX(), -projectedPoint.getY());

        //translate polygon (Made from Projected Points)to origin by projected Point
        translateProjectedVerticesBy(projectedPoint);

        // We have to calculate no. of intersection of this polygon on positive X-Axis
        // if it is odd it means point is in the polygon else it is outside

        // Counter to check intersection along positive X-Axis
        int xIntersectCtr=0;

        // Iterate through Projected Points
        for (int i = 0; i < projectedVertices.size(); i++) {
            // We will take two points from projectedVertices to Make a line
            // then we will check whether this line intersects positive x axis

            // First vertex of line
            Point v1= projectedVertices.get(i);

            // Index of second vertex of line, if it is i= last point then second
            // vertex would be first point of the polygon
            // as our polygon is cyclic
            int index2ndPoint = i==(projectedVertices.size()-1)?0:i+1;
            Point v2= projectedVertices.get(index2ndPoint);

            // we would only consider those lines who would be intersecting x-axis
            // this could be found out  by checking product of v1.y and v2.y
            // if it is negative then it would intersect x-axis
            if(v1.getY()*v2.getY()<0){

                // let us computer the intersection point of this line with positive X-Axis
                // using simple line equation
                float x= -v1.getY()*((v2.getX()-v1.getX())/(v2.getY()-v1.getY())) + v1.getX();

                // if x is greater than zero than the intersection is at positive X-axis
                if(x>0){
                    // intersection counter is increased by 1
                    xIntersectCtr++;
                }

            }


        }
        // check if intersection Counter is having odd parity or even
        if(xIntersectCtr%2==1){
            // This means intersection counter is odd
            // and the point is within polygon
            // and is of our interest
            return pointOnPlane;
        }
        return null;
    }

    /**
     * translate all the vertices according to the projected point
     * @param projectedPoint Point on the plane
     */
    private void translateProjectedVerticesBy(Point projectedPoint) {
        for (Point vertex :
                projectedVertices) {
            vertex.translateX(-projectedPoint.getX());
            vertex.translateY(-projectedPoint.getY());
        }
    }

    /**
     * Computes the shading value for the particular object.
     * @param ray Ray object
     * @param objectPoint Point of the object
     * @param lightPosition Position of Light object
     * @return
     */
    @Override
    public int shading(Ray ray, Point objectPoint, Point lightPosition) {
        // Light vector is a vector from point on object to the source of light
        Vector lightVector= lightPosition.minus(objectPoint);
        lightVector= lightVector.getUnitVector();
        float v = normalVector.dotProduct(lightVector);
        // shadingvalue =  Ip *kd * (NormalVector*lightVector)
        int value = (int) (Constants.INTENSITY_OF_POINT_SOURCE * diffusionCoefficient * v);
        //value=value<0?-value:0;
        return value;
    }

}
