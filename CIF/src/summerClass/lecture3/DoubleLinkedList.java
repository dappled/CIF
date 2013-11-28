package summerClass.lecture3;

import summerClass.lecture3.DoubleLinkedNode;
import summerClass.lecture3.QInterface;


public class DoubleLinkedList<ValType> implements QInterface<ValType>{
	
	protected DoubleLinkedNode<ValType> _first;
	protected DoubleLinkedNode<ValType> _last;
	protected int                       _count;
	protected int                       _fullSize;
	
	public DoubleLinkedList( int fullSize ) throws Exception {
		if( fullSize < 1 )
			throw new Exception( "Invalid specification of full size in DoubleLinedList" );
		_fullSize = fullSize;
		_first = null;
		_last = null;
		_count = 0;
	}
	
	public ValType getFirstValue() { return _first.getValue(); }
	public ValType getLastValue() { return _last.getValue(); }
	
	@Override
	public void addLast( ValType val ) {
		if( _count == 0 ) {
			_first = _last = new DoubleLinkedNode<ValType>( null, val, null );
		} else {
			_last = new DoubleLinkedNode<ValType>( _last, val, null );
		}
		_count++;
		
		// Has the list grown from its original 'fullSize' spec?
		if( _count > _fullSize )
			_fullSize = _count;
	}
	
	@Override
	public ValType removeFirst() throws Exception {
		if( _count == 0 )
			throw new Exception( "Can't remove node from empty list" );
		DoubleLinkedNode<ValType> dln = _first;
		if( _first == _last ) {
			_first = _last = null;
		} else {
			_first = _first.getNext();
			dln.detachNode();
		}
		_count--;
		return dln.getValue();
	}
	
	public int size() { return _count; }

	@Override
	public boolean isReady() {
		return _fullSize == _count;
	}

	@Override
	public boolean hasElementsToRemove() {
		return isReady();
	}

}
