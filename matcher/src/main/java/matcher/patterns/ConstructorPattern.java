package matcher.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.ConstructorInstance;
import matcher.entities.Visibility;

public class ConstructorPattern {

	private FreeVariable freeVariable;
	
	private Visibility visibility;
	
	private List<FreeVariable> dependencies;
	
	private ConstructorInstance lastMatchedConstructor;

	public ConstructorPattern(FreeVariable freeVariable, Visibility visibility) {
		super();
		this.freeVariable = freeVariable;
		this.visibility = visibility;
		dependencies = new ArrayList<>();
	}
	
	public ConstructorPattern(ConstructorPattern constructorPattern) {
		super();
		this.freeVariable = new FreeVariable(constructorPattern.freeVariable);
		this.visibility = constructorPattern.visibility;
		this.dependencies = new ArrayList<>();
		for(FreeVariable dependency: constructorPattern.dependencies) {
			this.dependencies.add(new FreeVariable(dependency));
		}
		lastMatchedConstructor = constructorPattern.lastMatchedConstructor;
	}
	
	public String getLastMatchedConstructorIdentifier() {
		if(lastMatchedConstructor == null)
			return null;
		return lastMatchedConstructor.getName() + 
				lastMatchedConstructor.getDescriptor();
	}
	
	public Visibility getVisibility() {
		return visibility;
	}
	
	public void addDependency(FreeVariable v) {
		dependencies.add(v);
	}
	
	public boolean hasInvocations() {
		return !dependencies.isEmpty();
	}
	
	public FreeVariable getFreeVariable() {
		return freeVariable;
	}
	
	public int getVariableId() {
		return freeVariable.getId();
	}
	
	public List<Integer> getInvocationsVariableIds(){
		return dependencies.stream()
				   .map(FreeVariable::getId)
				   .collect(Collectors.toList());
	}
	
	public boolean isVariableId(int id) {
		return freeVariable.isId(id);
	}
	
	public boolean hasVariableId(int id) {
		return isVariableId(id) ||
			   dependencies.stream().anyMatch(v -> v.isId(id));
	}
	
	public void setVariableValue(int id, String value) {
		if(isVariableId(id))
			freeVariable.setValue(value);
		else 
			setVariableValueDependencies(id, value);
	}
	
	public void clean() {
		freeVariable.clean();
		cleanDependencies();
		lastMatchedConstructor = null;
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

	public boolean filled() {
		return freeVariable.hasValue() && dependenciesFilled();
	}

	private boolean dependenciesFilled() {
		return dependencies.stream().allMatch(v -> v.hasValue());
	}
	
	public boolean matches(ConstructorInstance instance) {
		lastMatchedConstructor = instance;
		return filled() && 
				(visibility == null || sameVisibility(instance)) &&  
				sameName(instance) &&
				dependenciesMatch(instance);
	}
	
	private boolean dependenciesMatch(ConstructorInstance instance) {
		return dependencies.stream()
						   .allMatch(v -> instance.dependsOn(v.getValue()));
	}

	private boolean sameName(ConstructorInstance instance) {
		return instance.getQualifiedName().equals(freeVariable.getValue());
	}

	private boolean sameVisibility(ConstructorInstance instance) {
		return instance.getVisibility() == visibility;
	}

	public String toStringDebug(int classVariableId) {
		StringBuilder result = new StringBuilder();
		
		result.append("#" + classVariableId + " has " + 
				(visibility == null?"*":visibility.toString().toLowerCase()));
		result.append(" constructor #" + getVariableId() + "\n");
		
		for(FreeVariable v: dependencies) {
			result.append("#" + getVariableId() + " depends on #" + v.getId() + "\n");
		}
		
		return result.toString();
	}

	public String toStringFilled(String classVal) {
		StringBuilder result = new StringBuilder();
		
		result.append("#" + classVal + " has " + 
				(visibility == null?"*":visibility.toString().toLowerCase()));
		result.append(" constructor #" + freeVariable.getValue() + "\n");
		
		for(FreeVariable v: dependencies) {
			result.append("#" + freeVariable.getValue() + " depends on #" + v.getValue() + "\n");
		}
		
		return result.toString();
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(FreeVariable var: dependencies) {
			result.append("Method $" + getVariableId() + " depends on method $" 
					+ var.getId() + "\n");
		}		
		return result.toString();
	}
}
