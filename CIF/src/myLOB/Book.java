package myLOB;

import java.util.HashMap;

import myLOB.DataStructure.BinarySearchTree;
import myLOB.OrderMessage.Add;
import myLOB.OrderMessage.OrderType;

/**
 * @author Zhenghong Dong
 */
public class Book {
	private final BinarySearchTree<Double>	_buyTree;
	private final BinarySearchTree<Double>	_sellTree;
	private final HashMap<Double, Limit>	_limits;
	private Limit							_lowestSell;
	private Limit							_highestBuy;
	
	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	public Book() {
		_buyTree = new BinarySearchTree<>();
		_sellTree = new BinarySearchTree<>();
		_limits = new HashMap<>();
		_lowestSell = (Limit) _sellTree.min();
		_highestBuy = (Limit) _buyTree.max();
	}

	/***********************************************************************
	 * Utility
	 ***********************************************************************/
	/**
	 * Add an order to this book. This will NOT try to fill this order. Please call fill if you are trying to fill.
	 * If you want to first fill then add, use {@link Exchange} method.
	 * @param order
	 * @throws Exception when order itself is inapproporate( negative price/quantity etc. )
	 */
	protected void add(final Order order) throws Exception {
		final double price = order.getPrice();
		Limit limit;
		// append to the limit if already exist
		if (_limits.containsKey( price ) ) {
			limit = _limits.get( price );
			limit.add( order );
		}
		// else create corresponding limit
		else {
			limit = new Limit( order, this );

			// add the hashmap
			_limits.put( price, limit );

			// add the BST
			// if buy order
			if (order.getSide() == OrderSide.LONG) {
				_buyTree.put( limit );
				_highestBuy = (Limit) _buyTree.max();
			}
			// if sell order
			else if (order.getSide() == OrderSide.SHORT) {
				_sellTree.put( limit );
				_lowestSell = (Limit) _sellTree.min();
			}
			else throw new Exception("BookAdd: Order must be either long or short.");
		}
	}

	/**
	 * Remove the limit from the book
	 * @param idLimit
	 * @throws Exception when try to remove a limit not in this book
	 */
	protected void remove(final Limit limit) throws Exception {
		if (limit.getBook() != this) throw new Exception(
				"BookRemove: Cannot remove a limit not in this book." );
		// remove from HashMap;
		_limits.remove( limit.getKey() );

		// remove from BST;
		if (limit.getSide() == OrderSide.LONG) {
			_buyTree.delete( limit );
			_highestBuy = (Limit) _buyTree.max();
		} else {
			_sellTree.delete( limit );
			_lowestSell = (Limit) _sellTree.min();
		}
	}

	/**
	 * Try to fill a buy order
	 * @param order the order to be filled
	 * @param idOrder this order's id on exchange side
	 * @return the quantity left( wasn't filled )
	 * @throws Exception when try to fill an order with negative quantity
	 */
	protected int fillBuy(final Add order, final long idOrder) throws Exception {
		final OrderType type = order.getOrderType();
		int quantity = order.getQuantity();
		final double price = order.getPrice();
		if (quantity <= 0) throw new Exception("Order must have positive quantity.");
		if (price <= 0) throw new Exception("Order must have positive price.");
		
		// if there is no sell limits, fail to fill
		if (_lowestSell == null) return quantity;
		
		// if price < lowest sell price, fail to fill this order
		if (price < _lowestSell.getKey()) return quantity;

		// if FOK order
		if (type == OrderType.FOK) {
			if (canFillBuy( quantity, price )) {
				quantity = fillBuyHelp( idOrder, quantity, price );
			}
		}
		// o.w
		else {
			quantity = fillBuyHelp( idOrder, quantity, price );
		}
		return quantity;
	}

	// Test if this buy order can be filled(used by FOK order)
	private boolean canFillBuy(final int quantity, final double price) {
		int size = 0;
		Limit sell = _lowestSell;
		while (sell.getKey() <= price) {
			size += _lowestSell.getVolumn();
			if (size >= quantity) return true;
			sell = (Limit) _sellTree.nextLarger( sell );
		}
		return false;
	}

	// Fill the buy order with quantity at price below cetain price
	private int fillBuyHelp(final long idOrder, int quantity, final double price) throws Exception {
		// go through all sell limit with price less than this buy
		while (_lowestSell.getKey() <= price) {
			quantity = _lowestSell.fill( quantity );
			// if fully filled or has reaped all the available orders
			if (quantity == 0 || _lowestSell == null) break;
		}

		_lowestSell = (Limit) _sellTree.min();
		return quantity;
	}

	/**
	 * Try to fill a sell order
	 * @param order the order to be filled
	 * @param idOrder this order's id on exchange side
	 * @return the quantity left( wasn't filled )
	 * @throws Exception when try to fill an inapproparate order
	 */
	protected int fillSell(final Add order, final long idOrder) throws Exception {
		final OrderType type = order.getOrderType();
		int quantity = order.getQuantity();
		final double price = order.getPrice();
		if (quantity <= 0) throw new Exception("Order must have positive quantity.");
		if (price <= 0) throw new Exception("Order must have positive price.");
		
		// if there is no buy limits, fail to fill
			if (_highestBuy == null) return 0;
				
		// if price > highest buy price, fail to fill this order
		if (price > _highestBuy.getKey()) return quantity;

		// if FOK order
		if (type == OrderType.FOK) {
			if (canFillSell( quantity, price )) {
				quantity = fillSellHelp( idOrder, quantity, price );
			}
		}
		// o.w
		else {
			quantity = fillSellHelp( idOrder, quantity, price );
		}
		return quantity;
	}

	// Test if this sell order can be filled(used by FOK order)
	private boolean canFillSell(final int quantity, final double price) {
		int size = 0;
		Limit buy = _highestBuy;
		while (buy.getKey() >= price) {
			size += _highestBuy.getVolumn();
			if (size >= quantity) return true;
			buy = (Limit) _buyTree.nextSmaller( buy );
			if (buy == null) return false;
		}
		return false;
	}

	// Fill the sell order with quantity at price above cetain price
	private int fillSellHelp(final long idOrder, int quantity, final double price) throws Exception {
		// go through all buy limit with price greater than this sell
		while (_highestBuy.getKey() >= price) {
			quantity = _highestBuy.fill( quantity );
			// if fully filled
			if (quantity == 0 || _lowestSell == null) break;
		}

		_highestBuy = (Limit) _buyTree.max();
		return quantity;
	}
	
	/**
	 * Return the volumn of a given price
	 * @param price the price
	 * @return the volumn of that price, 0 if no order of that price exists
	 */
	protected int getLimitVolumn(double price) {
		Limit lim =  _limits.get( price );
		if (lim == null) return 0;
		return lim.getVolumn();
	}
	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	protected void setLowestSell(final Limit limit) 	{ _lowestSell = limit; }
	public Limit getLowestSell() 						{ return _lowestSell; }
	protected void setHighestBuy(final Limit limit)		{ _highestBuy = limit; }
	public Limit getHighestBuy()						{ return _highestBuy; }
}
