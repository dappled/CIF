package myLOB.DataStructure;

import myLOB.DataStructure.DoubleLinkedList;
import myLOB.DataStructure.DoubleLinkedNode;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Zhenghong Dong
 */
public class Test_DoubleLinkedList {

	/** Simple node implements {@link DoubleLinkedNode}, for test only */
	private class Node implements DoubleLinkedNode {
		private final int _x;
		private DoubleLinkedNode _prev;
		private DoubleLinkedNode _next;
		public Node(final int x) {
			_x = x;
			_prev = null;
			_next = null;
		}

		public int getValue()								{ return _x; }

		@Override
		public void setNext(final DoubleLinkedNode next) 	{ _next = next; }
		@Override
		public void setPrev(final DoubleLinkedNode prev) 	{ _prev = prev; }
		@Override
		public DoubleLinkedNode getNext() 					{ return _next;}
		@Override
		public DoubleLinkedNode getPrev() 					{ return _prev;}

	}

	/**
	 * Test {@link DoubleLinkedNode} with this local {@link Node} implementation
	 */
	@Test
	public void testNode() {
		final Node a = new Node( 59 );
		final Node b = new Node( 62 );

		Assert.assertTrue( a.getValue() == 59 );
		Assert.assertTrue( b.getValue() == 62 );

		Assert.assertTrue( a.getPrev() == null );
		Assert.assertTrue( a.getNext() == null );

		a.setNext( b );
		b.setPrev( a );

		Assert.assertTrue( a.getNext() == b );
		Assert.assertTrue( b.getPrev() == a );
	}

	/**
	 * Test {@link DoubleLinkedList} methods
	 */
	@Test
	public void testDoubleLinkedList() throws Exception {
		// test add
		final DoubleLinkedList list = new DoubleLinkedList();
		for (int i = 0; i < 10; i++) {
			list.add( new Node( i ) );
		}
		Assert.assertTrue( ((Node) list.getFirst()).getValue() == 0 );
		Assert.assertTrue( ((Node) list.getLast()).getValue() == 9 );

		// test deleteFirst
		Node j = (Node) list.deleteFirst();
		Assert.assertTrue( j.getValue() == 0 );
		Assert.assertTrue( ((Node) list.getFirst()).getValue() == 1 );

		// test deleteLast
		j = (Node) list.deleteLast();
		Assert.assertTrue( j.getValue() == 9 );
		Assert.assertTrue( ((Node) list.getLast()).getValue() == 8 );

		// test contains
		j = new Node( 100 );
		list.add( j );
		Assert.assertTrue( list.contains( j ) );

		// test delete
		list.delete( j );
		Assert.assertFalse( list.contains( j ) );
	}
}
