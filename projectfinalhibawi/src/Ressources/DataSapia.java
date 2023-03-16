package Ressources;

import java.io.Serializable;

public class DataSapia implements Serializable {
    public int[][] image;

    public DataSapia(int[][] image) {
        this.image = image;
    }

    public static int[][] sepiaFilter(int[][] image) {
        int width = image[0].length;
        int height = image.length;
        int[][] filteredImage = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image[y][x];
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                // Calculate new values for each color channel
                int newRed = (int) (0.393 * red + 0.769 * green + 0.189 * blue);
                int newGreen = (int) (0.349 * red + 0.686 * green + 0.168 * blue);
                int newBlue = (int) (0.272 * red + 0.534 * green + 0.131 * blue);

                // Check that color values are within the valid range
                newRed = Math.min(newRed, 255);
                newGreen = Math.min(newGreen, 255);
                newBlue = Math.min(newBlue, 255);

                // Set the new color of the pixel
                int newPixel = (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
                filteredImage[y][x] = newPixel;
            }
        }

        return filteredImage;
    }
}
