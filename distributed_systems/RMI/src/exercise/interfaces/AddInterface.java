package exercise.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Sparta on 6/2/2017.
 */
public interface AddInterface extends Remote {
    public int add(int x, int y) throws RemoteException;
}
