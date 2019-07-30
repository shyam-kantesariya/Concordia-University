package exercise.client;

import exercise.interfaces.AddInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Sparta on 6/2/2017.
 */
public class ClientMain {
    public static void main(String[] args){
        new ClientClass(1,2).start();
        new ClientClass(1,3).start();
        new ClientClass(1,4).start();
        new ClientClass(1,5).start();
        new ClientClass(1,6).start();
    }
}