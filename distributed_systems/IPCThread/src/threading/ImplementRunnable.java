package threading;

/**
 * Created by Sparta on 5/17/2017.
 */
public class ImplementRunnable implements Runnable {
    public void run()
    {
        System.out.println("My ID is: " + Thread.currentThread().getId());
    }
    public static void main(String[] args)
    {
        new Thread(new ImplementRunnable()).start();
        new Thread(new ImplementRunnable()).start();
        new Thread(new ImplementRunnable()).start();
    }
}
