package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteFieldAccessAction;
import matcher.patterns.FieldAccessPattern;
import matcher.patterns.MethodPattern;

public class DeleteFieldAccessPatternAction extends DeletePatternAction {

	private FieldAccessPattern deletedFieldAccessPattern;
	
	private MethodPattern holderMethodPattern;
	
	public DeleteFieldAccessPatternAction(FieldAccessPattern deletedFieldAccessPattern,
			MethodPattern holderMethodPattern) {
		super();
		this.deletedFieldAccessPattern = deletedFieldAccessPattern;
		this.holderMethodPattern = holderMethodPattern;
	}
	
	public int getDeletedFieldAccessVariableId() {
		return deletedFieldAccessPattern.getVariableId();
	}


	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteFieldAccessAction && filled() 
				&& matches((DeleteFieldAccessAction)action);
	}
	
	private boolean matches(DeleteFieldAccessAction action) {
		return getAction() == action.getAction() &&
			   deletedFieldAccessPattern.matches(action.getDeletedFieldAccess()) &&
			   holderMethodPattern.matches(action.getHolderMethod());
	}
	
	@Override
	public void setVariableValue(int id, String value) {
		deletedFieldAccessPattern.setVariableValue(id, value);
		holderMethodPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return deletedFieldAccessPattern.filled() && holderMethodPattern.filled();
	}
	
	@Override
	public void clean() {
		deletedFieldAccessPattern.clean();
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
