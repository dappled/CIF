package myLOB.DataStructure;

public class DoubleLinkedList{
	
	private DoubleLinkedNode 		_first;
	private DoubleLinkedNode 		_last;
	
	/***********************************************************************
	 * Constructor
	 ***********************************************************************/
	public DoubleLinkedList( ) {
		_first = null;
		_last =  null;
	}
	
	/***********************************************************************
	 * Utility
	 ***********************************************************************/
	/**
	 * Add a node to this list.
	 * @param node the node to be added
	 * @throws Exception when adding a null node
	 */
	public void add( DoubleLinkedNode node ) throws Exception {
		if (node == null) throw new Exception("DoubleLinkedList Add: Cannot add null node.");
		if (_first == null) {
			_first = node;
			_last = node;
		} else {
			_last.setNext( node );
			node.setPrev( _last );
			_last = node;
		}
	}
	
	/**
	 * Remove the first node in this list.
	 * @return the node removed
	 * @throws Exception when list is empty.
	 */
	public DoubleLinkedNode deleteFirst() throws Exception {
		if( _first == null ) throw new Exception( "DoubleLinkedListRemoveFirst: Cannot remove node from empty list" );
		DoubleLinkedNode dln = _first;
		if( _first == _last ) _first = _last =  null;
		else {
			_first.getNext().setPrev( null );
			_first = _first.getNext();
		}
		return dln;
	}
	
	/**
	 * Remove the last node in this list.
	 * @return the node removed
	 * @throws Exception when list is empty.
	 */
	public DoubleLinkedNode deleteLast() throws Exception {
		if( _last == null ) throw new Exception( "DoubleLinkedListRemoveLast: Cannot remove node from empty list" );
		DoubleLinkedNode dln = _last;
		if( _first == _last ) _first = _last =  null;
		else {
			_last.getPrev().setNext( null );
			_last = _last.getPrev();
		}
		return dln;
	}
	
	/**
	 * Test if a node is in the list
	 * @param node the node
	 * @return true if the node is in this list, false if not.
	 */
	public boolean contains(DoubleLinkedNode node) {
		DoubleLinkedNode tmp = _first;
		while (tmp.getNext() != null) {
			if (tmp == node) return true;
			tmp = tmp.getNext();
		}
		return _last == node;
	}
	
	/**
	 * Remove a specific node in the list
	 * @param node the node to be removed
	 * @throws Exception when node not in this list( so getPrev() == null has no setNext() ). However, you should
	 *             implement your own ways to make sure node is in the list before call delete.
	 */
	public void delete(DoubleLinkedNode node) throws Exception  {
		if (node == _first && node == _last) _first = _last = null;
		else if (node == _first) deleteFirst();
		else if (node == _last) deleteLast();
		else {
			node.getPrev().setNext( node.getNext() );
			node.getNext().setPrev( node.getPrev() );
		}
	}


	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public DoubleLinkedNode	getFirst() 	{ return _first; }	
	public DoubleLinkedNode	getLast()	{ return _last; }
}
