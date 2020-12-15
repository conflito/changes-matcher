package matcher.entities.deltas;

public class DeleteAction extends ActionInstance {

	private Deletable deletedEntity;
	private Holder holderEntity;
	
	public DeleteAction(Action action, Deletable deletedEntity, Holder holderEntity) {
		super(action);
		this.deletedEntity = deletedEntity;
		this.holderEntity = holderEntity;
	}

	public Deletable getDeletedEntity() {
		return deletedEntity;
	}

	public Holder getHolderEntity() {
		return holderEntity;
	}	
}
