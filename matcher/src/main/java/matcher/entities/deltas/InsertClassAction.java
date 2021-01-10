package matcher.entities.deltas;

public class InsertClassAction extends InsertAction {

	public InsertClassAction(Insertable insertedEntity) {
		super(insertedEntity, null);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("insert new class ");
		result.append(getInsertedEntityQualifiedName());
		return result.toString();
	}

}
