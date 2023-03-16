package MainServer;


import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import RessourcesForRMI.WorkerDataList;

public class MainServer extends Thread {
    // TODO
    /*
     * Listen to clientssssssssss
     * push to TaskQueue
     */
    // ! FOR TaskSchedule
    // ? Loop in TaskQueue
    // ? Define difficulty
    // ? Check disponibily of Worker(i)
    // ? Send PartTask To Workers
    // ? Recieve from Worker
    /*
     * Build Data
     * Send to client
     */

    private static Executor executor;
    private static TaskQueue taskQueue;
    private static int TaskID=100;

    static int MainServer_port;
    static String MainServer_host;
    public static void main(String[] args) {

        /* 
        * Read Proprieties from file
        */
        Properties prop=new Properties();
        FileInputStream ip;
        //Par defaut
        String FileConfiguration= "cfgMainServer.properties";
        if(args.length>0)
            FileConfiguration = args[0];
        try {
            ip = new FileInputStream(FileConfiguration);
            prop.load(ip);
        } catch (Exception e2) {
            System.exit(0);
        }
        


        /* 
        * Run the socket of Worker
        */
        MainServer_port = Integer.parseInt(prop.getProperty("MainServer.port"));
        MainServer_host = prop.getProperty("MainServer.host");
        // Creating an object of ServerSocket class
        // in the main() method for socket connection

        /*
        * *
        * GET SELVERS
        */
        getSelvers(prop);
        System.out.println("Number of Workers :"+WorkerDataList.ListWorkers.size());


        ServerSocket ss;
        try {
            ss = new ServerSocket(MainServer_port);

            executor = Executors.newFixedThreadPool(10);
            taskQueue = new TaskQueue();

            // ? Make it nonblocking
            ss.setReuseAddress(true);
            ss.setSoTimeout(0);

            System.out.println("Started at port "+MainServer_port);

            // !Lunch Mini Workers Threads executing the Queue
            for (int i = 0; i < 7; i++) {
                executor.execute(new ServerThread(taskQueue));
            }
            
            // !Listen to client
            while (true) {
                // Accept the socket from client
                Socket soc = ss.accept();
                System.out.println("+("+TaskID+") : New Task in queue");
                Task newTask = new Task(soc,TaskID++);
                taskQueue.add(newTask);
            }

        } catch (Exception e1) {
        }
    }



    public static int getSelvers(Properties prop) {

        String hostSlv = prop.getProperty("Worker1.host");
        String portSlv = prop.getProperty("Worker1.port");
        int i=1;
        while(hostSlv != null){
            WorkerDataList.addWorker("rmi://"+hostSlv+":"+portSlv+"/Worker");
            i++;
            
            hostSlv = prop.getProperty("Worker"+i+".host");
            portSlv = prop.getProperty("Worker"+i+".port");
        }
        // listSlevers.add(new ListSlevers("100.100.33.150", 1111));
        return i-1;
    }
}
