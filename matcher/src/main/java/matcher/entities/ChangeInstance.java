package matcher.entities;

import java.util.List;
import java.util.stream.Collectors;

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
	
	public List<String> getInvocationsQualifiedNames() {
		return baseInstance.getInvocationsQualifiedNames();
	}	

	public List<String> getConstructorsQualifiedNames() {
		return baseInstance.getConstructorsQualifiedNames();
	}

	public List<String> getClassQualifiedNames() {
		return baseInstance.getClassQualifiedNames();
	}

	public List<String> getDeltaFieldsQualifiedNames() {
		List<String> firstDeltaFields = firstDelta.getFieldsQualifiedNames();
		List<String> secondDeltaFields = secondDelta.getFieldsQualifiedNames();
		firstDeltaFields.addAll(secondDeltaFields);
		return firstDeltaFields.stream().distinct().collect(Collectors.toList());
	}

	public List<String> getDeltaMethodsQualifiedNames() {
		List<String> firstDeltaMethods = firstDelta.getMethodsQualifiedNames();
		List<String> secondDeltaMethods = secondDelta.getMethodsQualifiedNames();
		firstDeltaMethods.addAll(secondDeltaMethods);
		return firstDeltaMethods.stream().distinct().collect(Collectors.toList());
	}

	public List<String> getDeltaConstructorsQualifiedNames() {
		List<String> firstDeltaConstructors = firstDelta.getConstructorsQualifiedNames();
		List<String> secondDeltaConstructors = secondDelta.getConstructorsQualifiedNames();
		firstDeltaConstructors.addAll(secondDeltaConstructors);
		return firstDeltaConstructors.stream().distinct().collect(Collectors.toList());
	}

	public List<String> getDeltaInvocationsQualifiedNames() {
		List<String> firstDeltaInvocations = firstDelta.getInvocationsQualifiedNames();
		List<String> secondDeltaInvocations = secondDelta.getInvocationsQualifiedNames();
		firstDeltaInvocations.addAll(secondDeltaInvocations);
		return firstDeltaInvocations.stream().distinct().collect(Collectors.toList());
	}
	
}
