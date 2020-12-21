package matcher.patterns.deltas;

import matcher.entities.deltas.Action;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.UpdateAction;
import matcher.patterns.FreeVariable;

public class UpdatePatternAction extends ActionPattern {

	private FreeVariable entity;
	
	public UpdatePatternAction(Action action, FreeVariable entity) {
		super(action);
		this.entity = entity;
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
			   entity.matches(action.getEntity().getQualifiedName());
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

}
