package matcher.processors;

public abstract class Processor<K, T> {

	public abstract K process(T element);
}
