package myLOB.DataStructure;

public interface BinarySearchTreeNode<Key extends Comparable<Key>> {
	Key getKey();
	BinarySearchTreeNode<Key> getLeft();
	BinarySearchTreeNode<Key> getRight();
	BinarySearchTreeNode<Key> getParent();
	void setLeft(BinarySearchTreeNode<Key> node);
	void setRight(BinarySearchTreeNode<Key> node);
	void setParent(BinarySearchTreeNode<Key> node);
}
