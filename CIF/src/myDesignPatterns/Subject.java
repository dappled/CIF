package myDesignPatterns;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Zhenghong Dong
 * 
 */
public class Subject extends Observable {
	private String fileLocation;

	public Subject(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	/**
	 * expose the ability to subscribe from the data stream
	 */
	public synchronized void subscribe(Observer o) {
		this.addObserver(o);
	}

	/**
	 * expose the ability to unsubscribe from the data stream
	 */
	public synchronized void unsubscribe(Observer o) {
		this.deleteObserver(o);
	}

	/**
	 * Use the TradeIterator to read the file and distribute data to listeners.
	 */
	public void run() throws Exception {
		TradeIterator tradeIterator = new TradeIterator(this.fileLocation);
		Trade trade = null;
		for (; tradeIterator.hasNext();) {
			trade = tradeIterator.next();
			// distribute data only if iterator's next data is valid trade info
			if (trade != null) {
				this.setChanged();
				this.notifyObservers(trade);
			} else { // if not, ignore the error and skip to next line
				System.err.println("Skip this invalid line");
				// sleep a little to wait err message to be printed
				Thread.sleep(5);
				continue;
			}
		}
		tradeIterator.close();
	}

	/**
	 * called when we want to run on other files
	 * 
	 * @param fileLocation
	 *            : new file location
	 */
	public void setTradeIterator(String fileLocation) throws Exception {
		this.fileLocation = fileLocation;
	}

}
