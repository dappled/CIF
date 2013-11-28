package clustersSolution;

import java.util.LinkedList;
import java.util.List;

public class Test_Cluster extends junit.framework.TestCase {
	
	public void testCentroidAndDistanceMetric() throws Exception {
		
		double[] vector = { 1.0, 2.0 };
		Point centroid = new ClusterPoint( vector );

		double[] vector1 = { 5.0, 6.0 };
		ClusterPoint p1 = new ClusterPoint( vector1 );
		
		double[] vector2 = { 6.0, 8.0 };
		ClusterPoint p2 = new ClusterPoint( vector2 );
		
		Cluster cluster = new Cluster( centroid );
		cluster.addPoint( p1 );
		cluster.addPoint( p2 );

		// Test distance metric calculation
			
			double d1 = Math.sqrt( Math.pow( 5.0 - 1.0, 2.0 ) + Math.pow( 6.0 - 2.0, 2.0 ) ); 
			double d2 = Math.sqrt( Math.pow( 6.0 - 1.0, 2.0 ) + Math.pow( 8.0 - 2.0, 2.0 ) );
			
			double expectedMetric = d1 + d2;
			double actualMetric = cluster.getDistanceMetric(); // Test distance metric 
			assertEquals( actualMetric, expectedMetric );
		
		// Test centroid calculation
			
			cluster.computeCentroid();
			assertEquals(
					( 6.0 + 5.0 ) / 2.0,
					cluster.getCentroid().getCoordinate( 0 ),
					0.001
			);
			assertEquals(
					( 8.0 + 6.0 ) / 2.0,
					cluster.getCentroid().getCoordinate( 1 ),
					0.001
			);
			
	}
	
	public void testFindNearest() throws Exception {
		
		double[] vector = { 5.0, 5.0 };
		Point centroid = new ClusterPoint( vector );

		double[] vector1 = { 5.0, 6.0 };
		ClusterPoint p1 = new ClusterPoint( vector1 );
		
		double[] vector2 = { 6.0, 6.0 };
		ClusterPoint p2 = new ClusterPoint( vector2 );
		
		double[] vector3 = { 4.5, 5.5 };
		ClusterPoint p3 = new ClusterPoint( vector3 );
		
		double[] vector4 = { 3.5, 2.5 };
		ClusterPoint p4 = new ClusterPoint( vector4 );

		LinkedList<ClusterPoint> pointsList = new LinkedList<ClusterPoint>();
		pointsList.add( p4 );
		pointsList.add( p1 );
		pointsList.add( p2 );
		pointsList.add( p3 );
		
		Cluster cluster = new Cluster( centroid );
		List<ClusterPoint> foundPoints = cluster.findNearestNPoints( 2, pointsList );
		assertTrue( foundPoints.size() == 2 );

		assertEquals( foundPoints.get( 0 ).getCoordinate( 0 ), 4.5, 0.0001 );
		assertEquals( foundPoints.get( 0 ).getCoordinate( 1 ), 5.5, 0.0001 );

		assertEquals( foundPoints.get( 1 ).getCoordinate( 0 ), 5.0, 0.0001 );
		assertEquals( foundPoints.get( 1 ).getCoordinate( 1 ), 6.0, 0.0001 );
	}
	
	public void testReplaceAllPoints() {
		
		double[] zeroVector = { 0.0, 0.0 };
		Cluster cluster1 = new Cluster( new ClusterPoint( zeroVector ) );
		
		double[] vector = { 5.0, 5.0 };
		Point centroid = new ClusterPoint( vector );

		double[] vector1 = { 5.0, 6.0 };
		ClusterPoint p1 = new ClusterPoint( vector1 );
		p1.setCluster( cluster1 );
		p1.setNewCluster( null );
		
		double[] vector2 = { 6.0, 6.0 };
		ClusterPoint p2 = new ClusterPoint( vector2 );
		p2.setCluster( cluster1 );
		p2.setNewCluster( null );
		
		double[] vector3 = { 4.5, 5.5 };
		ClusterPoint p3 = new ClusterPoint( vector3 );
		p3.setCluster( cluster1 );
		p3.setNewCluster( null );

		double[] vector4 = { 3.5, 2.5 };
		ClusterPoint p4 = new ClusterPoint( vector4 );
		p4.setCluster( cluster1 );
		p4.setNewCluster( null );

		LinkedList<ClusterPoint> pointsList = new LinkedList<ClusterPoint>();
		pointsList.add( p4 );
		pointsList.add( p1 );
		pointsList.add( p2 );
		pointsList.add( p3 );
		
		Cluster cluster2 = new Cluster( centroid );
		cluster2.addPoint( p1 );
		cluster2.computeCentroid();
		
		assertEquals( cluster2.getCentroid().getCoordinate( 0 ), p1.getCoordinate( 0 ), 0.0001 );
		assertEquals( cluster2.getCentroid().getCoordinate( 1 ), p1.getCoordinate( 1 ), 0.0001 );
		
		cluster2.replaceAllPoints( pointsList );
		List<ClusterPoint> newPointsList = cluster2.getPoints();
		for( ClusterPoint point : newPointsList ) {
			assertTrue( point.getNewCluster() == cluster2 );
			assertTrue( point.getCluster() == cluster1 );
		}
		cluster2.computeCentroid();
		
		Point expectedCentroid = ClusterPoint.computeCentroids( pointsList );
		Point actualCentroid = cluster2.getCentroid();
		double tolerance = 0.0001;
		assertEquals( expectedCentroid.getCoordinate( 0 ), actualCentroid.getCoordinate( 0 ), tolerance );
		assertEquals( expectedCentroid.getCoordinate( 1 ), actualCentroid.getCoordinate( 1 ), tolerance );
		
	}
	
	
}
