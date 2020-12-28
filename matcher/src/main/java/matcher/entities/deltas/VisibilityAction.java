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
	
	public String getEntityQualifiedName() {
		return entity.getQualifiedName();
	}

	public Visibility getOldVisibility() {
		return oldVisibility;
	}

	public Visibility getNewVisibility() {
		return newVisibility;
	}
	
	public boolean isVisibilityInsert() {
		return getAction() == Action.INSERT;
	}
	
	public boolean isVisibilityDelete() {
		return getAction() == Action.DELETE;
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
		else if(isVisibilityInsert()) {
			result.append("insert visibility ");
			result.append(getNewVisibility());
			result.append(" for " + entity.getQualifiedName());
		}
		else if(isVisibilityDelete()) {
			result.append("delete visibility ");
			result.append(getOldVisibility());
			result.append(" for " + entity.getQualifiedName());
		}
		return result.toString();
	}
}
