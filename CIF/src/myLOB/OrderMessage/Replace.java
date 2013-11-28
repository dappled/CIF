package myLOB.OrderMessage;

import myLOB.Exchange;

public class Replace implements OrderMessage {
	private final OrderMessageType	_type;
	private final long				_clientId;
	private final int				_quantity;
	private final long				_exchangeOrderId;

	/***********************************************************************
	 * Static Factory
	 ***********************************************************************/
	private Replace(final long clientId, final long exchangeOrderId, final int newQuantity) {
		_type = OrderMessageType.Replace;
		_clientId = clientId;
		_quantity = newQuantity;
		_exchangeOrderId = exchangeOrderId;
	}

	public static Replace newMessage(final long clientId, final long exchangeOrderId,
			final int newQuantity) {
		return new Replace( clientId, exchangeOrderId, newQuantity );
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public int getNewQuantity() 							{ return _quantity; }
	public long getExchangeOrderId() 						{ return _exchangeOrderId; }

	/***********************************************************************
	 * {@link OrderMessage} methods
	 ***********************************************************************/
	@Override
	public long getClientId() 								{ return _clientId; }
	@Override
	public OrderMessageType getType() 						{ return _type; }
	@Override
	public void getProccessedBy(final Exchange exchange) 	{ exchange.replace( this ); }

}
