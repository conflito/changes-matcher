package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertFieldAccessAction;
import matcher.patterns.FieldAccessPattern;
import matcher.patterns.MethodPattern;

public class InsertFieldAccessPatternAction extends InsertPatternAction {

	private FieldAccessPattern insertedFieldAccessPattern;
	
	private MethodPattern holderMethodPattern;
	
	public InsertFieldAccessPatternAction(FieldAccessPattern insertedFieldAccessPattern,
			MethodPattern holderMethodPattern) {
		super();
		this.insertedFieldAccessPattern = insertedFieldAccessPattern;
		this.holderMethodPattern = holderMethodPattern;
	}
	
	public int getInsertedFieldAccessVariableId() {
		return insertedFieldAccessPattern.getVariableId();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertFieldAccessAction && filled() 
				&& matches((InsertFieldAccessAction)action);
	}
	
	private boolean matches(InsertFieldAccessAction action) {
		return getAction() == action.getAction() &&
			   insertedFieldAccessPattern.matches(action.getInsertedFieldAccess()) &&
			   holderMethodPattern.matches(action.getHolderMethod());
	}

	@Override
	public void setVariableValue(int id, String value) {
		insertedFieldAccessPattern.setVariableValue(id, value);
		holderMethodPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return insertedFieldAccessPattern.filled() && holderMethodPattern.filled();
	}
	
	@Override
	public void clean() {
		insertedFieldAccessPattern.clean();
		holderMethodPattern.clean();
	}

	@Override
	public String toStringDebug() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toStringFilled() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
