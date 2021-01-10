package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertClassAction;
import matcher.patterns.FreeVariable;

public class InsertClassPatternAction extends InsertPatternAction {

	public InsertClassPatternAction(FreeVariable insertedEntity) {
		super(insertedEntity, null);
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertClassAction && filled() &&
				matches((InsertClassAction) action);
	}
	
	private boolean matches(InsertClassAction action) {
		return getAction() == action.getAction() &&
			   getInsertedEntity().matches(action.getInsertedEntityQualifiedName());
	}

}
