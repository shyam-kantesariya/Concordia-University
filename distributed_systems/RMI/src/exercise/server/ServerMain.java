package exercise.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Sparta on 6/2/2017.
 */
public class ServerMain {
    public static void main(String[] args) throws RemoteException {
        ServerClass obj = null;
        try {
            obj = new ServerClass();
            Registry rg = LocateRegistry.createRegistry(12345);
            rg.rebind("MyRemoteRef", obj);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Server is created");
    }
}