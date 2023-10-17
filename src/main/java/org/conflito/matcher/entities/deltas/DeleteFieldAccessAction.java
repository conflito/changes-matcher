package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.FieldAccessInstance;
import org.conflito.matcher.entities.MethodInstance;

public class DeleteFieldAccessAction extends DeleteAction{

	private FieldAccessInstance deletedFieldAccess;
	
	private MethodInstance holderMethod;
	
	public DeleteFieldAccessAction(FieldAccessInstance deletedFieldAccess, 
			MethodInstance holderMethod) {
		super();
		this.deletedFieldAccess = deletedFieldAccess;
		this.holderMethod = holderMethod;
	}

	public FieldAccessInstance getDeletedFieldAccess() {
		return deletedFieldAccess;
	}

	public MethodInstance getHolderMethod() {
		return holderMethod;
	}

	public String getDeletedFieldAccessQualifiedName() {
		return deletedFieldAccess.getQualifiedName();
	}

	public String toString() {
		StringBuilder result = new StringBuilder("delete " + 
				deletedFieldAccess.getAccessType().toString().toLowerCase() + " of field ");
		result.append(deletedFieldAccess.getQualifiedName() + " in ");
		result.append(holderMethod.getQualifiedName());
		return result.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deletedFieldAccess == null) ? 0 : deletedFieldAccess.hashCode());
		result = prime * result + ((holderMethod == null) ? 0 : holderMethod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DeleteFieldAccessAction))
			return false;
		DeleteFieldAccessAction other = (DeleteFieldAccessAction) obj;
		if (deletedFieldAccess == null) {
			if (other.deletedFieldAccess != null)
				return false;
		} else if (!deletedFieldAccess.equals(other.deletedFieldAccess))
			return false;
		if (holderMethod == null) {
			if (other.holderMethod != null)
				return false;
		} else if (!holderMethod.equals(other.holderMethod))
			return false;
		return true;
	}
}
