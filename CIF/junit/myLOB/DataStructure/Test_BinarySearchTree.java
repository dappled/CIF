package myLOB.DataStructure;

import myLOB.DataStructure.BinarySearchTree;
import myLOB.DataStructure.BinarySearchTreeNode;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Zhenghong Dong
 */
public class Test_BinarySearchTree {

	/** Simple node implements {@link BinarySearchTreeNode}, for test only */
	private class Node implements BinarySearchTreeNode<Integer> {
		private BinarySearchTreeNode<Integer> _left;
		private BinarySearchTreeNode<Integer> _right;
		private BinarySearchTreeNode<Integer> _parent;
		private final int _key;

		public Node(final int key) {
			_key = key;
			_left = null;
			_right = null;
			_parent = null;
		}

		@Override
		public Integer getKey() { return _key; }
		@Override
		public BinarySearchTreeNode<Integer> getLeft() { return _left; }
		@Override
		public BinarySearchTreeNode<Integer> getRight() { return _right; }
		@Override
		public BinarySearchTreeNode<Integer> getParent() { return _parent; }
		@Override
		public void setLeft(final BinarySearchTreeNode<Integer> node) { _left = node; }
		@Override
		public void setRight(final BinarySearchTreeNode<Integer> node) { _right = node; }
		@Override
		public void setParent(final BinarySearchTreeNode<Integer> node) { _parent = node; }
	}


	/**
	 * Test {@link BinarySearchTreeNode} with this local {@link Node} implementation
	 */
	@Test
	public void testNode() {
		final Node a = new Node( 5 );

		Assert.assertTrue( a.getKey() == 5 );
		Assert.assertTrue( a.getLeft() == null );
		Assert.assertTrue( a.getRight() == null );
		Assert.assertTrue( a.getParent() == null );

		final Node b = new Node( 6 );
		final Node c = new Node( 7 );
		a.setLeft( b );
		a.setRight( c );
		b.setParent( a );
		c.setParent( a );

		Assert.assertTrue( a.getLeft() == b );
		Assert.assertTrue( a.getRight() == c );
		Assert.assertTrue( b.getParent() == a );
		Assert.assertTrue( c.getParent() == a );
	}

	/**
	 * Test {@link BinarySearchTree} methods
	 */
	@Test
	public void testTree() {
		// test put
		final BinarySearchTree<Integer> tree = new BinarySearchTree<>();
		for (int i = 0; i < 10; i++) {
			tree.put( new Node( i ) );
		}
		// test min max
		final Node min = (Node) tree.min();
		final Node max = (Node) tree.max();
		Assert.assertTrue( min.getKey() == 0 );
		Assert.assertTrue( max.getKey() == 9 );

		// test get nextLarger nextSmaller
		final Node j = (Node) tree.get( 6 );
		Assert.assertTrue( ((Node) tree.nextLarger( j )).getKey() == 7 );
		Assert.assertTrue( ((Node) tree.nextSmaller( j )).getKey() == 5 );

		// test deleteMin deleteMax
		Assert.assertTrue( ((Node) tree.deleteMin()) == min );
		Assert.assertTrue( ((Node) tree.deleteMax()) == max );
		Assert.assertTrue( ((Node) tree.deleteMin()).getKey() == 1 );
		Assert.assertTrue( ((Node) tree.deleteMax()).getKey() == 8 );
		// test min/max after deleteMin/Max
		Assert.assertTrue( ((Node) tree.min()).getKey() == 2 );
		Assert.assertTrue( ((Node) tree.max()).getKey() == 7 );

		// test delete
		tree.delete( j );
		Assert.assertTrue( ((Node) tree.nextSmaller( tree.max() )).getKey() == 5 );
		Assert.assertTrue( tree.nextLarger( tree.max() ) == null );
	}

}
