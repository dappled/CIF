package myMultiThreadMonteCarlo;

import myMonteCarlo.DataWrapper.OptionType;

/**
 * @author Zhenghong Dong
 */
public class MonteCarloPricingProblemSpecification implements I_PricingProblemSpecification {
	private final OptionType	_type;
	private final double		_currentPrice;
	private final double		_dailyVol;
	private final double		_dailyRate;
	private final int			_ttm;
	private final double		_strike;

	public MonteCarloPricingProblemSpecification(final OptionType type, final double currentPrice,
			final double dailyVol, final double dailyRate, final int TTM, final double strike) {
		_type = type;
		_currentPrice = currentPrice;
		_dailyVol = dailyVol;
		_dailyRate = dailyRate;
		_ttm = TTM;
		_strike = strike;
	}

	@Override
	public double getCurrentPrice() {
		return _currentPrice;
	}

	@Override
	public double getDailyVol() {
		return _dailyVol;
	}

	@Override
	public double getDailyRate() {
		return _dailyRate;
	}

	@Override
	public int getTTM() {
		return _ttm;
	}

	@Override
	public double getStrike() {
		return _strike;
	}

	@Override
	public OptionType getType() {
		return _type;
	}
}
