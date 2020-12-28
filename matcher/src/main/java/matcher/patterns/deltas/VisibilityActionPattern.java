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
	
	public int getEntityId() {
		return entity.getId();
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
		String entityName = action.getEntityQualifiedName();
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

	@Override
	public String toStringDebug() {
		if(isVisibilityInsert()) {
			String vis = newVisibility == null?"*":newVisibility.toString().toLowerCase();
			return "insert visibility " + vis + " into #" + entity.getId();
		}
		else if(isVisibilityDelete()) {
			String vis = oldVisibility == null?"*":oldVisibility.toString().toLowerCase();
			return "remove visibility " + vis + " from #" + entity.getId();
		}
		else {
			String visNew = newVisibility == null?"*":newVisibility.toString().toLowerCase();
			String visOld = oldVisibility == null?"*":oldVisibility.toString().toLowerCase();
			return "update visibility of #" + entity.getId() + " from " + visNew + " to " + visOld;
		}
	}

	@Override
	public void clean() {
		entity.clean();
	}
	
}
