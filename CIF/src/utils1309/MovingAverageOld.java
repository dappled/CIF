package utils1309;

import java.util.LinkedList;

/**
 * In a moving average, a changing list of values is used to compute an average.
 * When the queue of values is full, adding an additional value causes a value
 * to be dropped from the back of the queue. An object of this class will report
 * that it is ready only when the queue is full.
 *  
 * @author minddrill
 *
 */
public class MovingAverageOld extends LinkedList<Double> implements I_MovingAverage {

	private static final long serialVersionUID = 1L; // Ignore - we never plan to serialize this
	
	protected int _size;
	protected double _sum;
	protected LinkedList<Double> _data;
	
	/**
	 * Static factory method for moving average object.
	 * @param size Size of queue at which moving average is considered ready.
	 * @return An empty moving average object ready to accept additions of values. 
	 *         Returning the interface eliminates visibility of LinkedList methods. 
	 */
	public static I_MovingAverage newMovingAverage( int size ) {
		return new MovingAverageOld( size );
	}
	
	/**
	 * Private constructor of moving average. Saves size and instantiates internal data store for future additions.
	 * @param size Size of queue at which moving average is considered "ready"
	 */
	private MovingAverageOld( int size ) {
		super();
		_size = size;
		_sum = 0;
		_data = new LinkedList<Double>();
	}
	
	/**
	 * This is where values are added to the analysis.
	 * Note that if the queue is full, adding a value here 
	 * will cause the oldest value to be dropped from the
	 * queue
	 */
	public boolean add( double value ) {
		addToAnalysis(value);
		removeFromAnalysis();
		return isReady();
	}

	/**
	 * Add the effect of this value to the calculation
	 * @param value
	 */
	protected void addToAnalysis( double value ) {
		_sum += value;
		_data.addLast( value );
	} 

	/**
	 * If the queue has excess elements, remove thei
	 * effects from this calculation 
	 */
	protected void removeFromAnalysis() {
		while( hasElementsToRemove() )
			_sum -= _data.removeFirst();
	}

	/**
	 * @return Moving average of prices in queue
	 * @throws Exception Thrown if the number of elements in the queue is zero
	 */
	public double getAverage() throws Exception {
		if( _data.size() < 1 )
			throw new Exception( "Can't take moving average of empty data set" );
		return _sum / (double) _data.size();
	}

	/**
	 * @return True if the number of elements in the queue is number desired
	 */
	public boolean isReady() { return _data.size() == _size; }

	/**
	 * @return True if que is greater than desired size
	 */
	protected boolean hasElementsToRemove() { return _data.size() > _size; }
	
}
