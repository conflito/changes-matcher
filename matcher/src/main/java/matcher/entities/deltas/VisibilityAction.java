package matcher.entities.deltas;

import matcher.entities.Visibility;

public class VisibilityAction extends ActionInstance {

	private Visible entity;
	private Visibility oldVisibility;
	private Visibility newVisibility;
	
	public VisibilityAction(Action action, Visible entity, 
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
	
	public boolean isVisibilityInsert() {
		return oldVisibility == null;
	}
	
	public boolean isVisibilityDelete() {
		return newVisibility == null;
	}
	
	public boolean isVisibilityUpdate() {
		return !isVisibilityInsert() && !isVisibilityDelete();
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		if(isVisibilityUpdate()) {
			result.append("update visibility of ");
			result.append(entity.getQualifiedName());
			result.append(" from " + getOldVisibility() + " to " + getNewVisibility());
		}
		return result.toString();
	}
}
