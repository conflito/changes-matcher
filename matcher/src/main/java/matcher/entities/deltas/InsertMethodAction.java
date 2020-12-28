package matcher.entities.deltas;

import java.util.ArrayList;
import java.util.List;

import matcher.entities.Visibility;

public class InsertMethodAction extends InsertAction {

	private Visibility visibility;
	
	private List<Holder> compatibles;

	public InsertMethodAction(Insertable insertedEntity, Holder holderEntity, 
			Visibility visibility) {
		super(insertedEntity, holderEntity);
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
		StringBuilder result = new StringBuilder("insert " + visibility.toString().toLowerCase() 
				+ " method ");
		result.append(getInsertedEntityQualifiedName() + " in ");
		result.append(getHolderEntityQualifiedName());
		if(!compatibles.isEmpty()) {
			result.append("\n");
			for(Holder h: compatibles) {
				result.append(getInsertedEntityQualifiedName() + " compatible with ");
				result.append(h.getQualifiedName() + "\n");
			}			
		}
		return result.toString();
	}

}
