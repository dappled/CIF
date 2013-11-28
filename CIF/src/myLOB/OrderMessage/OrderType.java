package myLOB.OrderMessage;

public class OrderType {
	public static final OrderType	ORDINARY	= new OrderType( "Ordinary limit order" );
	public static final OrderType	FOK			= new OrderType( "Fill or kill" );
	public static final OrderType	IOC			= new OrderType( "Immediate or cancel" );

	private String					_type;
	
	private OrderType(String type) 	{ _type = type; }
	
	public String toString() 		{ return String.format( "OrderType(%s)", _type ); }
}
