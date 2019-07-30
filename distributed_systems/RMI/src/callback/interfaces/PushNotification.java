package callback.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Sparta on 6/20/2017.
 */
public interface PushNotification extends Remote {
    public void notify(String message) throws RemoteException;
}
