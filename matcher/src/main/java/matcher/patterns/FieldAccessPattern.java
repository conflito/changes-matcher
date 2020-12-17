package matcher.patterns;

import matcher.entities.FieldAccessInstance;
import matcher.entities.FieldAccessType;

public class FieldAccessPattern {

	private FreeVariable freeVariable;
	
	private FieldAccessType accessType;

	public FieldAccessPattern(FreeVariable freeVariable, FieldAccessType accessType) {
		super();
		this.freeVariable = freeVariable;
		this.accessType = accessType;
	}

	public FreeVariable getFreeVariable() {
		return freeVariable;
	}

	public FieldAccessType getType() {
		return accessType;
	}
	
	public int getVariableId() {
		return freeVariable.getId();
	}
	
	public void setVariableValue(String value) {
		freeVariable.setValue(value);
	}
	
	public boolean isFieldRead() {
		return accessType == FieldAccessType.READ;
	}
	
	public boolean isFieldWrite() {
		return accessType == FieldAccessType.WRITE;
	}
	
	public boolean isAnyAccess() {
		return !isFieldRead() && !isFieldWrite();
	}
	
	public boolean filled() {
		return freeVariable.hasValue();
	}
	
	public boolean matches(FieldAccessInstance accessInstance) {
		return filled() && (isAnyAccess() || sameAccess(accessInstance)) && sameName(accessInstance);
	}

	private boolean sameName(FieldAccessInstance accessInstance) {
		return accessInstance.getQualifiedName().equals(freeVariable.getValue());
	}

	private boolean sameAccess(FieldAccessInstance accessInstance) {
		return (isFieldRead() && accessInstance.isFieldRead()) ||
				(isFieldWrite() && accessInstance.isFieldWrite());
	}
}
