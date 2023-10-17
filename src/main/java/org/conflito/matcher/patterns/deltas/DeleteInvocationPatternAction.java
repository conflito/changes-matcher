package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.DeleteInvocationAction;
import org.conflito.matcher.patterns.ConstructorPattern;
import org.conflito.matcher.patterns.MethodInvocationPattern;
import org.conflito.matcher.patterns.MethodPattern;

public class DeleteInvocationPatternAction extends DeletePatternAction {

	private MethodInvocationPattern deletedInvocationPattern;
	
	private ConstructorPattern holderConstructorPattern;
	
	private MethodPattern holderMethodPattern;
	
	public DeleteInvocationPatternAction(MethodInvocationPattern deletedInvocationPattern,
			ConstructorPattern holderConstructorPattern) {
		super();
		this.deletedInvocationPattern = deletedInvocationPattern;
		this.holderConstructorPattern = holderConstructorPattern;
	}

	public DeleteInvocationPatternAction(MethodInvocationPattern deletedInvocationPattern,
			MethodPattern holderMethodPattern) {
		super();
		this.deletedInvocationPattern = deletedInvocationPattern;
		this.holderMethodPattern = holderMethodPattern;
	}
	
	public ActionPattern makeCopy() {
		MethodInvocationPattern invocationCopy = new MethodInvocationPattern(deletedInvocationPattern);
		if(deletedFromMethod()) {
			MethodPattern methodCopy = new MethodPattern(holderMethodPattern);
			return new DeleteInvocationPatternAction(invocationCopy, methodCopy);
		}
		else {
			ConstructorPattern constructorCopy = new ConstructorPattern(holderConstructorPattern);
			return new DeleteInvocationPatternAction(invocationCopy, constructorCopy);
		}
	}

	public int getDeletedInvocationVariableId() {
		return deletedInvocationPattern.getVariableId();
	}
	
	public boolean deletedFromMethod() {
		return holderMethodPattern != null;
	}

	public boolean deletedFromConstructor() {
		return holderConstructorPattern != null;
	}


	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteInvocationAction && filled()
				&& matches((DeleteInvocationAction)action);
	}

	private boolean matches(DeleteInvocationAction action) {
		return getAction() == action.getAction() &&
			   deletedInvocationPattern.matches(action.getDeletedInvocation()) &&
			   matchesHolder(action);
	}

	private boolean matchesHolder(DeleteInvocationAction action) {
		if(deletedFromMethod() && action.insertedInMethod())
			return holderMethodPattern.matches(action.getHolderMethod());
		else if(deletedFromConstructor() && action.insertedInConstructor())
			return holderConstructorPattern.matches(action.getHolderConstructor());
		return false;
	}

	@Override
	public void setVariableValue(int id, String value) {
		deletedInvocationPattern.setVariableValue(id, value);
		if(deletedFromMethod())
			holderMethodPattern.setVariableValue(id, value);
		else
			holderConstructorPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return deletedInvocationPattern.filled() && holderFilled();
	}
	
	private boolean holderFilled() {
		if(deletedFromMethod())
			return holderMethodPattern.filled();
		else
			return holderConstructorPattern.filled();
	}

	@Override
	public void clean() {
		deletedInvocationPattern.clean();
		if(deletedFromMethod())
			holderMethodPattern.clean();
		else
			holderConstructorPattern.clean();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Delete dependency to method $" + 
				deletedInvocationPattern.getVariableId());
		if(deletedFromMethod())
			result.append(" in method $" + holderMethodPattern.getVariableId());
		else
			result.append(" in constructor $" + holderConstructorPattern.getVariableId());
		
		return result.toString();
	}

}
