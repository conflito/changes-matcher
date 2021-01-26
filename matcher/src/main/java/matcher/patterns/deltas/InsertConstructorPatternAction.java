package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertConstructorAction;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConstructorPattern;

public class InsertConstructorPatternAction extends InsertPatternAction {

	private ConstructorPattern insertedConstructorPattern;
	
	private ClassPattern holderClassPattern;
	
	public InsertConstructorPatternAction(ConstructorPattern insertedConstructorPattern,
			ClassPattern holderClassPattern) {
		super();
		this.insertedConstructorPattern = insertedConstructorPattern;
		this.holderClassPattern = holderClassPattern;
	}
	
	public int getInsertedConstructorVariableId() {
		return insertedConstructorPattern.getVariableId();
	}
	
	public boolean hasInvocations() {
		return insertedConstructorPattern.hasInvocations();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertConstructorAction && filled() 
				&& matches((InsertConstructorAction)action);
	}
	
	private boolean matches(InsertConstructorAction action) {
		return getAction() == action.getAction() &&
			   insertedConstructorPattern.matches(action.getInsertedConstructor()) &&
			   holderClassPattern.matches(action.getHolderClass());
	}

	@Override
	public void setVariableValue(int id, String value) {
		insertedConstructorPattern.setVariableValue(id, value);
		holderClassPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return insertedConstructorPattern.filled() && holderClassPattern.filled();
	}
	
	@Override
	public void clean() {
		insertedConstructorPattern.clean();
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
