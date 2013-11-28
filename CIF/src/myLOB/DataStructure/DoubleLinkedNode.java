package myLOB.DataStructure;

/**
 * One node for the double linked list
 * @author Zhenghong Dong
 */
public interface DoubleLinkedNode {
	public void setNext( DoubleLinkedNode next );
	public void setPrev( DoubleLinkedNode prev );
	
	public DoubleLinkedNode getNext();
	public DoubleLinkedNode getPrev();
}
