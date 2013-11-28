package myMultiThreadMonteCarlo;

import myMonteCarlo.DataWrapper.OptionType;

/**
 * @author Zhenghong Dong
 */
public interface I_PricingProblemSpecification {
	public double getCurrentPrice();

	public double getDailyVol();

	public double getDailyRate();

	public int getTTM();

	public double getStrike();

	public OptionType getType();
}
