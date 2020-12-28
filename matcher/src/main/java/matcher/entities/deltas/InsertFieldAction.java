package matcher.entities.deltas;

import matcher.entities.Visibility;

public class InsertFieldAction extends InsertAction {

	private Visibility visibility;

	public InsertFieldAction(Insertable insertedEntity, Holder holderEntity, 
			Visibility visibility) {
		super(insertedEntity, holderEntity);
		this.visibility = visibility;
	}
	
	public Visibility getVisibility() {
		return visibility;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("insert " + visibility.toString().toLowerCase() 
				+ " field ");
		result.append(getInsertedEntityQualifiedName() + " in ");
		result.append(getHolderEntityQualifiedName());
		return result.toString();
	}

}
