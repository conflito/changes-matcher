package matcher.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import matcher.entities.deltas.Holder;
import matcher.entities.deltas.Insertable;
import matcher.utils.Pair;

public class ClassInstance implements Insertable, Holder{
	
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
		List<MethodInstance> result = new ArrayList<>();
		if(compatibleMethods.containsKey(m))
			result = compatibleMethods.get(m);
		return result;
	}
	
	public Iterable<MethodInstance> getMethodsWithCompatibles(){
		return compatibleMethods.keySet();
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
		List<String> result = getFields().stream()
						  					 .map(FieldInstance::getQualifiedName)
						  					 .collect(Collectors.toList());
		if(superClass != null)
			result.addAll(superClass.getFieldsQualifiedNames());
		return result;
	}
	
	
	public List<String> getMethodsQualifiedNames(){
		List<String> result = getMethodsQualifiedNames(getMethods());
		if(superClass != null)
			result.addAll(superClass.getMethodsQualifiedNames());
		return result;
	}
	
	public List<String> getFieldAccessesQualifiedNames() {
		List<String> result = new ArrayList<>();
		for(MethodInstance m: methods) {
			result.addAll(m.getFieldAccessesQualifiedNames());
		}
		if(superClass != null)
			result.addAll(superClass.getFieldAccessesQualifiedNames());
		return result;
	}
	
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
	
	public List<String> getClassesQualifiedNames() {
		List<String> result = new ArrayList<>();
		result.add(getQualifiedName());
		if(superClass != null)
			result.addAll(superClass.getClassesQualifiedNames());
		return result;
	}
	
	public List<String> getConstructorsQualifiedNames(){
		List<String> result = getConstructors().stream()
								.map(ConstructorInstance::getQualifiedName)
								.collect(Collectors.toList());
		if(superClass != null)
			result.addAll(superClass.getConstructorsQualifiedNames());
		return result;
	}
	
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
	
	private List<String> getMethodsQualifiedNames(List<MethodInstance> methods){
		return methods.stream()
					  .map(MethodInstance::getQualifiedName)
					  .collect(Collectors.toList());
	}
	
	public boolean equals(Object o) {
		return (this == o) || (o instanceof ClassInstance && equalsClassInstance((ClassInstance)o));
	}

	private boolean equalsClassInstance(ClassInstance o) {
		return getQualifiedName().equals(o.getQualifiedName());
	}
	
	public String toStringDebug() {
		StringBuilder result = new StringBuilder();
		if(getSuperClass().isPresent()) {
			result.append(getSuperClass().get().toStringDebug());
			result.append(getQualifiedName() + " extends " + getSuperClass().get().getQualifiedName());
			result.append("\n");
		}
		for(FieldInstance f: getFields()) {
			result.append(getQualifiedName() + " has field " + f.getVisibility() + " " + f.getQualifiedName());
			result.append("\n");
		}
		for(ConstructorInstance c: getConstructors()) {
			result.append(getQualifiedName() + " has constructor " + c.getVisibility() + " " + c.getSimpleName());
			result.append("\n");
			for(MethodInvocationInstance mii: c.getInvocations()) {
				result.append(c.getQualifiedName() + " invokes " + mii.getQualifiedName());
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
			for(MethodInvocationInstance mii: m.getInvocations()) {
				result.append(m.getQualifiedName() + " invokes " + mii.getQualifiedName());
				result.append("\n");
			}
			for(MethodInstance compatible: getCompatibles(m)) {
				result.append(m.getQualifiedName() + " compatible with " + compatible.getQualifiedName());
				result.append("\n");
			}
		}
		return result.toString();
	}

}
