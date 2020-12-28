package matcher.entities.deltas;

import matcher.entities.FieldAccessType;

public class InsertFieldAccessAction extends InsertAction {

	private FieldAccessType accessType;

	public InsertFieldAccessAction(Insertable insertedEntity, Holder holderEntity, 
			FieldAccessType accessType) {
		super(insertedEntity, holderEntity);
		this.accessType = accessType;
	}
	
	public FieldAccessType getAccessType() {
		return accessType;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("insert " + accessType.toString().toLowerCase() 
				+ " of field ");
		result.append(getInsertedEntityQualifiedName() + " in ");
		result.append(getHolderEntityQualifiedName());
		return result.toString();
	}

}
