package myLOB;

/**
 * @author Zhenghong Dong
 */
public class OrderSide {
	public static final OrderSide LONG = new OrderSide("long");
	public static final OrderSide SHORT = new OrderSide("short");
	
	private String _side;
	
	private OrderSide(String side) 	{ _side = side; }
	
	public String toString() 		{ return String.format( "OrderSide(%s)", _side ); }
}
