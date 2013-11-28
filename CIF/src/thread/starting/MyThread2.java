package thread.starting;

public class MyThread2 implements Runnable {

	public void run() {
		// Do something here
	}

	public static void main( String[] args ) {
		MyThread2 thread = new MyThread2();
		Thread t = new Thread( thread );
		t.start();
	}
	
}
