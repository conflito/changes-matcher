package matcher.patterns.deltas;

import matcher.entities.Visibility;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteConstructorAction;
import matcher.patterns.FreeVariable;

public class DeleteConstructorPatternAction extends DeletePatternAction {

	private Visibility visibility;

	public DeleteConstructorPatternAction(FreeVariable deletedEntity, FreeVariable holderEntity,
			Visibility visibility) {
		super(deletedEntity, holderEntity);
		this.visibility = visibility;
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteConstructorAction && filled() 
				&& matches((DeleteConstructorAction)action);
	}
	
	private boolean matches(DeleteConstructorAction action) {
		return getAction() == action.getAction() &&
			   getDeletedEntity().matches(action.getDeletedEntity().getQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntity().getQualifiedName()) &&
			   (visibility == null || visibility == action.getVisibility());
	}
}
