package matcher.entities.deltas;

import matcher.entities.MethodInstance;

public class UpdateMethodAction extends UpdateAction{
	
	private MethodInstance updatedMethodInstance;

	public UpdateMethodAction(MethodInstance updatedMethodInstance) {
		super();
		this.updatedMethodInstance = updatedMethodInstance;
	}

	public MethodInstance getUpdatedMethodInstance() {
		return updatedMethodInstance;
	}
	
	@Override
	public String getUpdatedEntityQualifiedName() {
		return updatedMethodInstance.getQualifiedName();
	}

	@Override
	public String toString() {
		return "update method " + updatedMethodInstance.getQualifiedName();
	}

}
