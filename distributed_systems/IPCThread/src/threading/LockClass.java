package threading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Sparta on 5/18/2017.
 */
public class LockClass {
    public static void main(String[] args){
        Lock l = new ReentrantLock();
        l.lock();
        try{
            System.out.println("Hello World I am Thread: " + Thread.currentThread().getId());
        }
        finally {
            l.unlock();
        }
    }
}
