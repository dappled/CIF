package clustersSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.TestCase;
import utils1309.DoubleComparator;

public class Test_Lloyd extends TestCase {
	
	double _distanceMetric400Clusters;
	double _distanceMetric625Clusters;
	
	/** 
	 * Creates a comparator for sorting Clusters by their centroids
	 * @param tolerance Tolerance for double comparison
	 * @return Comparator of clusters
	 */
	public Comparator<Cluster> getClusterComparator( final double tolerance ) {
		return new Comparator<Cluster>() {
			@Override
			public int compare(Cluster arg0, Cluster arg1) {
				int nDims = arg0.getCentroid().getNDims();
				for( int i = 0; i < nDims; i++ ) {
					int comparison = DoubleComparator.compare(
						arg0.getCentroid().getCoordinate( i ),
						arg1.getCentroid().getCoordinate( i ),
						tolerance
					);
					if( comparison != 0 )
						return comparison;
				}
				return 0;
			}
			
		};
	}
	
	
	/**
	 * Test 1 cluster solution
	 */
	public void testLloyd_1Cluster() {
		long randomSeed = 197234971;
		ClusterTestProblem ctp = new ClusterTestProblem( randomSeed );
		ArrayList<ClusterPoint> points = ctp.getPoints();
		ArrayList<Cluster> clusters = ctp.getClusters( 1, points );
		Lloyd la = new Lloyd( clusters, points );
		la.solve();
		List<Cluster> cluster = la.getClusters();
		assertEquals( cluster.get( 0 ).getCentroid().getCoordinate( 0 ), 49.5, 0.5 );
		assertEquals( cluster.get( 0 ).getCentroid().getCoordinate( 1 ), 49.5, 0.5 );
	}
	
	/**
	 * Test 2 cluster solution
	 */
	public void testLloyd_2Cluster() {
		long randomSeed = 197234971;
		ClusterTestProblem ctp = new ClusterTestProblem( randomSeed );
		for( int i = 0; i < 10; i++ ) {
			ArrayList<ClusterPoint> points = ctp.getPoints();
			ArrayList<Cluster> clusters = ctp.getClusters( 2, points );
			Lloyd la = new Lloyd( clusters, points );
			la.solve();
			List<Cluster> clusterList = la.getClusters();
			
			// 2 possible solutions (but 4 possible ways of ordering solution):
				// { { 24.5, 49.5 }, { 74.5, 49.5 } } <-- These might appear in reverse order so we have to sort
				// { { 49.5, 24.5 }, { 49.5, 74.5 } } <-- There might appear in reverse order so we have to sort

			// Reduce ordering by sorting
			
				double tolerance = 0.001;
				Collections.sort( clusterList, getClusterComparator( tolerance ) );
				
			// Make sure it's one of two solutions
				
				if( clusterList.get(0).getCentroid().getCoordinate(0) < 49 ) {
					// We know it's case 1
					assertTrue( DoubleComparator.compare( clusterList.get( 0 ).getCentroid().getCoordinate( 0 ), 24.5, tolerance ) == 0 );
					assertTrue( DoubleComparator.compare( clusterList.get( 0 ).getCentroid().getCoordinate( 1 ), 49.5, tolerance ) == 0 );
					assertTrue( DoubleComparator.compare( clusterList.get( 1 ).getCentroid().getCoordinate( 0 ), 74.5, tolerance ) == 0 );
					assertTrue( DoubleComparator.compare( clusterList.get( 1 ).getCentroid().getCoordinate( 1 ), 49.5, tolerance ) == 0 );
				} else {
					// We know it's case 2
					assertTrue( DoubleComparator.compare( clusterList.get( 0 ).getCentroid().getCoordinate( 0 ), 49.5, tolerance ) == 0 );
					assertTrue( DoubleComparator.compare( clusterList.get( 0 ).getCentroid().getCoordinate( 1 ), 24.5, tolerance ) == 0 );
					assertTrue( DoubleComparator.compare( clusterList.get( 1 ).getCentroid().getCoordinate( 0 ), 49.5, tolerance ) == 0 );
					assertTrue( DoubleComparator.compare( clusterList.get( 1 ).getCentroid().getCoordinate( 1 ), 74.5, tolerance ) == 0 );
				}
		}
	}
	
	/** Test 4 cluster solution
	 * 
	 */
	public void testLloyd_4Cluster() {
		long randomSeed = System.currentTimeMillis();
		ClusterTestProblem ctp = new ClusterTestProblem( randomSeed );
		for( int i = 0; i < 10; i++ ) {
			
			ArrayList<ClusterPoint> points = ctp.getPoints();
			ArrayList<Cluster> clusters = ctp.getClusters( 4, points );
			Lloyd la = new Lloyd( clusters, points );
			la.solve();
			
			List<Cluster> clusterList = la.getClusters();
			
			// First we want to sort the list so we know the order of the clusters
			
				double tolerance = 0.001;
				Collections.sort( clusterList, getClusterComparator( tolerance ) );
				
			// Now we want to compare to the four known optimal solutions
				
				assertTrue( DoubleComparator.compare( clusterList.get(0).getCentroid().getCoordinate(0), 24.5, tolerance ) == 0 );
				assertTrue( DoubleComparator.compare( clusterList.get(0).getCentroid().getCoordinate(1), 24.5, tolerance ) == 0 );
	
				assertTrue( DoubleComparator.compare( clusterList.get(1).getCentroid().getCoordinate(0), 24.5, tolerance ) == 0 );
				assertTrue( DoubleComparator.compare( clusterList.get(1).getCentroid().getCoordinate(1), 74.5, tolerance ) == 0 );
	
				assertTrue( DoubleComparator.compare( clusterList.get(2).getCentroid().getCoordinate(0), 74.5, tolerance ) == 0 );
				assertTrue( DoubleComparator.compare( clusterList.get(2).getCentroid().getCoordinate(1), 24.5, tolerance ) == 0 );
	
				assertTrue( DoubleComparator.compare( clusterList.get(3).getCentroid().getCoordinate(0), 74.5, tolerance ) == 0 );
				assertTrue( DoubleComparator.compare( clusterList.get(3).getCentroid().getCoordinate(1), 74.5, tolerance ) == 0 );
			
		}
		
	}

	/** Test 5x5 cluster solution
	 * 
	 */
	public void testLloyd_5x5Cluster() {
		long randomSeed = System.currentTimeMillis();
		ClusterTestProblem ctp = new ClusterTestProblem( randomSeed );
		ArrayList<ClusterPoint> points = ctp.getPoints();
		ArrayList<Cluster> clusters = ctp.getClusters( 400, points );
		Lloyd la = new Lloyd( clusters, points );
		la.solve();
		System.out.println( String.format( "5x5 cluster distance metric = %f", la.getDistanceMetric() ) );
	}
	/** Test 4x4 cluster solution
	 * 
	 */
	public void testLloyd_4x4Cluster() {
		long randomSeed = System.currentTimeMillis();
		ClusterTestProblem ctp = new ClusterTestProblem( randomSeed );
		ArrayList<ClusterPoint> points = ctp.getPoints();
		ArrayList<Cluster> clusters = ctp.getClusters( 625, points );
		Lloyd la = new Lloyd( clusters, points );
		la.solve();
		System.out.println( String.format( "4x4 cluster distance metric = %f", la.getDistanceMetric() ) );
	}
	
	/**
	 * Test Lloyd's Algorithm - This is the full test of the homework assignent problem
	 * @throws Exception
	 */
	public void testLloyd() throws Exception {
		long randomSeed = 197234971;
		ClusterTestProblem ctp = new ClusterTestProblem( randomSeed );
		ArrayList<ClusterPoint> points = ctp.getPoints();
		ArrayList<Cluster> clusters = ctp.getClusters( 500, points );
		Lloyd la = new Lloyd( clusters, points );
		la.solve();
		assertEquals( la.getDistanceMetric(), 1.73, 0.015 );
	}

}
