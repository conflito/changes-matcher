package matcher.patterns.deltas;

import matcher.entities.deltas.Action;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.UpdateAction;
import matcher.patterns.FreeVariable;

public class UpdatePatternAction extends ActionPattern {

	private FreeVariable entity;
	
	public UpdatePatternAction(FreeVariable entity) {
		super(Action.UPDATE);
		this.entity = entity;
	}
	
	public int getEntityId() {
		return entity.getId();
	}

	@Override
	public boolean filled() {
		return entity.hasValue();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof UpdateAction && filled() && matches((UpdateAction)action);
	}
	
	private boolean matches(UpdateAction action) {
		return getAction() == action.getAction() &&
			   entity.matches(action.getEntityQualifiedName());
	}

	@Override
	public void setVariableValue(int id, String value) {
		if(entity.isId(id))
			entity.setValue(value);
	}

	@Override
	public String toStringDebug() {
		return "update #" + entity.getId();
	}
	
	@Override
	public String toStringFilled() {
		return "update #" + entity.getValue();
	}

	@Override
	public void clean() {
		entity.clean();
	}

}
