package matcher.entities.deltas;

import matcher.entities.FieldAccessType;

public class DeleteFieldAccessAction extends DeleteAction{

	private FieldAccessType accessType;

	public DeleteFieldAccessAction(Deletable deletedEntity, Holder holderEntity, FieldAccessType accessType) {
		super(deletedEntity, holderEntity);
		this.accessType = accessType;
	}
	
	public FieldAccessType getAccessType() {
		return accessType;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("delete " + accessType.toString().toLowerCase() 
				+ " of field ");
		result.append(getDeletedEntityQualifiedName() + " in ");
		result.append(getHolderEntityQualifiedName());
		return result.toString();
	}
}
