package RessourcesForRMI;
import java.rmi.Remote;
import java.rmi.RemoteException;

import MainServer.ImageDivider.SubMatrix;
/**
 * Filters
 */
public interface Filters extends Remote {

    public SubMatrix applyFilter(SubMatrix inputMatrix, int[][] ker) throws RemoteException;

}