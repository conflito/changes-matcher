package matcher.entities.deltas;

public class UpdateInvocationAction extends UpdateAction {

	private Updatable newEntity;
	private Holder holder;

	public UpdateInvocationAction(Updatable entity, Updatable newEntity, Holder holder) {
		super(entity);
		this.newEntity = newEntity;
		this.holder = holder;
	}
	
	public String getNewEntityQualifiedName() {
		return newEntity.getQualifiedName();
	}
	
	public String getHolderQualifiedName() {
		return holder.getQualifiedName();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("update invocation ");
		result.append(getEntityQualifiedName() + " in ");
		result.append(holder.getQualifiedName() + " to ");
		result.append(getNewEntityQualifiedName());
		return result.toString();
	}
	
}
