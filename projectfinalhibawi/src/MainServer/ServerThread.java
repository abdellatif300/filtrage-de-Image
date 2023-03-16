package MainServer;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.Naming;
import java.util.ArrayList;


import MainServer.ImageDivider.SubMatrix;




import Ressources.Databright;
import Ressources.DataConvolution;
import Ressources.DataGray;
import Ressources.DataNoise;
import Ressources.DataResult;
import Ressources.Datainvert;
import Ressources.DataSapia;
import RessourcesForRMI.WorkerDataList;
import RessourcesForRMI.Filters;
import RessourcesForRMI.WorkerData;



public class ServerThread implements Runnable {
    private TaskQueue taskQueue;


    public ServerThread(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        try {

            while (true) {

                Task newTask = taskQueue.take();
                Socket soc = newTask.client;
                System.out.println("+(" + (newTask.TaskId) + ") : Entred");

                // Send to client "dispo"
                ObjectOutputStream d = new ObjectOutputStream(soc.getOutputStream());
                d.writeObject("active");

                // Wait for name task
                ObjectInputStream dis = new ObjectInputStream(soc.getInputStream());
                String taskName = (String) dis.readObject();

                System.out.println("+(" + (newTask.TaskId) + ") : Task is " + taskName);
                if (taskName.compareToIgnoreCase("NOISE SALT AND PEPPER") == 0) {
                    // ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
                    DataNoise dataNoise = (DataNoise) dis.readObject();

                    System.out.println("+(" + (newTask.TaskId) + ") : All data recieved");
                 
                    // !Traitement de worker .........
                    int[][] noisyImage = DataNoise.addNoise(dataNoise.image, dataNoise.density);

                    DataResult objectToSend = new DataResult(noisyImage);

                    ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                    oos.writeObject(objectToSend);

                    System.out.println("+(" + (newTask.TaskId) + ") : Result sent");
                } else if (taskName.compareToIgnoreCase("GRAY FILTER") == 0) {
                    // ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
                    DataGray dataGray = (DataGray) dis.readObject();

                    System.out.println("+(" + (newTask.TaskId) + ") : All data recieved");
                   
                    // !Traitement de worker .........
                    int[][] grayImage = DataGray.convertToGrayscale(dataGray.image);

                    DataResult objectToSend = new DataResult(grayImage);

                    ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                    oos.writeObject(objectToSend);

                    System.out.println("+(" + (newTask.TaskId) + ") : Result sent");

                } else if (taskName.compareToIgnoreCase("SEPIA FILTER") == 0) {
                    // ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
                    DataSapia sapil = (DataSapia) dis.readObject();

                    System.out.println("+(" + (newTask.TaskId) + ") : All data recieved");
                   

                    // !Traitement de worker .........
                    int[][] sapilImage = DataSapia.sepiaFilter(sapil.image);

                    DataResult objectToSend = new DataResult(sapilImage);

                    ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                    oos.writeObject(objectToSend);

                    System.out.println("+(" + (newTask.TaskId) + ") : Result sent");

                } else if (taskName.compareToIgnoreCase("INVERT FILTER") == 0) {
                    // ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
                    Datainvert invert = (Datainvert) dis.readObject();

                    System.out.println("+(" + (newTask.TaskId) + ") : All data recieved");
                   

                    // !Traitement de worker .........
                    int[][] invertImage = Datainvert.invertFilter(invert.image);

                    DataResult objectToSend = new DataResult(invertImage);

                    ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                    oos.writeObject(objectToSend);

                    System.out.println("+(" + (newTask.TaskId) + ") : Result sent");

                } else if (taskName.compareToIgnoreCase("BRIGHTNESS FILTER") == 0) {
                    // ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
                    Databright bright = (Databright) dis.readObject();

                    System.out.println("+(" + (newTask.TaskId) + ") : All data recieved");
                    /*
                     * if(!isDifficult(taskName)){
                     * 
                     * }
                     */

                    // !Traitement de worker .........
                    int[][] brightImage =  Databright.brightnessFilter(bright.image, bright.density);

                    DataResult objectToSend = new DataResult(brightImage);

                    ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                    oos.writeObject(objectToSend);

                    System.out.println("+(" + (newTask.TaskId) + ") : Result sent");


                } else if (taskName.compareToIgnoreCase("CONVOLUTION FILTER") == 0) {
                    // ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
                    DataConvolution dataConvolution = (DataConvolution) dis.readObject();

                    System.out.println("+(" + (newTask.TaskId) + ") : All data recieved");
                    int w = dataConvolution.image[0].length;
                    int h = dataConvolution.image.length;
                    int resDif = isDifficult(taskName, w, h);
                    if (resDif > 0) {
                        // !Call RMI
                        ArrayList<WorkerData> dispoWorkers;
                        // Try to connect to workers, if there is no worker, do sleep and try again for
                        // 5 times
                        int attempts = 0;
                        do {
                            if (attempts != 0)
                                Thread.sleep(2000);
                            dispoWorkers = WorkerDataList.DispoWorkers();

                        } while (dispoWorkers.size() == 0 && attempts++ < 5);

                        int numberOfWorkers = Math.min(resDif, dispoWorkers.size());

                        ArrayList<SubMatrix> AllSubMatrix = new ArrayList<SubMatrix>();
                        // ? Devide image
                        ArrayList<SubMatrix> sub = ImageDivider.divide(dataConvolution.image, numberOfWorkers);

                        for (int i = 0; i < numberOfWorkers; i++) {

                            WorkerData tmpWorker = dispoWorkers.get(i);

                            Object object = Naming.lookup(tmpWorker.linkRMI);
                            if (object instanceof Filters) {
                                Filters stub = (Filters) object;
                                // ...
                                SubMatrix result = stub.applyFilter(sub.get(i), dataConvolution.kernel);

                                AllSubMatrix.add(result);

                            } else {
                                // handle error or throw an exception
                                System.out.println("ro7 ro a7777");
                            }

                        }

                        int[][] convolution = ImageDivider.merge(AllSubMatrix, dataConvolution.image.length,
                                dataConvolution.image[0].length);

                        DataResult objectToSend = new DataResult(convolution);

                        ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                        oos.writeObject(objectToSend);

                        System.out.println("+(" + (newTask.TaskId) + ") : Result sent");

                    }

                }

            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int isDifficult(String taskName, int width, int height) {
         if (taskName.compareToIgnoreCase("CONVOLUTION FILTER") == 0) {
            if (width > 3000 && height > 3000)
                return 3;
            else
                return 1;
        } else
            return 0;
    }
}
