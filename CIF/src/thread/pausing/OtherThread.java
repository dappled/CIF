package thread.pausing;

public class OtherThread extends Thread {
	
	protected boolean _doWait;
	
	public OtherThread() {
		_doWait = false;
	}
	
	public void setWait( boolean trueOrFalse ) {
		synchronized( this ) {
			System.out.println( "OtherThread: My doWait flag has been set to " + trueOrFalse );
			_doWait = trueOrFalse;
		}
	}
	
	public void run() {
		while( true ) {
			synchronized( this ) { 
				while( _doWait ) {
					System.out.println( "OtherThread: I am going to try to enter a wait state.." );
					try {
						wait();
						System.out.println( "OtherThread: I've been awakened from my sleep state...");
					} catch( Exception e ) {
					}
					if( !_doWait ) {
						System.out.println( "OtherThread: My doWait flag is off so I am going to do some work now..." );
						try {
							Thread.sleep( 2000 ); // In real life this would be some real work, but here, we just sleep for 2 seconds
						} catch (InterruptedException e1) {
						}
						System.out.println( "OtherThread: Finished doing some work..." );
					}
				}
			}
		}
	}
}