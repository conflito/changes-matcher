package matcher.entities.deltas;

public class UpdateInvocationAction extends UpdateAction {

	private Updatable newEntity;

	public UpdateInvocationAction(Updatable entity, Updatable newEntity) {
		super(entity);
		this.newEntity = newEntity;
	}
	
	public String getNewEntityQualifiedName() {
		return newEntity.getQualifiedName();
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("update invocation ");
		result.append(getEntityQualifiedName() + " to ");
		result.append(getNewEntityQualifiedName());
		return result.toString();
	}
	
}
