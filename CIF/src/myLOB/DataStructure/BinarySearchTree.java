package myLOB.DataStructure;

import java.util.NoSuchElementException;

/** from http://algs4.cs.princeton.edu/32bst/BST.java.html and http://courses.csail.mit.edu/6.006/spring11/rec/rec03.pdf */
public class BinarySearchTree<K extends Comparable<K>> {
	// Root node pointer. Will be null for an empty tree.
	private BinarySearchTreeNode<K>	_root;
	private BinarySearchTreeNode<K>	_max;
	private BinarySearchTreeNode<K>	_min;

	/***********************************************************************
	 * Constructer
	 ***********************************************************************/
	public BinarySearchTree() {
		_root = null;
		_max = null;
		_min = null;
	}

	public BinarySearchTree(final BinarySearchTreeNode<K> node) {
		_root = node;
		_max = node;
		_min = node;
	}

	/***********************************************************************
	 * Search
	 ***********************************************************************/
	// return value associated with the given key, or null if no such key exists
	public BinarySearchTreeNode<K> get(final K data) {
		return (get( _root, data ));
	}

	private BinarySearchTreeNode<K> get(BinarySearchTreeNode<K> root, final K data) {
		while (root != null) {
			final int cmp = data.compareTo( root.getKey() );
			if (cmp < 0) {
				root = root.getLeft();
			} else if (cmp > 0) {
				root = root.getRight();
			} else return root;
		}
		return null;
	}

	/***********************************************************************
	 * Insert
	 ***********************************************************************/
	// inserts the given node into the binary tree. Save as min/max if qualified.
	public void put(final BinarySearchTreeNode<K> node) {
		if (_root == null) {
			_root = node;
			_min = _max = node;
		}
		else {
			put( _root, node );
			if (node.getKey().compareTo( _min.getKey() ) < 0) {
				_min = node;
			} else if (node.getKey().compareTo( _max.getKey() ) > 0) {
				_max = node;
			}
		}
	}

	// non-recursive, put node in the subtree of root
	private void put(BinarySearchTreeNode<K> root, final BinarySearchTreeNode<K> node) {
		node.setParent( null );
		int cmp;
		while (root != null) {
			node.setParent( root );
			cmp = node.getKey().compareTo( root.getKey() );
			if (cmp < 0) {
				root = root.getLeft();
			} else if (cmp > 0) {
				root = root.getRight();
			} else { // replace root if it is same as node
				node.setParent( root.getParent() );
				node.setLeft( root.getLeft() );
				node.setRight( root.getRight() );
				root = node;
				return;
			}
		}
		cmp = node.getKey().compareTo( node.getParent().getKey() );
		if (cmp < 0) {
			node.getParent().setLeft( node );
		} else {
			node.getParent().setRight( node );
		}
	}
	

	/***********************************************************************
	 * Delete
	 ***********************************************************************/
	// delete the minimum node, return the deleted node
	public BinarySearchTreeNode<K> deleteMin() {
		final BinarySearchTreeNode<K> ret = _min;
		delete( _min );
		return ret;
	}

	// delete the maximum node, return the deleted node
	public BinarySearchTreeNode<K> deleteMax() {
		final BinarySearchTreeNode<K> ret = _max;
		delete( _max );
		return ret;
	}

	// delete the specific node, non-recursive version
	public void delete(final BinarySearchTreeNode<K> node) {
		if (node == null) throw new NoSuchElementException( "Delete: no such node." );
		// if node is the only left node
		if (node == _root && node.getLeft() == null && node.getRight() == null) {
			_root = null;
			_min = null;
			_max = null;
			return;
		}
		// if equals min/max, update new min/max
		if (node == _min) _min = nextLarger( node );
		else if (node == _max) _max = nextSmaller( node );
		// delete this node
		// if node has no child
		if (node.getLeft() == null && node.getRight() == null) {
			if (node.getParent().getLeft() == node) node.getParent().setLeft( null );
			else node.getParent().setRight( null );
		}
		// node has one child
		else if (node.getLeft() == null) connect( node, node.getRight() );
		else if (node.getRight() == null) connect( node, node.getLeft() );
		// node has two child
		else {
			final BinarySearchTreeNode<K> nextLarge = nextLarger( node );
			connect( nextLarge, nextLarge.getRight() );
			connect( node, nextLarge );
		}

	}

	// connect node's parent to another node, so we remove this node
	private void connect(final BinarySearchTreeNode<K> node, final BinarySearchTreeNode<K> replace) {
		final BinarySearchTreeNode<K> parent = node.getParent();
		// if this is the root
		if (parent == null) _root = replace;
		else if (parent.getLeft() == node) parent.setLeft( replace );
		else parent.setRight( replace );
		replace.setParent( parent );
		
	}

	/*************************************************************************
	 * Ordered symbol table methods.
	 *************************************************************************/
	// the minimum node;
	public BinarySearchTreeNode<K> min() {
		return _min;
	}

	// the maximum node;
	public BinarySearchTreeNode<K> max() {
		return _max;
	}

	// get the next largest node than a given node
	public BinarySearchTreeNode<K> nextLarger(BinarySearchTreeNode<K> node) {
		if (node.getRight() != null) return getMin( node.getRight() );
		BinarySearchTreeNode<K> parent = node.getParent();
		while (parent != null && node == parent.getRight()) {
			node = parent;
			parent = parent.getParent();
		}
		return parent;
	}

	// get the next largest node than a given node
	public BinarySearchTreeNode<K> nextSmaller(BinarySearchTreeNode<K> node) {
		if (node.getLeft() != null) return getMax( node.getLeft() );
		BinarySearchTreeNode<K> parent = node.getParent();
		while (parent != null && node == parent.getLeft()) {
			node = parent;
			parent = parent.getParent();
		}
		return parent;
	}

	// get the min in the substree of node
	private BinarySearchTreeNode<K> getMin(BinarySearchTreeNode<K> node) {
		if (node == null) return null;
		while (node.getLeft() != null) node = node.getLeft();
		return node;
	}

	// get the max in a subtree of node
	private BinarySearchTreeNode<K> getMax(BinarySearchTreeNode<K> node) {
		if (node == null) return null;
		while (node.getRight() != null) node = node.getRight();
		return node;
	}

	// the largest node less than or equal to the given key
	public BinarySearchTreeNode<K> floor(final BinarySearchTreeNode<K> node) {
		final K key = node.getKey();
		final BinarySearchTreeNode<K> x = floor( _root, key );
		return x;
	}

	// the largest node in the subtree rooted at x less than or equal to the given key
	private BinarySearchTreeNode<K> floor(final BinarySearchTreeNode<K> x, final K key) {
		if (x == null) return null;
		final int cmp = key.compareTo( x.getKey() );
		if (cmp == 0) return x;
		if (cmp < 0) return floor( x.getLeft(), key );
		final BinarySearchTreeNode<K> t = floor( x.getRight(), key );
		if (t != null) return t;
		else return x;
	}

	// the smallest node greater than or equal to the given key
	public BinarySearchTreeNode<K> ceiling(final BinarySearchTreeNode<K> node) {
		final K key = node.getKey();
		final BinarySearchTreeNode<K> x = ceiling( _root, key );
		return x;
	}

	// the smallest node in the subtree rooted at x greater than or equal to the given key
	private BinarySearchTreeNode<K> ceiling(final BinarySearchTreeNode<K> x, final K key) {
		if (x == null) return null;
		final int cmp = key.compareTo( x.getKey() );
		if (cmp == 0) return x;
		if (cmp > 0) return ceiling( x.getRight(), key );
		final BinarySearchTreeNode<K> t = ceiling( x.getLeft(), key );
		if (t != null) return t;
		else return x;
	}

	/*************************************************************************
	 * Size methods
	 *************************************************************************/
	// is the symbol table empty?
	public boolean isEmpty() {
		return _root == null;
	}
}