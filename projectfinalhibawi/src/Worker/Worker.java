package Worker;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

public class Worker {
    // TODO
    /*
     * Listen to MainServer
     * Send OK
     * Recieve Task + Data
     * (The data depends on the type of task)
     * Execute Task
     * Send Result to MainServer
     * 
     * 
     */
    
    static int Worker_port;
    static String Worker_host;
    public static void main(String[] args) throws UnknownHostException {
        /* 
        * Read Proprieties from file
        */
        // Properties prop=new Properties();
        // FileInputStream ip;
        // //Par defaut
        // String FileConfiguration= "cfgWorker.properties";
        // if(args.length>0)
        //     FileConfiguration = args[0];
        // try {
        //     ip = new FileInputStream(FileConfiguration);
        //     prop.load(ip);
        // } catch (Exception e2) {
        //     System.exit(0);
        // }
        Worker_port =Integer.parseInt(args[0]);
        
        InetAddress localHost = InetAddress.getLocalHost();
            // Get the IP address of the local host
            String ipAddress = localHost.getHostAddress();
            // Print the IP address of the local host
            System.out.println("Worker is running at  " + ipAddress +":"+ Worker_port+"/Worker" );
            
            Worker_host = ipAddress;

        /* 
        * Run the socket of Worker
        */
       
        
        try {
            LocateRegistry.createRegistry(Worker_port);
            ImplFilters ob = new ImplFilters();
           
            Naming.rebind("rmi://"+Worker_host+":"+Worker_port+"/Worker", ob);
            
            System.out.println("server ready.........");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
     
}
