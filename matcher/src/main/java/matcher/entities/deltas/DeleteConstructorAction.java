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
}
