package matcher.patterns.deltas;

import matcher.entities.FieldAccessType;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertFieldAccessAction;
import matcher.patterns.FreeVariable;

public class InsertFieldAccessPatternAction extends InsertPatternAction {

	private FieldAccessType accessType;

	public InsertFieldAccessPatternAction(FreeVariable insertedEntity, FreeVariable holderEntity, 
			FieldAccessType accessType) {
		super(insertedEntity, holderEntity);
		this.accessType = accessType;
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertFieldAccessAction && filled() && matches((InsertFieldAccessAction)action);
	}
	
	private boolean matches(InsertFieldAccessAction action) {
		return getAction() == action.getAction() &&
			   getInsertedEntity().matches(action.getInsertedEntity().getQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntity().getQualifiedName()) &&
			   (accessType == null || accessType == action.getAccessType());
	}
	
	
}
