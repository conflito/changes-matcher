package org.conflito.matcher.entities.deltas;


public abstract class UpdateAction extends ActionInstance {

	public UpdateAction() {
		super(Action.UPDATE);
	}
	
	public abstract String getUpdatedEntityQualifiedName();

}
