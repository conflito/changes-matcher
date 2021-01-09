package matcher.entities.deltas;

public class DeleteInvocationAction extends DeleteAction{

	public DeleteInvocationAction(Deletable deletedEntity, Holder holderEntity) {
		super(deletedEntity, holderEntity);
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("remove ");
		result.append("method invocation of ");
		result.append(getDeletedEntityQualifiedName() + " from ");
		result.append(getHolderEntityQualifiedName());
		return result.toString();
	}

}
