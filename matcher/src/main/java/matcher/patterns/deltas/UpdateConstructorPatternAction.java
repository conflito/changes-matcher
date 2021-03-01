package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.UpdateConstructorAction;
import matcher.patterns.ConstructorPattern;

public class UpdateConstructorPatternAction extends UpdatePatternAction {

	private ConstructorPattern updatedConstructorPattern;
	
	public UpdateConstructorPatternAction(ConstructorPattern updatedMethodPattern) {
		super();
		this.updatedConstructorPattern = updatedMethodPattern;
	}
	
	public ActionPattern makeCopy() {
		return new UpdateConstructorPatternAction(new ConstructorPattern(updatedConstructorPattern));
	}
	
	public int getUpdatedConstructorVariableId() {
		return updatedConstructorPattern.getVariableId();
	}

	@Override
	public void setVariableValue(int id, String value) {
		updatedConstructorPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return updatedConstructorPattern.filled();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof UpdateConstructorAction && filled() && 
				matches((UpdateConstructorAction) action);
	}

	private boolean matches(UpdateConstructorAction action) {
		return getAction() == action.getAction() &&
				updatedConstructorPattern.matches(action.getUpdatedConstructorInstance());
	}

	@Override
	public String toStringDebug() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clean() {
		updatedConstructorPattern.clean();
	}

	@Override
	public String toStringFilled() {
		// TODO Auto-generated method stub
		return null;
	}

}
