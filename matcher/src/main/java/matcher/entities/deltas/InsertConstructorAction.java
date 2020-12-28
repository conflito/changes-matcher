package matcher.entities.deltas;

import matcher.entities.Visibility;

public class InsertConstructorAction extends InsertAction {

	private Visibility visibility;

	public InsertConstructorAction(Insertable insertedEntity, Holder holderEntity, 
			Visibility visibility) {
		super(insertedEntity, holderEntity);
		this.visibility = visibility;
	}
	
	public Visibility getVisibility() {
		return visibility;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("insert " + visibility.toString().toLowerCase() 
				+ " constructor ");
		result.append(getInsertedEntityQualifiedName() + " in ");
		result.append(getHolderEntityQualifiedName());
		return result.toString();
	}

}
