package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertFieldAction;
import matcher.patterns.ClassPattern;
import matcher.patterns.FieldPattern;

public class InsertFieldPatternAction extends InsertPatternAction {

	private FieldPattern insertedFieldPattern;
	
	private ClassPattern holderClassPattern;

	public InsertFieldPatternAction(FieldPattern insertedFieldPattern, 
			ClassPattern holderClassPattern) {
		super();
		this.insertedFieldPattern = insertedFieldPattern;
		this.holderClassPattern = holderClassPattern;
	}
	
	public int getInsertedFieldVariableId() {
		return insertedFieldPattern.getVariableId();
	}
	
	@Override
	public boolean filled() {
		return insertedFieldPattern.filled() && holderClassPattern.filled();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertFieldAction && filled() 
				&& matches((InsertFieldAction)action);
	}
	
	private boolean matches(InsertFieldAction action) {
		return getAction() == action.getAction() &&
			   insertedFieldPattern.matches(action.getInsertedField()) &&
			   holderClassPattern.matches(action.getHolderClass());
	}

	@Override
	public void setVariableValue(int id, String value) {
		insertedFieldPattern.setVariableValue(id, value);
		holderClassPattern.setVariableValue(id, value);
	}
	
	@Override
	public void clean() {
		insertedFieldPattern.clean();
		holderClassPattern.clean();
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
