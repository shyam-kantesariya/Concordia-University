import java.util.Random;

public class MyThread extends Thread{
	public Account ac;
	public void run() {
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		System.out.println("random number is "+n);
		for (int i=0;i<20;i++){
			if (n%2==0) {
				this.ac.d();
			}
			else {
				this.ac.w();
				try {
					Thread.sleep(123456);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public MyThread(Account a) {
		this.ac=a;
	}
}
