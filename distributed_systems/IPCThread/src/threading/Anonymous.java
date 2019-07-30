package threading;

/**
 * Created by Sparta on 5/17/2017.
 */
public class Anonymous {
    public static void main(String[] args)
    {
        System.out.println("I am: " + Thread.currentThread().getId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("I am: " + Thread.currentThread().getId());
            }
        }).start();
    }
}
