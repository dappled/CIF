package summerClass.lecture3;

import summerClass.lecture3.DoubleLinkedNode;

public class Test_DoubleLinkedNode extends junit.framework.TestCase {
	
	public void test1() {
		
		DoubleLinkedNode<Integer> dln = new DoubleLinkedNode<Integer>(
			null, 
			59, 
			null
		);
		
		DoubleLinkedNode<Integer> dln2 = new DoubleLinkedNode<Integer>(
			dln, 
			62, 
			null
		);
		
		assertTrue( dln.getNext() == dln2 );
		assertTrue( dln.getPrev() == null  );
		assertTrue( dln2.getNext() == null  );
		assertTrue( dln2.getPrev() == dln  );
		
		DoubleLinkedNode<Integer> dln3 = new DoubleLinkedNode<Integer>(
			dln, 
			73, 
			dln2
		);
		
		assertTrue( dln.getNext() == dln3 );
		assertTrue( dln3.getPrev() == dln  );
		assertTrue( dln3.getNext() == dln2  );
		assertTrue( dln2.getPrev() == dln3  );
		
		dln3.detachNode();

		assertTrue( dln.getNext() == dln2 );
		assertTrue( dln.getPrev() == null  );
		assertTrue( dln2.getNext() == null  );
		assertTrue( dln2.getPrev() == dln  );
		
		dln.detachNode();
		assertTrue( dln2.getPrev() == null );
		
	}
	
}

