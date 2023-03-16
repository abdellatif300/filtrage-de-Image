package RessourcesForRMI;

import java.util.ArrayList;

public class WorkerDataList {
    public static ArrayList<WorkerData> ListWorkers = new ArrayList<WorkerData>();

    public static void addWorker(String linkRMI){
        ListWorkers.add(new WorkerData(linkRMI));
    }


    public static ArrayList<WorkerData> DispoWorkers(){
        ArrayList<WorkerData> tmpWokers=new ArrayList<WorkerData>();
        for (WorkerData tmpWorker : ListWorkers) {
            if(tmpWorker.dispo==1){
                tmpWokers.add(tmpWorker);
            }
        }
        return tmpWokers;
    }
}

// Hello stub = (Hello) Naming.lookup("rmi://localhost:1099/BK");



