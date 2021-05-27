package matcher.entities.deltas;

import matcher.entities.ClassInstance;
import matcher.entities.MethodInstance;

public class UpdateMethodAction extends UpdateAction{
	
	private MethodInstance updatedMethodInstance;
	
	private ClassInstance classInstance;

	public UpdateMethodAction(MethodInstance updatedMethodInstance,
			 ClassInstance classInstance) {
		super();
		this.updatedMethodInstance = updatedMethodInstance;
		this.classInstance = classInstance;
	}

	public MethodInstance getUpdatedMethodInstance() {
		return updatedMethodInstance;
	}
	
	public ClassInstance getClassInstance() {
		return classInstance;
	}
	
	@Override
	public String getUpdatedEntityQualifiedName() {
		return updatedMethodInstance.getQualifiedName();
	}

	@Override
	public String toString() {
		return "update method " + updatedMethodInstance.getQualifiedName()
			+ " of class " + classInstance.getQualifiedName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classInstance == null) ? 0 : classInstance.hashCode());
		result = prime * result + ((updatedMethodInstance == null) ? 0 : updatedMethodInstance.hashCode());
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
		UpdateMethodAction other = (UpdateMethodAction) obj;
		if (classInstance == null) {
			if (other.classInstance != null)
				return false;
		} else if (!classInstance.equals(other.classInstance))
			return false;
		if (updatedMethodInstance == null) {
			if (other.updatedMethodInstance != null)
				return false;
		} else if (!updatedMethodInstance.equals(other.updatedMethodInstance))
			return false;
		return true;
	}
	
	

}
