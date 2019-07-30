package callback.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Sparta on 6/20/2017.
 */
public class ServerMain {
    public static void main(String[] main) throws RemoteException, AlreadyBoundException, InterruptedException {
        ServerImpl serverObj = new ServerImpl();
        Registry rg = LocateRegistry.createRegistry(1234);
        rg.bind("ServerReference", serverObj);
        System.out.println("Server has started. It will notify all subscribers every two seconds");
        while(true){
            serverObj.notifySubscribers();
            Thread.sleep(4000);
        }
    }
}