package Ressources;

import java.io.Serializable;

public class Datainvert implements Serializable {
    public int[][] image;

    public Datainvert(int[][] image) {
        this.image = image;
    }

    public static int[][] invertFilter(int[][] inputMatrix) {
        int width = inputMatrix.length;
        int height = inputMatrix[0].length;
        int[][] outputMatrix = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = inputMatrix[x][y];

                // Invert the color components
                int red = 255 - ((pixel >> 16) & 0xff);
                int green = 255 - ((pixel >> 8) & 0xff);
                int blue = 255 - (pixel & 0xff);

                // Create a new pixel with the inverted components and set the output pixel's
                // color
                int newPixel = (255 << 24) | (red << 16) | (green << 8) | blue;
                outputMatrix[x][y] = newPixel;
            }
        }

        return outputMatrix;
    }

}
