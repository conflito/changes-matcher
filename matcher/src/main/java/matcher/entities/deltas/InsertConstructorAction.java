package matcher.entities.deltas;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;

public class InsertConstructorAction extends InsertAction {
	
	private ConstructorInstance insertedConstructor;
	
	private ClassInstance holderClass;
	
	public InsertConstructorAction(ConstructorInstance insertedConstructor, ClassInstance holderClass) {
		super();
		this.insertedConstructor = insertedConstructor;
		this.holderClass = holderClass;
	}
	
	public ConstructorInstance getInsertedConstructor() {
		return insertedConstructor;
	}
	
	public String getInsertedConstructorQualifiedName() {
		return insertedConstructor.getQualifiedName();
	}

	public ClassInstance getHolderClass() {
		return holderClass;
	}

	public String toString() {
		StringBuilder result = new StringBuilder("insert " + 
				insertedConstructor.getVisibility().toString().toLowerCase() + " constructor ");
		result.append(insertedConstructor.getQualifiedName() + " in ");
		result.append(holderClass.getQualifiedName());
		return result.toString();
	}

}
