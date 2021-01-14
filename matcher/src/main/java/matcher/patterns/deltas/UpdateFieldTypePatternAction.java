package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.UpdateFieldTypeAction;
import matcher.patterns.FreeVariable;

public class UpdateFieldTypePatternAction extends UpdatePatternAction {

	private FreeVariable oldType;
	private FreeVariable newType;
	
	public UpdateFieldTypePatternAction(FreeVariable entity, FreeVariable oldType, 
			FreeVariable newType) {
		super(entity);
		this.oldType = oldType;
		this.newType = newType;
	}
	
	@Override
	public boolean filled() {
		return super.filled() && oldType.hasValue() && newType.hasValue();
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof UpdateFieldTypeAction && filled() && 
				matches((UpdateFieldTypeAction)action);
	}
	
	private boolean matches(UpdateFieldTypeAction action) {
		return getAction() == action.getAction() &&
			   getEntity().matches(action.getEntityQualifiedName()) &&
			   oldType.matches(action.getOldTypeName()) &&
			   newType.matches(action.getNewTypeName());
	}
	
	@Override
	public void setVariableValue(int id, String value) {
		super.setVariableValue(id, value);
		if(oldType.isId(id))
			oldType.setValue(value);
		if(newType.isId(id))
			newType.setValue(value);
	}
	
	@Override
	public String toStringDebug() {
		return "update #" + getEntityId() + " type from #" + oldType.getId() + " to #" +
				newType.getId();
	}
	
	@Override
	public String toStringFilled() {
		return "update #" + getEntity().getValue() + " type from #" + oldType.getValue() 
				+ " to #" + newType.getValue();
	}

	@Override
	public void clean() {
		super.clean();
		oldType.clean();
		newType.clean();
	}
	
}
