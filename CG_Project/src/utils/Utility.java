package utils;

import templateModels.Point;
import templateModels.Vector;

import java.io.*;
import java.util.Scanner;


public class Utility {

    public static final int SIZE = 128;

    public static void print(String message){
        System.out.println(message);
    }

    /**
     * Helper method to input values of vector elements
     * @param message name of vector
     * @return
     */
    public static Vector takeVectorAsInput(String message){
        Scanner scanner= new Scanner(System.in);
        Vector vector= new Vector();
        print("Please enter x value of vector "+message);
        vector.setX(scanner.nextFloat());
        print("Please enter y value of vector "+message);
        vector.setY(scanner.nextFloat());
        print("Please enter z value of vector "+message);
        vector.setZ(scanner.nextFloat());
        return vector;
    }


    /**
     * Helper method to input values of point elements
     * @param message name of Point
     * @return
     */
    public static Point takePointAsInput(String message){
        Scanner scanner= new Scanner(System.in);
        Point point= new Point();
        print("Please enter x value of point "+message);
        point.setX(scanner.nextFloat());
        print("Please enter y value of point "+message);
        point.setY(scanner.nextFloat());
        print("Please enter z value of point "+message);
        point.setZ(scanner.nextFloat());
        return point;
    }

    public static String fmt(float value) {
        return String.format("%6.3f",value);
    }

    /**
     * Reads object from a json file
     * For better code visibility and maintainability JSON has been used

     * @return
     * @throws Exception An exception is thrown if file is not present
     */
    /*public static Object readFromFile(Class t, String filename) throws Exception {
        try {
            String basePath = "./src/objects/";
            JsonReader reader = new JsonReader(new FileReader(basePath+filename));
            // JSON object is converted to a Java Object
            Object o = new Gson().fromJson(reader, t);
            if(t.isInstance(o)){
                return o;
            }
            throw new Exception("Incorrect Object");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }*/
/*
    *//**
     * It load CT data from smallHead.den file
     * @return opacity values
     *//*
    public static float[][][] loadCTData(){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("smallHead.den");
        System.out.println("is");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[SIZE * SIZE];
        float[][][] ctData= new float[SIZE][SIZE][SIZE];
        try {
            int layer=0;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);

                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        int i1 = data[i * SIZE + j] & 0xff;
                        // Ignores data if opacity is less than given Opacity
                        if(i1< Constants.THRESHOLD_OPACITY){
                            ctData[layer][i][j] = 0;
                        }else {
                            float i2 = i1 / 255f;       // opacity is divided by 255 get floating point value
                            ctData[layer][i][j] = i2;
                        }
                    }
                }
                layer++;
                if(layer>127){
                    break;
                }
            }
            buffer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ctData;
    }*/


    /**
     * It load CT data from smallHead.den file
     * @return opacity values
     */
    public static float[][][] loadCTData(){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        File file = new File("resources/smallHead.den");
        try (FileInputStream is = new FileInputStream(file)) {

            System.out.println("is");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[SIZE * SIZE];
            float[][][] ctData= new float[SIZE][SIZE][SIZE];
            try {
                int layer=0;
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);

                    for (int i = 0; i < SIZE; i++) {
                        for (int j = 0; j < SIZE; j++) {
                            int i1 = data[i * SIZE + j] & 0xff;
                            // Ignores data if opacity is less than given Opacity
                            if(i1< Constants.THRESHOLD_OPACITY){
                                ctData[layer][i][j] = 0;
                            }else {
                                float i2 = i1 / 255f;       // opacity is divided by 255 get floating point value
                                ctData[layer][i][j] = i2;
                            }
                        }
                    }
                    layer++;
                    if(layer>127){
                        break;
                    }
                }
                buffer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return ctData;
        }catch (IOException e){

        }
        return null;
    }
}
