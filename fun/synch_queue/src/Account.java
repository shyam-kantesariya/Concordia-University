public class Account {
	int bal;
	public Account() {
		bal=10000;
	}
	
	public synchronized void w() {
			
 		try {
 				System.out.print("##############" + Thread.currentThread() + " Started w");
				Thread.sleep(1234);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		bal-=5;

			System.out.println("Ended w  ");
	}
	public synchronized void d() {
		System.out.print(Thread.currentThread() + " Started d");
		bal+=5;
		System.out.println("Ended d   ");
	}
}
