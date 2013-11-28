package myDesignPatterns;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * TradeIterator class, can be used to iterate through a trade info file
 * 
 * @author Zhenghong Dong
 * 
 */
public class TradeIterator implements Iterator<Trade> {
	private final BufferedReader bufferedReader;

	public TradeIterator(String fileLocation) throws Exception {
		this.bufferedReader = new BufferedReader(new FileReader(fileLocation));
	}

	/**
	 * test if there is another line can be read off
	 */
	@Override
	public boolean hasNext() {
		try {
			this.bufferedReader.mark(100);
			if (this.bufferedReader.readLine() == null)
				return false;
			this.bufferedReader.reset();
			return true;
		} catch (IOException e) {
			System.err.println("Fail to call hasNext: " + e.getMessage());
			return false;
		}
	}

	/**
	 * try to read a new line and get trade info
	 */
	@Override
	public Trade next() {
		if (!hasNext()) {
			this.close();
			throw new NoSuchElementException();
		}
		try {
			return new Trade(this.bufferedReader.readLine().split(","));
		} catch (Exception e) {
			System.err.println("Fail to call Next: " + e.getMessage());
			return null;
		}
	}

	@Override
	public void remove() {
	}

	/**
	 * close the bufferedReader if called
	 */
	public void close() {
		try {
			this.bufferedReader.close();
		} catch (IOException e) {
			// ignore this Error
		}
	}
}
