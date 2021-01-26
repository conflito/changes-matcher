package matcher.entities.deltas;

import matcher.entities.ConstructorInstance;
import matcher.entities.MethodInstance;
import matcher.entities.MethodInvocationInstance;

public class InsertInvocationAction extends InsertAction {

	private MethodInvocationInstance insertedInvocation;
	
	private ConstructorInstance holderConstructor;
	
	private MethodInstance holderMethod;
	
	public InsertInvocationAction(MethodInvocationInstance insertedInvocation, 
			ConstructorInstance holderConstructor) {
		super();
		this.insertedInvocation = insertedInvocation;
		this.holderConstructor = holderConstructor;
	}

	public InsertInvocationAction(MethodInvocationInstance insertedInvocation, 
			MethodInstance holderMethod) {
		super();
		this.insertedInvocation = insertedInvocation;
		this.holderMethod = holderMethod;
	}
	public MethodInvocationInstance getInsertedInvocation() {
		return insertedInvocation;
	}
	
	public String getInsertedInvocationQualifiedName() {
		return insertedInvocation.getQualifiedName();
	}

	public ConstructorInstance getHolderConstructor() {
		return holderConstructor;
	}

	public MethodInstance getHolderMethod() {
		return holderMethod;
	}
	
	public boolean insertedInMethod() {
		return holderMethod != null;
	}
	
	public boolean insertedInConstructor() {
		return holderConstructor != null;
	}

	public String toString() {
		StringBuilder result = new StringBuilder("insert ");
		result.append("method invocation of ");
		result.append(insertedInvocation.getQualifiedName() + " in ");
		if(insertedInMethod())
			result.append(holderMethod.getQualifiedName());
		if(insertedInConstructor())
			result.append(holderConstructor.getQualifiedName());
		return result.toString();
	}

}
