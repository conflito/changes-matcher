package matcher.entities.deltas;

public abstract class InsertAction extends ActionInstance {

	private Insertable insertedEntity;
	private Holder holderEntity;
	
	
	public InsertAction(Insertable insertedEntity, Holder holderEntity) {
		super(Action.INSERT);
		this.insertedEntity = insertedEntity;
		this.holderEntity = holderEntity;
	}
	
	public String getInsertedEntityQualifiedName() {
		return insertedEntity.getQualifiedName();
	}
	
	public String getHolderEntityQualifiedName() {
		return holderEntity.getQualifiedName();
	}
	
	public abstract String toString();
}
