package Worker;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import MainServer.ImageDivider.SubMatrix;
/**
 * ImplFilters
 */
public class ImplFilters extends UnicastRemoteObject implements RessourcesForRMI.Filters  {

    
     public ImplFilters() throws RemoteException {
        super();
    }

    // convolution--------------------------------------------------
    @Override
    public SubMatrix applyFilter(SubMatrix inputSubMatrix, int[][] ker) {
        int[][] inputMatrix = inputSubMatrix.matrix;
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
        for (int y = 1; y < height; y++) {
            for (int x = 1; x < width; x++) {
                outputMatrix[y-1][x-1] = outputBufferedImage.getRGB(x, y);
            }
        }


        inputSubMatrix.matrix=outputMatrix;
        return inputSubMatrix;
    }

}