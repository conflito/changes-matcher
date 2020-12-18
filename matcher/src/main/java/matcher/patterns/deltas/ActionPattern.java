package matcher.patterns.deltas;

import matcher.entities.deltas.Action;
import matcher.entities.deltas.ActionInstance;

public abstract class ActionPattern {

	private Action action;

	public ActionPattern(Action action) {
		super();
		this.action = action;
	}

	public Action getAction() {
		return action;
	}
	
	public abstract void setVariableValue(int id, String value);
	
	public abstract boolean filled();

	public abstract boolean matches(ActionInstance action);
}