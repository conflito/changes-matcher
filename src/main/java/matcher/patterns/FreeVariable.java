package matcher.patterns;

public class FreeVariable {

	private int id;
	
	private String value;

	public FreeVariable(int id) {
		super();
		this.id = id;
	}
	
	public FreeVariable(FreeVariable freeVariable) {
		super();
		this.id = freeVariable.id;
		this.value = freeVariable.value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void clean() {
		setValue(null);
	}

	public int getId() {
		return id;
	}
	
	public boolean isId(int id) {
		return getId() == id;
	}
	
	public boolean hasValue() {
		return getValue() != null;
	}
	
	public int hashCode() {
		return getId();
	}
	
	public boolean equals(Object o) {
		return (this == o) || (o instanceof FreeVariable && 
									equalsFreeVariable((FreeVariable)o));
	}

	private boolean equalsFreeVariable(FreeVariable f) {
		if(getId() != f.getId())
			return false;
		if((hasValue() && !f.hasValue()) || (!hasValue() && f.hasValue()))
			return false;
		if(!hasValue() && !f.hasValue())
			return true;
		return getValue().equals(f.getValue());
	}
	
	public boolean matches(String value) {
		return hasValue() && this.value.equals(value);
	}
}
