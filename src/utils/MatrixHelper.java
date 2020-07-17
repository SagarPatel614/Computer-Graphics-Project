package utils;

import templateModels.Point;

public class MatrixHelper {

    /**
     * Matrix multiplication operation
     * @param m1 First Matrix
     * @param m2 Second Matrix
     * @return result of multiplication
     * @throws Exception
     */
    public static float[][] multiply(float m1[][],float m2[][]) throws Exception {

        if(m1.length==0||m2.length==0){
            // an exception is thrown if nothing is in matrix
            throw new IllegalArgumentException("Matrix can't be zero");
        }
        int m1RowSize= m1.length;       // row size of Matrix 1
        int m1ColumnSize= m1[0].length; // column size of Matrix 1
        int m2RowSize= m2.length;       // row size of Matrix 2
        int m2ColumnSize= m2[0].length; // column size of matrix 2

        // if Matrix 1 column size not equal to Matrix 2 row size then there is an error in Input
        if(m1ColumnSize!=m2RowSize){
            throw new IllegalArgumentException("Column size of first matrix should be equal to Row size of second Matrix");
        }
        // initialize resultant matrix
        float[][] result= new float[m1RowSize][m2ColumnSize];
        // multiplication
        for(int i=0;i< m1RowSize;i++){
            for (int j=0;j<m2ColumnSize;j++){
                for(int k=0;k<m1RowSize;k++){
                    result[i][j]+= m1[i][k]*m2[k][j];
                }
            }
        }
        return result;
    }


    public static Point multiply(float m1[][], float m2[]) throws Exception {
        if(m2.length==0)
                return null;


        float[][] transposedPoint = getTransposePoint(m2);
        float[][] result = multiply(m1, transposedPoint);
        Point point = new Point(result[0][0],result[0][1],result[0][2]);
        return point;
    }

    /**
     * Return transpose of a point
     * @param m2
     * @return
     */
    private static float[][] getTransposePoint(float[] m2) {
        int rowSize=m2.length;
        float[][] transposedPoint= new float[rowSize+1][1];
        for (int i = 0; i < rowSize; i++) {
            transposedPoint[0][i]=m2[i];
        }
        transposedPoint[0][rowSize]=1f;
        return transposedPoint;
    }

    /**
     * Helper method to display Matrix
     * @param matrix matrix to display
     */
    public static void displayMatrix(float[][] matrix){
        if(matrix.length==0)
            return;
        int rowSize= matrix.length;
        int columnSize= matrix[0].length;
        for (int i=0;i<rowSize;i++){
            for (int j=0;j<columnSize;j++){
                System.out.printf("%10.3f ",matrix[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Takes any matrix and returns its transpose Matrix
     * @param matrix input matrix
     * @return Transpose of the input Matrix
     */
    public static float[][] transpose(float[][] matrix) {
        if(matrix.length==0)
            throw new IllegalArgumentException("matrix length can't be zero");
        int rowSize= matrix.length;
        int columnSize= matrix[0].length;
        float[][] transposeMatrix= new float[columnSize][rowSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                transposeMatrix[j][i]=matrix[i][j];
            }
        }
        return transposeMatrix;
    }
}
