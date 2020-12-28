package matcher.patterns.deltas;

import matcher.entities.Visibility;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertConstructorAction;
import matcher.patterns.FreeVariable;

public class InsertConstructorPatternAction extends InsertPatternAction {

	private Visibility visibility;

	public InsertConstructorPatternAction(FreeVariable insertedEntity, FreeVariable holderEntity, 
			Visibility visibility) {
		super(insertedEntity, holderEntity);
		this.visibility = visibility;
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertConstructorAction && filled() 
				&& matches((InsertConstructorAction)action);
	}
	
	private boolean matches(InsertConstructorAction action) {
		return getAction() == action.getAction() &&
			   getInsertedEntity().matches(action.getInsertedEntityQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntityQualifiedName()) &&
			   (visibility == null || visibility == action.getVisibility());
	}
	
	
}
