package RessourcesForRMI;

public class WorkerData{
    public String linkRMI;
    public int dispo;

    public WorkerData(String linkRMI,int dispo){
        this.linkRMI=linkRMI;
        this.dispo=dispo;
    }

    public WorkerData(String linkRMI){
        this.linkRMI=linkRMI;
        this.dispo=1;
    }
}