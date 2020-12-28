package matcher.patterns.deltas;

import matcher.entities.FieldAccessType;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteFieldAccessAction;
import matcher.patterns.FreeVariable;

public class DeleteFieldAccessPatternAction extends DeletePatternAction {

	private FieldAccessType accessType;

	public DeleteFieldAccessPatternAction(FreeVariable deletedEntity, FreeVariable holderEntity,
			FieldAccessType accessType) {
		super(deletedEntity, holderEntity);
		this.accessType = accessType;
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteFieldAccessAction && filled() 
				&& matches((DeleteFieldAccessAction)action);
	}
	
	private boolean matches(DeleteFieldAccessAction action) {
		return getAction() == action.getAction() &&
			   getDeletedEntity().matches(action.getDeletedEntityQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntityQualifiedName()) &&
			   (accessType == null || accessType == action.getAccessType());
	}
}
