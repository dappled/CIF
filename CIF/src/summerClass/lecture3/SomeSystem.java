package summerClass.lecture3;

import java.util.LinkedList;

import summerClass.lecture3.RelativePriceChange;

public class SomeSystem {
	
	LinkedList< AnalyticsInterface< RelativePriceChange > > _analyticsList;
	
	public SomeSystem() {
		_analyticsList = new LinkedList< AnalyticsInterface<RelativePriceChange> >();
	}
	
	public void addAnalytics( AnalyticsInterface< RelativePriceChange > analysisObject ) {
		_analyticsList.add( analysisObject );
	}
	
	public void updatePriceChange( RelativePriceChange rpc ) throws Exception {
		for( AnalyticsInterface<RelativePriceChange> a : _analyticsList )
			a.add( rpc );
	}
	
}
