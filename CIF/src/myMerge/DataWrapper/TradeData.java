package myMerge.DataWrapper;

/**
 * This is the data structure that save a trade data
 * @author Zhenghong Dong
 */
public class TradeData {
	private final long		_secsFromEpoch;
	private final long		_millisecondsFromMidnight;
	private final int		_size;
	private final float		_price;
	private final String	_ticker;

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	public TradeData(final long day, final long time, final int size, final float price, final String ticker) {
		_size = size;
		_price = price;
		_millisecondsFromMidnight = time;
		_secsFromEpoch = day;
		_ticker = ticker;
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public int getSize() 						{ return _size; }
	public float getPrice() 					{ return _price; }
	public long getMillisecondsFromMidnight() 	{ return _millisecondsFromMidnight; }
	public String getTicker() 					{ return _ticker; }
	public long	getSecsFromEpoch()				{ return _secsFromEpoch; }
}
