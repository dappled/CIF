package clustersSolution;

import java.util.ArrayList;

/**
 * This is the base class of concrete cluster algorithms such as Lloyd and ReverseLloyd.
 * In its solve method, it executes a loop that calls the performOneIteration method of
 * a concrete algorithm. This base class takes care of reporting and keeping track of
 * elapsed time, number of iterations, and number of swaps.
 * 
 */
public abstract class AbstractClusterAlgorithm {

	protected ArrayList<Cluster>       _clusters; // List of clusters
	protected ArrayList<ClusterPoint>  _points; // List of points
	protected int                      _iIteration; // Counter keeping track of which iteration this is
	protected int                      _nChanges; // Number of points that changed clusters in last iteration
	protected long                     _startTime; // Start time of search for solution 
	protected long                     _endTime; // End time of search for solution
	
	public AbstractClusterAlgorithm( ArrayList<Cluster> clusters, ArrayList<ClusterPoint> points ) {
		_clusters = clusters;
		_points = points;
	}

	/**
	 * Call concrete algorithm's method to initialize clusters - Of the
	 * three we have - Lloyd, ReverseLloyd, and PointSwapper - only
	 * PointSwapper does anything in this method.
	 */
	public abstract void initializeClusters  ();
	
	/**
	 * Main processing method for concrete implementation of algorithm.
	 * Re-allocates points, etc.
	 */
	public abstract void performOneIteration ();
	
	/**
	 * None of our algorithms do anything in this method, but it gets
	 * called anyway, just in case some future algorithm wants to use
	 * it to do some post processing on the solution.
	 */
	public abstract void postProcessResults  ();
	
	/**
	 * Main processing loop - Calls algorithm specific method,
	 * performOneIteration. 
	 */
	public void solve() {
		initialReport(); // Mostly counters and reporting
		initializeClusters(); // Algorithm specific - How points are initially allocated to clusters
		do {
			_nChanges = 0; // Will be modified in specific algorithm
			performOneIteration(); // Algorithm specific - Reallocate points, recompute clusters
			reportOnIntermediateResults(); // Report iteration, number of changes, and distance metric 
			_iIteration++;
		} while( isFinished() ); // Stop if no swaps or taking too long
		postProcessResults(); // Algorithm specific - Pull statistics from solution
		finalReport(); // Report elapsed time and final distance metric
	}
	
	/**
	 * Get the distance metric as calculated by individual clusters that
	 * already have points assigned to them. We can't do this before
	 * points are assigned to clusters, so we can't call this method in
	 * our initialReport method.
	 * 
	 * @return Average distance of points from their cluster centroids
	 */
	public double getDistanceMetric() {
		double globalDistanceMetric = 0;
		for( Cluster cluster : _clusters ) {
			double clusterDistanceMetric = cluster.getDistanceMetric();
			if( !Double.isNaN( clusterDistanceMetric ) )
				globalDistanceMetric += clusterDistanceMetric; 
		}
		return globalDistanceMetric / _points.size();
	}
	
	/**
	 * Report start time of solution, save start time, and reset iteration counter
	 */
	public void initialReport() {
		_iIteration = 0;
		_startTime = System.currentTimeMillis();
		System.out.println( 
			String.format( 
				"Start time = %d", 
					_startTime
			) 
		);
	}
	
	/**
	 * Report elapsed time and final distance metric
	 */
	public void finalReport() {
		_endTime = System.currentTimeMillis();
		System.out.println(
			String.format(
				"Elapsed time = %d, Distance metric = %f",
					_endTime - _startTime,
					getDistanceMetric()
			)
		);
	}
	
	/**
	 * Write cluster sizes to console
	 */
	public void outputClusterSizes() {
		for( int i = 0; i < _clusters.size(); i++ )
			if( _clusters.get( i ).size() != 20 )
				System.out.println( String.format( "Cluster %d has %d elements", i, _clusters.get(i).size() ) );
	}
	
	/**
	 * Report iteration counter, number of changes in last iteration, and
	 * distance metric
	 */
	public void reportOnIntermediateResults() {
		System.out.println( 
			String.format( 
				"%d,%d,%f", 
					_iIteration, 
					_nChanges, 
					getDistanceMetric() 
			) 
		);
	}
	
	/**
	 * @return True if converged to solution or if taking too long
	 */
	public boolean isFinished() {
		if( _iIteration > 100 )
			return false;
		if( _nChanges == 0 )
			return false;
		return true;
	}
	
}
