package matcher.patterns.deltas;

import matcher.entities.Visibility;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertMethodAction;
import matcher.patterns.FreeVariable;

public class InsertMethodPatternAction extends InsertPatternAction {

	private Visibility visibility;

	public InsertMethodPatternAction(FreeVariable insertedEntity, FreeVariable holderEntity, 
			Visibility visibility) {
		super(insertedEntity, holderEntity);
		this.visibility = visibility;
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertMethodAction && filled() && matches((InsertMethodAction)action);
	}
	
	private boolean matches(InsertMethodAction action) {
		return getAction() == action.getAction() &&
			   getInsertedEntity().matches(action.getInsertedEntity().getQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntity().getQualifiedName()) &&
			   (visibility == null || visibility == action.getVisibility());
	}
	
	
}
