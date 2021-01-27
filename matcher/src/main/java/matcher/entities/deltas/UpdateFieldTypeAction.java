package matcher.entities.deltas;

import matcher.entities.FieldInstance;
import matcher.entities.Type;

public class UpdateFieldTypeAction extends UpdateAction {

	private FieldInstance updatedField;
	
	private Type newType;
	
	public UpdateFieldTypeAction(FieldInstance updatedField, Type newType) {
		super();
		this.updatedField = updatedField;
		this.newType = newType;
	}
	
	public FieldInstance getUpdatedField() {
		return updatedField;
	}

	public Type getNewType() {
		return newType;
	}
	
	public String getNewTypeName() {
		return newType.toString();
	}
	
	@Override
	public String getUpdatedEntityQualifiedName() {
		return updatedField.getQualifiedName();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("update field ");
		result.append(updatedField.getQualifiedName() + " type from ");
		result.append(updatedField.getType() + " to " + newType);
		return result.toString();
	}

	
}
