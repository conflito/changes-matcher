package matcher.patterns;

import matcher.entities.FieldInstance;
import matcher.entities.Visibility;

public class FieldPattern {

	private FreeVariable freeVariable;
	
	private Visibility visibility;
	
	private TypePattern typePattern;

	public FieldPattern(FreeVariable freeVar, Visibility visibility) {
		super();
		this.freeVariable = freeVar;
		this.visibility = visibility;
		this.typePattern = null;
	}
	
	public FieldPattern(FieldPattern fieldPattern) {
		super();
		this.freeVariable = new FreeVariable(fieldPattern.freeVariable);
		this.visibility = fieldPattern.visibility;
		if(fieldPattern.hasType())
			this.typePattern = new TypePattern(fieldPattern.typePattern);
	}
	
	public FreeVariable getFreeVariable() {
		return freeVariable;
	}
	
	public int getVariableId() {
		return freeVariable.getId();
	}
	
	public void setType(TypePattern type) {
		this.typePattern = type;
	}
	
	public boolean hasType() {
		return typePattern != null;
	}
	
	/**
	 * @requires hasType()
	 */
	public int getTypeVariableId() {
		return typePattern.getVariableId();
	}
	
	public void setVariableValue(int id, String value) {
		if(freeVariable.isId(id))
			freeVariable.setValue(value);
		if(typePattern != null)
			typePattern.setVariableValue(id, value);
	}
	
	public void clean() {
		freeVariable.clean();
		if(typePattern != null)
			typePattern.clean();
	}
	
	public boolean filled() {
		return freeVariable.hasValue() && (typePattern == null || typePattern.filled());
	}
	
	public boolean hasVariableId(int id) {
		return freeVariable.isId(id) || (typePattern == null || typePattern.hasVariableId(id));
	}
	
	public boolean matches(FieldInstance instance) {
		return filled() && 
			   (visibility == null || sameVisibility(instance)) && 
			   sameName(instance) &&
			   (typePattern == null || sameType(instance));
	}
	
	private boolean sameType(FieldInstance instance) {
		return typePattern.matches(instance.getType());
	}

	private boolean sameVisibility(FieldInstance instance) {
		return instance.getVisibility() == visibility;
	}
	
	private boolean sameName(FieldInstance instance) {
		return instance.getQualifiedName().equals(freeVariable.getValue());
	}
	
	public String toStringDegub() {
		return (visibility == null?"*":visibility.toString().toLowerCase()) + " #" + getVariableId();
	}
	
	public String toStringFilled() {
		return (visibility == null?"*":visibility.toString().toLowerCase()) + " #" + freeVariable.getValue();
	}
}
