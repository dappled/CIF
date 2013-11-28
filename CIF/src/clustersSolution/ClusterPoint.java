
package clustersSolution;

import java.util.Iterator;
import java.util.List;

/** 
 * This extends Point and implements two additional field, _cluster
 * and _newCluster. These are used to allocate points to clusters
 * and to tell clusters which points are available for re-allocation.
 */
public class ClusterPoint extends Point {

	protected Cluster  _cluster;
	protected Cluster  _newCluster;
	
	/**
	 * Construct a point from a given array representing a vector of coordinate values
	 * 
	 * @param coordinates Vector of coordinate values
	 */
	public ClusterPoint( double[] coordinates ) {
		super( coordinates );
		_cluster = null;
		_newCluster = null;
	}
	
	/**
	 * Construct a point by copying the coordinates of another point
	 * 
	 * @param otherPoint Point to copy coordinates from
	 */
	public ClusterPoint( ClusterPoint otherPoint ) {
		this( otherPoint.getCoordinates() );
	}
	
	/**
	 * Construct a point with a value of zero in all dimensions
	 * 
	 * @param nDims Number of dimensions
	 */
	public ClusterPoint( int nDims ) {
		super( nDims );
	}

	/**
	 * The cluster instance variable is used to keep track of the cluster
	 * to which this point belongs. This method sets the cluster variable.
	 * 
	 * @param cluster New value of cluster variable.
	 */
	public void setCluster( Cluster cluster ) { _cluster = cluster; }
	
	/**
	 * Return cluster to which this point belongs
	 * 
	 * @return Cluster to which this poing belongs
	 */
	public Cluster getCluster() { return _cluster; }
	
	public void setNewCluster( Cluster newCluster ) { _newCluster = newCluster; }
	public Cluster getNewCluster() { return _newCluster; }
	
	/**
	 * Compute centroid of a list of points
	 * 
	 * @param pointsList
	 * @return A point representing the centroid of the provided list
	 */
	public static Point computeCentroids(List<ClusterPoint> pointsList) {
		Iterator<ClusterPoint> pointsIterator = pointsList.iterator();
		Point point = new Point( pointsIterator.next() );
		while( pointsIterator.hasNext() ) 
			// Add two points, leaving result in point that receives message
			point.addAsVector( pointsIterator.next() ); 
		point.rescaleVector( pointsList.size() );
		return point;
	}
	
	
}
