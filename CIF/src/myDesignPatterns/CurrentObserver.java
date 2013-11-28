package myDesignPatterns;

import java.util.Observable;
import java.util.Observer;

/**
 * Print out current trade info
 * 
 * @author Zhenghong Dong
 * 
 */
public class CurrentObserver implements Observer {

	@Override
	public void update(Observable subject, Object arg) {
		Trade trade = (Trade) arg;
		System.out.println("Current Trade: " + trade.toString());
	}
}
