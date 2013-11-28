package thread.pausing;

public class MainThread {
	
	protected boolean _hasMoreWork;
	
	public MainThread() {
		_hasMoreWork = true;
	}
	
	public void launch() {
		
		// Launch other thread

			System.out.println( "MainThread: Putting OtherThread into wait state and starting it..." );
			OtherThread t = new OtherThread();
			t.setWait( true );
			t.start();
			
		while( true ) { 

			// About to start work. Tell other thread to stop working.
			
				System.out.println( "MainThread: About to put OtherThread into wait state..." );
				synchronized( t ) {
					t.setWait(true);
				}
				
			// Do work
				
				System.out.println( "MainThread: I'm doing some work for 2 seconds...");

			// Sleep for two seconds
				
				try {
					Thread.sleep( 2000 ); // Goes to sleep for two seconds but would presumably do some work here
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			// Tell other thread to do some work
				
				System.out.println( "MainThread: Finished doing some work and telling Thread to wake up..." );
				
				synchronized( t ) {
					t.setWait(false);
					t.notify();
				}
				
				System.out.println( "MainThread: Told other thread to wake up. Going to sleep for 2 seconds..." );
			
			// Sleep for two seconds
				
				try {
					Thread.sleep( 2000 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

		}
		
	}
	
	public static void main( String[] args ) {
		MainThread soc = new MainThread();
		soc.launch();
	}

}
