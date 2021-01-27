package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteFieldAction;
import matcher.patterns.ClassPattern;
import matcher.patterns.FieldPattern;

public class DeleteFieldPatternAction extends DeletePatternAction {

	private FieldPattern deletedFieldPattern;
	
	private ClassPattern holderClassPattern;
	
	public DeleteFieldPatternAction(FieldPattern deletedFieldPattern, ClassPattern holderClassPattern) {
		super();
		this.deletedFieldPattern = deletedFieldPattern;
		this.holderClassPattern = holderClassPattern;
	}

	public int getDeletedFieldVariableId() {
		return deletedFieldPattern.getVariableId();
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteFieldAction && filled() && matches((DeleteFieldAction)action);
	}
	
	private boolean matches(DeleteFieldAction action) {
		return getAction() == action.getAction() &&
			   deletedFieldPattern.matches(action.getDeletedField()) &&
			   holderClassPattern.matches(action.getHolderClass());
	}

	@Override
	public void setVariableValue(int id, String value) {
		deletedFieldPattern.setVariableValue(id, value);
		holderClassPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return deletedFieldPattern.filled() && holderClassPattern.filled();
	}
	
	@Override
	public void clean() {
		deletedFieldPattern.clean();
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
