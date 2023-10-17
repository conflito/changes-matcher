package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.DeleteFieldAction;
import org.conflito.matcher.patterns.ClassPattern;
import org.conflito.matcher.patterns.FieldPattern;

public class DeleteFieldPatternAction extends DeletePatternAction {

	private FieldPattern deletedFieldPattern;
	
	private ClassPattern holderClassPattern;
	
	public DeleteFieldPatternAction(FieldPattern deletedFieldPattern, ClassPattern holderClassPattern) {
		super();
		this.deletedFieldPattern = deletedFieldPattern;
		this.holderClassPattern = holderClassPattern;
	}
	
	public ActionPattern makeCopy() {
		FieldPattern fieldCopy = new FieldPattern(deletedFieldPattern);
		ClassPattern classCopy = new ClassPattern(holderClassPattern);
		return new DeleteFieldPatternAction(fieldCopy, classCopy);
	}

	public int getDeletedFieldVariableId() {
		return deletedFieldPattern.getVariableId();
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteFieldAction && filled() && matches((DeleteFieldAction)action);
	}
	
	private boolean matches(DeleteFieldAction action) {
		return getAction() == action.getAction() &&
			   deletedFieldPattern.matches(action.getDeletedField()) &&
			   holderClassPattern.matches(action.getHolderClass());
	}

	@Override
	public void setVariableValue(int id, String value) {
		deletedFieldPattern.setVariableValue(id, value);
		holderClassPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return deletedFieldPattern.filled() && holderClassPattern.filled();
	}
	
	@Override
	public void clean() {
		deletedFieldPattern.clean();
		holderClassPattern.clean();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Delete field $" + deletedFieldPattern.getVariableId());
		result.append(" from class $" + holderClassPattern.getVariableId());
		return result.toString();
	}
}
