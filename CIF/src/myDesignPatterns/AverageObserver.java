package myDesignPatterns;

import java.util.Observable;
import java.util.Observer;

/**
 * print out average trade price
 * 
 * @author Zhenghong Dong
 * 
 */
public class AverageObserver implements Observer {
	private double sum = 0;
	private long num = 0L;

	@Override
	public void update(Observable subject, Object arg) {
		Trade trade = (Trade) arg;
		num++;
		sum += trade.getPrice();
		System.out.format("Average Price: %.2f%n", sum / num);
	}

}
