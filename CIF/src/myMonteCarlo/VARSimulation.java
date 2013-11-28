package myMonteCarlo;

import myMonteCarlo.Path.StockPath;
import myMonteCarlo.Path.VARPath;
import myMonteCarlo.PayOut.PayOut;
import myMonteCarlo.PayOut.VARPayout;

import org.joda.time.DateTime;

/**
 * @author Zhenghong Dong
 */
public class VARSimulation {
	private final int			_k;
	private final double		_per, _t;
	private final double[]		_price, _vol, _shares;
	private final double[][]	_cov;

	public VARSimulation(final int k, final double per, final double[] price, final double[] vol,
			final double[] shares, final double[][] cov, final double t) {
		_price = price;
		_vol = vol;
		_shares = shares;
		_cov = cov;
		_k = k;
		_t = t;
		_per = per;
	}

	private double monteCarloError(final int n, final double analytical) {
		double res = 0;
		StockPath path;
		PayOut po;
		for (int i = 0; i < _k; i++) {
			path = new VARPath( DateTime.now(), _price, _vol, _shares, _cov, _t, n );
			po = new VARPayout( _per );
			res += Math.pow( po.getPayout( path ) - analytical, 2 );
		}

		return res/_k;
	}

	public void errorTrack(final int[] nList, final double analytical){
		for (int i = 0; i < nList.length; i++) {
			System.out.printf("MonteCarlo Simulation Mean Square Error for %d simulations is %.5f\n", nList[i], monteCarloError( nList[i], analytical ));
		}
	}

	public static final void main(final String[] args) {
		final double[] price = { 47, 144, 31 };
		final double[] vol = { 0.4, 0.45, 0.3 };
		final double[] shares = { 100000L, -40000L, 10000L };
		final double[][] cov = { { 1, 0.6, 0.2 },
				{ 0.6, 1, 0.4 },
				{ 0.2, 0.4, 1 } };
		final double t = 1 / 252.0;
		final double percentile = 0.01;
		final VARSimulation sim = new VARSimulation( 20, percentile, price, vol, shares, cov, t );
		final double analytical = -303568.21;
		final int[] nSimulation = {1000, 5000, 10000, 50000, 100000, 1000000};
		sim.errorTrack(nSimulation, analytical);
		
		/* final double var = (100000L*100000L*47*47*0.4*0.4+ //C
		 * (-40000L)*(-40000L)*144*144*0.45*0.45+ //GS
		 * 10000L*10000L*31*31*0.3*0.3+ //MSFT
		 * 100000L*(-40000L)*47*144*0.4*0.45*0.6+ //C,GS
		 * 100000L*10000L*31*47*0.4*0.3*0.2+ //C,MSFT
		 * (-40000L)*10000L*144*31*0.45*0.3*0.4)* //GS,MSFT
		 * t;
		System.out.printf( "Analytical: %f\nSimulation: %f\n",
				-303, 568.21F, sim.getVAR( 10000 ) );
		*/
	}
}
