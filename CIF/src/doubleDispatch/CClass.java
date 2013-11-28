package doubleDispatch;

public class CClass extends AClass {
	
	public void getProcessedBy( Processor p ) {
		p.processCClass( this );
	}
	
	public String identity() { return "CClass"; }
	
}
