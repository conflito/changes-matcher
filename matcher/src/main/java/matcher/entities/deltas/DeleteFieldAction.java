package matcher.entities.deltas;

import matcher.entities.ClassInstance;
import matcher.entities.FieldInstance;

public class DeleteFieldAction extends DeleteAction {
	
	private FieldInstance deletedField;
	
	private ClassInstance holderClass;
	
	public DeleteFieldAction(FieldInstance deletedField, ClassInstance holderClass) {
		super();
		this.deletedField = deletedField;
		this.holderClass = holderClass;
	}

	public FieldInstance getDeletedField() {
		return deletedField;
	}

	public ClassInstance getHolderClass() {
		return holderClass;
	}

	public String getDeletedFieldQualifiedName() {
		return deletedField.getQualifiedName();
	}

	public String toString() {
		StringBuilder result = new StringBuilder("delete " + 
				deletedField.getVisibility().toString().toLowerCase() + " field ");
		result.append(deletedField.getQualifiedName() + " in ");
		result.append(holderClass.getQualifiedName());
		return result.toString();
	}
	
}
