package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.UpdateDependencyAction;
import org.conflito.matcher.patterns.ConstructorPattern;
import org.conflito.matcher.patterns.FreeVariable;
import org.conflito.matcher.patterns.MethodPattern;

public class UpdateDependencyPatternAction extends UpdatePatternAction {
	
	private MethodPattern holderMethodPattern;
	
	private ConstructorPattern holderConstructorPattern;
	
	private FreeVariable oldDependency;
	
	private FreeVariable newDependency;

	public UpdateDependencyPatternAction(MethodPattern holderMethodPattern, FreeVariable oldDependency,
			FreeVariable newDependency) {
		super();
		this.holderMethodPattern = holderMethodPattern;
		this.oldDependency = oldDependency;
		this.newDependency = newDependency;
	}

	public UpdateDependencyPatternAction(ConstructorPattern holderConstructorPattern, FreeVariable oldDependency,
			FreeVariable newDependency) {
		super();
		this.holderConstructorPattern = holderConstructorPattern;
		this.oldDependency = oldDependency;
		this.newDependency = newDependency;
	}
	
	public ActionPattern makeCopy() {
		FreeVariable oldCopy = new FreeVariable(oldDependency);
		FreeVariable newCopy = new FreeVariable(newDependency);
		if(updatedInMethod()) {
			MethodPattern methodCopy = new MethodPattern(holderMethodPattern);
			return new UpdateDependencyPatternAction(methodCopy, oldCopy, newCopy);
		}
		else {
			ConstructorPattern constructorCopy = new ConstructorPattern(holderConstructorPattern);
			return new UpdateDependencyPatternAction(constructorCopy, oldCopy, newCopy);
		}
	}

	public int getNewInvocationVariableId() {
		return newDependency.getId();
	}

	public boolean updatedInMethod() {
		return holderMethodPattern != null;
	}
	
	public boolean updatedInConstructor() {
		return holderConstructorPattern != null;
	}
	
	@Override
	public boolean filled() {
		return oldDependency.hasValue() &&
				newDependency.hasValue() &&
				holderFilled();
	}
	
	private boolean holderFilled() {
		if(updatedInMethod())
			return holderMethodPattern.filled();
		else
			return holderConstructorPattern.filled();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof UpdateDependencyAction && filled() &&
				matches((UpdateDependencyAction)action);
	}
	
	private boolean matches(UpdateDependencyAction action) {
		return getAction() == action.getAction() &&
			   dependenciesMatch(action) &&
			   holderMatches(action);
	}
	
	private boolean dependenciesMatch(UpdateDependencyAction action) {
		if(updatedInMethod() && action.updatedInMethod())
			return action.getPreviousHolderMethod().dependsOn(oldDependency.getValue()) &&
				action.getNewHolderMethod().dependsOn(newDependency.getValue());
		else if(updatedInConstructor() && action.updatedInMethod())
			return action.getPreviousHolderConstructor().dependsOn(oldDependency.getValue()) &&
					action.getNewHolderConstructor().dependsOn(newDependency.getValue());
		return false;
	}

	private boolean holderMatches(UpdateDependencyAction action) {
		if(updatedInConstructor() && action.updatedInMethod())
			return holderConstructorPattern.matches(action.getHolderConstructor());
		else if(updatedInMethod() && action.updatedInMethod())
			return holderMethodPattern.matches(action.getHolderMethod());
		return false;
	}

	@Override
	public void setVariableValue(int id, String value) {
		if(oldDependency.isId(id))
			oldDependency.setValue(value);
		if(newDependency.isId(id))
			newDependency.setValue(value);
		if(updatedInConstructor())
			holderConstructorPattern.setVariableValue(id, value);
		else
			holderMethodPattern.setVariableValue(id, value);
	}

	@Override
	public void clean() {
		oldDependency.clean();
		newDependency.clean();
		if(updatedInConstructor())
			holderConstructorPattern.clean();
		else
			holderMethodPattern.clean();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Update dependency from method $" + oldDependency.getId());
		result.append(" to method $" + newDependency.getId());
		if(updatedInConstructor())
			result.append(" in constructor $" + holderConstructorPattern.getVariableId());
		else
			result.append(" in method $" + holderMethodPattern.getVariableId());
		return result.toString();
	}
}
