package myMonteCarlo.PayOut;

import myMonteCarlo.Path.StockPath;

/**
 * The interface for calculating the payout function
 * 
 * @author Zhenghong Dong
 * 
 */
public interface PayOut {
	/**
	 * @param path one single possible path of the underlying
	 * @return the payout given the path
	 */
	public double getPayout(StockPath path);
}
