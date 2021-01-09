package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteAction;
import matcher.entities.deltas.DeleteInvocationAction;
import matcher.patterns.FreeVariable;

public class DeleteInvocationPatternAction extends DeletePatternAction {

	public DeleteInvocationPatternAction(FreeVariable deletedEntity, FreeVariable holderEntity) {
		super(deletedEntity, holderEntity);
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteInvocationAction && filled() 
				&& matches((DeleteInvocationAction)action);
	}

	private boolean matches(DeleteAction action) {
		return getAction() == action.getAction() &&
			   getDeletedEntity().matches(action.getDeletedEntityQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntityQualifiedName());
	}

}
