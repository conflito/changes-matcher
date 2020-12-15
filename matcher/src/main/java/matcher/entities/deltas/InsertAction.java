package matcher.entities.deltas;

public class InsertAction extends ActionInstance {

	private Insertable insertedEntity;
	private Holder holderEntity;
	
	
	public InsertAction(Action action, Insertable insertedEntity, Holder holderEntity) {
		super(action);
		this.insertedEntity = insertedEntity;
		this.holderEntity = holderEntity;
	}

	public Insertable getInsertedEntity() {
		return insertedEntity;
	}

	public Holder getHolderEntity() {
		return holderEntity;
	}
}
