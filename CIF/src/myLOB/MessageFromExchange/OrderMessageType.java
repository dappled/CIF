package myLOB.MessageFromExchange;

public class OrderMessageType {
	public static final OrderMessageType	Reject	= new OrderMessageType( "Rejected" );
	public static final OrderMessageType	Accept	= new OrderMessageType( "Accepted" );
	public static final OrderMessageType	Fill	= new OrderMessageType( "Fill" );

	private final String							_type;

	private OrderMessageType(final String type) 	{ _type = type; }

	@Override
	public String toString() 						{ return String.format( "ExchangeMessageType: %s", _type ); }
}
