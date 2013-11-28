package myLOB.MessageFromExchange;

public class Fill implements MessageFromExchange {
	private final OrderMessageType	_type;
	private final int				_quantity;
	private final long				_exchangeOrderId;

	/***********************************************************************
	 * Static Factory
	 ***********************************************************************/
	private Fill(final int quantity, final long exchangeOrderId) {
		_type = OrderMessageType.Fill;
		_quantity = quantity;
		_exchangeOrderId = exchangeOrderId;
	}

	public static Fill newMessage(final int quantity, final long id) {
		return new Fill( quantity, id );
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public int getQuantity() 			{ return _quantity; }
	public long getExchangeOrderId() 	{ return _exchangeOrderId; }

	/***********************************************************************
	 * {@link MessageFromExchange} methods
	 ***********************************************************************/
	@Override
	public OrderMessageType getType() 	{ return _type;}

}
