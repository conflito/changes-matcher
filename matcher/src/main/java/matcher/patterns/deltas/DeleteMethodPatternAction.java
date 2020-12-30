package matcher.patterns.deltas;

import matcher.entities.Visibility;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteMethodAction;
import matcher.patterns.FreeVariable;

public class DeleteMethodPatternAction extends DeletePatternAction {

	private Visibility visibility;

	public DeleteMethodPatternAction(FreeVariable deletedEntity, FreeVariable holderEntity, Visibility visibility) {
		super(deletedEntity, holderEntity);
		this.visibility = visibility;
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteMethodAction && filled() && matches((DeleteMethodAction)action);
	}
	
	private boolean matches(DeleteMethodAction action) {
		return getAction() == action.getAction() &&
			   getDeletedEntity().matches(action.getDeletedEntityQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntityQualifiedName()) &&
			   (visibility == null || visibility == action.getVisibility());
	}
	
}
