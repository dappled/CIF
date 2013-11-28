package doubleDispatch;

public class BClass extends AClass {

	public void getProcessedBy( Processor p ) {
		p.processBClass( this );
	}
	
	public String identity() { return "BClass"; }
	
}
