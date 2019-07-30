package callback.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Sparta on 6/20/2017.
 */
public interface Subscription extends Remote{
    public int subscribe(PushNotification clientRef) throws RemoteException;
    public void unsubscribe(int id) throws RemoteException;
}
