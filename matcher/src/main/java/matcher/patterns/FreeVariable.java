package matcher.patterns;

public class FreeVariable {

	private int id;
	
	private String value;

	public FreeVariable(int id) {
		super();
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}
	
	public boolean hasValue() {
		return getValue() != null;
	}
}
