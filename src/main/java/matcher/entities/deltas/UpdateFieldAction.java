package matcher.entities.deltas;

import matcher.entities.ClassInstance;
import matcher.entities.FieldInstance;

public class UpdateFieldAction extends UpdateAction{

	private FieldInstance updatedFieldInstance;
	
	private ClassInstance classInstance;
	
	public UpdateFieldAction(FieldInstance updatedFieldInstance,
			ClassInstance classInstance) {
		super();
		this.updatedFieldInstance = updatedFieldInstance;
		this.classInstance = classInstance;
	}

	public FieldInstance getUpdatedFieldInstance() {
		return updatedFieldInstance;
	}

	public ClassInstance getClassInstance() {
		return classInstance;
	}

	@Override
	public String getUpdatedEntityQualifiedName() {
		return updatedFieldInstance.getQualifiedName();
	}
	
	@Override
	public String toString() {
		return "update field " + updatedFieldInstance.getQualifiedName()
		+ " of class " + classInstance.getQualifiedName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classInstance == null) ? 0 : classInstance.hashCode());
		result = prime * result + ((updatedFieldInstance == null) ? 0 : updatedFieldInstance.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UpdateFieldAction other = (UpdateFieldAction) obj;
		if (classInstance == null) {
			if (other.classInstance != null)
				return false;
		} else if (!classInstance.equals(other.classInstance))
			return false;
		if (updatedFieldInstance == null) {
			if (other.updatedFieldInstance != null)
				return false;
		} else if (!updatedFieldInstance.equals(other.updatedFieldInstance))
			return false;
		return true;
	}

}
