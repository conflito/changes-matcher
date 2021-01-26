package matcher.entities.deltas;

import matcher.entities.ClassInstance;
import matcher.entities.FieldInstance;

public class InsertFieldAction extends InsertAction {
	
	private FieldInstance insertedField;
	
	private ClassInstance holderClass;
	
	public InsertFieldAction(FieldInstance insertedField, ClassInstance holderClass) {
		super();
		this.insertedField = insertedField;
		this.holderClass = holderClass;
	}
	
	public FieldInstance getInsertedField() {
		return insertedField;
	}

	public ClassInstance getHolderClass() {
		return holderClass;
	}
	
	public String getInsertedFieldQualifiedName() {
		return insertedField.getQualifiedName();
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("insert " + 
				insertedField.getVisibility().toString().toLowerCase() + " field ");
		result.append(insertedField.getQualifiedName() + " in ");
		result.append(holderClass.getQualifiedName());
		return result.toString();
	}

}
