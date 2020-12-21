package matcher.entities.deltas;

import matcher.entities.Visibility;

public class InsertMethodAction extends InsertAction {

	private Visibility visibility;

	public InsertMethodAction(Insertable insertedEntity, Holder holderEntity, 
			Visibility visibility) {
		super(insertedEntity, holderEntity);
		this.visibility = visibility;
	}
	
	public Visibility getVisibility() {
		return visibility;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("insert " + visibility.toString().toLowerCase() 
				+ " method ");
		result.append(getInsertedEntity().getQualifiedName() + " in ");
		result.append(getHolderEntity().getQualifiedName());
		return result.toString();
	}

}
