package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteInvocationAction;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.MethodInvocationPattern;
import matcher.patterns.MethodPattern;

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

	public int getDeletedInvocationVariableId() {
		return deletedInvocationPattern.getVariableId();
	}
	
	public boolean insertedInMethod() {
		return holderMethodPattern != null;
	}

	public boolean insertedInConstructor() {
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
		if(insertedInMethod() && action.insertedInMethod())
			return holderMethodPattern.matches(action.getHolderMethod());
		else if(insertedInConstructor() && action.insertedInConstructor())
			return holderConstructorPattern.matches(action.getHolderConstructor());
		return false;
	}

	@Override
	public void setVariableValue(int id, String value) {
		deletedInvocationPattern.setVariableValue(id, value);
		if(insertedInMethod())
			holderMethodPattern.setVariableValue(id, value);
		else
			holderConstructorPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return deletedInvocationPattern.filled() && holderFilled();
	}
	
	private boolean holderFilled() {
		if(insertedInMethod())
			return holderMethodPattern.filled();
		else
			return holderConstructorPattern.filled();
	}

	@Override
	public void clean() {
		deletedInvocationPattern.clean();
		if(insertedInMethod())
			holderMethodPattern.clean();
		else
			holderConstructorPattern.clean();
	}
	
	@Override
	public String toStringDebug() {
		return null;
	}

	@Override
	public String toStringFilled() {
		// TODO Auto-generated method stub
		return null;
	}

}
