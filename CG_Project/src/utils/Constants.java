package utils;

public class Constants {
    public static final int SIZE = 128;
    public static final int NO_OF_COLS = 512;
    public static final int NO_OF_ROWS = 512;
    public static final float XMIN = -0.0175f;
    public static final float XMAX = 0.0175f;
    public static final float YMIN = -0.0175f;
    public static final float YMAX = 0.0175f;
    public static final float FOCAL_LENGTH = .05f;
    public static final float INTENSITY_OF_POINT_SOURCE = 255;
    public static final float KD = .75f;
    public static final int THRESHOLD_OPACITY = 40;
    public static final float SAMPLING_INTERVAL = 1;
    public static final String help="Movement Control\n" +
            "Move camera using left,right,top,bottom ARROW keys\n" +
            "or\n" +
            "Dragging mouse around the screen\n" +
            "Zoom In \t Z\n" +
            "Zoom Out\t X\n";
    public static float Light[] = new float[]{0.577f, -0.577f, -0.577f};
    
    // Camera coordinates
    /*
    // Bottom Angle for face (Looking from the Top-Left Corner of the front face)
    public static float VRP[] = new float[]{128.0f, 64.0f, 250.0f};
    public static float VPN[] = new float[]{-64.0f, 0.0f, -186.0f};
    public static float VUP[] = new float[]{0.0f, 1.0f, 0.0f};
    */
 
    /*
 	// Back View  - Upside down
 	public static float[] VPN = new float[]{50.0f, 186.0f, 50.0f};
 	public static float[] VUP = new float[]{0,0,-1};
 	public static float[] VRP = new float[]{0.0f,-200.0f,0.0f};
 	*/
    
 	/*
    // Top View
 	public static float[] VPN = new float[]{0.0f, 0.0f, -186.0f};
 	public static float[] VUP = new float[]{0,-1,0};
 	public static float[] VRP = new float[]{64.0f,64.0f,250.0f};
 	*/
    
    
 	// Face/Front View
 	public static float[] VPN = new float[]{0.0f, -186.0f, 0.0f};
 	public static float[] VUP = new float[]{0,0,-1};
 	public static float[] VRP = new float[]{64.0f, 250.0f, 64.0f};
    
}
