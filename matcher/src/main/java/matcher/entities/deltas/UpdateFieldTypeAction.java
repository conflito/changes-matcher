package matcher.entities.deltas;

import matcher.entities.Type;

public class UpdateFieldTypeAction extends UpdateAction {

	private Type oldType;
	private Type newType;
	
	public UpdateFieldTypeAction(Updatable entity, Type oldType, Type newType) {
		super(entity);
		this.oldType = oldType;
		this.newType = newType;
	}
	
	public String getOldTypeName() {
		return oldType.toString();
	}
	
	public String getNewTypeName() {
		return newType.toString();
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("update field ");
		result.append(getEntityQualifiedName() + " type from ");
		result.append(getOldTypeName() + " to " + getNewTypeName());
		return result.toString();
	}
}
