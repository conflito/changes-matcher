package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.UpdateMethodAction;
import matcher.patterns.MethodPattern;

public class UpdateMethodPatternAction extends UpdatePatternAction {

	private MethodPattern updatedMethodPattern;
	
	public UpdateMethodPatternAction(MethodPattern updatedMethodPattern) {
		super();
		this.updatedMethodPattern = updatedMethodPattern;
	}
	
	public ActionPattern makeCopy() {
		return new UpdateMethodPatternAction(new MethodPattern(updatedMethodPattern));
	}
	
	public int getUpdatedMethodVariableId() {
		return updatedMethodPattern.getVariableId();
	}

	@Override
	public void setVariableValue(int id, String value) {
		updatedMethodPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return updatedMethodPattern.filled();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof UpdateMethodAction && filled() && 
				matches((UpdateMethodAction) action);
	}

	private boolean matches(UpdateMethodAction action) {
		return getAction() == action.getAction() &&
				updatedMethodPattern.matches(action.getUpdatedMethodInstance());
	}

	@Override
	public String toStringDebug() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clean() {
		updatedMethodPattern.clean();
	}

	@Override
	public String toStringFilled() {
		// TODO Auto-generated method stub
		return null;
	}

}
