package Ressources;

import java.io.Serializable;
import java.util.Random;

public class DataNoise implements Serializable {
    public int[][] image;
    public double density;

    public DataNoise(int[][] image, double density) {
        this.image = image;
        this.density = density;
    }

    public static int[][] addNoise(int[][] image, double noiseLevel) {
        int height = image.length;
        int width = image[0].length;
        Random random = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = image[i][j];
                if (random.nextDouble() < noiseLevel / 2) {
                    pixel = 0xffffffff;
                } else if (random.nextDouble() < noiseLevel / 2) {
                    pixel = 0xff000000;
                }
                image[i][j] = pixel;
            }
        }
        return image;
    }
}
