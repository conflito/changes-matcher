package matcher.patterns;

import matcher.entities.FieldInstance;
import matcher.entities.Visibility;

public class FieldPattern {

	private FreeVariable freeVariable;
	
	private Visibility visibility;
	
	private FreeVariable typeVar;

	public FieldPattern(FreeVariable freeVar, Visibility visibility) {
		super();
		this.freeVariable = freeVar;
		this.visibility = visibility;
		this.typeVar = null;
	}
	
	public FreeVariable getFreeVariable() {
		return freeVariable;
	}
	
	public int getVariableId() {
		return freeVariable.getId();
	}
	
	public void setType(FreeVariable type) {
		this.typeVar = type;
	}
	
	public boolean hasType() {
		return typeVar != null;
	}
	
	/**
	 * @requires hasType()
	 */
	public int getTypeVariableId() {
		return typeVar.getId();
	}
	
	public void setVariableValue(int id, String value) {
		if(freeVariable.isId(id))
			freeVariable.setValue(value);
		if(typeVar != null && typeVar.isId(id))
			typeVar.setValue(value);
	}
	
	public void clean() {
		freeVariable.clean();
	}
	
	public boolean filled() {
		return freeVariable.hasValue() && (typeVar == null || typeVar.hasValue());
	}
	
	public boolean hasVariableId(int id) {
		return freeVariable.isId(id) || (typeVar == null || typeVar.isId(id));
	}
	
	public boolean matches(FieldInstance instance) {
		return filled() && 
			   (visibility == null || sameVisibility(instance)) && 
			   sameName(instance) &&
			   (typeVar == null || sameType(instance));
	}
	
	private boolean sameType(FieldInstance instance) {
		return instance.getType().toString().equals(typeVar.getValue());
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
