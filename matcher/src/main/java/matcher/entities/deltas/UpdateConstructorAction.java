package matcher.entities.deltas;

import matcher.entities.ConstructorInstance;

public class UpdateConstructorAction extends UpdateAction{
	
	private ConstructorInstance updatedConstructorInstance;

	public UpdateConstructorAction(ConstructorInstance updatedConstructorInstance) {
		super();
		this.updatedConstructorInstance = updatedConstructorInstance;
	}

	public ConstructorInstance getUpdatedConstructorInstance() {
		return updatedConstructorInstance;
	}
	
	@Override
	public String getUpdatedEntityQualifiedName() {
		return updatedConstructorInstance.getQualifiedName();
	}

	@Override
	public String toString() {
		return "update method " + updatedConstructorInstance.getQualifiedName();
	}

}
