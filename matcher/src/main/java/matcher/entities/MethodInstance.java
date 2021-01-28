package matcher.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import matcher.entities.deltas.Deletable;
import matcher.entities.deltas.Holder;
import matcher.entities.deltas.Insertable;
import matcher.entities.deltas.Updatable;
import matcher.entities.deltas.Visible;

public class MethodInstance implements Insertable, Deletable, Visible, Holder, Updatable{

	private String name;
	
	private Visibility visibility;
	
	private Type returnType;
	
	private List<Type> parameters;
		
	private List<FieldAccessInstance> fieldAccesses;
	
	private List<MethodInstance> directDependencies;
	
	public MethodInstance(String name, Visibility visibility, Type returnType) {
		super();
		this.name = name;
		this.visibility = visibility;
		this.returnType = returnType;
		this.parameters = new ArrayList<>();
		this.fieldAccesses = new ArrayList<>();
		this.directDependencies = new ArrayList<>();
	}

	public MethodInstance(String name, Visibility visibility, Type returnType, 
			List<Type> parameters) {
		super();
		this.name = name;
		this.visibility = visibility;
		this.returnType = returnType;
		this.parameters = parameters;
		this.fieldAccesses = new ArrayList<>();
		this.directDependencies = new ArrayList<>();
	}
	
	public List<MethodInstance> getDirectDependencies(){
		return directDependencies;
	}
	
	public void addDirectDependency(MethodInstance m) {
		directDependencies.add(m);
	}
	
	public boolean dependsOn(String methodName) {
		return dependsOn(methodName, new HashSet<>());
	}
	
	private boolean dependsOn(String methodName, Set<String> visited) {
		if(!visited.contains(getQualifiedName())) {
			visited.add(getQualifiedName());
			boolean result = 
					directDependencies.stream()
									  .anyMatch(m -> m.getQualifiedName().equals(methodName));
			if(!result) {
				for(MethodInstance m: directDependencies) {
					if(m.dependsOn(methodName, visited)) {
						result = true;
					}
				}
			}
			return result;
		}
		return false;
	}

	public String getName() {
		return name;
	}
	
	public String getQualifiedName() {
		return getSimpleSignature();
	}
	
	private String getSimpleSignature() {
		return getName() + parametersToString();
	}

	private String parametersToString() {
		StringBuilder result = new StringBuilder(getParameters().toString());
		result.replace(0, 1, "(");
		result.replace(result.length()-1, result.length(), ")");
		return result.toString();//getParameters().toString().replace("[", "(").replace("]", ")");
	}
	
	public Visibility getVisibility() {
		return visibility;
	}

	public Type getReturnType() {
		return returnType;
	}

	public List<Type> getParameters() {
		return parameters;
	}

	public List<FieldAccessInstance> getFieldAccesses() {
		return fieldAccesses;
	}
	
	public List<String> getFieldAccessesQualifiedNames() {
		return fieldAccesses.stream()
							.map(FieldAccessInstance::getQualifiedName)
							.collect(Collectors.toList());
	}

	public List<String> getInvocationsQualifiedNames() {
		return directDependencies.stream()
								  	  .map(MethodInstance::getQualifiedName)
								  	  .collect(Collectors.toList());
	}
	
	public boolean isCompatibleWith(MethodInstance m) {
		if(m == null || !getName().equals(m.getName())) {
			return false;
		}
		List<Type> thisParameters = getParameters();
		List<Type> mParameters = m.getParameters();
		if(thisParameters.size() != mParameters.size()) {
			return false;
		}
		for(int i = 0; i < thisParameters.size(); i++) {
			Type t1 = thisParameters.get(i);
			if(t1.isPrimitive())
				t1 = Type.primitiveToWrapper(t1);
			Type t2 = mParameters.get(i);
			if(t2.isPrimitive())
				t2 = Type.primitiveToWrapper(t2);
			if(!t1.isSubtypeOf(t2))
				return false;
		}
		return true;
	}
	
	public void addParameter(Type type) {
		this.parameters.add(type);
	}
	
	public void addFieldAccess(FieldAccessInstance access) {
		this.fieldAccesses.add(access);
	}
	
	public boolean equals(Object o) {
		return (this == o) || (o instanceof MethodInstance && equalsMethodInstance((MethodInstance)o));
	}

	private boolean equalsMethodInstance(MethodInstance o) {
		return getQualifiedName().equals(o.getQualifiedName());
	}
	
	public int hashCode() {
		return getQualifiedName().hashCode();
	}

	public String toString() {
		return getQualifiedName();
	}

}
