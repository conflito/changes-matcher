package matcher.entities.deltas;

import matcher.entities.Visibility;

public class UpdateVisibilityAction extends ActionInstance {

	private Visible entity;
	private Visibility oldVisibility;
	private Visibility newVisibility;
	
	public UpdateVisibilityAction(Action action, Visible entity, 
			Visibility oldVisibility, Visibility newVisibility) {
		super(action);
		this.entity = entity;
		this.oldVisibility = oldVisibility;
		this.newVisibility = newVisibility;
	}

	public Visible getEntity() {
		return entity;
	}

	public Visibility getOldVisibility() {
		return oldVisibility;
	}

	public Visibility getNewVisibility() {
		return newVisibility;
	}
	
	
}
