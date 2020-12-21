package matcher.patterns;

import matcher.entities.ChangeInstance;
import matcher.patterns.deltas.DeltaPattern;

public class ConflictPattern {
	
	private BasePattern basePattern;
	private DeltaPattern firstDelta;
	private DeltaPattern secondDelta;

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

}
