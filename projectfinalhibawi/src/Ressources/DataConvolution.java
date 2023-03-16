package Ressources;

import java.io.Serializable;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.BufferedImage;

public class DataConvolution implements Serializable {
    public int[][] image;
    public int[][] kernel;

    public DataConvolution(int[][] image, int[][] kernel) {
        this.image = image;
        this.kernel = kernel;
    }

    public  int[][]  getImage(){
        return image;
    }

    public static int[][] applyFilter(int[][] inputMatrix, int[][] ker) {
        int height = inputMatrix.length;
        int width = inputMatrix[0].length;
        // float[] ker = { -1, 0, 1, -2, 0, 2, -1, 0, 1 };
        // *Kernel
        int rows = ker.length;
        int columns = ker[0].length;

        float[] arraykernel = new float[rows * columns];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                arraykernel[index++] = ker[i][j];
            }
        }

        Kernel kernel = new Kernel(3, 3, arraykernel);
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage inputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                inputImage.setRGB(x, y, inputMatrix[y][x]);
            }
        }
        // convolution--------------------------------------------------
        // gryskil-----------------------------------------------------
        BufferedImage outputBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        convolveOp.filter(inputImage, outputBufferedImage);
        int[][] outputMatrix = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                outputMatrix[y][x] = outputBufferedImage.getRGB(x, y);
            }
        }
        return outputMatrix;
    }
}
