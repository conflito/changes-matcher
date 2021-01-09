package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertInvocationAction;
import matcher.patterns.FreeVariable;

public class InsertInvocationPatternAction extends InsertPatternAction {

	public InsertInvocationPatternAction(FreeVariable insertedEntity, 
			FreeVariable holderEntity) {
		super(insertedEntity, holderEntity);
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertInvocationAction && filled() 
				&& matches((InsertInvocationAction)action);
	}
	
	private boolean matches(InsertInvocationAction action) {
		return getAction() == action.getAction() &&
			   getInsertedEntity().matches(action.getInsertedEntityQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntityQualifiedName());
	}

}
