package callback.client;

import callback.interfaces.PushNotification;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Sparta on 6/20/2017.
 */
public class ClientImpl extends UnicastRemoteObject implements PushNotification {
    private int subscriptionId;

    public ClientImpl() throws RemoteException {
        super();
    }

    public void notify(String message) {
        System.out.println("Message from Server is: " + message);
    }

    public void setSubscriptionId(int val) {
        subscriptionId = val;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }
}
