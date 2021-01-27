package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.UpdateInvocationAction;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodPattern;

public class UpdateInvocationPatternAction extends UpdatePatternAction {
	
	private MethodPattern holderMethodPattern;
	
	private ConstructorPattern holderConstructorPattern;
	
	private FreeVariable oldDependency;
	
	private FreeVariable newDependency;

	public UpdateInvocationPatternAction(MethodPattern holderMethodPattern, FreeVariable oldDependency,
			FreeVariable newDependency) {
		super();
		this.holderMethodPattern = holderMethodPattern;
		this.oldDependency = oldDependency;
		this.newDependency = newDependency;
	}

	public UpdateInvocationPatternAction(ConstructorPattern holderConstructorPattern, FreeVariable oldDependency,
			FreeVariable newDependency) {
		super();
		this.holderConstructorPattern = holderConstructorPattern;
		this.oldDependency = oldDependency;
		this.newDependency = newDependency;
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
		return action instanceof UpdateInvocationAction && filled() && 
				matches((UpdateInvocationAction)action);
	}
	
	private boolean matches(UpdateInvocationAction action) {
		return getAction() == action.getAction() &&
			   dependenciesMatch(action) &&
			   holderMatches(action);
	}
	
	private boolean dependenciesMatch(UpdateInvocationAction action) {
		if(updatedInMethod() && action.updatedInMethod())
			return action.getPreviousHolderMethod().dependsOn(oldDependency.getValue()) &&
				action.getNewHolderMethod().dependsOn(newDependency.getValue());
		else if(updatedInConstructor() && action.updatedInMethod())
			return action.getPreviousHolderConstructor().dependsOn(oldDependency.getValue()) &&
					action.getNewHolderConstructor().dependsOn(newDependency.getValue());
		return false;
	}

	private boolean holderMatches(UpdateInvocationAction action) {
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
	public String toStringDebug() {
		return null;
	}
	
	@Override
	public String toStringFilled() {
		return null;
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
}
