package thread.pausing;

public class WaitingForThreadToFinish implements Runnable {

	public void run() {
		System.out.println("Hi I'm the baby and I'm gonna sleep for 2 secs!");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Hi I slept for whole day and ate no food so now I have to die!");
	}

	public static void main( String[] args ) {
		WaitingForThreadToFinish thread = new WaitingForThreadToFinish();
		Thread t = new Thread( thread );
		System.out.println("I'm gonna have a baby I'm so happy!");
		t.start();
		
		// Do something else here
		
		try {
			t.join(); // Wait for thread to finish
		} catch (InterruptedException e) {
			
		}
		System.out.println("Shit happens...");
		
	}
	
}
