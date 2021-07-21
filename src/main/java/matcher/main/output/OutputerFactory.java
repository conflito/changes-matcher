package matcher.main.output;

public class OutputerFactory {
	private static OutputerFactory instance = new OutputerFactory();

	private OutputerFactory() {}

	public Outputer getOutputer() {
		return new StandardOutputer();
	}

	public static OutputerFactory getInstance() {
		return instance;
	}
}
