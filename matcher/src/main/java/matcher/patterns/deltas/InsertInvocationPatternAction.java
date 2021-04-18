package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertInvocationAction;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.MethodInvocationPattern;
import matcher.patterns.MethodPattern;

public class InsertInvocationPatternAction extends InsertPatternAction {
	
	private MethodInvocationPattern insertedInvocationPattern;
	
	private ConstructorPattern holderConstructorPattern;
	
	private MethodPattern holderMethodPattern;
	
	public InsertInvocationPatternAction(MethodInvocationPattern insertedInvocationPattern,
			ConstructorPattern holderConstructorPattern) {
		super();
		this.insertedInvocationPattern = insertedInvocationPattern;
		this.holderConstructorPattern = holderConstructorPattern;
	}

	public InsertInvocationPatternAction(MethodInvocationPattern insertedInvocationPattern,
			MethodPattern holderMethodPattern) {
		super();
		this.insertedInvocationPattern = insertedInvocationPattern;
		this.holderMethodPattern = holderMethodPattern;
	}
	
	public ActionPattern makeCopy() {
		MethodInvocationPattern invocationCopy = new MethodInvocationPattern(insertedInvocationPattern);
		if(insertedInMethod()) {
			MethodPattern methodCopy = new MethodPattern(holderMethodPattern);
			return new InsertInvocationPatternAction(invocationCopy, methodCopy);
		}
		else {
			ConstructorPattern constructorCopy = new ConstructorPattern(holderConstructorPattern);
			return new InsertInvocationPatternAction(invocationCopy, constructorCopy);
		}
	}
	
	public int getInsertedInvocationVariableId() {
		return insertedInvocationPattern.getVariableId();
	}
	
	public boolean insertedInMethod() {
		return holderMethodPattern != null;
	}

	public boolean insertedInConstructor() {
		return holderConstructorPattern != null;
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertInvocationAction && filled() 
				&& matches((InsertInvocationAction)action);
	}
	
	private boolean matches(InsertInvocationAction action) {
		return getAction() == action.getAction() &&
			   insertedInvocationPattern.matches(action.getInsertedInvocation()) &&
			   matchesHolder(action);
	}

	private boolean matchesHolder(InsertInvocationAction action) {
		if(insertedInMethod() && action.insertedInMethod())
			return holderMethodPattern.matches(action.getHolderMethod());
		else if(insertedInConstructor() && action.insertedInConstructor())
			return holderConstructorPattern.matches(action.getHolderConstructor());
		return false;
	}

	@Override
	public void setVariableValue(int id, String value) {
		insertedInvocationPattern.setVariableValue(id, value);
		if(insertedInMethod())
			holderMethodPattern.setVariableValue(id, value);
		else
			holderConstructorPattern.setVariableValue(id, value);
		
	}

	@Override
	public boolean filled() {
		return insertedInvocationPattern.filled() &&
			   holderFilled();
	}

	private boolean holderFilled() {
		if(insertedInMethod())
			return holderMethodPattern.filled();
		else
			return holderConstructorPattern.filled();
	}
	
	@Override
	public void clean() {
		insertedInvocationPattern.clean();
		if(insertedInMethod())
			holderMethodPattern.clean();
		else
			holderConstructorPattern.clean();
		
	}

	@Override
	public String toStringDebug() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toStringFilled() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Insert dependency to method $" + 
				insertedInvocationPattern.getVariableId());
		if(insertedInMethod())
			result.append(" in method $" + holderMethodPattern.getVariableId());
		else
			result.append(" in constructor $" + holderConstructorPattern.getVariableId());
		
		return result.toString();
	}

}
