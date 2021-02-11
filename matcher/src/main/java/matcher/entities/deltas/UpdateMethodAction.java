package matcher.entities.deltas;

import matcher.entities.MethodInstance;

public class UpdateMethodAction extends UpdateAction{
	
	private MethodInstance updatedMethodInstance;

	public UpdateMethodAction(MethodInstance updatedMethodInstance) {
		super();
		this.updatedMethodInstance = updatedMethodInstance;
	}

	public MethodInstance getUpdatedMethodInstance() {
		return updatedMethodInstance;
	}
	
	@Override
	public String getUpdatedEntityQualifiedName() {
		return updatedMethodInstance.getQualifiedName();
	}

	@Override
	public String toString() {
		return "update method " + updatedMethodInstance.getQualifiedName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((updatedMethodInstance == null) ? 0 : updatedMethodInstance.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UpdateMethodAction))
			return false;
		UpdateMethodAction other = (UpdateMethodAction) obj;
		if (updatedMethodInstance == null) {
			if (other.updatedMethodInstance != null)
				return false;
		} else if (!updatedMethodInstance.equals(other.updatedMethodInstance))
			return false;
		return true;
	}

}
