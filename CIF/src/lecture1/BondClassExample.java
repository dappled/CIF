package lecture1;

/**
 * This class abstracts a bond. It provides various methods for
 * setting parameters and getting information about this bond.
 *
 * @author eran
 *
 */
public class BondClassExample {
	
	private double faceValue;
	private double maturity;
	private double r;
	
	/**
	 * @param faceValue The bond face value.
	 * @param maturity The maturity for the bond.
	 * 
	 */
	public BondClassExample(double faceValue, double maturity){
		this.faceValue = faceValue;
		this.maturity = maturity;
	}
	
	/**
	 * @param faceValue The bond face value.
	 * @param maturity The maturity for the bond.
	 * @param r The market interest rate.
	 */
	public BondClassExample(double faceValue, double maturity, double r){
		this(faceValue, maturity);
		this.r = r;
	}
	
	
	/**
	 * @return This object estimated market rate.
	 */
	public double getR(){
		return r;
	}
	
	/**
	 * @return The face value for the bond.
	 */
	public double getFaceValue(){
		return faceValue;
	}
	
	/**
	 * @return The maturity of the bond in years.
	 */
	public double getMaturity(){
		return maturity;
	}
	
	/**
	 * @return The fair price for the bond. NaN if this could not be
	 * calculated.
	 */
	public double getFairPrice(){
		return Double.NaN;
	}

}
