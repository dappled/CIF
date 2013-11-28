package clustersSolution;

import java.util.ArrayList;

public class ReverseLloyd extends AbstractClusterAlgorithm {
	
	protected int _clusterSize;
	
	public ReverseLloyd( ArrayList<Cluster> clusters, ArrayList<ClusterPoint> points ) throws Exception {
		super( clusters, points );
		_clusterSize = points.size() / clusters.size();
		if( _clusterSize * clusters.size() != points.size() )
			throw new Exception( "Number of points must be a multiple of cluster size" );
	}
	
	@Override
	public void initializeClusters() {}
	
	@Override
	public void postProcessResults() {}
	
	/** 
	 * Main processing method for this algorithm.
	 * Each cluster seeks the nearest 20 points and adds them
	 * to itself.
	 */
	@Override
	public void performOneIteration() {
		for( Cluster cluster : _clusters ) {
			cluster.replaceWithNearest( _clusterSize, _points );
			cluster.computeCentroid();
		}
		for( ClusterPoint point : _points ) {
			if( point.getCluster() != point.getNewCluster() ) {
				_nChanges++;
				point.setCluster( point.getNewCluster() );
			}
			point.setNewCluster( null );
		}
	}

}
