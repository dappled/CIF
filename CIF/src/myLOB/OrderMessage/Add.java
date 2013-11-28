package myLOB.OrderMessage;

import myLOB.Exchange;
import myLOB.OrderSide;

public class Add implements OrderMessage {
	private final OrderMessageType	_type;
	private final long				_clientId;
	private final long				_clientMessageId;
	private final String			_ticker;
	private final OrderSide			_side;
	private final int				_quantity;
	private final double			_price;
	private final OrderType			_orderType;

	/***********************************************************************
	 * Static Factory
	 ***********************************************************************/
	private Add(final long clinetId, final long clientMessageId, final String ticker, final OrderSide side,
			final int quantity, final double price,
			final OrderType orderType) {
		_type = OrderMessageType.Add;
		_clientId = clinetId;
		_clientMessageId = clientMessageId;
		_ticker = ticker;
		_side = side;
		_quantity = quantity;
		_price = price;
		_orderType = orderType;
	}

	public static Add newMessage(final long clinetId, final long clientMessageId, final String ticker,
			final OrderSide side, final int quantity,
			final double price, final OrderType orderType) {
		return new Add( clinetId, clientMessageId, ticker, side, quantity, price, orderType );
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public String getTicker() 								{ return _ticker; }
	public OrderSide getSide() 								{ return _side; }
	public int getQuantity() 								{ return _quantity; }
	public double getPrice()	 							{ return _price; }
	public OrderType getOrderType()		 					{ return _orderType; }
	public long getClientMessageId() 						{ return _clientMessageId; }
	
	/***********************************************************************
	 * {@link OrderMessage} methods
	 ***********************************************************************/
	@Override
	public long getClientId() 								{ return _clientId; }
	@Override
	public OrderMessageType getType() 						{ return _type; }
	@Override
	public void getProccessedBy(final Exchange exchange) 	{ exchange.add( this );	}
}
