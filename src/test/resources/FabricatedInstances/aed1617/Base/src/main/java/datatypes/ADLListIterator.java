package datatypes;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A custom iterator class that traverses the elements of this list.
 * This iterator works by traversing the nodes as a proper linked list.
 */
final class ADLListIterator<T> implements Iterator<T> {

	private Node<T> currNode;
	private int currIndex;

	/**
	 * Constructs a new ADLListIterator that will traverse the elements of the
	 * given ADLList.
	 * @param adlList The node we want to start to iterate
	 */
	public ADLListIterator(Node<T> node) {
		Node<T> curr = node;
		Node<T> prev = null;			
		while (curr != null){
			prev = curr;
			curr = curr.next;
		}

		/* Set the current node to the tail. */
		currNode = prev;
		if (currNode != null)
			currIndex = currNode.lastIndex(); 
	}

	/**
	 * 
	 */
	@Override
	public boolean hasNext() {
		return currNode != null;
	}

	/**
	 * Advances the iterator and returns the element it used to be over.
	 */
	@Override
	public T next() {
		/* Bounds-check. */
		if (!hasNext())
			throw new NoSuchElementException();

		/* Cache the return value. */
		T result = currNode.elems[currIndex];

		/* Back up one step. */
		currIndex--;

		/* If we walked off the end of the buffer, advance to the next
		 * element of the list.
		 */
		if (currIndex < currNode.indexFirst) {
			currNode = currNode.previous;
			if (currNode != null)
				currIndex = currNode.lastIndex();
		}

		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}