/**
 * API for mutable maps with strings as keys. 
 *
 * @param <V> The type of the values in the map.
*/
public interface StringsMap<V> {

	/**
	 * The number of entries (key-value pairs) in this map.
	 * @return The number of entries (key-value pairs) in this map.
	 */
	int size();

	/**
	 * Does this map contains a given key?
	 * @param key The key being sought.
	 * @requires key != null && key.lenght > 0;
	 * @return Whether this map contains a given key.
	 */
	boolean containsKey(String key);

	/**
	 * Obtain the value associated with a given key.
	 * 
	 * @param key The key being sought
	 * @return The value associated with this key if found; otherwise, null
	 * @requires key != null && key.lenght > 0;
	 */
	V get(String key);

	/**
	 * This key-value pair is inserted in the map. 
	 * If the key is already in the map, its value is changed to the argument value. 
	 * 
	 * @param key The key of item being inserted
	 * @param value The value for this key
	 * @requires key != null && key.lenght > 0 && value != null;
	 */
	void put(String key, V value);	
	
	/**
	 * Returns all keys in the map as an {@code Iterable}.
	 * @return all keys in the map as an {@code Iterable}
	 */
	public Iterable<String> keys();
}