package myLOB.OrderMessage;

public class OrderMessageType {
	public static final OrderMessageType	Add		= new OrderMessageType( "Add" );
	public static final OrderMessageType	Cancel	= new OrderMessageType( "Cancel" );
	public static final OrderMessageType	Replace	= new OrderMessageType( "Replace" );

	private final String							_type;

	private OrderMessageType(final String type) 	{ _type = type; }

	@Override
	public String toString() 						{ return String.format( "ClientMessageType: %s", _type ); }
}
