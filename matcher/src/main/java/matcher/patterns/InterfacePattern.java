package matcher.patterns;

import matcher.entities.InterfaceInstance;

public class InterfacePattern {

	private FreeVariable freeVariable;

	public InterfacePattern(FreeVariable freeVariable) {
		super();
		this.freeVariable = freeVariable;
	}

	public FreeVariable getFreeVariable() {
		return freeVariable;
	}
	
	public int getVariableId() {
		return freeVariable.getId();
	}
	
	public boolean isVariableId(int id) {
		return getVariableId() == id;
	}
	
	public void clean() {
		freeVariable.clean();
	}
	
	public void setVariableValue(int id, String value) {
		if(isVariableId(id))
			freeVariable.setValue(value);
	}
	
	public boolean filled() {
		return freeVariable.hasValue();
	}
	
	public boolean matches(InterfaceInstance i) {
		return filled() && sameName(i);
	}

	private boolean sameName(InterfaceInstance i) {
		return i.getName().equals(getFreeVariable().getValue());
	}
}
