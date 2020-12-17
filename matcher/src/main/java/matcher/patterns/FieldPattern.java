package matcher.patterns;

import matcher.entities.FieldInstance;
import matcher.entities.Visibility;

public class FieldPattern {

	private FreeVariable freeVariable;
	
	private Visibility visibility;

	public FieldPattern(FreeVariable freeVar, Visibility visibility) {
		super();
		this.freeVariable = freeVar;
		this.visibility = visibility;
	}
	
	public FreeVariable getFreeVariable() {
		return freeVariable;
	}
	
	public int getVariableId() {
		return freeVariable.getId();
	}
	
	public void setVariableValue(String value) {
		freeVariable.setValue(value);
	}
	
	public boolean filled() {
		return freeVariable.hasValue();
	}
	
	public boolean isVariableId(int id) {
		return freeVariable.getId() == id;
	}
	
	public boolean matches(FieldInstance instance) {
		return filled() && (visibility == null || sameVisibility(instance)) &&  sameName(instance);
	}
	
	private boolean sameVisibility(FieldInstance instance) {
		return instance.getVisibility() == visibility;
	}
	
	private boolean sameName(FieldInstance instance) {
		return instance.getQualifiedName().equals(freeVariable.getValue());
	}
}
