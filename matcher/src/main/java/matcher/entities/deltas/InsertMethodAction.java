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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((holderClass == null) ? 0 : holderClass.hashCode());
		result = prime * result + ((insertedMethod == null) ? 0 : insertedMethod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InsertMethodAction))
			return false;
		InsertMethodAction other = (InsertMethodAction) obj;
		if (holderClass == null) {
			if (other.holderClass != null)
				return false;
		} else if (!holderClass.equals(other.holderClass))
			return false;
		if (insertedMethod == null) {
			if (other.insertedMethod != null)
				return false;
		} else if (!insertedMethod.equals(other.insertedMethod))
			return false;
		return true;
	}

}
