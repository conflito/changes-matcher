package matcher.entities.deltas;


import matcher.entities.Visibility;

public class DeleteMethodAction extends DeleteAction {

	private Visibility visibility;

	public DeleteMethodAction(Deletable deletedEntity, Holder holderEntity, Visibility visibility) {
		super(deletedEntity, holderEntity);
		this.visibility = visibility;
	}
	
	public Visibility getVisibility() {
		return visibility;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("delete " + visibility.toString().toLowerCase() 
				+ " method ");
		result.append(getDeletedEntityQualifiedName() + " in ");
		result.append(getHolderEntityQualifiedName());
		return result.toString();
	}
}
