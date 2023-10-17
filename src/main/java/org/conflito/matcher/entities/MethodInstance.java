package org.conflito.matcher.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.conflito.matcher.entities.deltas.Visible;

/**
 * A class representing a method in the system's domain
 * 
 * @author Nuno Castanho
 *
 */
public class MethodInstance implements Visible {

	private String name;
	
	private Visibility visibility;
	
	private Type returnType;
	
	private List<Type> parameters;
		
	private List<FieldAccessInstance> fieldAccesses;
	
	private List<MethodInstance> directDependencies;
	
	/**
	 * Creates an instance of MethodInstance
	 * @param name
	 * 			the method's name
	 * @param visibility
	 * 			the method's visibility
	 * @param returnType
	 * 			the method's return type
	 */
	public MethodInstance(String name, Visibility visibility, Type returnType) {
		super();
		this.name = name;
		this.visibility = visibility;
		this.returnType = returnType;
		this.parameters = new ArrayList<>();
		this.fieldAccesses = new ArrayList<>();
		this.directDependencies = new ArrayList<>();
	}

	/**
	 * Creates an instance of MethodInstance
	 * @param name
	 * 			the method's name
	 * @param visibility
	 * 			the method's visibility
	 * @param returnType
	 * 			the method's return type
	 * @param parameters
	 * 			the method's parameters' types
	 */
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
	
	/**
	 * Creates an instance of MethodInstance from another instance
	 * @param m
	 * 			the other instance
	 */
	public MethodInstance(MethodInstance m) {
		this.name = m.name;
		this.visibility = m.visibility;
		this.returnType = new Type(m.returnType);
		
		this.parameters = new ArrayList<>();
		this.fieldAccesses = new ArrayList<>();
		this.directDependencies = new ArrayList<>();
		
		for(Type t: m.parameters) {
			this.parameters.add(new Type(t));
		}
		
		for(FieldAccessInstance f: m.fieldAccesses) {
			this.fieldAccesses.add(new FieldAccessInstance(f));
		}
		
		for(MethodInstance d: m.directDependencies) {
			this.directDependencies.add(d);
		}
	}
	
	/**
	 * Get this method's direct dependencies
	 * @return this method's direct dependencies
	 */
	public List<MethodInstance> getDirectDependencies(){
		return directDependencies;
	}
	
	/**
	 * Adds a direct dependency to this method
	 * @param m
	 * 			the new method dependency 
	 */
	public void addDirectDependency(MethodInstance m) {
		directDependencies.add(m);
	}
	
	/**
	 * Checks if this method accesses any fields
	 * @return true if this method accesses any fields; false otherwise
	 */
	public boolean hasFieldAccesses() {
		return !fieldAccesses.isEmpty();
	}
	
	/**
	 * Checks if this method has direct dependencies
	 * @return true if this method has direct dependencies; false otherwise
	 */
	public boolean hasDependencies() {
		return !directDependencies.isEmpty();
	}
	
	/**
	 * Checks if this method depends on a method with a given name
	 * @param methodName
	 * 			the name of the method to test dependency
	 * @return true if this method depends (directly or not) to a method with 
	 * 		the name methodName; false otherwise
	 */
	public boolean dependsOn(String methodName) {
		return dependsOn(methodName, new HashSet<>());
	}
	
	/**
	 * Auxiliary method to the dependsOn(String) method
	 * @param methodName
	 * 			the name of the method to test dependency
	 * @param visited
	 * 			names of the methods already visited to avoid loops
	 * @return true if this method depends (directly or not) to a method with 
	 * 		the name methodName; false otherwise
	 */
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

	/**
	 * Get this method's name
	 * @return this method's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get this method's qualified name (i.e., its signature)
	 * @return this method's qualified name
	 */
	public String getQualifiedName() {
		return getSimpleSignature();
	}
	
	/**
	 * Calculates this method's descriptor as per the Java format
	 * 
	 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">
	 * 		https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html</a>
	 * @return this method's descriptor
	 */
	public String getDescriptor() {
		StringBuilder result = new StringBuilder("(");
		
		parameters.stream()
			.map(Type::getDescriptor)
			.forEach(s -> result.append(s));
		
		result.append(")" + returnType.getDescriptor());
		return result.toString();
	}
	
	/**
	 * Get this method's signature
	 * 
	 * A method's signature is its name followed by its parameter types
	 * @return this method's signature
	 */
	private String getSimpleSignature() {
		return getName() + parametersToString();
	}

	/**
	 * Calculates the string form for this method's parameters
	 * @return the string of the parameters of this method
	 */
	private String parametersToString() {
		StringBuilder result = new StringBuilder(getParameters().toString());
		result.replace(0, 1, "(");
		result.replace(result.length()-1, result.length(), ")");
		return result.toString();
	}
	
	/**
	 * Get this method's visibility
	 * @return this method's visibility
	 */
	public Visibility getVisibility() {
		return visibility;
	}

	/**
	 * Get this method's return type
	 * @return this method's return type
	 */
	public Type getReturnType() {
		return returnType;
	}

	/**
	 * Get this method's parameters
	 * @return thie method's parameters
	 */
	public List<Type> getParameters() {
		return parameters;
	}

	/**
	 * Get the field accesses in this method
	 * @return the fields accesses in thie method
	 */
	public List<FieldAccessInstance> getFieldAccesses() {
		return fieldAccesses;
	}
	
	/**
	 * Get the qualified names of the accessed fields in this method
	 * @return the qualified names of the accessed fields in this method
	 */
	public List<String> getFieldAccessesQualifiedNames() {
		return fieldAccesses.stream()
							.map(FieldAccessInstance::getQualifiedName)
							.collect(Collectors.toList());
	}

	/**
	 * Get the qualified names of the methods invoked in this method
	 * @return the qualified names of the methods invoked in this method
	 */
	public List<String> getInvocationsQualifiedNames() {
		return directDependencies.stream()
								  	  .map(MethodInstance::getQualifiedName)
								  	  .collect(Collectors.toList());
	}
	
	/**
	 * Checks if this method is compatible with another one
	 * <br><br>
	 * A method M is compatible with a methond N if:<br>
	 * - M and N have the same name <b>and</b><br>
	 * - M and N have the same number of parameters <b>and</b><br>
	 * - For 0<=i<=X, where X is the number of parameters,
	 * the i-th parameter of M is sub-type of the i-th parameter of N  
	 * 
	 * @param m
	 * 			the other method
	 * @return true if this method is compatible with m; false otherwise
	 */
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
	
	/**
	 * Adds a parameter's type to this method
	 * @param type
	 * 			the type of the parameter
	 */
	public void addParameter(Type type) {
		this.parameters.add(type);
	}
	
	/**
	 * Adds a field access to this method
	 * @param access
	 * 			the field access
	 */
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
