package doubleDispatch;

public class Processor {

	public void process( AClass a ) {
		a.getProcessedBy( this );
	}
	
	public void processAClass( AClass a ) {
		System.out.println( "I am processing an " + a.identity() );
	}
	
	public void processBClass( BClass b) {
		System.out.println( "I am processing a " + b.identity() );
	}
	
	public void processCClass( CClass c ) {
		System.out.println( "I am processing a " + c.identity() );
	}
	
	public static void main( String[] args ) {
		Processor p = new Processor();
		p.process( new AClass() );
		p.process( new BClass() );
		p.process( new CClass() );
	}
	
}
