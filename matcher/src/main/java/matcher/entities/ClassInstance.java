package matcher.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import matcher.utils.Pair;

public class ClassInstance {
	
	private String name;
	
	private String qualifiedName;
	
	private List<FieldInstance> fields;
	
	private List<MethodInstance> methods;
	
	private List<ConstructorInstance> constructors;
	
	private ClassInstance superClass;
	
	private Map<MethodInstance, List<MethodInstance>> compatibleMethods;

	public ClassInstance() {
		this.fields = new ArrayList<>();
		this.methods = new ArrayList<>();
		this.constructors = new ArrayList<>();
		this.compatibleMethods = new HashMap<>();
	}
	
	public ClassInstance(String name, String qualifiedName) {
		super();
		this.name = name;
		this.qualifiedName = qualifiedName;
		
		this.fields = new ArrayList<>();
		this.methods = new ArrayList<>();
		this.constructors = new ArrayList<>();
		this.compatibleMethods = new HashMap<>();
	}

	public String getName() {
		return name;
	}
	
	public String getQualifiedName() {
		return qualifiedName;
	}

	public Optional<ClassInstance> getSuperClass() {
		return Optional.ofNullable(superClass);
	}

	public void setSuperClass(ClassInstance superClass) {
		this.superClass = superClass;
	}

	public List<FieldInstance> getFields() {
		return fields;
	}

	public List<MethodInstance> getMethods() {
		return methods;
	}

	public List<ConstructorInstance> getConstructors() {
		return constructors;
	}

	public List<MethodInstance> getCompatibles(MethodInstance m){
		return compatibleMethods.get(m);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}
	
	public void addField(FieldInstance field) {
		this.fields.add(field);
	}
	
	public void addMethod(MethodInstance method) {
		this.methods.add(method);
	}
	
	public void addConstructor(ConstructorInstance constructor) {
		this.constructors.add(constructor);
	}
	
	public void addCompatible(MethodInstance subMethod, MethodInstance topMethod) {
		if(!compatibleMethods.containsKey(subMethod))
			compatibleMethods.put(subMethod, new ArrayList<MethodInstance>());
		compatibleMethods.get(subMethod).add(topMethod);
	}
	
	public List<String> getFieldsQualifiedNames(){
		return getFields().stream()
						  .map(FieldInstance::getQualifiedName)
						  .collect(Collectors.toList());
	}
	
	public List<String> getMethodsQualifiedNames(){
		return getMethodsQualifiedNames(getMethods());
	}
	
	public List<String> getConstructorsQualifiedNames(){
		return getConstructors().stream()
								.map(ConstructorInstance::getQualifiedName)
								.collect(Collectors.toList());
	}
	
	public List<Pair<String, List<String>>> getCompatibleMethodsQualifiedNames(){
		List<Pair<String, List<String>>> result = new ArrayList<>();
		for(Entry<MethodInstance, List<MethodInstance>> e: compatibleMethods.entrySet()) {
			String methodQualifiedName = e.getKey().getQualifiedName();
			List<String> compatibleQualifiedNames = getMethodsQualifiedNames(e.getValue());
			Pair<String, List<String>> pair = new Pair<>(methodQualifiedName, compatibleQualifiedNames);
			result.add(pair);
		}
		return result;
	}
	
	private List<String> getMethodsQualifiedNames(List<MethodInstance> methods){
		return methods.stream()
					  .map(MethodInstance::getQualifiedName)
					  .collect(Collectors.toList());
	}
}
