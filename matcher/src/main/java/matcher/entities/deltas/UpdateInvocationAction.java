package matcher.entities.deltas;

import matcher.entities.ConstructorInstance;
import matcher.entities.MethodInstance;

public class UpdateInvocationAction extends UpdateAction {
	
	private MethodInstance previousHolderMethod;
	
	private ConstructorInstance previousHolderConstructor;
	
	private MethodInstance newHolderMethod;
	
	private ConstructorInstance newHolderConstructor;
	
	private MethodInstance oldDependency;
	
	private MethodInstance newDependency;

	public UpdateInvocationAction(ConstructorInstance previousHolderConstructor,
			ConstructorInstance newHolderConstructor, MethodInstance oldDependency, MethodInstance newDependency) {
		super();
		this.previousHolderConstructor = previousHolderConstructor;
		this.newHolderConstructor = newHolderConstructor;
		this.oldDependency = oldDependency;
		this.newDependency = newDependency;
	}

	public UpdateInvocationAction(MethodInstance previousHolderMethod, MethodInstance newHolderMethod,
			MethodInstance oldDependency, MethodInstance newDependency) {
		super();
		this.previousHolderMethod = previousHolderMethod;
		this.newHolderMethod = newHolderMethod;
		this.oldDependency = oldDependency;
		this.newDependency = newDependency;
	}

	public MethodInstance getOldDependency() {
		return oldDependency;
	}

	public MethodInstance getNewDependency() {
		return newDependency;
	}

	public MethodInstance getPreviousHolderMethod() {
		return previousHolderMethod;
	}

	public ConstructorInstance getPreviousHolderConstructor() {
		return previousHolderConstructor;
	}

	public MethodInstance getNewHolderMethod() {
		return newHolderMethod;
	}

	public ConstructorInstance getNewHolderConstructor() {
		return newHolderConstructor;
	}

	@Override
	public String getUpdatedEntityQualifiedName() {
		return newDependency.getQualifiedName();
	}

	public ConstructorInstance getHolderConstructor() {
		return previousHolderConstructor;
	}

	public MethodInstance getHolderMethod() {
		return previousHolderMethod;
	}
	
	public boolean updatedInMethod() {
		return previousHolderMethod != null;
	}
	
	public boolean updatedInConstructor() {
		return previousHolderConstructor != null;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("update invocation ");
		result.append(oldDependency.getQualifiedName() + " in ");
		if(updatedInConstructor())
			result.append(previousHolderConstructor.getQualifiedName() + " to ");
		if(updatedInMethod())
			result.append(previousHolderMethod.getQualifiedName() + " to ");
		result.append(newDependency.getQualifiedName());
		return result.toString();
	}
	
}
