package matcher.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.FieldAccessInstance;
import matcher.entities.MethodInstance;
import matcher.entities.MethodInvocationInstance;
import matcher.entities.Visibility;

public class MethodPattern {

	private FreeVariable freeVariable;
	
	private Visibility visibility;
	
	private List<MethodInvocationPattern> invocations;
	
	private List<FieldAccessPattern> fieldAccesses;

	public MethodPattern(FreeVariable freeVariable, Visibility visibility) {
		super();
		this.freeVariable = freeVariable;
		this.visibility = visibility;
		invocations = new ArrayList<>();
		fieldAccesses = new ArrayList<>();
	}
	
	public void addMethodInvocationPattern(MethodInvocationPattern pattern) {
		invocations.add(pattern);
	}
	
	public void addFieldAccessPattern(FieldAccessPattern pattern) {
		fieldAccesses.add(pattern);
	}
	
	public boolean hasFieldAccesses() {
		return !fieldAccesses.isEmpty();
	}
	
	public boolean hasInvocations() {
		return !invocations.isEmpty();
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
	
	public List<Integer> getFieldAccessesVariableIds(){
		return fieldAccesses.stream()
							.map(FieldAccessPattern::getVariableId)
							.collect(Collectors.toList());
	}
	
	public boolean isVariableId(int id) {
		return getVariableId() == id;
	}
	
	private boolean invocationsHasVariableId(int id) {
		return invocations.stream().anyMatch(invocation -> invocation.isVariableId(id));
	}
	
	private boolean fieldAccessesHasVariableId(int id) {
		return fieldAccesses.stream().anyMatch(field -> field.isVariableId(id));
	}
	
	public boolean hasVariableId(int id) {
		return isVariableId(id) ||
			   invocationsHasVariableId(id) ||
			   fieldAccessesHasVariableId(id);
	}
	
	public void setVariableValue(int id, String value) {
		if(isVariableId(id))
			freeVariable.setValue(value);
		else if(fieldAccessesHasVariableId(id))
			setVariableValueFieldAccesses(id, value);
		else
			setVariableValueInvocations(id, value);
	}

	private void setVariableValueInvocations(int id, String value) {
		for(MethodInvocationPattern pattern: invocations) {
			if(pattern.isVariableId(id))
				pattern.setVariableValue(value);
		}
	}

	private void setVariableValueFieldAccesses(int id, String value) {
		for(FieldAccessPattern pattern: fieldAccesses) {
			if(pattern.isVariableId(id))
				pattern.setVariableValue(value);
		}
		
	}
	
	public boolean filled() {
		return freeVariable.hasValue() && invocationsFilled() && fieldAccessesFilled();
	}

	private boolean fieldAccessesFilled() {
		return fieldAccesses.stream().allMatch(FieldAccessPattern::filled);
	}

	private boolean invocationsFilled() {
		return invocations.stream().allMatch(MethodInvocationPattern::filled);
	}
	
	public boolean matches(MethodInstance instance) {
		return filled() &&
			   (visibility == null || sameVisibility(instance)) &&
			   sameName(instance) &&
			   invocationsMatch(instance) &&
			   fieldAccessesMatch(instance);
	}

	private boolean fieldAccessesMatch(MethodInstance instance) {
		return fieldAccesses.stream()
							.allMatch(a -> fieldAccessesMatchesOne(a, instance.getFieldAccesses()));
	}

	private boolean fieldAccessesMatchesOne(FieldAccessPattern access, 
			List<FieldAccessInstance> fieldAccesses) {
		for(FieldAccessInstance fAccess: fieldAccesses) {
			if(access.matches(fAccess))
				return true;
		}
		return false;
	}

	private boolean invocationsMatch(MethodInstance instance) {
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

	private boolean sameName(MethodInstance instance) {
		return instance.getQualifiedName().equals(freeVariable.getValue());
	}

	private boolean sameVisibility(MethodInstance instance) {
		return instance.getVisibility() == visibility;
	}
}
