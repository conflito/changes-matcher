package datatypes;

/**
 * A node of the linked list holding consecutive 
 * elements of the list in an array.
 */
final class Node<T>{
	final T[] elems;
	int indexFirst;	
	Node<T> next;
	Node<T> previous;

	/**
	 * Constructs a new Node given the number of elements it
	 * should hold and the next node. 
	 * 
	 * @param numElems
	 *            The number of elements this node should have space for.
	 * @param next
	 *            The node in the linked list that follows this one.
	 */
	@SuppressWarnings("unchecked")
	public Node(int numElems, Node<T> nxt) {
		this.elems = (T[]) new Object[numElems];
		this.next = nxt;
		this.previous = null;

		/* Update the next cell to point back to us. */
		if (this.next != null)
			this.next.previous = this;

		/* Since the array is empty, numElems i.e. elems.lenght. */
		this.indexFirst = numElems;
	}

	/**
	 * Constructs a new Node given the array with the elements,
	 * the previous and the next node, and the index of the first 
	 * element.
	 * 
	 * @param elems
	 *            The array with the elements.
	 * @param prev
	 *            The node in the linked list that precedes this one.
	 * @param next
	 *            The node in the linked list that follows this one.
	 * @param indexFirst
	 *            The index of the first element in the array.
	 */
	Node(T[] v, Node<T> prev, Node<T> nx, int indexF) {
		this.elems = v;
		this.next = nx;		
		this.previous = prev;
		this.indexFirst = indexF;
	}

	/**
	 * @return The number of elements of the list stored
	 * in the node.
	 */
	public int numElems(){
		return elems.length - indexFirst;
	}

	/**
	 * @return If the number of elements of the list stored
	 * in the node is the node's capacity.
	 */
	public boolean isFull(){
		return indexFirst == 0 ;
	}

	/**
	 * @return If the node does not hold any list element.
	 */
	public boolean isEmpty(){
		return indexFirst == elems.length;
	}

	public int lastIndex(){
		return elems.length - 1;
	}	

}