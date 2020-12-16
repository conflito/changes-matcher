package matcher.entities.deltas;

import matcher.entities.ConstructorInstance;
import matcher.entities.MethodInstance;

public class UpdateAction extends ActionInstance {

	private Holder entity;

	public UpdateAction(Action action, Holder entity) {
		super(action);
		this.entity = entity;
	}

	public Holder getEntity() {
		return entity;
	}
	
	public boolean isMethodUpdate() {
		return entity instanceof MethodInstance;
	}
	
	public boolean isConstructorUpdate() {
		return entity instanceof ConstructorInstance;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("update ");
		if(isMethodUpdate())
			result.append("method ");
		if(isConstructorUpdate())
			result.append("constructor ");
		result.append(entity.getQualifiedName());
		return result.toString();
	}
}
