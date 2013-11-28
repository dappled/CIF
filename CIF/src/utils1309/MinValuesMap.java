package utils1309;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class MinValuesMap<K extends Comparable<K>,V> {

	private TreeMap<K,LinkedList<V>> _map;
	private K                        _maxKey;
	private int                      _count;
	private int                      _maxCount;
	private V                        _lastRemoved;
	
	/**
	 * Construct a min values map that will allow at most maxCount entries
	 * @param maxCount Number of entries this min values map should allow
	 */
	public MinValuesMap( int maxCount ) {
		_map = new TreeMap<K,LinkedList<V>>();
		_maxKey = null;
		_count = 0;
		_maxCount = maxCount;
		_lastRemoved = null;
	}
	
	/**
	 * If the key is lower than the lowest key, add a point to 
	 * the list of points stored at key.
	 *  
	 * @param key Key at which to add a value
	 * @param value Vaue to add to list
	 * @return True if the value was added to the map. False if the key was not small enough.
	 */
	public boolean addPoint( K key, V value ) {
		_lastRemoved = null;
		if( _count == _maxCount ) {
			if( key.compareTo( _maxKey ) > -1 ) return false;
			_lastRemoved = removePoint( _maxKey );
		}
		addToMap( key, value );
		return true;
	}
	
	/**
	 * Remove one value stored at this key. If the new list of values at this
	 * key is empty, remove the list.
	 * 
	 * @param key Key at which to obtain list from which a value will be removed
	 * @return Value that was removed
	 */
	private V removePoint( K key ) {
		LinkedList<V> values = _map.get( key );
		_count--;
		V value = values.remove();
		if( values.size() == 0 )
			_map.remove( key );
		return value;
	}
	
	/**
	 * Traverse this map to make a collection of values without their keys
	 * @return Collection of values stored in map
	 */
	public List<V> getValues() {
		List<V> values = new LinkedList<V>(); 
		Iterator<LinkedList<V>> firstLevelIterator = _map.values().iterator();
		while( firstLevelIterator.hasNext() ) {
			Iterator<V> secondLevelIterator = firstLevelIterator.next().iterator();
			while( secondLevelIterator.hasNext() )
				values.add( secondLevelIterator.next() );
		}
		return values;
	}
	
	/**
	 * Internal add to map method. If necessary, creates a new linked list for values
	 * at this key. Keeps track of total number of values and saves last key. Should
	 * only get called if the new key is the new min value.
	 * 
	 * @param key Key by which to store value
	 * @param value Value to be stored
	 */
	private void addToMap( K key, V value ) {
		LinkedList<V> values = _map.get( key );
		if( values == null ) {
			values = new LinkedList<V>();
			_map.put( key, values );
		}
		values.add( value );
		_count++;
		_maxKey = _map.lastKey();
	}

	public V getLastRemoved() { return _lastRemoved; }
	
}
