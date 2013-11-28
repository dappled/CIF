package thread.starting;

public class MyThread3 {

	protected Thread t;
	public MyThread3() {
		t = new Thread( 
			new Runnable() {
				public void run() {
					// Do something here
				}
			}
		);
		t.start();
	}
	
	public static void main( String[] args ) {
		new MyThread3();
	}
	
}
