package matcher.entities;

import java.util.List;

import matcher.entities.deltas.DeltaInstance;

public class ChangeInstance {

	private BaseInstance baseInstance;
	private DeltaInstance firstDelta;
	private DeltaInstance secondDelta;
	
	public ChangeInstance(BaseInstance baseInstance, DeltaInstance firstDelta, DeltaInstance secondDelta) {
		super();
		this.baseInstance = baseInstance;
		this.firstDelta = firstDelta;
		this.secondDelta = secondDelta;
	}

	public BaseInstance getBaseInstance() {
		return baseInstance;
	}

	public DeltaInstance getFirstDelta() {
		return firstDelta;
	}

	public DeltaInstance getSecondDelta() {
		return secondDelta;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(baseInstance.toStringDebug() + "\n---------\n");
		result.append(firstDelta.toString() + "\n---------\n");
		result.append(secondDelta.toString() + "\n---------\n");
		return result.toString();
	}
	
	public List<String> getFieldsQualifiedNames(){
		return baseInstance.getFieldsQualifiedNames();
	}

	public List<String> getMethodsQualifiedNames() {
		return baseInstance.getMethodsQualifiedNames();
	}

	public List<String> getConstructorsQualifiedNames() {
		return baseInstance.getConstructorsQualifiedNames();
	}

	public List<String> getClassQualifiedNames() {
		return baseInstance.getClassQualifiedNames();
	}	
	
}
