package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.ConstructorInstance;

public class UpdateConstructorAction extends UpdateAction{
	
	private ConstructorInstance updatedConstructorInstance;

	public UpdateConstructorAction(ConstructorInstance updatedConstructorInstance) {
		super();
		this.updatedConstructorInstance = updatedConstructorInstance;
	}

	public ConstructorInstance getUpdatedConstructorInstance() {
		return updatedConstructorInstance;
	}
	
	@Override
	public String getUpdatedEntityQualifiedName() {
		return updatedConstructorInstance.getQualifiedName();
	}

	@Override
	public String toString() {
		return "update method " + updatedConstructorInstance.getQualifiedName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((updatedConstructorInstance == null) ? 0 : updatedConstructorInstance.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UpdateConstructorAction))
			return false;
		UpdateConstructorAction other = (UpdateConstructorAction) obj;
		if (updatedConstructorInstance == null) {
			if (other.updatedConstructorInstance != null)
				return false;
		} else if (!updatedConstructorInstance.equals(other.updatedConstructorInstance))
			return false;
		return true;
	}

}
