package matcher.patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.FieldInstance;
import matcher.entities.MethodInstance;
import matcher.utils.Pair;

public class ClassPattern {

	private FreeVariable freeVariable;
	
	private ClassPattern superClass;
	
	private Map<FreeVariable, List<FreeVariable>> compatible;
	
	private List<FieldPattern> fields;
	
	private List<MethodPattern> methods;
	
	private List<ConstructorPattern> constructors;

	public ClassPattern(FreeVariable freeVariable) {
		super();
		this.freeVariable = freeVariable;
		compatible = new HashMap<>();
		fields = new ArrayList<>();
		methods = new ArrayList<>();
		constructors = new ArrayList<>();
	}
	
	public void setSuperClass(ClassPattern superClass) {
		this.superClass = superClass;
	}
	
	public void addFieldPattern(FieldPattern f) {
		fields.add(f);
	}
	
	public void addMethodPattern(MethodPattern m) {
		methods.add(m);
	}
	
	public void addConstructorPattern(ConstructorPattern c) {
		constructors.add(c);
	}
	
	public void addCompatible(FreeVariable subMethod, FreeVariable topMethod) {
		if(!compatible.containsKey(subMethod))
			compatible.put(subMethod, new ArrayList<>());
		compatible.get(subMethod).add(topMethod);
	}
	
	public boolean hasFields() {
		return !fields.isEmpty();
	}
	
	public boolean hasMethods() {
		return !methods.isEmpty();
	}
	
	public boolean hasConstructors() {
		return !constructors.isEmpty();
	}
	
	public boolean hasCompatibles() {
		return !compatible.isEmpty();
	}
	
	public boolean hasSuperClass() {
		return superClass != null;
	}
	
	public boolean hasInvocations() {
		return methodsHaveInvocations() || constructorsHaveInvocations();
	}
	
	public boolean hasFieldAccesses() {
		return methodsHaveFieldAccesses();
	}
	
	private boolean constructorsHaveInvocations() {
		return constructors.stream().anyMatch(ConstructorPattern::hasInvocations);
	}

	private boolean methodsHaveInvocations() {
		return methods.stream().anyMatch(MethodPattern::hasInvocations);
	}
	
	private boolean methodsHaveFieldAccesses() {
		return methods.stream().anyMatch(MethodPattern::hasFieldAccesses);
	}
	
	public FreeVariable getFreeVariable() {
		return freeVariable;
	}
	
	public int getVariableId() {
		return freeVariable.getId();
	}
	
	public List<Integer> getFieldVariableIds(){
		List<Integer> result = fields.stream()
									 .map(FieldPattern::getVariableId)
									 .collect(Collectors.toList());
		if(hasSuperClass())
			result.addAll(superClass.getFieldVariableIds());

		return result;
	}
	
	public List<Integer> getMethodVariableIds(){
		return methods.stream().map(MethodPattern::getVariableId).collect(Collectors.toList());
	}
	
	public List<Integer> getConstructorsVariableIds(){
		return constructors.stream().map(ConstructorPattern::getVariableId)
									.collect(Collectors.toList());
	}
	
	public Optional<Integer> getSuperClassVariableId(){
		if(!hasSuperClass())
			return Optional.empty();
		return Optional.of(superClass.getVariableId());
	}
	
	public List<Pair<Integer, List<Integer>>> getCompatibleVariables(){
		List<Pair<Integer, List<Integer>>> result = new ArrayList<>();
		for(Entry<FreeVariable, List<FreeVariable>> e: compatible.entrySet()) {
			int id = e.getKey().getId();
			List<Integer> list = e.getValue().stream()
											 .map(FreeVariable::getId)
											 .collect(Collectors.toList());
			Pair<Integer, List<Integer>> p = new Pair<>(id, list);
			result.add(p);
		}
		return result;
	}	

	public List<Integer> getClassVariableIds() {
		List<Integer> result = new ArrayList<>();
		result.add(freeVariable.getId());
		if(hasSuperClass())
			result.addAll(superClass.getClassVariableIds());
		return result;
	}
	
	public List<Integer> getInvocationsVariableIds(){
		List<Integer> result = new ArrayList<>();
		getInvocationsInMethodsVariableIds(result);
		getInvocationsInConstructorsVariableIds(result);
		return result;
	}

	private void getInvocationsInConstructorsVariableIds(List<Integer> result) {
		for(ConstructorPattern c: constructors) {
			result.addAll(c.getInvocationsVariableIds());
		}
	}

	private void getInvocationsInMethodsVariableIds(List<Integer> result) {
		for(MethodPattern m: methods) {
			result.addAll(m.getInvocationsVariableIds());
		}
	}
	
	public boolean isVariableId(int id) {
		return getVariableId() == id;
	}
	
	public boolean hasVariableId(int id) {
		return isVariableId(id) ||
			   fieldsHaveId(id) ||
			   methodsHaveId(id) ||
			   constructorsHaveId(id) ||
			   superClassHasId(id) ||
			   compatiblesHaveId(id);
	}

	private boolean compatiblesHaveId(int id) {
		for(Entry<FreeVariable, List<FreeVariable>> e: compatible.entrySet()) {
			if(e.getKey().isId(id) ||
			   e.getValue().stream().anyMatch(v -> v.isId(id)))
				return true;
		}
		return false;
	}

	private boolean superClassHasId(int id) {
		if(hasSuperClass())
			return superClass.hasVariableId(id);
		return false;
	}

	private boolean constructorsHaveId(int id) {
		return constructors.stream().anyMatch(c -> c.hasVariableId(id));
	}

	private boolean methodsHaveId(int id) {
		return methods.stream().anyMatch(m -> m.hasVariableId(id));
	}

	private boolean fieldsHaveId(int id) {
		return fields.stream().anyMatch(f -> f.isVariableId(id));
	}
	
	public void setVariableValue(int id, String value) {
		if(isVariableId(id))
			freeVariable.setValue(value);
		if(fieldsHaveId(id))
			setVariableValueFields(id, value);
		if(methodsHaveId(id))
			setVariableValueMethods(id, value);
		if(constructorsHaveId(id))
			setVariableValueConstructors(id, value);
		if(superClassHasId(id))
			superClass.setVariableValue(id, value);
		if(compatiblesHaveId(id))
			setVariableValueCompatibles(id, value);
	}

	private void setVariableValueCompatibles(int id, String value) {
		for(Entry<FreeVariable, List<FreeVariable>> e: compatible.entrySet()) {
			FreeVariable key = e.getKey();
			List<FreeVariable> valList = e.getValue();
			if(key.isId(id))
				key.setValue(value);
			for(FreeVariable free: valList) {
				if(free.isId(id))
					free.setValue(value);
			}
		}
		
	}

	private void setVariableValueConstructors(int id, String value) {
		for(ConstructorPattern c: constructors) {
			if(c.hasVariableId(id))
				c.setVariableValue(id, value);
		}
	}

	private void setVariableValueMethods(int id, String value) {
		for(MethodPattern m: methods) {
			if(m.hasVariableId(id))
				m.setVariableValue(id, value);
		}
		
	}

	private void setVariableValueFields(int id, String value) {
		for(FieldPattern f: fields) {
			if(f.isVariableId(id))
				f.setVariableValue(value);
		}
	}
	
	public boolean filled() {
		return freeVariable.hasValue() &&
			   fieldsFilled() &&
			   methodsFilled() &&
			   constructorsFilled() &&
			   superClassFilled() &&
			   compatiblesFilled();
	}

	private boolean compatiblesFilled() {
		for(Entry<FreeVariable, List<FreeVariable>> e: compatible.entrySet()) {
			if(!e.getKey().hasValue() ||
			   !e.getValue().stream().allMatch(FreeVariable::hasValue))
				return false;
		}
		return true;
	}

	private boolean superClassFilled() {
		if(hasSuperClass())
			return superClass.filled();
		return true;
	}

	private boolean constructorsFilled() {
		return constructors.stream().allMatch(ConstructorPattern::filled);
	}

	private boolean methodsFilled() {
		return methods.stream().allMatch(MethodPattern::filled);
	}

	private boolean fieldsFilled() {
		return fields.stream().allMatch(FieldPattern::filled);
	}
	
	public boolean matches(ClassInstance instance) {
		return filled() &&
				sameName(instance) &&
				fieldsMatch(instance) &&
				methodsMatch(instance) &&
				constructorsMatch(instance) &&
				compatiblesMatch(instance) &&
				superClassMatch(instance);
	}
	
	public boolean matchesOne(List<ClassInstance> instances) {
		return instances.stream().anyMatch(ci -> matches(ci));
	}

	private boolean compatiblesMatch(ClassInstance instance) {
		for(Entry<FreeVariable, List<FreeVariable>> e: compatible.entrySet()) {
			FreeVariable free = e.getKey();
			boolean foundMatch = false;
			boolean foundCompatible = false;
			for(MethodInstance first: instance.getMethodsWithCompatibles()) {
				if(free.matches(first.getQualifiedName())) {
					foundCompatible = true;
					List<FreeVariable> vars = e.getValue();
					List<MethodInstance> compatibleMethods = instance.getCompatibles(first);
					if(compatiblesMatch(vars, compatibleMethods)) {
						foundMatch = true;
					}
				}
			}
			if(!foundCompatible || !foundMatch)
				return false;
		}
		return true;
	}
	
	private boolean compatiblesMatch(List<FreeVariable> vars, List<MethodInstance> methods) {
		List<String> methodNames = methods.stream()
										  .map(MethodInstance::getQualifiedName)
										  .collect(Collectors.toList());
		List<String> varValues = vars.stream()
									 .map(FreeVariable::getValue)
									 .collect(Collectors.toList());
		return methodNames.containsAll(varValues);
	}

	private boolean superClassMatch(ClassInstance instance) {
		if(hasSuperClass() && instance.getSuperClass().isPresent())
			return superClass.matches(instance.getSuperClass().get());
		return true;
	}

	private boolean constructorsMatch(ClassInstance instance) {
		return constructors.stream().allMatch(c -> 
					constructorMatchesOne(c, instance.getConstructors()));
	}
	
	private boolean constructorMatchesOne(ConstructorPattern constructor,
			List<ConstructorInstance> constructorInstances) {
		for(ConstructorInstance constructorInstance: constructorInstances) {
			if(constructor.matches(constructorInstance))
				return true;
		}
		return false;
	}

	private boolean methodsMatch(ClassInstance instance) {
		return methods.stream()
					  .allMatch(m -> methodMatchesOne(m, instance.getMethods()));
	}
	
	private boolean methodMatchesOne(MethodPattern method, List<MethodInstance> methodInstances) {
		for(MethodInstance methodInstance: methodInstances) {
			if(method.matches(methodInstance))
				return true;
		}
		return false;
	}

	private boolean fieldsMatch(ClassInstance instance) {
		return fields.stream()
					 .allMatch(f -> fieldMatchesOne(f, instance.getFields()));
	}
	
	private boolean fieldMatchesOne(FieldPattern field, List<FieldInstance> fieldInstances) {
		for(FieldInstance fieldInstance: fieldInstances) {
			if(field.matches(fieldInstance))
				return true;
		}
		return false;
	}

	private boolean sameName(ClassInstance instance) {
		return instance.getQualifiedName().equals(freeVariable.getValue());
	}
	
	public String toStringDebug() {
		StringBuilder result = new StringBuilder();
		
		if(hasSuperClass()) {
			result.append("#" + getVariableId() + " extends #" 
					+ getSuperClassVariableId().get() + "\n");
			result.append(superClass.toStringDebug());
		}
		for(FieldPattern f : fields) {
			result.append("#" + getVariableId() + " has " + f.toStringDegub() + "\n");
		}
		for(ConstructorPattern c: constructors) {
			result.append(c.toStringDebug(getVariableId()));
		}
		for(MethodPattern m: methods) {
			result.append(m.toStringDebug(getVariableId()));
		}
		for(Entry<FreeVariable, List<FreeVariable>> e: compatible.entrySet()) {
			FreeVariable v = e.getKey();
			for(FreeVariable f: e.getValue()) {
				result.append("#" + v.getId() + " compatible with " + f.getId() + "\n");
			}
		}
		
		return result.toString();
	}

}
