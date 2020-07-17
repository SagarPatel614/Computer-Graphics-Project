package eventHandler;

/**
 * A class which represents events of key up, down, right, left and mouse/touch drag
 */
public class Events {

    public static class KeyRight{};
    public static class KeyLeft{};
    public static class KeyUp{};
    public static class KeyDown{};
    public static class ZoomIn{};
    public static class ZoomOut{};
    public static class CameraRotate{
        public float rotation;
        public float elevation;

        public CameraRotate(float rotation, float elevation) {
            this.rotation = rotation;
            this.elevation = elevation;
        }
    }
}
