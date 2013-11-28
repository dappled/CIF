package myLOB.MessageFromExchange;

public class Accepted implements MessageFromExchange {
	private final OrderMessageType	_type;
	private final long				_clientId;
	private final long				_exchangeOrderId;
	private final long				_clientMessageId;

	/***********************************************************************
	 * Static Factory
	 ***********************************************************************/
	private Accepted(final long clinetId, final long exchangeOrderId, final long clientMessageId) {
		_type = OrderMessageType.Accept;
		_clientId = clinetId;
		_exchangeOrderId = exchangeOrderId;
		_clientMessageId = clientMessageId;
	}

	public static Accepted newMessage(final long clientId, final long exchangeOrderId, final long clientMessageId) {
		return new Accepted( clientId, exchangeOrderId, clientMessageId );
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public long getClientId() 			{ return _clientId; }
	public long getExchangeOrderId()	{ return _exchangeOrderId; }
	public long getClientMessageId() 	{ return _clientMessageId; }

	/***********************************************************************
	 * {@link MessageFromExchange} methods
	 ***********************************************************************/
	@Override
	public OrderMessageType getType() 	{ return _type; }
}
