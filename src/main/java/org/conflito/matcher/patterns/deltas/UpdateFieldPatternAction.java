package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.UpdateFieldAction;
import org.conflito.matcher.patterns.ClassPattern;
import org.conflito.matcher.patterns.FieldPattern;

public class UpdateFieldPatternAction extends UpdatePatternAction{

	private FieldPattern updatedFieldPattern;
	
	private ClassPattern classPattern;

	public UpdateFieldPatternAction(FieldPattern updatedFieldPattern, ClassPattern classPattern) {
		super();
		this.updatedFieldPattern = updatedFieldPattern;
		this.classPattern = classPattern;
	}
	
	public int getUpdatedFieldId() {
		return updatedFieldPattern.getVariableId();
	}

	@Override
	public void setVariableValue(int id, String value) {
		updatedFieldPattern.setVariableValue(id, value);
		classPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return updatedFieldPattern.filled() && classPattern.filled();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof UpdateFieldAction && filled() &&
				matches((UpdateFieldAction)action);
	}
	
	private boolean matches(UpdateFieldAction action) {
		return getAction() == action.getAction() &&
				updatedFieldPattern.matches(action.getUpdatedFieldInstance()) &&
				classPattern.matches(action.getClassInstance());
	}

	@Override
	public void clean() {
		updatedFieldPattern.clean();
		classPattern.clean();
	}

	@Override
	public ActionPattern makeCopy() {
		return new UpdateFieldPatternAction(
				new FieldPattern(updatedFieldPattern), 
				new ClassPattern(classPattern));
	}
	
	@Override
	public String toString() {
		return "Update method $" + updatedFieldPattern.getVariableId() + 
				" of class " + classPattern.getVariableId();
	}
	
}
