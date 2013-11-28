package thread.starting;

public class MyThread extends Thread {

	public void run() {
		// Do something here
	}

	public static void main( String[] args ) {
		MyThread thread = new MyThread();
		thread.start();
	}
	
}
