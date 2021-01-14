package matcher.entities.deltas;

import matcher.entities.ConstructorInstance;
import matcher.entities.FieldInstance;
import matcher.entities.MethodInstance;

public class UpdateAction extends ActionInstance {

	private Updatable entity;

	public UpdateAction(Updatable entity) {
		super(Action.UPDATE);
		this.entity = entity;
	}
	
	public String getEntityQualifiedName() {
		return entity.getQualifiedName();
	}
	
	public boolean isMethodUpdate() {
		return entity instanceof MethodInstance;
	}
	
	public boolean isConstructorUpdate() {
		return entity instanceof ConstructorInstance;
	}
	
	public boolean isFieldUpdate() {
		return entity instanceof FieldInstance;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("update ");
		if(isMethodUpdate())
			result.append("method ");
		if(isConstructorUpdate())
			result.append("constructor ");
		if(isFieldUpdate())
			result.append("field ");
		result.append(entity.getQualifiedName());
		return result.toString();
	}
}
