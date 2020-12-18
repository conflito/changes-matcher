package matcher.patterns.deltas;

import matcher.entities.deltas.Action;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteAction;
import matcher.patterns.FreeVariable;

public class DeletePatternAction extends ActionPattern{

	private FreeVariable deletedEntity;
	private FreeVariable holderEntity;

	public DeletePatternAction(Action action, FreeVariable deletedEntity, FreeVariable holderEntity) {
		super(action);
		this.deletedEntity = deletedEntity;
		this.holderEntity = holderEntity;
	}
	
	@Override
	public boolean filled() {
		return deletedEntity.hasValue() && holderEntity.hasValue();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteAction && filled() && matches((DeleteAction)action);
	}

	private boolean matches(DeleteAction action) {
		return getAction() == action.getAction() &&
			   deletedEntity.matches(action.getDeletedEntity().getQualifiedName()) &&
			   holderEntity.matches(action.getHolderEntity().getQualifiedName());
	}

	@Override
	public void setVariableValue(int id, String value) {
		if(deletedEntity.isId(id))
			deletedEntity.setValue(value);
		else if(holderEntity.isId(id))
			holderEntity.setValue(value);
	}

}
