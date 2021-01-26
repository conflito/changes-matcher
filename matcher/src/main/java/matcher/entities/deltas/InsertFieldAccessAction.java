package matcher.entities.deltas;

import matcher.entities.FieldAccessInstance;
import matcher.entities.MethodInstance;

public class InsertFieldAccessAction extends InsertAction {

	private FieldAccessInstance insertedFieldAccess;
	
	private MethodInstance holderMethod;

	public InsertFieldAccessAction(FieldAccessInstance insertedFieldAccess, 
			MethodInstance holderMethod) {
		super();
		this.insertedFieldAccess = insertedFieldAccess;
		this.holderMethod = holderMethod;
	}

	public FieldAccessInstance getInsertedFieldAccess() {
		return insertedFieldAccess;
	}
	
	public String getInsertedFieldAccessQualifiedName() {
		return insertedFieldAccess.getQualifiedName();
	}

	public MethodInstance getHolderMethod() {
		return holderMethod;
	}

	public String toString() {
		StringBuilder result = new StringBuilder("insert " + 
				insertedFieldAccess.getAccessType().toString().toLowerCase() + " of field ");
		result.append(insertedFieldAccess.getQualifiedName() + " in ");
		result.append(holderMethod.getQualifiedName());
		return result.toString();
	}

}
