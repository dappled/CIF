package myDesignPatterns;

/**
 * Trade class, contains trade info including time, symbol, quantity and price
 * 
 * @author Zhenghong Dong
 * 
 */
public class Trade {
	private int time;
	private String symbol;
	private int quantity;
	private double price;

	public Trade(String[] args) throws Exception {
		if (args.length != 4)
			throw new Exception(
					"Invalid Trade format, should be: time, symbol, quantity, price");
		this.time = Integer.parseInt(args[0]);
		this.symbol = args[1];
		this.quantity = Integer.parseInt(args[2]);
		this.price = Double.parseDouble(args[3]);
	}

	/**
	 * return the Trade's string representation
	 */
	@Override
	public String toString() {
		return this.time + "," + this.symbol + "," + this.quantity + ","
				+ this.price;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}
