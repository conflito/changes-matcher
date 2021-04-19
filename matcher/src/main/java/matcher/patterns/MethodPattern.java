package matcher.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.FieldAccessInstance;
import matcher.entities.MethodInstance;
import matcher.entities.Visibility;

public class MethodPattern {

	private FreeVariable freeVariable;
	
	private Visibility visibility;
		
	private List<FieldAccessPattern> fieldAccesses;
	
	private List<FreeVariable> dependencies;
	
	private MethodInstance lastMatchedMethod;

	public MethodPattern(FreeVariable freeVariable, Visibility visibility) {
		super();
		this.freeVariable = freeVariable;
		this.visibility = visibility;
		fieldAccesses = new ArrayList<>();
		dependencies = new ArrayList<>();
	}
	
	public MethodPattern(MethodPattern methodPattern) {
		super();
		this.freeVariable = new FreeVariable(methodPattern.freeVariable);
		this.visibility = methodPattern.visibility;
		this.fieldAccesses = new ArrayList<>();
		this.dependencies = new ArrayList<>();
		for(FieldAccessPattern fieldAccessPattern: methodPattern.fieldAccesses) {
			this.fieldAccesses.add(new FieldAccessPattern(fieldAccessPattern));
		}
		for(FreeVariable dependency: methodPattern.dependencies) {
			this.dependencies.add(new FreeVariable(dependency));
		}
		lastMatchedMethod = methodPattern.lastMatchedMethod;
	}
	
	public String getLastMatchedMethodIdentifier() {
		if(lastMatchedMethod == null)
			return null;
		return getLastMatchedMethodName() + getLastMatchedMethodDescriptor();
	}
	
	private String getLastMatchedMethodName() {
		return lastMatchedMethod.getName();
	}
	
	private String getLastMatchedMethodDescriptor() {
		return lastMatchedMethod.getDescriptor();
	}
	
	public void addDependency(FreeVariable v) {
		dependencies.add(v);
	}
	
	public void addFieldAccessPattern(FieldAccessPattern pattern) {
		fieldAccesses.add(pattern);
	}
	
	public boolean hasFieldAccesses() {
		return !fieldAccesses.isEmpty();
	}
	
	public boolean hasInvocations() {
		return !dependencies.isEmpty();
	}
	
	public FreeVariable getFreeVariable() {
		return freeVariable;
	}
	
	public Visibility getVisibility() {
		return visibility;
	}

	public int getVariableId() {
		return freeVariable.getId();
	}
	
	public List<Integer> getInvocationsVariableIds(){
		return dependencies.stream()
						   .map(FreeVariable::getId)
						   .collect(Collectors.toList());
	}
	
	public List<Integer> getDependenciesVariableIds(){
		return dependencies.stream()
						   .map(FreeVariable::getId)
						   .collect(Collectors.toList());
	}
	
	public List<Integer> getFieldAccessesVariableIds(){
		return fieldAccesses.stream()
							.map(FieldAccessPattern::getVariableId)
							.collect(Collectors.toList());
	}
	
	public boolean isVariableId(int id) {
		return freeVariable.isId(id);
	}
	
	private boolean dependenciesHasVariableId(int id) {
		return dependencies.stream().anyMatch(d -> d.isId(id));
	}
	
	private boolean fieldAccessesHasVariableId(int id) {
		return fieldAccesses.stream().anyMatch(field -> field.isVariableId(id));
	}
	
	public boolean hasVariableId(int id) {
		return isVariableId(id) ||
			   dependenciesHasVariableId(id) ||
			   fieldAccessesHasVariableId(id);
	}
	
	public void setVariableValue(int id, String value) {
		if(isVariableId(id))
			freeVariable.setValue(value);
		else if(fieldAccessesHasVariableId(id))
			setVariableValueFieldAccesses(id, value);
		else if(dependenciesHasVariableId(id))
			setVariableValueDependencies(id, value);
			
	}
	
	public void clean() {
		freeVariable.clean();
		cleanDependencies();
		cleanFieldAccesses();
		lastMatchedMethod = null;
	}

	private void cleanFieldAccesses() {
		for(FieldAccessPattern f: fieldAccesses) {
			f.clean();
		}
	}
	
	private void cleanDependencies() {
		dependencies.forEach(v -> v.clean());
	}

	private void setVariableValueDependencies(int id, String value) {
		for(FreeVariable v: dependencies) {
			if(v.isId(id))
				v.setValue(value);
		}
	}

	private void setVariableValueFieldAccesses(int id, String value) {
		for(FieldAccessPattern pattern: fieldAccesses) {
			pattern.setVariableValue(id, value);
		}
		
	}
	
	public boolean filled() {
		return freeVariable.hasValue() && dependenciesFilled() 
				&& fieldAccessesFilled();
	}

	private boolean fieldAccessesFilled() {
		return fieldAccesses.stream().allMatch(FieldAccessPattern::filled);
	}
	
	private boolean dependenciesFilled() {
		return dependencies.stream().allMatch(v -> v.hasValue());
	}
	
	public boolean matches(MethodInstance instance) {
		lastMatchedMethod = instance;
		return filled() &&
			   (visibility == null || sameVisibility(instance)) &&
			   sameName(instance) &&
			   dependenciesMatch(instance) &&
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

	private boolean dependenciesMatch(MethodInstance instance) {
		return dependencies.stream()
						   .allMatch(v -> instance.dependsOn(v.getValue()));
	}

	private boolean sameName(MethodInstance instance) {
		return instance.getQualifiedName().equals(freeVariable.getValue());
	}

	private boolean sameVisibility(MethodInstance instance) {
		return instance.getVisibility() == visibility;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(FreeVariable var: dependencies) {
			result.append("Method $" + getVariableId() + " depends on method $" 
					+ var.getId() + "\n");
		}
		
		for(FieldAccessPattern f: fieldAccesses) {
			result.append("Method $" + getVariableId() + " ");
			if(f.isAnyAccess())
				result.append("uses ");
			else if(f.isFieldRead())
				result.append("reads ");
			else
				result.append("writes ");
			result.append("field $" + f.getVariableId() + "\n");
		}
		
		return result.toString();
	}
}
