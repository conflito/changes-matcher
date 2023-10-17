package matcher.entities.deltas;


import matcher.entities.ClassInstance;
import matcher.entities.MethodInstance;

public class DeleteMethodAction extends DeleteAction {

	private MethodInstance deletedMethod;
	
	private ClassInstance holderClass;
	
	public DeleteMethodAction(MethodInstance insertedMethod, ClassInstance holderClass) {
		super();
		this.deletedMethod = insertedMethod;
		this.holderClass = holderClass;
	}

	public MethodInstance getDeletedMethod() {
		return deletedMethod;
	}

	public ClassInstance getHolderClass() {
		return holderClass;
	}
	
	public String getDeletedMethodQualifiedName() {
		return deletedMethod.getQualifiedName();
	}

	public String toString() {
		StringBuilder result = new StringBuilder("delete " + 
				deletedMethod.getVisibility().toString().toLowerCase() + " method ");
		result.append(deletedMethod.getQualifiedName()+ " in ");
		result.append(deletedMethod.getQualifiedName());
		return result.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deletedMethod == null) ? 0 : deletedMethod.hashCode());
		result = prime * result + ((holderClass == null) ? 0 : holderClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DeleteMethodAction))
			return false;
		DeleteMethodAction other = (DeleteMethodAction) obj;
		if (deletedMethod == null) {
			if (other.deletedMethod != null)
				return false;
		} else if (!deletedMethod.equals(other.deletedMethod))
			return false;
		if (holderClass == null) {
			if (other.holderClass != null)
				return false;
		} else if (!holderClass.equals(other.holderClass))
			return false;
		return true;
	}
}
