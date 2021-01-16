package matcher.patterns.deltas;

import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.UpdateFieldTypeAction;
import matcher.patterns.FreeVariable;

public class UpdateFieldTypePatternAction extends UpdatePatternAction {

	private FreeVariable oldType;
	private FreeVariable newType;
	
	public UpdateFieldTypePatternAction(FreeVariable entity) {
		super(entity);
	}
	
	public UpdateFieldTypePatternAction(FreeVariable entity, FreeVariable oldType, 
			FreeVariable newType) {
		super(entity);
		this.oldType = oldType;
		this.newType = newType;
	}
	
	public int getNewTypeVariableId() {
		return newType.getId();
	}
	
	public boolean hasNewType() {
		return newType != null;
	}
	
	@Override
	public boolean filled() {
		return super.filled() && 
			   (oldType == null || oldType.hasValue()) && 
			   (newType == null || newType.hasValue());
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof UpdateFieldTypeAction && filled() && 
				matches((UpdateFieldTypeAction)action);
	}
	
	private boolean matches(UpdateFieldTypeAction action) {
		return getAction() == action.getAction() &&
			   getEntity().matches(action.getEntityQualifiedName()) &&
			   (oldType == null || oldType.matches(action.getOldTypeName())) &&
			   (newType == null || newType.matches(action.getNewTypeName()));
	}
	
	@Override
	public void setVariableValue(int id, String value) {
		super.setVariableValue(id, value);
		if(oldType != null && oldType.isId(id))
			oldType.setValue(value);
		if(newType != null && newType.isId(id))
			newType.setValue(value);
	}
	
	@Override
	public String toStringDebug() {
		StringBuilder result = new StringBuilder();
		result.append("update #" + getEntityId() + " type");
		if(oldType != null && newType != null)
			result.append(" from #" + oldType.getId() + " to #" + newType.getId());
		else if(oldType != null)
			result.append(" from #" + oldType.getId());
		else
			result.append(" to #" + newType.getId());
		return result.toString();
	}
	
	@Override
	public String toStringFilled() {
		StringBuilder result = new StringBuilder();
		result.append("update #" + getEntity().getValue() + " type");
		if(oldType != null && newType != null)
			result.append(" from #" + oldType.getValue() + " to #" + newType.getValue());
		else if(oldType != null)
			result.append(" from #" + oldType.getValue());
		else
			result.append(" to #" + newType.getValue());
		return result.toString();
	}

	@Override
	public void clean() {
		super.clean();
		if(oldType != null)
			oldType.clean();
		if(newType != null)
			newType.clean();
	}
	
}
