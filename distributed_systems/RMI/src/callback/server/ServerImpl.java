package callback.server;

import callback.interfaces.PushNotification;
import callback.interfaces.Subscription;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Created by Sparta on 6/20/2017.
 */
public class ServerImpl extends UnicastRemoteObject implements Subscription {
    int subscriptionCounter;
    HashMap<Integer, PushNotification> subscriberList ;
    public ServerImpl() throws RemoteException{
        super();
        subscriptionCounter = 0;
        subscriberList = new HashMap<Integer, PushNotification>();
    }
    public int subscribe(PushNotification subscriber){
        subscriptionCounter++;
        subscriberList.put(subscriptionCounter, subscriber);
        return subscriptionCounter;
    }
    public void unsubscribe(int subscriberId){
        subscriberList.remove(subscriberId);
    }
    public void notifySubscribers(){
        for(int key:subscriberList.keySet()){
            try {
                subscriberList.get(key).notify("Push message to Client: " + key);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
