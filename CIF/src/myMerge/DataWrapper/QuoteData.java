package myMerge.DataWrapper;

/**
 * This is the data structure that save a quote data
 * @author Zhenghong Dong
 */
public class QuoteData {
	private final long		_secsFromEpoch;
	private final long		_millisecondsFromMidnight;
	private final int		_bidSize;
	private final float		_bidPrice;
	private final int		_askSize;
	private final float		_askPrice;
	private final String	_ticker;

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	public QuoteData(final long day, final long time, final int bs, final float bp, final int as, final float ap, final String ticker) {
		_bidSize = bs;
		_bidPrice = bp;
		_askPrice = ap;
		_askSize = as;
		_millisecondsFromMidnight = time;
		_ticker = ticker;
		_secsFromEpoch = day;
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public int getBidSize() 					{ return _bidSize; }
	public float getBidPrice() 					{ return _bidPrice; }
	public int getAskSize() 					{ return _askSize; }
	public float getAskPrice() 					{ return _askPrice; }
	public long getMillisecondsFromMidnight() 	{ return _millisecondsFromMidnight; }
	public String getTicker() 					{ return _ticker; }
	public long getSecsFromEpoch()				{ return _secsFromEpoch; }
}
