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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((holderClass == null) ? 0 : holderClass.hashCode());
		result = prime * result + ((insertedField == null) ? 0 : insertedField.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InsertFieldAction))
			return false;
		InsertFieldAction other = (InsertFieldAction) obj;
		if (holderClass == null) {
			if (other.holderClass != null)
				return false;
		} else if (!holderClass.equals(other.holderClass))
			return false;
		if (insertedField == null) {
			if (other.insertedField != null)
				return false;
		} else if (!insertedField.equals(other.insertedField))
			return false;
		return true;
	}

}
