package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import Ressources.Databright;
import Ressources.DataConvolution;
import Ressources.DataGray;
import Ressources.DataNoise;
import Ressources.DataResult;
import Ressources.Datainvert;
import Ressources.DataSapia;

public class Client extends JFrame {

    // TODO
    /*
     * _ GUI
     * + Axe of original image
     * + Axe of filtred image
     * + dropdown of tasks
     * -> CONVOLUTION FILTER (Diff : 1)
     * -> NOISE SALT AND PEPPER (Diff : 0)
     * -> GRAY FILTER (Diff : 0)
     * _ Connect with MainServer
     * _ Send Image + Task
     * _ Recieve Filtred Image
     * 
     */
    private static JLabel originalImageLabel;
    private static JLabel filteredImageLabel;
    private static JComboBox<String> tasksComboBox;
    private static JFrame F;
    static BufferedImage originalImage;
    static BufferedImage filteredImage;
    static String value;
    static String task;
    static int[][] kernel = new int[3][3];

    static int MainServer_port;
    static String MainServer_host;

    public static void main(String[] args) {

        /*
         * Read Proprieties from file
         */
        Properties prop = new Properties();
        FileInputStream ip;
        // Par defaut
        String FileConfiguration = "cfgClient.properties";
        if (args.length > 0)
            FileConfiguration = args[0];
        try {
            ip = new FileInputStream(FileConfiguration);
            prop.load(ip);
        } catch (Exception e2) {
            System.exit(0);
        }

        /*
         * Run the socket of MainServer
         */
        MainServer_port = Integer.parseInt(prop.getProperty("MainServer.port"));
        MainServer_host = prop.getProperty("MainServer.host");

        F = new JFrame();

        F.setTitle("image proccesing");
        F.setSize(1370, 703);
        F.setDefaultCloseOperation(EXIT_ON_CLOSE);
        F.setExtendedState(JFrame.MAXIMIZED_BOTH);
        F.setLayout(new BorderLayout(20, 20));

        originalImageLabel = new JLabel();
        filteredImageLabel = new JLabel();

        JPanel imagesPanel = new JPanel(new GridLayout(1, 2));
        imagesPanel.add(originalImageLabel);
        imagesPanel.add(filteredImageLabel);
        F.add(imagesPanel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel();
        F.add(controlsPanel, BorderLayout.NORTH);

        JButton chooseImageButton = new JButton("Choose Image");
        chooseImageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(F);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        originalImage = ImageIO.read(fileChooser.getSelectedFile());
                        int newWidth = 640;
                        int newHeight = 640;
                        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                        originalImageLabel.setIcon(new ImageIcon(resizedImage));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        });
        controlsPanel.add(chooseImageButton);

        tasksComboBox = new JComboBox<>(
                new String[] { "CONVOLUTION FILTER", "NOISE SALT AND PEPPER", "GRAY FILTER", "SEPIA FILTER",
                        "INVERT FILTER", "BRIGHTNESS FILTER" });
        controlsPanel.add(tasksComboBox);

        tasksComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage == null) {
                    JOptionPane.showMessageDialog(F, "Please select an image.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                task = (String) tasksComboBox.getSelectedItem();
                if (task.equals("CONVOLUTION FILTER")) {

                    JTextField[][] textFields = new JTextField[3][3];
                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                    for (int i = 0; i < 3; i++) {
                        JPanel rowPanel = new JPanel();
                        for (int j = 0; j < 3; j++) {
                            textFields[i][j] = new JTextField(5);
                            rowPanel.add(textFields[i][j]);
                        }
                        panel.add(rowPanel);
                    }

                    int result = JOptionPane.showConfirmDialog(null, panel, "Enter Matrix",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                kernel[i][j] = Integer.parseInt(textFields[i][j].getText());
                            }
                        }

                        // JFileChooser fileChooser = new JFileChooser();
                        // int result = fileChooser.showOpenDialog(F);
                        // if (result == JFileChooser.APPROVE_OPTION) {
                        // File file = fileChooser.getSelectedFile();
                        // Scanner scanner;
                        // try {
                        // scanner = new Scanner(file);
                        // int i = 0, j = 0;
                        // while (scanner.hasNextLine()) {
                        // String[] values = scanner.nextLine().split(",");
                        // for (j = 0; j < values.length; j++) {
                        // kernel[i][j] = Integer.parseInt(values[j].trim());
                        // System.out.println(kernel[i][j]);
                        // }
                        // i++;
                        // }
                        // } catch (FileNotFoundException e1) {
                        // // TODO Auto-generated catch block
                        // e1.printStackTrace();
                        // }
                        // }
                    }
                }

                if (task.equals("NOISE SALT AND PEPPER")) {
                    value = JOptionPane.showInputDialog(F, "Enter level of noise ex : {0.02}:");
                    System.out.println(value);
                }
                if (task.equals("BRIGHTNESS FILTER")) {
                    value = JOptionPane.showInputDialog(F, "Enter level of BRIGHTNESS ex :{100} :");
                    System.out.println(value);
                }

            }
        });

        JButton processImageButton = new JButton("Process Image");
        controlsPanel.add(processImageButton);
        F.setVisible(true);
        processImageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage == null) {
                    JOptionPane.showMessageDialog(F, "Please select an image.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    System.out.println("Adresse de MainServer");
                    System.out.println("=> " + MainServer_host + ":" + MainServer_port);
                    Socket socket = new Socket(MainServer_host, MainServer_port);
                    System.out.println("connected");
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    String state = (String) input.readObject();
                    int[][] img = bufferedImageToIntArray(originalImage);
                    if (state.compareToIgnoreCase("active") == 0) {
                        output.writeObject(task);

                        if (task.compareToIgnoreCase("CONVOLUTION FILTER") == 0) {
                            DataConvolution dataConvolution = new DataConvolution(img, kernel);
                            output.writeObject(dataConvolution);
                        }
                        if (task.compareToIgnoreCase("NOISE SALT AND PEPPER") == 0) {
                            DataNoise dataNoise = new DataNoise(img, Double.parseDouble(value));
                            output.writeObject(dataNoise);
                        }

                        if (task.compareToIgnoreCase("GRAY FILTER") == 0) {
                            DataGray dataGray = new DataGray(img);
                            output.writeObject(dataGray);
                        }
                        if (task.compareToIgnoreCase("SEPIA FILTER") == 0) {
                            DataSapia sapil = new DataSapia(img);
                            output.writeObject(sapil);
                        }
                        if (task.compareToIgnoreCase("INVERT FILTER") == 0) {
                            Datainvert inverse = new Datainvert(img);
                            output.writeObject(inverse);
                        }
                        if (task.compareToIgnoreCase("BRIGHTNESS FILTER") == 0) {
                            Databright dataNoise = new Databright(img, Double.parseDouble(value));
                            output.writeObject(dataNoise);
                        }

                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        DataResult pixels = (DataResult) in.readObject();
                        filteredImage = intArrayToBufferedImage(pixels.image);
                        int newWidth = 640;
                        int newHeight = 640;
                        Image resizedImage = filteredImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                        filteredImageLabel.setIcon(new ImageIcon(resizedImage));

                    }
                    socket.close();
                } catch (Exception r) {
                    System.out.println(r.getMessage());
                }

            }
        });

    }

    public static int[][] bufferedImageToIntArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result[y][x] = image.getRGB(x, y);
            }
        }
        return result;
    }

    public static BufferedImage intArrayToBufferedImage(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result.setRGB(x, y, pixels[y][x]);
            }
        }
        return result;
    }

}
