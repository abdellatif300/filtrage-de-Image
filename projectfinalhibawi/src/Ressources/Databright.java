package Ressources;

import java.io.Serializable;

public class Databright implements Serializable {
    public int[][] image;
    public double density;

    public Databright(int[][] image, double density) {
        this.image = image;
        this.density = density;
    }

    public static int[][] brightnessFilter(int[][] pixels, double density) {
        int width = pixels.length;
        int height = pixels[0].length;
        int[][] output = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the current pixel color
                int color = pixels[x][y];

                // Extract the RGB components
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;

                // Increase the brightness by the specified amount
                red += density;
                green += density;
                blue += density;

                // Clip the values to the range of 0-255
                red = Math.min(255, Math.max(0, red));
                green = Math.min(255, Math.max(0, green));
                blue = Math.min(255, Math.max(0, blue));

                // Combine the RGB components back into a single pixel color
                output[x][y] = (red << 16) | (green << 8) | blue;
            }
        }

        return output;
    }

}