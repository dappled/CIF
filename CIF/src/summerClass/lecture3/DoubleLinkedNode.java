package summerClass.lecture3;


public class DoubleLinkedNode<ValType> {
	
	protected DoubleLinkedNode<ValType> _prevNode;
	protected ValType _rpc;
	protected DoubleLinkedNode<ValType> _nextNode;

	public DoubleLinkedNode( 
		DoubleLinkedNode<ValType> prevNode,
		ValType rpc,
		DoubleLinkedNode<ValType> nextNode
	) {
		_prevNode = prevNode;
		if( prevNode != null )
			prevNode.setNext( this );
		_nextNode = nextNode;
		if( nextNode != null )
			nextNode.setPrev( this );
		_rpc = rpc;
	}
	
	public void detachNode() {
		if( _prevNode != null ) {
			_prevNode.setNext( _nextNode );
		}
		if( _nextNode != null ) {
			_nextNode.setPrev( _prevNode );
		}
	}

	public void setNext( DoubleLinkedNode<ValType> nextNode ) {
		_nextNode = nextNode;
	}
	public void setPrev( DoubleLinkedNode<ValType> prevNode ) {
		_prevNode = prevNode;
	}
	
	public ValType getValue() { return _rpc; }

	public DoubleLinkedNode<ValType> getNext() {
		return _nextNode;
	}

	public DoubleLinkedNode<ValType> getPrev() {
		return _prevNode;
	}
	
}
