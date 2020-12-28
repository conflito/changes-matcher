package matcher.patterns.deltas;

import matcher.entities.Visibility;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteFieldAction;
import matcher.patterns.FreeVariable;

public class DeleteFieldPatternAction extends DeletePatternAction {

	private Visibility visibility;

	public DeleteFieldPatternAction(FreeVariable deletedEntity, FreeVariable holderEntity, Visibility visibility) {
		super(deletedEntity, holderEntity);
		this.visibility = visibility;
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteFieldAction && filled() && matches((DeleteFieldAction)action);
	}
	
	private boolean matches(DeleteFieldAction action) {
		return getAction() == action.getAction() &&
			   getDeletedEntity().matches(action.getDeletedEntity().getQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntity().getQualifiedName()) &&
			   (visibility == null || visibility == action.getVisibility());
	}
}
