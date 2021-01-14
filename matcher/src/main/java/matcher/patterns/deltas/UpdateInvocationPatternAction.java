package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.UpdateInvocationAction;
import matcher.patterns.FreeVariable;

public class UpdateInvocationPatternAction extends UpdatePatternAction {

	private FreeVariable newEntity;

	public UpdateInvocationPatternAction(FreeVariable entity, FreeVariable newEntity) {
		super(entity);
		this.newEntity = newEntity;
	}
	
	public int getNewEntityId() {
		return newEntity.getId();
	}

	public FreeVariable getNewEntity() {
		return newEntity;
	}
	
	@Override
	public boolean filled() {
		return super.filled() && newEntity.hasValue();
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof UpdateInvocationAction && filled() && 
				matches((UpdateInvocationAction)action);
	}
	
	private boolean matches(UpdateInvocationAction action) {
		return getAction() == action.getAction() &&
			   getEntity().matches(action.getEntityQualifiedName()) &&
			   getNewEntity().matches(action.getNewEntityQualifiedName());
	}
	
	@Override
	public void setVariableValue(int id, String value) {
		super.setVariableValue(id, value);
		if(newEntity.isId(id))
			newEntity.setValue(value);
	}
	
	@Override
	public String toStringDebug() {
		return "update #" + getEntityId() + " to #" + newEntity.getId();
	}
	
	@Override
	public String toStringFilled() {
		return "update #" + getEntity().getValue() + " to #" + newEntity.getValue();
	}

	@Override
	public void clean() {
		super.clean();
		newEntity.clean();
	}
}
