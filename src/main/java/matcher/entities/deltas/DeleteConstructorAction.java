package matcher.entities.deltas;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;

public class DeleteConstructorAction extends DeleteAction {
	
	private ConstructorInstance deletedConstructor;
	
	private ClassInstance holderClass;
	
	public DeleteConstructorAction(ConstructorInstance deletedConstructor, ClassInstance holderClass) {
		super();
		this.deletedConstructor = deletedConstructor;
		this.holderClass = holderClass;
	}

	public ConstructorInstance getDeletedConstructor() {
		return deletedConstructor;
	}

	public ClassInstance getHolderClass() {
		return holderClass;
	}

	public String getDeletedConstructorQualifiedName() {
		return deletedConstructor.getQualifiedName();
	}

	public String toString() {
		StringBuilder result = new StringBuilder("delete " + 
				deletedConstructor.getVisibility().toString().toLowerCase() + " constructor ");
		result.append(deletedConstructor.getQualifiedName() + " in ");
		result.append(holderClass.getQualifiedName());
		return result.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deletedConstructor == null) ? 0 : deletedConstructor.hashCode());
		result = prime * result + ((holderClass == null) ? 0 : holderClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DeleteConstructorAction))
			return false;
		DeleteConstructorAction other = (DeleteConstructorAction) obj;
		if (deletedConstructor == null) {
			if (other.deletedConstructor != null)
				return false;
		} else if (!deletedConstructor.equals(other.deletedConstructor))
			return false;
		if (holderClass == null) {
			if (other.holderClass != null)
				return false;
		} else if (!holderClass.equals(other.holderClass))
			return false;
		return true;
	}
	
	
}
