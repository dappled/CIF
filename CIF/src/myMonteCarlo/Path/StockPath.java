package myMonteCarlo.Path;

import java.util.List;

import myMonteCarlo.DataWrapper.PriceRecord;

/**
 * The interface for creating StockPath. The returned map should be ordered by date
 * 
 * @author Zhenghong Dong
 * 
 */
public interface StockPath {
	/**
	 * @return a List representing one possible stock path, with each element being PriceRecord holding one day's date and price 
	 */
	public List<PriceRecord> getPrices();
}
