package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.InsertFieldAccessAction;
import org.conflito.matcher.patterns.FieldAccessPattern;
import org.conflito.matcher.patterns.MethodPattern;

public class InsertFieldAccessPatternAction extends InsertPatternAction {

	private FieldAccessPattern insertedFieldAccessPattern;
	
	private MethodPattern holderMethodPattern;
	
	public InsertFieldAccessPatternAction(FieldAccessPattern insertedFieldAccessPattern,
			MethodPattern holderMethodPattern) {
		super();
		this.insertedFieldAccessPattern = insertedFieldAccessPattern;
		this.holderMethodPattern = holderMethodPattern;
	}
	
	public ActionPattern makeCopy() {
		FieldAccessPattern fieldAccessCopy = new FieldAccessPattern(insertedFieldAccessPattern);
		MethodPattern methodCopy = new MethodPattern(holderMethodPattern);
		return new InsertFieldAccessPatternAction(fieldAccessCopy, methodCopy);
	}
	
	public int getInsertedFieldAccessVariableId() {
		return insertedFieldAccessPattern.getVariableId();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertFieldAccessAction && filled()
				&& matches((InsertFieldAccessAction)action);
	}
	
	private boolean matches(InsertFieldAccessAction action) {
		return getAction() == action.getAction() &&
			   insertedFieldAccessPattern.matches(action.getInsertedFieldAccess()) &&
			   holderMethodPattern.matches(action.getHolderMethod());
	}

	@Override
	public void setVariableValue(int id, String value) {
		insertedFieldAccessPattern.setVariableValue(id, value);
		holderMethodPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return insertedFieldAccessPattern.filled() && holderMethodPattern.filled();
	}
	
	@Override
	public void clean() {
		insertedFieldAccessPattern.clean();
		holderMethodPattern.clean();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Insert field ");
		if(insertedFieldAccessPattern.isFieldRead())
			result.append("read ");
		else if(insertedFieldAccessPattern.isFieldWrite())
			result.append("write ");
		else
			result.append("use ");
		result.append("of field $" + insertedFieldAccessPattern.getVariableId());
		result.append(" in method " + holderMethodPattern.getVariableId());
		return result.toString();
	}
}
