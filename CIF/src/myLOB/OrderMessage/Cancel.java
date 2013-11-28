package myLOB.OrderMessage;

import myLOB.Exchange;

public class Cancel implements OrderMessage {
	private final OrderMessageType	_type;
	private final long				_clientId;
	private final long				_exchangeOrderId;

	/***********************************************************************
	 * Static Factory
	 ***********************************************************************/
	private Cancel(final long clinetId, final long exchangeOrderId) {
		_type = OrderMessageType.Add;
		_clientId = clinetId;
		_exchangeOrderId = exchangeOrderId;
	}

	public static Cancel newMessage(final long clientId, final long exchangeOrderId) {
		return new Cancel( clientId, exchangeOrderId );
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public long getExchangeOrderId() 						{ return _exchangeOrderId; }

	/***********************************************************************
	 * {@link OrderMessage} methods
	 ***********************************************************************/
	@Override
	public long getClientId() 								{ return _clientId; }
	@Override
	public OrderMessageType getType() 						{ return _type; }
	@Override
	public void getProccessedBy(final Exchange exchange) 	{ exchange.cancel( this );	}
}
