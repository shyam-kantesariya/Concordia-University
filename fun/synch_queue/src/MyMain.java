public class MyMain {
	public static void main(String[] args) {
		Account a=new Account();
		MyThread [] thr = new MyThread[10]; 
		
		for(int i=0;i<10;i++) {
			thr[i]=new MyThread(a);
		}
		for(int i=0;i<10;i++) {
			System.out.println("Thread "+i+" started");
			thr[i].start();
		}
	}
}
