package clustersSolution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cluster {
	
	protected Point             _centroid;
	protected LinkedList<ClusterPoint> _points;
	protected Point             _vectorSum;

	public Cluster( Point centroid ) {
		_centroid = centroid;
		resetPointsList();
	}

	/**
	 * @return Centroid of this cluster
	 */
	public Point getCentroid() { return _centroid; }
	
	/**
	 * @return Number of points in this cluster
	 */
	public int size() { return _points.size(); }
	
	/**
	 * Return underlying array of points in this cluster
	 * @return Array of points in this cluster
	 */
	public List<ClusterPoint> getPoints() { return _points; }
	
	public void computeCentroid() {
		
		// If there are no points in this cluster, do not
		// recompute centroid
		
			if( _points.size() == 0 )
				return;
			
		_vectorSum.rescaleVector( _points.size() );
		_centroid = _vectorSum;
	}
	
	public void addPoint( ClusterPoint point ) {
		_points.add( point );
		_vectorSum.addAsVector( point );
	}
	
	/**
	 * Replace all points in this cluster with a new list of points.
	 * 
	 * @param points List of points to use to replace points in this cluster
	 */
	protected void replaceAllPoints( List<ClusterPoint> points ) {
		resetPointsList();
		for( ClusterPoint point : points ) {
			addPoint( point ); // Add this point to list of points for this cluster
			point.setNewCluster( this );
		}
	}
	
	/**
	 * Find specified number of nearest unallocated points. Unallocated points are points
	 * for which the newCluster variable is null
	 *  
	 * @param nPointsToFind Number of nearest points to find
	 * @param pointsList List of points to search for nearest unallocated points
	 * @return List of points found by the search
	 */
	protected List<ClusterPoint> findNearestNPoints( int nPointsToFind, List<ClusterPoint> pointsList ) {
		MinValuesMap<Double, ClusterPoint> mvp = new MinValuesMap<Double, ClusterPoint>( nPointsToFind );
		for( ClusterPoint point : pointsList )
			if( point.getNewCluster() == null )
				mvp.addPoint( point.distanceFrom( _centroid ), point );
		return mvp.getValues();
	}

	/**
	 * Replaces this cluster's list of points with the nearest nPoint points from pointsList.
	 * 
	 * @param nPointsToFind Number of closest points to use
	 * @param pointsList List of points to evaluate for closest nPoints
	 */
	public void replaceWithNearest( 
		int nPointsToFind, 
		ArrayList<ClusterPoint> pointsList
	) {
		List<ClusterPoint> nearestPointsFound = findNearestNPoints( nPointsToFind, pointsList );
		replaceAllPoints( nearestPointsFound );
	}
	
	/**
	 * Compute the sum of distances of all points in this cluster from 
	 * its centroid
	 * 
	 * @return Sum of distances or NaN if the cluster is empty
	 */
	public double getDistanceMetric() {
		if( _points.size() == 0 )
			return Double.NaN;
		double distanceSum = 0;
		for( Point point : _points )
			distanceSum += point.distanceFrom( _centroid );
		return distanceSum;
	}

	/**
	 * Remove all points from the points list of this cluster in
	 * preparation to receive more points. Reset vectorSum to zero
	 * values so it can be used to compute centroid later.
	 */
	public void resetPointsList() {
		_points = new LinkedList<ClusterPoint>();
		_vectorSum = new ClusterPoint( _centroid.getNDims() );
	}
	
}
