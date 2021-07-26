package datatypes;

/**
 * A utility class containing information about where an element 
 * is in the ADLList: the node and the index in its array.
 */
final class Location<T> {
	public final Node<T> node;
	public final int index;

	public Location(Node<T> cell, int index) {
		node = cell;
		this.index = index;
	}
}