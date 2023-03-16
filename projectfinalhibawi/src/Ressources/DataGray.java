package Ressources;

import java.io.Serializable;

public class DataGray implements Serializable {
    public int[][] image;

    public DataGray(int[][] image) {
        this.image = image;
    }

    public static int[][] convertToGrayscale(int[][] colorImage) {
        int height = colorImage.length;
        int width = colorImage[0].length;
        int[][] grayImage = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = colorImage[y][x];

                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;

                int gray = (int) (0.3 * red + 0.59 * green + 0.11 * blue);

                grayImage[y][x] = (gray << 16) | (gray << 8) | gray;
            }
        }
        return grayImage;
    }

   
}