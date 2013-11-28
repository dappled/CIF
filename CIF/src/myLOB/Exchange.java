package myLOB;

import java.util.HashMap;

import myLOB.MessageFromExchange.Accepted;
import myLOB.MessageFromExchange.Fill;
import myLOB.MessageFromExchange.MessageFromExchange;
import myLOB.MessageFromExchange.Rejected;
import myLOB.OrderMessage.Add;
import myLOB.OrderMessage.Cancel;
import myLOB.OrderMessage.OrderMessage;
import myLOB.OrderMessage.OrderType;
import myLOB.OrderMessage.Replace;

/**
 * @author Zhenghong Dong
 */
public class Exchange {
	private final HashMap<String, Book>	_books;
	private final HashMap<Long, Order>	_orders;
	private final HashMap<Long, Long>	_messages;
	private long						_idOrder;	// choose long because the amount of order everyday is rediculous

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	public Exchange() {
		_books = new HashMap<>();
		_orders = new HashMap<>();
		_messages = new HashMap<>();
		_idOrder = 0;
	}

	/***********************************************************************
	 * Utility
	 ***********************************************************************/
	/**
	 * Create a new book for ticker
	 * @param ticker the ticker
	 */
	public void createBook(final String ticker) {
		_books.put( ticker, new Book() );
	}

	/**
	 * Send a message to client
	 * @param msgFromExchagne
	 */
	public void sentToClient(final MessageFromExchange msgFromExchagne) {}

	/**
	 * Do something according to the receiving message
	 * @param orderMessage the received message
	 * @throws Exception when message is inapproporate
	 */
	public void receiveMessage(final OrderMessage orderMessage) {
		orderMessage.getProccessedBy( this );
	}

	/**
	 * Cancel an order
	 * @param orderMessage contains the information of the order
	 */
	public void cancel(final Cancel orderMessage) {
		final long exchangeOrderId = orderMessage.getExchangeOrderId();
		final long clientId = orderMessage.getClientId();
		final long clientMessageId;

		// reject if the order doesn't exist, from the email it seems like we can assume this condition will not happen
		try {
			clientMessageId = _messages.get( exchangeOrderId );
		} catch (final Exception e) {
			final MessageFromExchange reject = Rejected.newMessage( clientId, -1,
					"The order you want to cancel doesn't exist." );
			sentToClient( reject );
			return;
		}

		try {
			final Order order = _orders.get( exchangeOrderId );
			// throw error when order has been filled or cancelled
			if (order == null) throw new Exception(
					"The order you want to cancel has been fully filled or previously cancelled." );

			if (clientId != order.getClientId()) throw new Exception( "You can only cancel your own order." );
			order.cancel();
			// sent accepted message if accepted
			final MessageFromExchange accept = Accepted.newMessage( clientId, exchangeOrderId, clientMessageId );
			sentToClient( accept );
		} catch (final Exception e) {
			// sent rejected message if rejected
			final MessageFromExchange reject = Rejected.newMessage( clientId, clientMessageId, e.getMessage() );
			sentToClient( reject );
		}
	}

	/**
	 * Replace an order with new quantity
	 * @param orderMessage contains the information of the order
	 */
	public void replace(final Replace orderMessage) {
		final long exchangeOrderId = orderMessage.getExchangeOrderId();
		final long clientId = orderMessage.getClientId();
		final int newQuantity = orderMessage.getNewQuantity();
		final long clientMessageId;

		// reject if the order doesn't exist, from the email it seems like we can assume this condition will not happen
		try {
			clientMessageId = _messages.get( exchangeOrderId );
		} catch (final Exception e) {
			final MessageFromExchange reject = Rejected.newMessage( clientId, -1,
					"The order you want to replace doesn't exist." );
			sentToClient( reject );
			return;
		}

		try {
			final Order order = _orders.get( exchangeOrderId );
			// throw error when order has been filled or cancelled
			if (order == null) throw new Exception(
					"The order you want to replace has been fully filled or previously cancelled." );

			// throw error when try to increase the quantity of an order
			if (order.getQuantity() < newQuantity) throw new Exception(
					"You can only reduce( but NOT INCREASE ) the quantity of an order that has not been excuted" );

			if (clientId != order.getClientId()) throw new Exception( "You can only replace your own order." );
			order.replace( newQuantity );
			// sent accepted message if accepted
			final MessageFromExchange accept = Accepted.newMessage( clientId, exchangeOrderId, clientMessageId );
			sentToClient( accept );
		} catch (final Exception e) {
			// sent rejected message if error
			final MessageFromExchange reject = Rejected.newMessage( clientId, clientMessageId, e.getMessage() );
			sentToClient( reject );
		}
	}

	/**
	 * Add an order
	 * @param orderMessage contains the information of the order
	 */
	public void add(final Add orderMessage) {
		final long exchangeOrderId = ++_idOrder;
		final long clientId = orderMessage.getClientId();
		final long clientMessageId = orderMessage.getClientMessageId();
		_messages.put( exchangeOrderId, clientMessageId );

		final String ticker = orderMessage.getTicker();
		if (!_books.containsKey( ticker )) {
			createBook( ticker );
		}
		final OrderSide side = orderMessage.getSide();
		final OrderType type = orderMessage.getOrderType();
		Order order;
		int quantity;

		try {
			if (side == OrderSide.LONG) {
				quantity = _books.get( ticker ).fillBuy( orderMessage, exchangeOrderId );
			} else if (side == OrderSide.SHORT) {
				quantity = _books.get( ticker ).fillSell( orderMessage, exchangeOrderId );
			}
			else throw new Exception( "ExchangeFill: Order can only be either long or short." );
			// if filled ( fully or partially )
			if (quantity != orderMessage.getQuantity()) {
				// sent fill message if filled
				final MessageFromExchange fill = Fill.newMessage( orderMessage.getQuantity() - quantity,
						exchangeOrderId );
				sentToClient( fill );

				if (type == OrderType.FOK || type == OrderType.IOC) {
					// sent accepted message if accepted
					final MessageFromExchange accept = Accepted.newMessage( clientId, exchangeOrderId, clientMessageId );
					sentToClient( accept );
					return;
				}
			}
			// if not fully filled
			if (quantity != 0 && type == OrderType.ORDINARY) {
				order = new Order( exchangeOrderId, clientId, ticker, side, orderMessage.getPrice(),
						quantity, this );
				_orders.put( exchangeOrderId, order );
				_books.get( ticker ).add( order );

			}

			if (type == OrderType.ORDINARY) {
				// sent accepted message if accepted
				final MessageFromExchange accept = Accepted.newMessage( clientId, exchangeOrderId, clientMessageId );
				sentToClient( accept );
			} else {
				// sent rejected message if rejected
				final MessageFromExchange reject = Rejected.newMessage( clientId, clientMessageId,
						"Your order cannot be filled at this moment." );
				sentToClient( reject );
			}
		} catch (final Exception e) {
			// sent rejected message if rejected
			final MessageFromExchange reject = Rejected.newMessage( clientId, clientMessageId, e.getMessage() );
			sentToClient( reject );

		}
	}

	/**
	 * Get the best bid price of a stock
	 * @param ticker the ticker
	 * @return the best bid( highest buy price) of that stock, -1 if no bids available.
	 */
	public double getBestBid(final String ticker) {
		final Limit ret = _books.get( ticker ).getHighestBuy();
		if (ret == null) return -1;
		return ret.getKey();
	}

	/**
	 * Get the best offer price of a stock
	 * @param ticker the ticker
	 * @return the best offer( lowest sell price) of that stock, -1 if no offers available.
	 */
	public double getBestOffer(final String ticker) {
		final Limit ret = _books.get( ticker ).getLowestSell();
		if (ret == null) return -1;
		return ret.getKey();
	}

	/**
	 * A common operation, returns the volumn of a stock at a specific price
	 * @param ticker the ticker
	 * @param price the price
	 * @return the volumn of that price of that stock, 0 if no limit orders at that price
	 */
	public int getLimitVolumn(final String ticker, final double price) {
		return _books.get( ticker ).getLimitVolumn( price );
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	protected HashMap<Long, Order> getOrders() {
		return _orders;
	}
}
