package clustersSolution;

import java.util.ArrayList;

import junit.framework.TestCase;

public class Test_ReverseLloyd extends TestCase {
	
	public void testReverseLloyd() throws Exception {
		long randomSeed = 59;
		ClusterTestProblem ctp = new ClusterTestProblem( randomSeed );
		ArrayList<ClusterPoint> points = ctp.getPoints();
		ArrayList<Cluster> clusters = ctp.getClusters( 500, points );
		ReverseLloyd la = new ReverseLloyd( clusters, points );
		la.solve();
	}

}
