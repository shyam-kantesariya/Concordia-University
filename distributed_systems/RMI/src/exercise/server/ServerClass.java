package exercise.server;

import exercise.interfaces.AddInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Sparta on 6/2/2017.
 */
public class ServerClass extends UnicastRemoteObject implements AddInterface {

    public ServerClass() throws RemoteException {
        super();
    }

    public synchronized int add(int x, int y) throws RemoteException {
        System.out.println(this.getRef());

        for(int cntr=0; cntr<10;cntr++){
            if (cntr %2 == 0) try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread " + Thread.currentThread().getName() + "# Itr: "+cntr);
        }
        try {
            if (y %2 == 0) Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println(Thread.currentThread().getName());
        return x+y;
    }
}
