package clustersSolution;

import java.util.ArrayList;
import java.util.Random;

public class ClusterTestProblem {

	protected Random _random = null;

	public ClusterTestProblem( long randomSeed ) { _random = new Random( randomSeed ); }
	
	/**
	 * Generates a 100 x 100 grid of points, 10,000 points in all.
	 * 
	 * @return ArrayList of points generated
	 */
	public ArrayList<ClusterPoint> getPoints() {
		ArrayList<ClusterPoint> points = new ArrayList<ClusterPoint>();
		for( int x = 0; x < 6; x++ ) {
			for( int y = 0; y < 6; y++ ) {
				double[] vector = new double[ 2 ];
				vector[ 0 ] = x;
				vector[ 1 ] = y;
				points.add( new ClusterPoint( vector ) );
			}
		}
		return points;
	}
	
	/**
	 * Generates a list of prospective cluster centroids by randomly sampling from
	 * points in the solution space. This is called The Forgy initialization approach.
	 *  
	 * @param nClusters Number of clusters to create
	 * @param points List of points from which to sample
	 * @return A list of clusters
	 */
	public ArrayList<Cluster> getClusters( int nClusters, ArrayList<ClusterPoint> points ) {
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
//		for( int i = 0; i < nClusters; i++ ) {
//			int iPoint = (int) Math.floor( _random.nextDouble() * (double)points.size() );
//			clusters.add( new Cluster( new ClusterPoint( points.get( iPoint ) ) ) );
//		}
		double[] vec1 = new double[2];
		double[] vec2 = new double[2];
		vec1[0] = 2; vec1[1] = 2;
		vec2[0] = 3; vec2[1] = 3;
		clusters.add( new Cluster(new ClusterPoint( vec1 ) ));
		clusters.add( new Cluster(new ClusterPoint( vec2 ) ));
		return clusters;
	}

}
