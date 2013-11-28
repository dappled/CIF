package myLOB;

import myLOB.DataStructure.DoubleLinkedNode;
import myLOB.MessageFromExchange.Fill;

/**
 * Order store a single order information
 * 
 * @author Zhenghong Dong
 */
public class Order implements DoubleLinkedNode {
	private final long		_idOrder;
	private final String	_ticker;
	private final OrderSide	_side;
	private final double	_price;
	private int				_quantity;
	private Limit			_limit;
	private final Exchange	_exchange;
	private final long		_clientId;
	@SuppressWarnings("unused")
	private int				_entryTime; // might be used in the future
	@SuppressWarnings("unused")
	private int				_eventTime; // might be used in the future

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	public Order(final long idOrder, final long clientId, final String ticker, final OrderSide side, final double price, final int quantity,
			final Exchange exchange) throws Exception {
		_idOrder = idOrder;
		_clientId = clientId;
		_ticker = ticker;
		_side = side;
		if (price <= 0) throw new Exception( "Order: Price must be positive" );
		if (quantity <= 0) throw new Exception( "Order: Quantity must be positive" );
		_price = price;
		_quantity = quantity;
		_limit = null;
		_prev = null;
		_next = null;
		_exchange = exchange;
	}

	/***********************************************************************
	 * Utility
	 ***********************************************************************/
	/**
	 * cancel this order
	 */
	protected void cancel() throws Exception {
		getLimit().remove( this );
		_exchange.getOrders().remove( _idOrder );
	}

	/**
	 * replace the order's quantity with newQuantity, update its corresponding limit's volume
	 * @param newQuantity
	 * @exception when the newQuantity is negative
	 */
	protected void replace(final int newQuantity) throws Exception {
		if (newQuantity < 0) throw new Exception( "OrderReplace: Quantity cannot be negative" );
		getLimit().replace( this, newQuantity );
	}

	/**
	 * Fill this order with another order with quantity amount of shares
	 * @param quantity the quantity of the other order
	 * @return quantity left( wasn't filled )
	 */
	protected int fill(int quantity) throws Exception {
		if (quantity <= 0) throw new Exception( "OrderFill: Quantity must be positive" );
		if (getQuantity() < quantity) {
			_exchange.sentToClient( Fill.newMessage( getQuantity(), getId() ) );
			quantity -= getQuantity();
			replace( 0 );
		}
		else {
			_exchange.sentToClient( Fill.newMessage( quantity, getId() ) );
			replace( getQuantity() - quantity );
			quantity = 0;
		}
		return quantity;
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	protected String getTicker()						{ return _ticker; }
	protected int getQuantity() 						{ return _quantity; }
	protected void setQuantity(final int quantity)		{ _quantity = quantity; }
	protected double getPrice()							{ return _price; }
	protected long getId()		 						{ return _idOrder; }
	protected OrderSide getSide() 						{ return _side; }
	protected Limit getLimit() 							{ return _limit; }
	protected void setLimit(final Limit limit) 			{ _limit = limit; }
	protected Exchange getExchange()					{ return _exchange; }
	protected long getClientId()						{ return _clientId; }
	
	/***********************************************************************
	 * {@link DoubleLinkedNode} methods
	 ***********************************************************************/
	private DoubleLinkedNode _prev;
	private DoubleLinkedNode _next;

	@Override
	public DoubleLinkedNode getPrev() 					{ return _prev; }
	@Override
	public void setPrev(final DoubleLinkedNode prev) 	{ _prev = prev; }
	@Override
	public DoubleLinkedNode getNext() 					{ return _next; }
	@Override
	public void setNext(final DoubleLinkedNode next) 	{ _next = next; }
}
