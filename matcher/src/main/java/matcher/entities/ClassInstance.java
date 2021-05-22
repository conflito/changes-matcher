package matcher.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import matcher.utils.Pair;

/**
 * A class representing a class in the system's domain
 * 
 * @author Nuno Castanho
 *
 */
public class ClassInstance {
	
	private String name;
	
	private String qualifiedName;
	
	private List<FieldInstance> fields;
	
	private List<MethodInstance> methods;
	
	private List<ConstructorInstance> constructors;
	
	private ClassInstance superClass;
	
	private List<InterfaceImplementationInstance> interfaces;
	
	private Map<MethodInstance, List<MethodInstance>> compatibleMethods;

	/**
	 * Creates an instance of ClassInstance
	 */
	public ClassInstance() {
		this.fields = new ArrayList<>();
		this.methods = new ArrayList<>();
		this.constructors = new ArrayList<>();
		this.interfaces = new ArrayList<>();
		this.compatibleMethods = new HashMap<>();
	}
	
	/**
	 * Creates an instance of ClassInstance
	 * @param name
	 * 			the class's simple name
	 * @param qualifiedName
	 * 			the class' qualified name
	 */
	public ClassInstance(String name, String qualifiedName) {
		super();
		this.name = name;
		this.qualifiedName = qualifiedName;
		
		this.fields = new ArrayList<>();
		this.methods = new ArrayList<>();
		this.constructors = new ArrayList<>();
		this.interfaces = new ArrayList<>();
		this.compatibleMethods = new HashMap<>();
	}

	/**
	 * Get this class' simple name
	 * @return this class' simple name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get this class' qualified name
	 * @return this class' qualified name
	 */
	public String getQualifiedName() {
		return qualifiedName;
	}

	/**
	 * Get this class' superclass
	 * @return an optional with this class' superclass if it has one; 
	 * 		an empty optional otherwise 
	 */
	public Optional<ClassInstance> getSuperClass() {
		return Optional.ofNullable(superClass);
	}
	
	/**
	 * Checks if this class has a superclass
	 * @return true if this class has a superclass; false otherwise
	 */
	public boolean hasSuperClass() {
		return superClass != null;
	}

	/**
	 * Sets this class' superclass
	 * @param superClass
	 * 			the superclass for this class
	 */
	public void setSuperClass(ClassInstance superClass) {
		this.superClass = superClass;
	}

	/**
	 * Get this class' fields
	 * @return the fields of this class
	 */
	public List<FieldInstance> getFields() {
		return fields;
	}
	
	/**
	 * Checks if this class has fields
	 * @return true if this class has fields; false otherwise
	 */
	public boolean hasFields() {
		return !fields.isEmpty();
	}

	/**
	 * Get this class's methods
	 * @return the methods of this class
	 */
	public List<MethodInstance> getMethods() {
		return methods;
	}
	
	/**
	 * Checks if this class has methods
	 * @return true if this class has methods; false otherwise
	 */
	public boolean hasMethods() {
		return !methods.isEmpty();
	}
	
	/**
	 * Checks if the methods in this class have field accesses
	 * @return true if a method of this class has field accesses; false otherwise
	 */
	public boolean hasFieldAccesses() {
		return methods.stream().anyMatch(m -> m.hasFieldAccesses());
	}
	
	/**
	 * Checks if the methods or constructors in this class have method invocations
	 * @return true if a method or constructor of this class has a method invocation;
	 * 		false otherwise
	 */
	public boolean hasInvocations() {
		return methods.stream().anyMatch(m -> m.hasDependencies()) ||
				constructors.stream().anyMatch(c -> c.hasDependencies());
	}

	/**
	 * Get this class' constructors
	 * @return the constructors of this class
	 */
	public List<ConstructorInstance> getConstructors() {
		return constructors;
	}
	
	/**
	 * Checks if this class has constructors
	 * @return true if this class has constructors; false otherwise
	 */
	public boolean hasConstructors() {
		return !constructors.isEmpty();
	}

	/**
	 * Get this class' implemented interfaces
	 * @return the interfaces implemented by this class
	 */
	public List<InterfaceImplementationInstance> getInterfaces() {
		return interfaces;
	}
	
	/**
	 * Checks if this class implements an interface
	 * @return true if this class implements an interface; false otherwise
	 */
	public boolean hasInterfaces() {
		return !interfaces.isEmpty();
	}

	/**
	 * Get the methods in this class compatible with a given method
	 * @param m
	 * 			the method to test compatibility
	 * @return the methods in this class compatible with m
	 */
	public List<MethodInstance> getCompatibles(MethodInstance m){
		List<MethodInstance> result = new ArrayList<>();
		if(compatibleMethods.containsKey(m))
			result = compatibleMethods.get(m);
		return result;
	}
	
	/**
	 * Checks if this class has compatible methods in it
	 * @return true if this class has compatible methods in it; false otherwise
	 */
	public boolean hasCompatibles() {
		return !compatibleMethods.isEmpty();
	}
	
	/**
	 * Get the methods of this class with compatibles in it
	 * @return the methods in this class with compatibles
	 */
	public Iterable<MethodInstance> getMethodsWithCompatibles(){
		return compatibleMethods.keySet();
	}

	/**
	 * Sets the name of this class
	 * @param name
	 * 			the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the qualified name of this class
	 * @param qualifiedName
	 * 		the qualified name to set
	 */
	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}
	
	/**
	 * Adds a field to this class
	 * @param field
	 * 			the field to add
	 */
	public void addField(FieldInstance field) {
		this.fields.add(field);
	}
	
	/**
	 * Adds a method to this class
	 * @param method
	 * 			the method to add
	 */
	public void addMethod(MethodInstance method) {
		this.methods.add(method);
	}
	
	/**
	 * Adds an implementation of an interface to this class
	 * @param i
	 * 			the interface implementation to add
	 */
	public void addInterface(InterfaceImplementationInstance i) {
		this.interfaces.add(i);
	}
	
	/**
	 * Checks if this class has a method with the given name
	 * @param qualifiedName
	 * 			the name to test
	 * @return true if this class has a method with the specified qualified name;
	 * 		false otherwise
	 */
	public boolean hasMethod(String qualifiedName) {
		return methods.stream().anyMatch(m -> m.getQualifiedName().equals(qualifiedName));
	}
	
	/**
	 * Checks if this class implements an interface with the given name
	 * @param interfaceName
	 * 			the name to test
	 * @return true if this class implements an interface with the specified name;
	 * 			false otherwise
	 */
	public boolean hasInterface(String interfaceName) {
		return interfaces.stream().anyMatch(i -> i .getName().equals(interfaceName));
	}
	
	/**
	 * Adds a constructor to this class
	 * @param constructor
	 * 			the constructor to add
	 */
	public void addConstructor(ConstructorInstance constructor) {
		this.constructors.add(constructor);
	}
	
	/**
	 * Register compatibility between two methods
	 * @param subMethod
	 * 			the method that's compatible with topMethod
	 * @param topMethod
	 * 			the method that has a compatible
	 */
	public void addCompatible(MethodInstance subMethod, MethodInstance topMethod) {
		if(!compatibleMethods.containsKey(subMethod))
			compatibleMethods.put(subMethod, new ArrayList<MethodInstance>());
		compatibleMethods.get(subMethod).add(topMethod);
	}
	
	/**
	 * Get the qualified names of this class and its superclasses' fields
	 * @return the qualified names of this class and its superclasses' fields
	 */
	public List<String> getFieldsQualifiedNames(){
		List<String> result = getFields().stream()
						  					 .map(FieldInstance::getQualifiedName)
						  					 .collect(Collectors.toList());
		if(superClass != null)
			result.addAll(superClass.getFieldsQualifiedNames());
		return result;
	}
	
	/**
	 * Get the qualified names of this class and its superclasses's fields' types
	 * @return the qualified names of this class and its superclasses' fields' types
	 */
	public List<String> getFieldTypesQualifiedNames(){
		List<String> result = getFields().stream()
										 .filter(f -> !f.primitiveType())
										 .map(FieldInstance::getTypeName)
										 .collect(Collectors.toList());
		if(superClass != null)
			result.addAll(superClass.getFieldTypesQualifiedNames());
		return result;
	}
	
	/**
	 * Get the qualified names of this class and its superclasses's methods
	 * @return the qualified names of this class and its superclasses' methods
	 */
	public List<String> getMethodsQualifiedNames(){
		List<String> result = getMethodsQualifiedNames(getMethods());
		if(superClass != null)
			result.addAll(superClass.getMethodsQualifiedNames());
		return result;
	}
	
	/**
	 * Get the qualified names of this class and its superclasses's interfaces
	 * @return the qualified names of this class and its superclasses' interfaces
	 */
	public List<String> getInterfacesQualifiedNames(){
		List<String> result = new ArrayList<>();
		for(InterfaceImplementationInstance i: interfaces) {
			result.add(i.getName());
		}
		if(superClass != null) {
			result.addAll(superClass.getInterfacesQualifiedNames());
		}
		return result;
	}
	
	/**
	 * Get the qualified names of this class and its superclasses's accessed fields
	 * @return the qualified names of this class and its superclasses' accessed fields
	 */
	public List<String> getFieldAccessesQualifiedNames() {
		List<String> result = new ArrayList<>();
		for(MethodInstance m: methods) {
			result.addAll(m.getFieldAccessesQualifiedNames());
		}
		if(superClass != null)
			result.addAll(superClass.getFieldAccessesQualifiedNames());
		return result;
	}
	
	/**
	 * Get the qualified names of this class and its superclasses's invocations
	 * @return the qualified names of this class and its superclasses' invocations
	 */
	public List<String> getInvocationsQualifiedNames() {
		List<String> result = new ArrayList<>();
		for(MethodInstance m: methods) {
			result.addAll(m.getInvocationsQualifiedNames());
		}
		for(ConstructorInstance c: constructors) {
			result.addAll(c.getInvocationsQualifiedNames());
		}
		if(superClass != null)
			result.addAll(superClass.getInvocationsQualifiedNames());
		return result;
	}
	
	/**
	 * Get the qualified names of this class and its superclasses
	 * @return the qualified names of this class and its superclasses
	 */
	public List<String> getClassesQualifiedNames() {
		List<String> result = new ArrayList<>();
		result.add(getQualifiedName());
		if(superClass != null)
			result.addAll(superClass.getClassesQualifiedNames());
		return result;
	}
	
	/**
	 * Get the qualified names of this class and its superclasses's constructors
	 * @return the qualified names of this class and its superclasses' constructors
	 */
	public List<String> getConstructorsQualifiedNames(){
		List<String> result = getConstructors().stream()
								.map(ConstructorInstance::getQualifiedName)
								.collect(Collectors.toList());
		if(superClass != null)
			result.addAll(superClass.getConstructorsQualifiedNames());
		return result;
	}
	
	/**
	 * Get the qualified names of this class and its superclasses's compatible methods
	 * @return the qualified names of this class and its superclasses' compatible methods
	 */
	public List<Pair<String, List<String>>> getCompatibleMethodsQualifiedNames(){
		List<Pair<String, List<String>>> result = new ArrayList<>();
		for(Entry<MethodInstance, List<MethodInstance>> e: compatibleMethods.entrySet()) {
			String methodQualifiedName = e.getKey().getQualifiedName();
			List<String> compatibleQualifiedNames = getMethodsQualifiedNames(e.getValue());
			Pair<String, List<String>> pair = new Pair<>(methodQualifiedName, compatibleQualifiedNames);
			result.add(pair);
		}
		if(superClass != null)
			result.addAll(superClass.getCompatibleMethodsQualifiedNames());
		return result;
	}
	
	/**
	 * Transforms a list of methods into their qualified names
	 * @param methods
	 * 			the list of method instances
	 * @return a list of the methods' qualified names
	 */
	private List<String> getMethodsQualifiedNames(List<MethodInstance> methods){
		return methods.stream()
					  .map(MethodInstance::getQualifiedName)
					  .collect(Collectors.toList());
	}
	
	public boolean equals(Object o) {
		return (this == o) || (o instanceof ClassInstance && equalsClassInstance((ClassInstance)o));
	}
	
	public int hashCode() {
		return getQualifiedName().hashCode();
	}

	private boolean equalsClassInstance(ClassInstance o) {
		return getQualifiedName().equals(o.getQualifiedName());
	}
	
	/**
	 * Debug toString method for this class
	 * @return a string representing this class
	 */
	public String toStringDebug() {
		StringBuilder result = new StringBuilder();
		Optional<ClassInstance> superClassOp = getSuperClass();
		if(superClassOp.isPresent()) {
			result.append(superClassOp.get().toStringDebug());
			result.append(getQualifiedName() + " extends " + superClassOp.get().getQualifiedName());
			result.append("\n");
		}
		result.append("Class " + getQualifiedName() + "\n");
		for(InterfaceImplementationInstance i: getInterfaces()) {
			result.append(getQualifiedName() + " implements " + i.getName());
			result.append("\n");
		}
		for(FieldInstance f: getFields()) {
			result.append(getQualifiedName() + " has field: " + f.getVisibility() + " " + f.getType().toString() + " " + f.getQualifiedName());
			result.append("\n");
		}
		for(ConstructorInstance c: getConstructors()) {
			result.append(getQualifiedName() + " has constructor " + c.getVisibility() + " " + c.getSimpleName());
			result.append("\n");

			for(MethodInstance dependency: c.getDirectDependencies()) {
				result.append(c.getQualifiedName() + " depends on " + dependency.getQualifiedName());
				result.append("\n");
			}
		}
		for(MethodInstance m: getMethods()) {
			result.append(getQualifiedName() + " has method " + m.getVisibility() + " " + m.getQualifiedName());
			result.append("\n");
			for(FieldAccessInstance fai: m.getFieldAccesses()) {
				result.append(m.getQualifiedName() + " " + fai.getAccessType() + " " + fai.getQualifiedName());
				result.append("\n");
			}
			for(MethodInstance compatible: getCompatibles(m)) {
				result.append(m.getQualifiedName() + " compatible with " + compatible.getQualifiedName());
				result.append("\n");
			}
			for(MethodInstance dependency: m.getDirectDependencies()) {
				result.append(m.getQualifiedName() + " depends on " + dependency.getQualifiedName());
				result.append("\n");
			}
		}
		return result.toString();
	}

}
