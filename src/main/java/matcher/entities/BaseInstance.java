package matcher.entities;

import java.util.ArrayList;
import java.util.List;

import matcher.utils.Pair;

public class BaseInstance {

	private List<ClassInstance> classInstances;
	private List<InterfaceInstance> interfaceInstances;
	
	public BaseInstance() {
		this.classInstances = new ArrayList<>();
		this.interfaceInstances = new ArrayList<>();
	}
	
	public List<ClassInstance> getClassInstances() {
		return classInstances;
	}
	
	public List<InterfaceInstance> getInterfaceInstances(){
		return interfaceInstances;
	}

	public void addClassInstance(ClassInstance classInstance) {
		if(!classInstances.contains(classInstance))
			classInstances.add(classInstance);
	}
	
	public void addInterfaceInstance(InterfaceInstance interfaceInstace) {
		if(!interfaceInstances.contains(interfaceInstace))
			interfaceInstances.add(interfaceInstace);
	}
	
	public void merge(BaseInstance b) {
		for(ClassInstance c: b.classInstances) {
			addClassInstance(c);
		}
		for(InterfaceInstance i: b.interfaceInstances) {
			addInterfaceInstance(i);
		}
	}
	
	public String toStringDebug() {
		StringBuilder result = new StringBuilder();
		for(ClassInstance c: classInstances) {
			result.append(c.toStringDebug() + "\n");
		}
		for(InterfaceInstance i: interfaceInstances) {
			result.append(i.toStringDebug() + "\n");
		}
		return result.toString();
	}
	
	public List<String> getFieldsQualifiedNames(){
		List<String> result = new ArrayList<>();
		for(ClassInstance ci: classInstances) {
			result.addAll(ci.getFieldsQualifiedNames());
		}
		return result;
	}
	
	public List<String> getFieldTypesQualifiedNames(){
		List<String> result =  new ArrayList<>();
		for(ClassInstance ci: classInstances) {
			result.addAll(ci.getFieldTypesQualifiedNames());
		}
		return result;
	}
	
	public List<String> getMethodsQualifiedNames(){
		List<String> result = new ArrayList<>();
		for(ClassInstance ci: classInstances) {
			result.addAll(ci.getMethodsQualifiedNames());
		}
		return result;
	}
	
	public List<String> getConstructorsQualifiedNames(){
		List<String> result = new ArrayList<>();
		for(ClassInstance ci: classInstances) {
			result.addAll(ci.getConstructorsQualifiedNames());
		}
		return result;
	}
	
	public List<String> getInterfacesQualifiedNames(){
		List<String> result = new ArrayList<>();
		for(ClassInstance ci: classInstances) {
			result.addAll(ci.getInterfacesQualifiedNames());
		}
		for(InterfaceInstance i: interfaceInstances) {
			result.add(i.getQualifiedName());
		}
		return result;
	}
	
	public List<Pair<String, List<String>>> getCompatibleQualifiedNames(){
		List<Pair<String, List<String>>> result = new ArrayList<>();
		for(ClassInstance ci: classInstances) {
			result.addAll(ci.getCompatibleMethodsQualifiedNames());
		}
		return result;
	}

	public List<String> getClassQualifiedNames() {
		List<String> result = new ArrayList<>();
		for(ClassInstance ci: classInstances) {
			result.addAll(ci.getClassesQualifiedNames());
		}
		return result;
	}

	public List<String> getInvocationsQualifiedNames() {
		List<String> result = new ArrayList<>();
		for(ClassInstance ci: classInstances) {
			result.addAll(ci.getInvocationsQualifiedNames());
		}
		return result;
	}

	public List<String> getFieldAccessesQualifiedNames() {
		List<String> result = new ArrayList<>();
		for(ClassInstance ci: classInstances) {
			result.addAll(ci.getFieldAccessesQualifiedNames());
		}
		return result;
	}
	
	public boolean hasFields() {
		return classInstances.stream().anyMatch(c -> c.hasFields());
	}
	
	public boolean hasMethods() {
		return classInstances.stream().anyMatch(c -> c.hasMethods());
	}
	
	public boolean hasInterfaces() {
		return classInstances.stream().anyMatch(c -> c.hasInterfaces());
	}
	
	public boolean hasConstructors() {
		return classInstances.stream().anyMatch(c -> c.hasConstructors());
	}
	
	public boolean hasSuperClass() {
		return classInstances.stream().anyMatch(c -> c.hasSuperClass());
	}
	
	public boolean hasCompatible() {
		return classInstances.stream().anyMatch(c -> c.hasCompatibles());
	}
	
	public boolean hasFieldAccesses() {
		return classInstances.stream().anyMatch(c -> c.hasFieldAccesses());
	}
	
	public boolean hasInvocations() {
		return classInstances.stream().anyMatch(c -> c.hasInvocations());
	}
	
}
