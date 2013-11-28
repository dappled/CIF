package doubleDispatch;

public class AClass {

	public void getProcessedBy( Processor p ) {
		p.processAClass( this );
	}
	
	public String identity() { return "AClass"; }
	
}
