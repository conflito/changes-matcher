package matcher.entities.deltas;

public class InsertInvocationAction extends InsertAction {

	public InsertInvocationAction(Insertable insertedEntity, Holder holderEntity) {
		super(insertedEntity, holderEntity);
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("insert ");
		result.append("method invocation of ");
		result.append(getInsertedEntityQualifiedName() + " in ");
		result.append(getHolderEntityQualifiedName());
		return result.toString();
	}

}
