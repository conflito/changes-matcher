package datatypes;

import java.util.Iterator;

import org.apache.commons.lang3.SerializationUtils;

/**
 * An implementation of a list based on a double linked list of nodes 
 * holding consecutive elements of the list in an array.
 *  
 * A       - Array
 * DLList  - Double Linked List
 * 
 * @author malopes @ AED1617 
 * (adaptation of class VList by Keith Schwarz)
 */

public final class ADLList<T> implements Iterable<T>, Cloneable{


	/* ************ FIELDS ************ */

	/* The head of the linked list, which contains the final elements of the list. */
	private Node<T> head;

	/* Cached total number of elements in the list. */
	private int size;

	//@invariant for all node n in the linked list but the first n.isfull()

	/* ************ METHODS ************ */

	/**
	 * Adds a new element to the end of the list.
	 * 
	 * @param elem
	 *            The element to add.
	 */
	public void add(T elem) {
		/* If head is null or no free space exists in the head node,
		 *  add a new node to the linked list. */
		if (head == null || head.isFull())
			head = new Node<T>(head == null? 1 : head.elems.length * 2, head);

		/* Prepend this element to the current head node. */
		head.elems[head.indexFirst-1 ] = elem;

		head.indexFirst--;
		size++;
	}

	/**
	 * Returns the cached size of the list.
	 * 
	 * @return The size of the ADLList.
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks if the list is empty.
	 * 
	 * @return Whether the list is empty or not.
	 */  
	public boolean isEmpty(){
		return size()==0;
	}

	/**
	 * Given an index of the ADLList, returns a location 
	 * where that object is in the ADLList.
	 * 
	 * @param index
	 *            The index into the ADLList.
	 * @return A Location object holding information about where that
	 *         element can be found in the linked list.
	 */
	//@requires 0 <= index && index < size 
	private Location<T> locateListIndex(int index) {


		Node<T> curr = head;
		/* The index relative to the part of the list not scanned yet. */
		int currIndex = size() - 1 - index;

		while (currIndex >= curr.elems.length - curr.indexFirst) {
			currIndex -= curr.numElems();
			curr = curr.next;
		}
		return new Location<T>(curr, currIndex + curr.indexFirst);
	}

	/**
	 * Scans for the proper location in the cell list for the element, then
	 * returns the element at that position.
	 * 
	 * @param index
	 *            The index at which to look up the element.
	 * @return The element at that position.
	 */
	//@requires 0 <= index && index < size 
	public T get(int index) {
		Location<T> where = locateListIndex(index);
		return where.node.elems[where.index];
	}

	/**
	 * Sets an element at a particular position to have a particular value.
	 * 
	 * @param index
	 *            The index at which to write a new value.
	 * @param value
	 *            The value to write at that position.
	 * @return The value originally held at that position.
	 */
	//@requires 0 <= index && index < size 
	public T set(int index, T value) {
		Location<T> where = locateListIndex(index);
		T result = where.node.elems[where.index];
		where.node.elems[where.index] = value;
		return result;
	}

	/**
	 * Removes the element at the specified position from the ADLList, returning
	 * its value.
	 * 
	 * @param index
	 *            The index at which the element should be removed.
	 * @return The value held at that position.
	 */
	public T remove(int index) {
		Location<T> where = locateListIndex(index);
		T result = where.node.elems[where.index];
		/* Invoke the helper to do most of the work. */
		removeAtPosition(where);
		return result;
	}

	/**
	 * Removes the element at the indicated ADLListLocation.
	 * 
	 * @param where
	 *            The location at which the element should be removed.
	 */
	private void removeAtPosition(Location<T> where) {
		Node<T> curr = where.node;
		int pos = where.index; 

		while (curr != null){
			for (int i = pos - 1; i >= 0; i--)
				curr.elems[i + 1] = curr.elems[i];
			if (curr.previous != null)
				curr.elems[0] = curr.previous.elems[curr.previous.lastIndex()];

			curr = curr.previous;
			pos = curr == null? 0 : curr.lastIndex();
		}

		head.indexFirst++;
		head.elems[head.indexFirst - 1] = null;

		size--;

		if (head.isEmpty()) {
			head = head.next;
			if (head != null)
				head.previous = null;
		}
	}


	/**
	 * Equals.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		return this == other || other!= null && this.getClass() == other.getClass()
				&& equalLists((ADLList<T>) other);
	}

	/**
	 * @param other
	 * 			The other list
	 * @return Is this list equal to another list?
	 */
	private boolean equalLists(ADLList<T> other) {
		if (size() != other.size())
			return false;
		Iterator<T> it = other.iterator();
		for (T e: this){
			if (!e.equals(it.next()))
				return false;
		}
		return true;
	}

	/**
	 * @return Textual representation, in the form [1, 2, 3, 4].
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("[");
		for (T e: this){
			result.append(e+", ");
		}
		if (result.length() > 1)
			result.delete(result.length()-2, result.length());
		result.append("]");
		return result.toString();
	}
	
	/**
	 * @return Textual representation with internal representation.
	 * Useful for debugging.
	 */
	public String toStringForDebugging() {
		StringBuilder result = new StringBuilder(" ");
		
		Node<T> curr = head;
		while (curr != null){
			for (int i = 0; i < curr.elems.length; i++)
				result.append("v["+i+"]=" + curr.elems[i] + " ");
			result.append("indexF "+ curr.indexFirst);
			result.append(" --> ");
			curr = curr.next;
		}
		return result.toString();
	}

	/**
	 * @return A deep clone of the list (clones the elements of the list).
	 */
	@Override
	public ADLList<T> clone(){
		ADLList<T> result = null;
		try {
			result = (ADLList<T>) super.clone();
			copyNodes(result);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;		
	}

	/**
	 * Clones the node structure and the elements (deep clone).
	 */
	private void copyNodes(ADLList<T> result) {
		if (!isEmpty()) {
			result.head = new Node<>(SerializationUtils.clone(head.elems), 
					null, head.next, head.indexFirst);
			Node<T> p = result.head;
			while (p.next != null) {
				p.next = new Node<>(SerializationUtils.clone(p.next.elems),
						p, p.next.next,p.next.indexFirst);
				p = p.next;
			}
		}
	}

	/**
	 * An iterator for the elements in the ADLList.
	 */
	@Override
	public Iterator<T> iterator() {
		return new ADLListIterator<T>(this.head);
	}
}