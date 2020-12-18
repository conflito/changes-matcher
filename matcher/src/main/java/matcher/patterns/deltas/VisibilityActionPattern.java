package matcher.patterns.deltas;

import matcher.entities.Visibility;
import matcher.entities.deltas.Action;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.VisibilityAction;
import matcher.patterns.FreeVariable;

public class VisibilityActionPattern extends ActionPattern{

	private Visibility newVisibility;
	
	private Visibility oldVisibility;
	
	private FreeVariable entity;

	public VisibilityActionPattern(Action action, Visibility newVisibility, 
			Visibility oldVisibility, FreeVariable entity) {
		super(action);
		this.newVisibility = newVisibility;
		this.oldVisibility = oldVisibility;
		this.entity = entity;
	}
	
	public boolean isVisibilityInsert() {
		return getAction() == Action.INSERT;
	}
	
	public boolean isVisibilityDelete() {
		return getAction() == Action.DELETE;
	}
	
	public boolean isVisibilityUpdate() {
		return !isVisibilityDelete() && !isVisibilityInsert();
	}

	@Override
	public void setVariableValue(int id, String value) {
		if(entity.isId(id))
			entity.setValue(value);
	}

	@Override
	public boolean filled() {
		return entity.hasValue();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof VisibilityAction && filled() && matches((VisibilityAction)action);
	}
	
	private boolean matches(VisibilityAction action) {
		String entityName = action.getEntity().getQualifiedName();
		if(isVisibilityInsert() && action.isVisibilityInsert()) {
			return (newVisibility == null || newVisibility == action.getNewVisibility()) &&
				   entity.matches(entityName);
		}
		else if(isVisibilityDelete() && action.isVisibilityDelete()) {
			return (oldVisibility == null || oldVisibility == action.getOldVisibility()) &&
				   entity.matches(entityName);
		}
		else if(isVisibilityUpdate() && action.isVisibilityUpdate()) {
			return (oldVisibility == null || oldVisibility == action.getOldVisibility()) &&
				   (newVisibility == null || newVisibility == action.getNewVisibility()) &&
				   entity.matches(entityName);
		}
		return false;
	}
	
}
