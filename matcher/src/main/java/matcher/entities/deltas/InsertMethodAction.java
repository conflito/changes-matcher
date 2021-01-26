package matcher.entities.deltas;

import java.util.ArrayList;
import java.util.List;

import matcher.entities.ClassInstance;
import matcher.entities.MethodInstance;

public class InsertMethodAction extends InsertAction {
	
	private MethodInstance insertedMethod;
	
	private ClassInstance holderClass;
	
	private List<MethodInstance> compatibles;
	
	public InsertMethodAction(MethodInstance insertedMethod, ClassInstance holderClass) {
		super();
		this.insertedMethod = insertedMethod;
		this.holderClass = holderClass;
		this.compatibles = new ArrayList<>();
	}
	
	public MethodInstance getInsertedMethod() {
		return insertedMethod;
	}
	
	public String getInsertedMethodQualifiedName() {
		return insertedMethod.getQualifiedName();
	}

	public ClassInstance getHolderClass() {
		return holderClass;
	}

	public List<MethodInstance> getCompatibles() {
		return compatibles;
	}

	public void addCompatible(MethodInstance compatible) {
		compatibles.add(compatible);
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("insert " + 
				insertedMethod.getVisibility().toString().toLowerCase() + " method ");
		result.append(insertedMethod.getQualifiedName() + " in ");
		result.append(holderClass.getQualifiedName());
		result.append("\n" + insertedMethod.getDirectDependencies());
		if(!compatibles.isEmpty()) {
			result.append("\n");
			for(MethodInstance m: compatibles) {
				result.append(insertedMethod.getQualifiedName() + " compatible with ");
				result.append(m.getQualifiedName() + "\n");
			}			
		}
		return result.toString();
	}

}
