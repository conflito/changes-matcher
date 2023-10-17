package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.Visibility;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		result = prime * result + ((newVisibility == null) ? 0 : newVisibility.hashCode());
		result = prime * result + ((oldVisibility == null) ? 0 : oldVisibility.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VisibilityAction))
			return false;
		VisibilityAction other = (VisibilityAction) obj;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		if (newVisibility != other.newVisibility)
			return false;
		if (oldVisibility != other.oldVisibility)
			return false;
		return true;
	}
}
