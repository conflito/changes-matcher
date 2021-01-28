package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.UpdateFieldTypeAction;
import matcher.patterns.FieldPattern;
import matcher.patterns.TypePattern;

public class UpdateFieldTypePatternAction extends UpdatePatternAction {

	private FieldPattern updatedFieldPattern;

	private TypePattern newType;

	public UpdateFieldTypePatternAction(FieldPattern updatedFieldPattern) {
		super();
		this.updatedFieldPattern = updatedFieldPattern;
	}

	public UpdateFieldTypePatternAction(FieldPattern updatedFieldPattern, TypePattern newType) {
		super();
		this.updatedFieldPattern = updatedFieldPattern;
		this.newType = newType;
	}
	
	public int getUpdatedFieldVariableId() {
		return updatedFieldPattern.getVariableId();
	}

	public int getNewTypeVariableId() {
		return newType.getVariableId();
	}

	public boolean hasNewType() {
		return newType != null;
	}

	@Override
	public boolean filled() {
		return updatedFieldPattern.filled() && 
				(newType == null || newType.filled());
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof UpdateFieldTypeAction && filled() && 
				matches((UpdateFieldTypeAction)action);
	}

	private boolean matches(UpdateFieldTypeAction action) {
		return getAction() == action.getAction() &&
				updatedFieldPattern.matches(action.getUpdatedField()) &&
				(newType == null || newType.matches(action.getNewType()));
	}

	@Override
	public void setVariableValue(int id, String value) {
		updatedFieldPattern.setVariableValue(id, value);
		if(newType != null)
			newType.setVariableValue(id, value);
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
		updatedFieldPattern.clean();
		if(newType != null)
			newType.clean();
	}

}
