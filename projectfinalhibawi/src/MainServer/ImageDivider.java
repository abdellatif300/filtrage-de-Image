package MainServer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageDivider {

    public static ArrayList<SubMatrix> divide(int[][] image, int numWorkers) {
        int rows = image.length;
        int cols = image[0].length;
        int rowsPerWorker = rows / numWorkers;
        int remainingRows = rows % numWorkers;

        ArrayList<SubMatrix> subMatrices = new ArrayList<>();
        int startRow = 0;
        int index = 0;

        for (int i = 0; i < numWorkers; i++) {
            int endRow = startRow + rowsPerWorker;
            if (remainingRows > 0) {
                endRow++;
                remainingRows--;
            }

            subMatrices.add(new SubMatrix(image, startRow, endRow, index));
            startRow = endRow;
            index++;
        }

        return subMatrices;
    }

    public static int[][] merge(ArrayList<SubMatrix> subMatrices, int rows, int cols) {
    int[][] image = new int[rows][cols];

    subMatrices.sort((a, b) -> a.index - b.index);

    for (SubMatrix subMatrix : subMatrices) {
        int startRow = subMatrix.startRow;
        int endRow = subMatrix.endRow;
        int index = subMatrix.index;

        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < cols; j++) {
                image[i][j] = subMatrix.matrix[i - startRow][j];
            }
        }
    }

    return image;
}


    public static class SubMatrix implements Serializable{
        public int[][] matrix;
        public int startRow;
        public int endRow;
        public int index;

        SubMatrix(int[][] matrix, int startRow, int endRow, int index) {
            this.matrix = new int[endRow - startRow+2][matrix[0].length+2];
            for(int[] row:this.matrix){
                Arrays.fill(row, 0);
            }

            this.startRow = startRow;
            this.endRow = endRow;
            this.index = index;

            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    this.matrix[i - startRow][j] = matrix[i][j];
                }
            }
        }
    }
}
