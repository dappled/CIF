package clustersSolution;

import java.util.ArrayList;
import java.util.List;

public class Lloyd extends AbstractClusterAlgorithm {
	
	public Lloyd( ArrayList<Cluster> clusters, ArrayList<ClusterPoint> points ) {
		super( clusters, points );
	}
	
	@Override
	public void initializeClusters() {}
	
	@Override
	public void postProcessResults() {}

	/**
	 * Main processing method for one iteration of this algorithm.
	 * Each point in a list will seek out the closest cluster and
	 * add itself to that cluster. All clusters will then re-compute
	 * their centroids.
	 */
	@Override
	public void performOneIteration() {
		
		// Each cluster may have some points associated with it from the
		// previous iteration. We want to clear these points.
		
			for( Cluster cluster : _clusters )
				cluster.resetPointsList();
			
		// We will now iterate over every point, assigning it to its
		// closest cluster. In the standard version of Lloyd's algo,
		// it doesn't matter how many points are already assigned to
		// this cluster
			
			for( ClusterPoint point: _points ) {
				Cluster cluster = point.findNearestCluster( _clusters );
				cluster.addPoint( point );
				point.setNewCluster( cluster );
			}
			
		// Recompute centroids for all clusters
			
			for( Cluster cluster : _clusters )
				cluster.computeCentroid();
		
		// Count how many points moved from their previous cluster
		// to a new cluster and return this value
		
			for( ClusterPoint point : _points ) {
				if( point.getCluster() != point.getNewCluster() ) {
					_nChanges++;
					point.setCluster( point.getNewCluster() );
				}
			}
	}

	public List<Cluster> getClusters() { return _clusters; }

}
