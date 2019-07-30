package callback.client;

import callback.interfaces.PushNotification;
import callback.interfaces.Subscription;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Sparta on 6/20/2017.
 */
public class ClientMain {
    public static void main(String[] main) throws RemoteException {
        ClientImpl clientObj1 = new ClientImpl();
        ClientImpl clientObj2 = new ClientImpl();
        ClientImpl clientObj3 = new ClientImpl();
        try {
            Registry rg = LocateRegistry.getRegistry(1234);
            Subscription serverRef = (Subscription) rg.lookup("ServerReference");
            clientObj1.setSubscriptionId(serverRef.subscribe((PushNotification)clientObj1));
            clientObj2.setSubscriptionId(serverRef.subscribe((PushNotification)clientObj2));
            clientObj3.setSubscriptionId(serverRef.subscribe((PushNotification)clientObj3));
            Thread.sleep(5000);
            System.out.println("Seems like Client " + clientObj1.getSubscriptionId() + " is not happy with this subscription " +
                    "lets unsubscribe it.");
            serverRef.unsubscribe(clientObj1.getSubscriptionId());
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
