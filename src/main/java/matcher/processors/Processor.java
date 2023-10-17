package matcher.processors;

public interface Processor<K, T> {

	public K process(T element);
}
