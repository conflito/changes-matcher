package matcher.patterns;

import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.ChangeInstance;
import matcher.patterns.deltas.DeltaPattern;

public class ConflictPattern {
	
	private BasePattern basePattern;
	private DeltaPattern firstDelta;
	private DeltaPattern secondDelta;

	public ConflictPattern(BasePattern basePattern, DeltaPattern firstDelta, DeltaPattern secondDelta) {
		super();
		this.basePattern = basePattern;
		this.firstDelta = firstDelta;
		this.secondDelta = secondDelta;
	}

	public boolean hasInvocations() {
		return basePattern.hasInvocations();
	}

	public boolean hasFieldAccesses() {
		return basePattern.hasFieldAccesses();
	}

	public boolean hasSuperClasses() {
		return basePattern.hasSuperClass();
	}

	public boolean hasMethods() {
		return basePattern.hasMethods();
	}

	public boolean hasFields() {
		return basePattern.hasFields();
	}

	public boolean hasConstructors() {
		return basePattern.hasConstructors();
	}

	public boolean hasCompatibleMethods() {
		return basePattern.hasCompatible();
	}
	
	public boolean hasInsertActions() {
		return firstDelta.hasInsertActions() || secondDelta.hasInsertActions();
	}
	
	public boolean hasInsertMethodActions() {
		return firstDelta.hasInsertMethodActions() || secondDelta.hasInsertMethodActions();
	}
	
	public boolean hasInsertConstructorActions() {
		return firstDelta.hasInsertConstructorActions() || 
				secondDelta.hasInsertConstructorActions();
	}
	
	public boolean hasInsertFieldActions() {
		return firstDelta.hasInsertFieldActions() || secondDelta.hasInsertFieldActions();
	}
	
	public boolean hasInsertFieldAccessActions() {
		return firstDelta.hasInsertFieldAccessActions() 
				|| secondDelta.hasInsertFieldAccessActions();
	}
	
	public boolean hasDeleteActions() {
		return firstDelta.hasDeleteActions() || secondDelta.hasDeleteActions();
	}
	
	public boolean hasUpdateActions() {
		return firstDelta.hasUpdateActions() || secondDelta.hasUpdateActions();
	}
	
	public boolean hasVisibilityActions() {
		return firstDelta.hasVisibilityActions() || secondDelta.hasVisibilityActions();
	}
	
	public boolean matches(ChangeInstance instance) {
		return basePattern.filled() && firstDelta.filled() && secondDelta.filled() &&
			   basePattern.matches(instance.getBaseInstance()) &&
			   deltasMatch(instance);
	}

	private boolean deltasMatch(ChangeInstance instance) {
		return (firstDelta.matches(instance.getFirstDelta()) 
					&& secondDelta.matches(instance.getSecondDelta())) ||
			   (firstDelta.matches(instance.getSecondDelta()) 
					&& secondDelta.matches(instance.getFirstDelta()));
	}
	
	public void setVariableValue(int id, String value) {
		basePattern.setVariableValue(id, value);
		firstDelta.setVariableValue(id, value);
		secondDelta.setVariableValue(id, value);
	}
	
	public String toStringDebug() {
		return basePattern.toStringDebug() + "\n----------------\n" 
			  + firstDelta.toStringDebug() + "\n----------------\n" 
			  + secondDelta.toString();
	}
	
	public List<Integer> getFieldsVariableIds(){
		List<Integer> result = basePattern.getFieldsVariableIds();
		return result;
	}

	public List<Integer> getMethodVariableIds() {
		return basePattern.getMethodsVariableIds();
	}

	public List<Integer> getConstructorsVariableIds() {
		return basePattern.getConstructorsVariableIds();
	}

	public List<Integer> getClassVariableIds() {
		return basePattern.getClassVariableIds();
	}

	public List<Integer> getDeltaFieldsVariableIds() {
		List<Integer> firstDeltaFields = firstDelta.getFieldsVariableIds();
		List<Integer> secondDeltaFields = secondDelta.getFieldsVariableIds();
		firstDeltaFields.addAll(secondDeltaFields);
		return firstDeltaFields.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getDeltaMethodsVariableIds() {
		List<Integer> firstDeltaMethods = firstDelta.getMethodsVariableIds();
		List<Integer> secondDeltaMethods = secondDelta.getMethodsVariableIds();
		firstDeltaMethods.addAll(secondDeltaMethods);
		return firstDeltaMethods.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getDeltaConstructorsVariableIds() {
		List<Integer> firstDeltaConstructors = firstDelta.getConstructorsVariableIds();
		List<Integer> secondDeltaConstructors = secondDelta.getConstructorsVariableIds();
		firstDeltaConstructors.addAll(secondDeltaConstructors);
		return firstDeltaConstructors.stream().distinct().collect(Collectors.toList());
	}



}
