package matcher.entities.deltas;

import matcher.entities.ConstructorInstance;
import matcher.entities.MethodInstance;
import matcher.entities.MethodInvocationInstance;

public class DeleteInvocationAction extends DeleteAction{

	private MethodInvocationInstance deletedInvocation;
	
	private ConstructorInstance holderConstructor;
	
	private MethodInstance holderMethod;

	public DeleteInvocationAction(MethodInvocationInstance deletedInvocation, 
			ConstructorInstance holderConstructor) {
		super();
		this.deletedInvocation = deletedInvocation;
		this.holderConstructor = holderConstructor;
	}

	public DeleteInvocationAction(MethodInvocationInstance deletedInvocation, 
			MethodInstance holderMethod) {
		super();
		this.deletedInvocation = deletedInvocation;
		this.holderMethod = holderMethod;
	}

	public MethodInvocationInstance getDeletedInvocation() {
		return deletedInvocation;
	}

	public ConstructorInstance getHolderConstructor() {
		return holderConstructor;
	}

	public MethodInstance getHolderMethod() {
		return holderMethod;
	}
	
	public String getDeletedInvocationQualifiedName() {
		return deletedInvocation.getQualifiedName();
	}
	
	public boolean insertedInMethod() {
		return holderMethod != null;
	}
	
	public boolean insertedInConstructor() {
		return holderConstructor != null;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("insert ");
		result.append("method invocation of ");
		result.append(deletedInvocation.getQualifiedName() + " in ");
		if(insertedInMethod())
			result.append(holderMethod.getQualifiedName());
		if(insertedInConstructor())
			result.append(holderConstructor.getQualifiedName());
		return result.toString();
	}

}
