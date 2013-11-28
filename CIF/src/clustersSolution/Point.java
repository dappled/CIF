package clustersSolution;

import java.util.List;

public class Point {
	
	protected double[] _coordinates;

	/**
	 * Create a point by copying coordinates from an array
	 * @param coordinates Array from which coordinates will be copied
	 */
	public Point( double[] coordinates ) {
		_coordinates = new double[ coordinates.length ];
		for( int i = 0; i < coordinates.length; i++ )
			_coordinates[ i ] = coordinates[ i ];
	}
	
	/**
	 * Create a point by copying the coordinates of another point
	 * @param otherPoint Point from which coordinates will be copied
	 */
	public Point( Point otherPoint ) {
		this( otherPoint.getCoordinates() );
	}

	/**
	 * Create zero value point in nDims - All values set to zero
	 * @param nDims Number of dimensions
	 */
	public Point( int nDims ) {
		_coordinates = new double[ nDims ];
	}

	/**
	 * Return this points coordinates vector
	 * 
	 * @return Coordinates comprising this point
	 */
	public double[] getCoordinates() { return _coordinates; }

	/**
	 * Compute distance from this point to another point
	 * 
	 * @param otherPoint Point that will be used to compute distance from this one
	 * @return Geometric distance between two points
	 */
	public double distanceFrom(Point otherPoint) {
		double sum = 0;
		double diff = 0;
		for( int i = 0; i < _coordinates.length; i++ ) {
			diff = _coordinates[ i ] - otherPoint.getCoordinate( i );
			sum +=  diff * diff;
		}
		return Math.sqrt( sum );
	}

	/**
	 * Traverses list of clusters to find one whose centroid is nearest to this point.
	 * 
	 * @param clusterList List of clusters to traverse
	 * @return Cluster whose centroid is nearest to this point
	 */
	public Cluster findNearestCluster(List<Cluster> clusterList) {
		Cluster nearestCluster = null;
		double bestDistance = Double.MAX_VALUE;
		for( Cluster cluster : clusterList ) {
			double distance = distanceFrom( cluster.getCentroid() );
			if( distance < bestDistance ) {
				bestDistance = distance;
				nearestCluster = cluster;
			}
		}
		return nearestCluster;
	}

	/**
	 * A string representation of the contents of this point. Automatically used by
	 * debugger, if available. (Otherwise, the generic version is used.)
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Point(");
		sb.append(String.format("%.4g",_coordinates[0]));
		for( int i = 1; i < _coordinates.length; i++ )
			sb.append(String.format(",%.4g", _coordinates[i] ) );
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Get this point's coordinate in the specified dimension
	 * @param iDimension Dimension in which the coordinate we're interested in resides  
	 * @return Coordinate value in the specified dimension
	 */
	public double getCoordinate(int iDimension) { return _coordinates[ iDimension ]; }

	/**
	 * In place, add the coordinate of each dimension of this point to the coordinate of
	 * the same dimension of the point passed in as an argument
	 * 
	 * @param point The other point, the coordinates of which will be added to this point
	 */
	public void addAsVector(Point point) {
		for( int i = 0; i < _coordinates.length; i++ )
			_coordinates[ i ] += point.getCoordinate( i );
	}

	/**
	 * @return The number of dimensions that comprise this point
	 */
	public int getNDims() { return _coordinates.length; }

	/**
	 * In place, divide each coordinate of this point by a value
	 * 
	 * @param valueToDivideBy Value by which each coordinate of this point will be divided
	 */
	public void rescaleVector(double valueToDivideBy) {
		for( int i = 0; i < _coordinates.length; i++ )
			_coordinates[ i ] /= valueToDivideBy;
	}


}
