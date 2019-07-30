package exercise.client;

import exercise.interfaces.AddInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Sparta on 6/2/2017.
 */
public class ClientClass extends Thread{
    int x;
    int y;
    public ClientClass(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void run(){
        Thread.currentThread().setName("I am" + x+"+"+y);
        try {
            Registry rg = LocateRegistry.getRegistry(12345);
            AddInterface adObj = (AddInterface) rg.lookup("MyRemoteRef");
            System.out.println(x +"+"+y+"=" + adObj.add(x,y));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
