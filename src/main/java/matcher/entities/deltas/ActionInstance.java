package matcher.entities.deltas;

public abstract class ActionInstance {

	private Action action;
	
	public ActionInstance(Action action) {
		this.action = action;
	}

	public Action getAction() {
		return action;
	}
	
}
