package thread.synchronization;

/**
 * This class is thread safe
 * @author Lees
 *
 */
public class SynchronizationError_Fixed_Big {
	
	private int size;
	
	public void adjust() {
		synchronized( this ) {
			size = size + 1;
			if( size >= 100 ) {
				size = 0;
			}
		}
	}
	
	public void reset() {
		synchronized( this ) {
			size = 0;
		}
	}
	
}
