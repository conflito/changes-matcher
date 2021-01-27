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
}
