package matcher.patterns.deltas;

import java.util.List;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertClassAction;
import matcher.patterns.ClassPattern;

public class InsertClassPatternAction extends InsertPatternAction {
	
	private ClassPattern insertedClassPattern;

	public InsertClassPatternAction(ClassPattern insertedClassPattern) {
		super();
		this.insertedClassPattern = insertedClassPattern;
	}
	
	public ActionPattern makeCopy() {
		return new InsertClassPatternAction(new ClassPattern(insertedClassPattern));
	}
	
	public int getInsertedClassVariableId() {
		return insertedClassPattern.getVariableId();
	}
	
	public List<Integer> getMethodsVariableIds(){
		return insertedClassPattern.getMethodVariableIds();
	}
	
	public boolean hasInvocations() {
		return insertedClassPattern.hasInvocations();
	}
	
	public boolean hasFieldAccesses() {
		return insertedClassPattern.hasFieldAccesses();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertClassAction && filled() &&
				matches((InsertClassAction) action);
	}
	
	private boolean matches(InsertClassAction action) {
		return getAction() == action.getAction() &&
			   insertedClassPattern.matches(action.getInsertedClass());
	}

	@Override
	public void setVariableValue(int id, String value) {
		insertedClassPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return insertedClassPattern.filled();
	}
	
	@Override
	public void clean() {
		insertedClassPattern.clean();
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
