package matcher.entities;

import java.util.ArrayList;
import java.util.List;

public class BaseInstance {

	private List<ClassInstance> classInstances;
	
	public BaseInstance() {
		this.classInstances = new ArrayList<>();
	}
	
	public void addClassInstance(ClassInstance classInstance) {
		classInstances.add(classInstance);
	}
	
	public String toStringDebug() {
		StringBuilder result = new StringBuilder();
		for(ClassInstance c: classInstances) {
			result.append(c.toStringDebug() + "\n");
		}
		return result.toString();
	}
	
}
