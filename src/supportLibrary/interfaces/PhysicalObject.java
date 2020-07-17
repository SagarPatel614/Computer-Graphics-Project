package supportLibrary.interfaces;


import templateModels.Point;
import supportLibrary.worldEnvironment.Ray;

/**
 * An interface which is to be implemented by any object in the scene
 * which will need to be shown
 */
public interface PhysicalObject {
    Point checkIntersection(Ray ray);
    int shading(Ray ray, Point objectPoint, Point lightPosition);
}
