package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteConstructorAction;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConstructorPattern;

public class DeleteConstructorPatternAction extends DeletePatternAction {

	private ConstructorPattern deletedConstructorPattern;
	
	private ClassPattern holderClassPattern;
	
	public DeleteConstructorPatternAction(ConstructorPattern insertedConstructorPattern,
			ClassPattern holderClassPattern) {
		super();
		this.deletedConstructorPattern = insertedConstructorPattern;
		this.holderClassPattern = holderClassPattern;
	}
	
	public ActionPattern makeCopy() {
		ConstructorPattern constructorCopy = new ConstructorPattern(deletedConstructorPattern);
		ClassPattern classCopy = new ClassPattern(holderClassPattern);
		return new DeleteConstructorPatternAction(constructorCopy, classCopy);
	}
	
	public int getDeletedConstructorVariableId() {
		return deletedConstructorPattern.getVariableId();
	}
	
	public boolean hasInvocations() {
		return deletedConstructorPattern.hasInvocations();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteConstructorAction && filled() 
				&& matches((DeleteConstructorAction)action);
	}
	
	private boolean matches(DeleteConstructorAction action) {
		return getAction() == action.getAction() &&
			   deletedConstructorPattern.matches(action.getDeletedConstructor()) &&
			   holderClassPattern.matches(action.getHolderClass());
	}
	
	@Override
	public void setVariableValue(int id, String value) {
		deletedConstructorPattern.setVariableValue(id, value);
		holderClassPattern.setVariableValue(id, value);
	}

	@Override
	public boolean filled() {
		return deletedConstructorPattern.filled() && holderClassPattern.filled();
	}
	
	@Override
	public void clean() {
		deletedConstructorPattern.clean();
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
