package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.ConstructorInstance;
import org.conflito.matcher.entities.MethodInstance;
import org.conflito.matcher.entities.MethodInvocationInstance;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((holderConstructor == null) ? 0 : holderConstructor.hashCode());
		result = prime * result + ((holderMethod == null) ? 0 : holderMethod.hashCode());
		result = prime * result + ((insertedInvocation == null) ? 0 : insertedInvocation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InsertInvocationAction))
			return false;
		InsertInvocationAction other = (InsertInvocationAction) obj;
		if (holderConstructor == null) {
			if (other.holderConstructor != null)
				return false;
		} else if (!holderConstructor.equals(other.holderConstructor))
			return false;
		if (holderMethod == null) {
			if (other.holderMethod != null)
				return false;
		} else if (!holderMethod.equals(other.holderMethod))
			return false;
		if (insertedInvocation == null) {
			if (other.insertedInvocation != null)
				return false;
		} else if (!insertedInvocation.equals(other.insertedInvocation))
			return false;
		return true;
	}

}
