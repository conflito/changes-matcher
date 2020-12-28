package matcher.patterns.deltas;

import matcher.entities.Visibility;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertFieldAction;
import matcher.patterns.FreeVariable;

public class InsertFieldPatternAction extends InsertPatternAction {

	private Visibility visibility;

	public InsertFieldPatternAction(FreeVariable insertedEntity, FreeVariable holderEntity, 
			Visibility visibility) {
		super(insertedEntity, holderEntity);
		this.visibility = visibility;
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertFieldAction && filled() && matches((InsertFieldAction)action);
	}
	
	private boolean matches(InsertFieldAction action) {
		return getAction() == action.getAction() &&
			   getInsertedEntity().matches(action.getInsertedEntity().getQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntity().getQualifiedName()) &&
			   (visibility == null || visibility == action.getVisibility());
	}
	
	
}
