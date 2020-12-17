package matcher.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.ConstructorInstance;
import matcher.entities.MethodInvocationInstance;
import matcher.entities.Visibility;

public class ConstructorPattern {

	private FreeVariable freeVariable;
	
	private Visibility visibility;
	
	private List<MethodInvocationPattern> invocations;

	public ConstructorPattern(FreeVariable freeVariable, Visibility visibility) {
		super();
		this.freeVariable = freeVariable;
		this.visibility = visibility;
		invocations = new ArrayList<>();
	}
	
	public void addMethodInvocationPattern(MethodInvocationPattern pattern) {
		invocations.add(pattern);
	}
	
	public FreeVariable getFreeVariable() {
		return freeVariable;
	}
	
	public int getVariableId() {
		return freeVariable.getId();
	}
	
	public List<Integer> getInvocationsVariableIds(){
		return invocations.stream()
						  .map(MethodInvocationPattern::getVariableId)
						  .collect(Collectors.toList());
	}
	
	public boolean isVariableId(int id) {
		return getVariableId() == id;
	}
	
	public boolean hasVariableId(int id) {
		return invocations.stream().anyMatch(invocation -> invocation.isVariableId(id));
	}
	
	public void setVariableValue(int id, String value) {
		if(isVariableId(id))
			freeVariable.setValue(value);
		else 
			setVariableValueInvocations(id, value);
	}
	
	private void setVariableValueInvocations(int id, String value) {
		for(MethodInvocationPattern pattern: invocations) {
			if(pattern.isVariableId(id))
				pattern.setVariableValue(value);
		}
	}

	public boolean filled() {
		return freeVariable.hasValue() && invocationsFilled();
	}

	private boolean invocationsFilled() {
		return invocations.stream().allMatch(MethodInvocationPattern::filled);
	}
	
	public boolean matches(ConstructorInstance instance) {
		return filled() && 
				(visibility == null || sameVisibility(instance)) &&  
				sameName(instance) &&
				invocationsMatch(instance);
	}

	private boolean invocationsMatch(ConstructorInstance instance) {
		return invocations.stream()
						  .allMatch(i -> invocationMatchesOne(i, instance.getInvocations()));
	}
	
	private boolean invocationMatchesOne(MethodInvocationPattern invocation, 
			List<MethodInvocationInstance> invocations) {
		for(MethodInvocationInstance i: invocations) {
			if(invocation.matches(i))
				return true;
		}
		return false;
	}

	private boolean sameName(ConstructorInstance instance) {
		return instance.getQualifiedName().equals(freeVariable.getValue());
	}

	private boolean sameVisibility(ConstructorInstance instance) {
		return instance.getVisibility() == visibility;
	}
}
