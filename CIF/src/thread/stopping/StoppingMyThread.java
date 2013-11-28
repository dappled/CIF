package thread.stopping;


public class StoppingMyThread extends Thread {

	private boolean done = false;
	
	public void run() {
		while( ! done ) {
			System.out.println( "Thread running!" );
			try {
				sleep( 1000 );
			} catch( InterruptedException ex ) {
			}
		}
		System.out.println( "Thread finished" );
	}
	
	public void setDone() { done = true; }

	public static void main( String[] args ) {
		StoppingMyThread thread = new StoppingMyThread();
		thread.start();
		try {
			Thread.sleep( 5500 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread.setDone();
	}
	
}
