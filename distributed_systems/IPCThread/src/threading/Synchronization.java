package threading;

/**
 * Created by Sparta on 5/17/2017.
 */
public class Synchronization {
    public static void main(String[] args){
        counter cntr = new counter();
        new Thread(new CounterUsage(cntr, Operation.INCR)).start();
        new Thread(new CounterUsage(cntr, Operation.INCR)).start();
        new Thread(new CounterUsage(cntr, Operation.DISPLAY)).start();
        new Thread(new CounterUsage(cntr, Operation.DECR)).start();
        new Thread(new CounterUsage(cntr, Operation.DECR)).start();
        new Thread(new CounterUsage(cntr, Operation.DISPLAY)).start();
    }
}

enum Operation{
    INCR, DECR, DISPLAY
}

class CounterUsage implements Runnable{
    counter cntrObj;
    Operation opr;
    CounterUsage(counter cntObj, Operation opr){
        cntrObj = cntObj;
        this.opr = opr;
    }
    public void run(){
        switch (opr){
            case DECR:
                cntrObj.decrement();
                break;
            case INCR:
                cntrObj.increment();
                break;
            default:
                cntrObj.getCntr();
        }
    }
}

class counter{
    int cntr;
    public counter(int x)
    {
        cntr = x;
    }
    public counter()
    {
        cntr = 0;
    }
    public synchronized void increment()
    {
        cntr ++;
        System.out.println("Value of counter is: " + cntr);
    }
    public synchronized void decrement()
    {
        cntr --;
        System.out.println("Value of counter is: " + cntr);
    }
    public synchronized void getCntr() {
        System.out.println("value of counter is: " + cntr);
    }
}
