package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.UpdateInvocationAction;
import matcher.patterns.FreeVariable;

public class UpdateInvocationPatternAction extends UpdatePatternAction {

	private FreeVariable newEntity;
	private FreeVariable holder;

	public UpdateInvocationPatternAction(FreeVariable entity, FreeVariable newEntity,
			FreeVariable holder) {
		super(entity);
		this.newEntity = newEntity;
		this.holder = holder;
	}
	
	public int getNewEntityId() {
		return newEntity.getId();
	}
	
	public int getHolderId() {
		return holder.getId();
	}

	public FreeVariable getNewEntity() {
		return newEntity;
	}
	
	public FreeVariable getHolder() {
		return holder;
	}
	
	@Override
	public boolean filled() {
		return super.filled() && newEntity.hasValue() && holder.hasValue();
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof UpdateInvocationAction && filled() && 
				matches((UpdateInvocationAction)action);
	}
	
	private boolean matches(UpdateInvocationAction action) {
		return getAction() == action.getAction() &&
			   getEntity().matches(action.getEntityQualifiedName()) &&
			   getNewEntity().matches(action.getNewEntityQualifiedName()) &&
			   getHolder().matches(action.getHolderQualifiedName());
	}
	
	@Override
	public void setVariableValue(int id, String value) {
		super.setVariableValue(id, value);
		if(newEntity.isId(id))
			newEntity.setValue(value);
		if(holder.isId(id))
			holder.setValue(value);
	}
	
	@Override
	public String toStringDebug() {
		return "update #" + getEntityId() + " in #" + holder.getId() 
				+ " to #" + newEntity.getId();
	}
	
	@Override
	public String toStringFilled() {
		return "update #" + getEntity().getValue() + " in #" + holder.getValue() + 
				" to #" + newEntity.getValue();
	}

	@Override
	public void clean() {
		super.clean();
		newEntity.clean();
		holder.clean();
	}
}
