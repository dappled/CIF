package myMonteCarlo.DataWrapper;

import org.joda.time.DateTime;

/**
 * Price record
 * 
 * @author Zhenghong Dong
 *
 */
public class PriceRecord implements Comparable<PriceRecord>{

	private final DateTime		_date;
	private final double		_price;

	public DateTime getDate() { return _date;  }

	public double getPrice()  { return _price; }

	/**
	 * Instantiate a price record
	 * 
	 * @param date
	 * @param price
	 */
	public PriceRecord( final DateTime date, final double price){
		_date = date;
		_price = price;
	}

	/** Converts a PriceRecord object to a String representation */
	@Override
	public String toString() {
		return String.format( "%s,%.2f", _date.toString(), _price );
	}

	@Override
	public int compareTo(PriceRecord o) {
		return Double.compare( _price, o.getPrice() );
	}
}
