package matcher.entities.deltas;

import matcher.entities.FieldAccessInstance;
import matcher.entities.MethodInstance;

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
}
