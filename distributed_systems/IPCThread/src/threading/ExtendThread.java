package threading;

/**
 * Created by Sparta on 5/17/2017.
 */
public class ExtendThread extends Thread{
    public void run()
    {
        System.out.println("My ID is: " + this.getId());
    }
    public static void main(String[] args)
    {
        new ExtendThread().start();
        new ExtendThread().start();
        new ExtendThread().start();
    }
}
