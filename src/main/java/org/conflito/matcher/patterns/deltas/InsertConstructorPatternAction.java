package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.InsertConstructorAction;
import org.conflito.matcher.patterns.ClassPattern;
import org.conflito.matcher.patterns.ConstructorPattern;

public class InsertConstructorPatternAction extends InsertPatternAction {

	private ConstructorPattern insertedConstructorPattern;
	
	private ClassPattern holderClassPattern;
	
	public InsertConstructorPatternAction(ConstructorPattern insertedConstructorPattern,
			ClassPattern holderClassPattern) {
		super();
		this.insertedConstructorPattern = insertedConstructorPattern;
		this.holderClassPattern = holderClassPattern;
	}
	
	public ActionPattern makeCopy() {
		ConstructorPattern constructorCopy = new ConstructorPattern(insertedConstructorPattern);
		ClassPattern classCopy = new ClassPattern(holderClassPattern);
		return new InsertConstructorPatternAction(constructorCopy, classCopy);
	}
	
	public int getInsertedConstructorVariableId() {
		return insertedConstructorPattern.getVariableId();
	}
	
	public boolean hasInvocations() {
		return insertedConstructorPattern.hasInvocations();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertConstructorAction && filled()
				&& matches((InsertConstructorAction)action);
	}
	
	private boolean matches(InsertConstructorAction action) {
		return getAction() == action.getAction() &&
			   insertedConstructorPattern.matches(action.getInsertedConstructor()) &&
			   holderClassPattern.matches(action.getHolderClass());
	}

	@Override
	public void setVariableValue(int id, String value) {
		insertedConstructorPattern.setVariableValue(id, value);
		holderClassPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return insertedConstructorPattern.filled() && holderClassPattern.filled();
	}
	
	@Override
	public void clean() {
		insertedConstructorPattern.clean();
		holderClassPattern.clean();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Insert constructor $" + insertedConstructorPattern.getVariableId());
		result.append(" in class $" + holderClassPattern.getVariableId() + "\n");
		result.append(insertedConstructorPattern.toString());
		return result.toString();
	}
	
	
}
