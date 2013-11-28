package myLOB;

import myLOB.DataStructure.BinarySearchTreeNode;
import myLOB.DataStructure.DoubleLinkedList;

/**
 * Limit stands for a single price, which might contains multiple order at this price.
 * 
 * @author Zhenghong Dong
 */
public class Limit implements BinarySearchTreeNode<Double> {
	private final double		_price;
	private int					_volumn;
	private DoubleLinkedList	_orders;
	private final Book			_book;
	private final OrderSide		_side;

	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	public Limit(final double price, final OrderSide side, final Book book) {
		_price = price;
		_volumn = 0;
		_orders = new DoubleLinkedList();
		_parent = null;
		_left = null;
		_right = null;
		_book = book;
		_side = side;
	}

	public Limit(final Order order, final Book book) throws Exception {
		this( order.getPrice(), order.getSide(), book );
		add( order );
	}

	/***********************************************************************
	 * Utility
	 ***********************************************************************/
	/**
	 * Add an order to this limit price
	 * @param order the order
	 * @throws Exception when adding a null order
	 */
	protected void add(final Order order) throws Exception {
		_orders.add( order );

		order.setLimit( this );
		_volumn += order.getQuantity();
	}

	/**
	 * Remove the specific order in the limit
	 */
	protected void remove(final Order order) throws Exception {
		if (order.getLimit() != this) throw new Exception( "LimitOrder: Cannot remove an order not in this limit" );
		_volumn -= order.getQuantity();
		_orders.delete( order );
		// if this remove will make this limit empty
		if (_volumn == 0) {
			close();
		}
	}

	/**
	 * Fill this limit with another order with quantity amount of shares
	 * @param quantity the quantity of the other order
	 * @return quantity left( wasn't filled )
	 * @throws Exception when quantity is less than or equal to zero
	 */
	protected int fill(int quantity) throws Exception {
		if (quantity <= 0) throw new Exception( "LimitFill: Quantity must be positive" );
		while (quantity > 0 && getVolumn() > 0) {
			quantity = ((Order) _orders.getFirst()).fill( quantity );
		}
		return quantity;
	}

	/**
	 * Replace one order's quantity to newQuantity
	 * @param order to be replaced quantity
	 * @param newQuantity the new quantity
	 */
	protected void replace(final Order order, final int newQuantity) throws Exception {
		if (newQuantity == 0) {
			order.cancel();
		} else {
			_volumn += (newQuantity - order.getQuantity());
			order.setQuantity( newQuantity );
		}
	}

	/**
	 * Close this limit when its empty
	 * @throws Exception not gonna happen
	 */
	private void close() throws Exception {
		_orders = null;
		getBook().remove( this );
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	protected int getVolumn() 							{ return _volumn; }
	protected Book getBook()							{ return _book; }
	protected OrderSide getSide()						{ return _side; }

	/***********************************************************************
	 * {@link BinarySearchTreeNode} methods
	 ***********************************************************************/
	private BinarySearchTreeNode<Double> _parent;
	private BinarySearchTreeNode<Double> _left;
	private BinarySearchTreeNode<Double> _right;
	@Override
	public BinarySearchTreeNode<Double> getLeft() 					{ return _left; }
	@Override
	public BinarySearchTreeNode<Double> getRight()		 			{ return _right; }
	@Override
	public BinarySearchTreeNode<Double> getParent() 				{ return _parent; }
	@Override
	public void setLeft(final BinarySearchTreeNode<Double> node) 	{ _left = node; }
	@Override
	public void setRight(final BinarySearchTreeNode<Double> node) 	{ _right = node; }
	@Override
	public void setParent(final BinarySearchTreeNode<Double> node) 	{ _parent = node; }
	@Override
	public Double getKey() 											{ return _price; }

}
