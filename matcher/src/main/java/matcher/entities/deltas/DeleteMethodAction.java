package matcher.entities.deltas;

import java.util.ArrayList;
import java.util.List;

import matcher.entities.Visibility;

public class DeleteMethodAction extends DeleteAction {

	private Visibility visibility;
	
	private List<Holder> compatibles;

	public DeleteMethodAction(Deletable deletedEntity, Holder holderEntity, Visibility visibility) {
		super(deletedEntity, holderEntity);
		this.visibility = visibility;
		compatibles = new ArrayList<>();
	}
	
	public Visibility getVisibility() {
		return visibility;
	}
	
	public List<Holder> getCompatibles() {
		return compatibles;
	}

	public void addCompatible(Holder compatible) {
		compatibles.add(compatible);
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("delete " + visibility.toString().toLowerCase() 
				+ " method ");
		result.append(getDeletedEntityQualifiedName() + " in ");
		result.append(getHolderEntityQualifiedName());
		if(!compatibles.isEmpty()) {
			result.append("\n");
			for(Holder h: compatibles) {
				result.append(getDeletedEntityQualifiedName() + " compatible with ");
				result.append(h.getQualifiedName() + "\n");
			}			
		}
		return result.toString();
	}
}
