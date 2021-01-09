package matcher.entities.deltas;

public abstract class DeleteAction extends ActionInstance {

	private Deletable deletedEntity;
	private Holder holderEntity;
	
	public DeleteAction(Deletable deletedEntity, Holder holderEntity) {
		super(Action.DELETE);
		this.deletedEntity = deletedEntity;
		this.holderEntity = holderEntity;
	}
	
	public String getDeletedEntityQualifiedName() {
		return deletedEntity.getQualifiedName();
	}
	
	public String getHolderEntityQualifiedName() {
		return holderEntity.getQualifiedName();
	}
	
}
