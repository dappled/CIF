package summerClass.lecture3;

public class ArrayBasedList<ValType> implements QInterface<ValType> {
	
	protected ValType[] _data;
	int                 _count;
	int                 _fullSize;
	
	@SuppressWarnings("unchecked")
	public ArrayBasedList( int fullSize ) throws Exception {
		if( fullSize < 1 )
			throw new Exception( "Invalid size specification in BadList = " + fullSize );
		_data  = (ValType[]) new Object[ fullSize ];
		_count  = 0;
		_fullSize = fullSize;
	}

	public void addLast(ValType value) {
		if( _count == _data.length ) {
			// Not enough room, so we need to grow this queue
			@SuppressWarnings("unchecked")
			ValType[] newData = (ValType[]) new Object[ _data.length * 2 ]; 
			for( int i = 0; i < _count; i++ ) {
				newData[ i ] = _data[ i ];
			}
			_data = newData;
		}
		_data[ _count ] = value;
		if( _count == _fullSize )
			_fullSize++;
		_count++;
	}

	public ValType removeFirst() throws Exception {
		ValType value = getValue( 0 );
		int iEnd = _count - 1;
		for( int i = 0; i < iEnd; i++ ) {
			_data[ i ] = _data[ i + 1 ];
		}
		_count--;
		return value;
	}

	public ValType getValue( int index ) throws IndexOutOfBoundsException {
		if( ( index < 0 ) || ( index >= _count ) )
			throw new IndexOutOfBoundsException( "Can't get element at index " + index + " in CircularList of size " + _count );
		return _data[ index ];
	}
	
	public ValType getFirstValue() { return getValue( 0 ); }
	public ValType getLastValue() { return getValue( _count - 1 ); }

	public int size() { return _count; }
	
	public boolean isReady() { return _count == _fullSize; }
	
	public boolean hasElementsToRemove() { return isReady(); }

}
