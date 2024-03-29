package org.conflito.matcher.patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.conflito.matcher.entities.ClassInstance;
import org.conflito.matcher.entities.ConstructorInstance;
import org.conflito.matcher.entities.FieldInstance;
import org.conflito.matcher.entities.InterfaceImplementationInstance;
import org.conflito.matcher.entities.MethodInstance;
import org.conflito.matcher.entities.Visibility;
import org.conflito.matcher.utils.Pair;

public class ClassPattern {

  private final FreeVariable freeVariable;

  private ClassPattern superClass;

  private final Map<FreeVariable, List<FreeVariable>> compatible;

  private final List<FieldPattern> fields;

  private final List<MethodPattern> methods;

  private final List<ConstructorPattern> constructors;

  private final List<InterfaceImplementationPattern> interfaces;

  private final List<FreeVariable> excludedMethods;

  public ClassPattern(FreeVariable freeVariable) {
    super();
    this.freeVariable = freeVariable;
    compatible = new HashMap<>();
    fields = new ArrayList<>();
    methods = new ArrayList<>();
    constructors = new ArrayList<>();
    interfaces = new ArrayList<>();
    excludedMethods = new ArrayList<>();
  }

  public ClassPattern(ClassPattern classPattern) {
    super();
    this.freeVariable = new FreeVariable(classPattern.freeVariable);

    if (classPattern.hasSuperClass()) {
      this.superClass = new ClassPattern(classPattern.superClass);
    }

    this.compatible = new HashMap<>();
    for (Entry<FreeVariable, List<FreeVariable>> e : classPattern.compatible.entrySet()) {
      FreeVariable key = new FreeVariable(e.getKey());
      List<FreeVariable> values = new ArrayList<>();
      for (FreeVariable value : e.getValue()) {
        values.add(new FreeVariable(value));
      }
      this.compatible.put(key, values);
    }

    this.fields = new ArrayList<>();
    for (FieldPattern fieldPattern : classPattern.fields) {
      this.fields.add(new FieldPattern(fieldPattern));
    }

    this.methods = new ArrayList<>();
    for (MethodPattern methodPattern : classPattern.methods) {
      this.methods.add(new MethodPattern(methodPattern));
    }

    this.constructors = new ArrayList<>();
    for (ConstructorPattern constructorPattern : classPattern.constructors) {
      this.constructors.add(new ConstructorPattern(constructorPattern));
    }

    this.interfaces = new ArrayList<>();
    for (InterfaceImplementationPattern
        interfaceImplementationPattern : classPattern.interfaces) {
      this.interfaces.add(
          new InterfaceImplementationPattern(interfaceImplementationPattern));
    }

    this.excludedMethods = new ArrayList<>();
    for (FreeVariable freeVariable : classPattern.excludedMethods) {
      this.excludedMethods.add(new FreeVariable(freeVariable));
    }
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
    if (!compatible.containsKey(subMethod)) {
      compatible.put(subMethod, new ArrayList<>());
    }
    compatible.get(subMethod).add(topMethod);
  }

  public List<Integer> getCompatibles(int id) {
    for (Entry<FreeVariable, List<FreeVariable>> e : compatible.entrySet()) {
      if (e.getKey().getId() == id) {
        return e.getValue().stream()
            .map(FreeVariable::getId)
            .collect(Collectors.toList());
      }
    }
    return null;
  }

  public void addExcludedMethod(FreeVariable method) {
    excludedMethods.add(method);
  }

  public boolean excludesMethod(FreeVariable method) {
    return excludedMethods.contains(method);
  }

  public void removeExcludedMethod(FreeVariable method) {
    if (excludesMethod(method)) {
      excludedMethods.remove(method);
    }
  }

  public void addInterface(InterfaceImplementationPattern i) {
    interfaces.add(i);
  }

  public boolean hasFields() {
    boolean result = !fields.isEmpty();
    if (!result && hasSuperClass()) {
      result = superClass.hasFields();
    }
    return result;
  }

  public boolean hasMethods() {
    boolean result = !methods.isEmpty();
    if (!result && hasSuperClass()) {
      result = superClass.hasMethods();
    }
    return result;
  }

  public boolean hasInterfaces() {
    boolean result = !interfaces.isEmpty();
    if (!result && hasSuperClass()) {
      result = superClass.hasInterfaces();
    }
    return result;
  }

  public boolean hasConstructors() {
    boolean result = !constructors.isEmpty();
    if (!result && hasSuperClass()) {
      result = superClass.hasConstructors();
    }
    return result;
  }

  public boolean hasCompatibles() {
    boolean result = !compatible.isEmpty();
    if (!result && hasSuperClass()) {
      result = superClass.hasCompatibles();
    }
    return result;
  }

  public boolean hasSuperClass() {
    return superClass != null;
  }

  public boolean hasInvocations() {
    boolean result = methodsHaveInvocations() || constructorsHaveInvocations();
    if (!result && hasSuperClass()) {
      result = superClass.hasInvocations();
    }
    return result;
  }

  public boolean hasFieldAccesses() {
    boolean result = methodsHaveFieldAccesses();
    if (!result && hasSuperClass()) {
      result = superClass.hasFieldAccesses();
    }
    return result;
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

  public List<Integer> getFieldVariableIds() {
    List<Integer> result = fields.stream()
        .map(FieldPattern::getVariableId)
        .collect(Collectors.toList());
    if (hasSuperClass()) {
      result.addAll(superClass.getFieldVariableIds());
    }

    return result;
  }

  public List<Integer> getFieldTypesVariableIds() {
    List<Integer> result = new ArrayList<>();
    for (FieldPattern f : fields) {
      if (f.hasType()) {
        result.add(f.getTypeVariableId());
      }
    }
    if (hasSuperClass()) {
      result.addAll(superClass.getFieldTypesVariableIds());
    }
    return result;
  }

  public List<Integer> getInterfaceVariableIds() {
    List<Integer> result = interfaces.stream()
        .map(InterfaceImplementationPattern::getVariableId)
        .collect(Collectors.toList());
    if (hasSuperClass()) {
      result.addAll(superClass.getInterfaceVariableIds());
    }
    return result;
  }

  public List<Integer> getMethodVariableIds() {
    List<Integer> result = methods.stream()
        .map(MethodPattern::getVariableId)
        .collect(Collectors.toList());
    if (hasSuperClass()) {
      result.addAll(superClass.getMethodVariableIds());
    }
    return result;
  }

  public List<Integer> getFieldAccessesVariableIds() {
    List<Integer> result = new ArrayList<>();
    for (MethodPattern m : methods) {
      result.addAll(m.getFieldAccessesVariableIds());
    }
    if (hasSuperClass()) {
      result.addAll(superClass.getFieldAccessesVariableIds());
    }

    return result;
  }

  public List<Integer> getConstructorsVariableIds() {
    List<Integer> result = constructors.stream()
        .map(ConstructorPattern::getVariableId)
        .collect(Collectors.toList());
    if (hasSuperClass()) {
      result.addAll(superClass.getConstructorsVariableIds());
    }
    return result;
  }

  public List<Pair<Integer, List<Integer>>> getCompatibleVariables() {
    List<Pair<Integer, List<Integer>>> result = new ArrayList<>();
    for (Entry<FreeVariable, List<FreeVariable>> e : compatible.entrySet()) {
      int id = e.getKey().getId();
      List<Integer> list = e.getValue().stream()
          .map(FreeVariable::getId)
          .collect(Collectors.toList());
      Pair<Integer, List<Integer>> p = new Pair<>(id, list);
      result.add(p);
    }
    if (hasSuperClass()) {
      result.addAll(superClass.getCompatibleVariables());
    }
    return result;
  }

  public List<Integer> getClassVariableIds() {
    List<Integer> result = new ArrayList<>();
    result.add(freeVariable.getId());
    if (hasSuperClass()) {
      result.addAll(superClass.getClassVariableIds());
    }
    return result;
  }

  public List<Integer> getInvocationsVariableIds() {
    List<Integer> result = new ArrayList<>();
    getInvocationsInMethodsVariableIds(result);
    getInvocationsInConstructorsVariableIds(result);
    if (hasSuperClass()) {
      result.addAll(superClass.getInvocationsVariableIds());
    }

    return result;
  }

  private void getInvocationsInConstructorsVariableIds(List<Integer> result) {
    for (ConstructorPattern c : constructors) {
      result.addAll(c.getInvocationsVariableIds());
    }
  }

  private void getInvocationsInMethodsVariableIds(List<Integer> result) {
    for (MethodPattern m : methods) {
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
        compatiblesHaveId(id) ||
        excludedHaveId(id) ||
        interfacesHaveId(id);
  }

  private boolean interfacesHaveId(int id) {
    return interfaces.stream().anyMatch(i -> i.isVariableId(id));
  }

  private boolean excludedHaveId(int id) {
    return excludedMethods.stream().anyMatch(v -> v.isId(id));
  }

  private boolean compatiblesHaveId(int id) {
    for (Entry<FreeVariable, List<FreeVariable>> e : compatible.entrySet()) {
      if (e.getKey().isId(id) ||
          e.getValue().stream().anyMatch(v -> v.isId(id))) {
        return true;
      }
    }
    return false;
  }

  private boolean superClassHasId(int id) {
    if (hasSuperClass()) {
      return superClass.hasVariableId(id);
    }
    return false;
  }

  private boolean constructorsHaveId(int id) {
    return constructors.stream().anyMatch(c -> c.hasVariableId(id));
  }

  private boolean methodsHaveId(int id) {
    return methods.stream().anyMatch(m -> m.hasVariableId(id));
  }

  private boolean fieldsHaveId(int id) {
    return fields.stream().anyMatch(f -> f.hasVariableId(id));
  }

  public void clean() {
    freeVariable.clean();
    cleanFields();
    cleanMethods();
    cleanConstructors();
    cleanCompatibles();
    if (hasSuperClass()) {
      superClass.clean();
    }
    cleanExcludedMethods();
    cleanInterfaces();
  }

  private void cleanInterfaces() {
    for (InterfaceImplementationPattern i : interfaces) {
      i.clean();
    }
  }

  private void cleanExcludedMethods() {
    for (FreeVariable v : excludedMethods) {
      v.clean();
    }
  }

  private void cleanCompatibles() {
    for (Entry<FreeVariable, List<FreeVariable>> e : compatible.entrySet()) {
      e.getKey().clean();
      List<FreeVariable> valList = e.getValue();
      for (FreeVariable free : valList) {
        free.clean();
      }
    }
  }

  private void cleanConstructors() {
    for (ConstructorPattern c : constructors) {
      c.clean();
    }
  }

  private void cleanMethods() {
    for (MethodPattern m : methods) {
      m.clean();
    }
  }

  private void cleanFields() {
    for (FieldPattern f : fields) {
      f.clean();
    }
  }

  public void setVariableValue(int id, String value) {
    if (isVariableId(id)) {
      freeVariable.setValue(value);
    }
    if (fieldsHaveId(id)) {
      setVariableValueFields(id, value);
    }
    if (methodsHaveId(id)) {
      setVariableValueMethods(id, value);
    }
    if (constructorsHaveId(id)) {
      setVariableValueConstructors(id, value);
    }
    if (superClassHasId(id)) {
      superClass.setVariableValue(id, value);
    }
    if (compatiblesHaveId(id)) {
      setVariableValueCompatibles(id, value);
    }
    if (excludedHaveId(id)) {
      setVariableValueExcluded(id, value);
    }
    if (interfacesHaveId(id)) {
      setVariableValueInterfaces(id, value);
    }
  }

  private void setVariableValueInterfaces(int id, String value) {
    for (InterfaceImplementationPattern i : interfaces) {
      i.setVariableValue(id, value);
    }
  }

  private void setVariableValueExcluded(int id, String value) {
    for (FreeVariable v : excludedMethods) {
      if (v.isId(id)) {
        v.setValue(value);
      }
    }
  }

  private void setVariableValueCompatibles(int id, String value) {
    for (Entry<FreeVariable, List<FreeVariable>> e : compatible.entrySet()) {
      FreeVariable key = e.getKey();
      List<FreeVariable> valList = e.getValue();
      if (key.isId(id)) {
        key.setValue(value);
      }
      for (FreeVariable free : valList) {
        if (free.isId(id)) {
          free.setValue(value);
        }
      }
    }

  }

  private void setVariableValueConstructors(int id, String value) {
    for (ConstructorPattern c : constructors) {
      if (c.hasVariableId(id)) {
        c.setVariableValue(id, value);
      }
    }
  }

  private void setVariableValueMethods(int id, String value) {
    for (MethodPattern m : methods) {
      if (m.hasVariableId(id)) {
        m.setVariableValue(id, value);
      }
    }

  }

  private void setVariableValueFields(int id, String value) {
    for (FieldPattern f : fields) {
      if (f.hasVariableId(id)) {
        f.setVariableValue(id, value);
      }
    }
  }

  public boolean filled() {
    return freeVariable.hasValue() &&
        fieldsFilled() &&
        methodsFilled() &&
        constructorsFilled() &&
        superClassFilled() &&
        compatiblesFilled() &&
        excludedFilled() &&
        interfacesFilled();
  }

  private boolean interfacesFilled() {
    return interfaces.stream().allMatch(InterfaceImplementationPattern::filled);
  }

  private boolean excludedFilled() {
    return excludedMethods.stream().allMatch(FreeVariable::hasValue);
  }

  private boolean compatiblesFilled() {
    for (Entry<FreeVariable, List<FreeVariable>> e : compatible.entrySet()) {
      if (!e.getKey().hasValue() ||
          !e.getValue().stream().allMatch(FreeVariable::hasValue)) {
        return false;
      }
    }
    return true;
  }

  private boolean superClassFilled() {
    if (hasSuperClass()) {
      return superClass.filled();
    }
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
        superClassMatch(instance) &&
        excludedMatch(instance) &&
        interfacesMatch(instance);
  }

  public boolean matchesOne(List<ClassInstance> instances) {
    return instances.stream().anyMatch(ci -> matches(ci));
  }

  private boolean interfacesMatch(ClassInstance instance) {
    return interfaces.stream().allMatch(i ->
        interfaceMatchesOne(i, instance.getInterfaces()));
  }

  private boolean interfaceMatchesOne(InterfaceImplementationPattern ip,
      List<InterfaceImplementationInstance> interfaceInstances) {
    for (InterfaceImplementationInstance i : interfaceInstances) {
      if (ip.matches(i)) {
        return true;
      }
    }
    return false;
  }

  private boolean excludedMatch(ClassInstance instance) {
    return excludedMethods.stream().allMatch(v -> !instance.hasMethod(v.getValue()));
  }

  private boolean compatiblesMatch(ClassInstance instance) {
    for (Entry<FreeVariable, List<FreeVariable>> e : compatible.entrySet()) {
      FreeVariable free = e.getKey();
      boolean foundMatch = false;
      boolean foundCompatible = false;
      for (MethodInstance first : instance.getMethodsWithCompatibles()) {
        if (free.matches(first.getQualifiedName())) {
          foundCompatible = true;
          List<FreeVariable> vars = e.getValue();
          List<MethodInstance> compatibleMethods = instance.getCompatibles(first);
          if (compatiblesMatch(vars, compatibleMethods)) {
            foundMatch = true;
          }
        }
      }
      if (!foundCompatible || !foundMatch) {
        return false;
      }
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
    Optional<ClassInstance> superClassOp = instance.getSuperClass();
    if (hasSuperClass() && superClassOp.isPresent()) {
      ClassInstance superClassInstance = superClassOp.get();
      boolean result = superClass.matches(superClassInstance);
      if (!result && superClassInstance.getSuperClass().isPresent()) {
        result = superClassMatch(superClassInstance);
      }
      return result;//superClass.matches(superClassOp.get()) || superClassMatch(superClassOp.get());
    }
    return true;
  }

  private boolean constructorsMatch(ClassInstance instance) {
    return constructors.stream().allMatch(c ->
        constructorMatchesOne(c, instance.getConstructors()));
  }

  private boolean constructorMatchesOne(ConstructorPattern constructor,
      List<ConstructorInstance> constructorInstances) {
    for (ConstructorInstance constructorInstance : constructorInstances) {
      if (constructor.matches(constructorInstance)) {
        return true;
      }
    }
    return false;
  }

  private boolean methodsMatch(ClassInstance instance) {
    return methods.stream()
        .allMatch(m -> methodMatchesOne(m, instance.getMethods()));
  }

  private boolean methodMatchesOne(MethodPattern method, List<MethodInstance> methodInstances) {
    for (MethodInstance methodInstance : methodInstances) {
      if (method.matches(methodInstance)) {
        return true;
      }
    }
    return false;
  }

  private boolean fieldsMatch(ClassInstance instance) {
    return fields.stream()
        .allMatch(f -> fieldMatchesOne(f, instance.getFields()));
  }

  private boolean fieldMatchesOne(FieldPattern field, List<FieldInstance> fieldInstances) {
    for (FieldInstance fieldInstance : fieldInstances) {
      if (field.matches(fieldInstance)) {
        return true;
      }
    }
    return false;
  }

  private boolean sameName(ClassInstance instance) {
    return instance.getQualifiedName().equals(freeVariable.getValue());
  }

  public String toString() {
    StringBuilder result = new StringBuilder();

    if (hasSuperClass()) {
      result.append("Class $" + getVariableId() + " extends class $" +
          superClass.getVariableId() + "\n");
    }

    for (FieldPattern f : fields) {
      Visibility visibility = f.getVisibility();
      result.append("Class $" + getVariableId() + " has" +
          (visibility == null ? " " :
              " " + visibility.toString().toLowerCase() + " "));
      result.append("field $" + f.getVariableId());
      if (f.hasType()) {
        result.append(" of type $" + f.getTypeVariableId());
      }
      result.append("\n");
    }

    for (MethodPattern m : methods) {
      Visibility visibility = m.getVisibility();

      result.append("Class $" + getVariableId() + " has" +
          (visibility == null ? " " :
              " " + visibility.toString().toLowerCase() + " "));
      result.append("method $" + m.getVariableId() + "\n");
      result.append(m);
    }

    for (ConstructorPattern c : constructors) {
      Visibility visibility = c.getVisibility();

      result.append("Class $" + getVariableId() + " has" +
          (visibility == null ? " " :
              " " + visibility.toString().toLowerCase() + " "));
      result.append("constructor $" + c.getVariableId() + "\n");
      result.append(c);
    }

    for (Map.Entry<FreeVariable, List<FreeVariable>> e : compatible.entrySet()) {
      for (FreeVariable v : e.getValue()) {
        result.append("Method $" + e.getKey().getId() + " compatible with ");
        result.append("method $" + v.getId() + "\n");
      }
    }

    for (FreeVariable var : excludedMethods) {
      result.append("Class $" + getVariableId() + " does not have ");
      result.append("method $" + var.getId() + "\n");
    }

    if (result.length() > 0) {
      result.deleteCharAt(result.length() - 1);
    }

    return result.toString();
  }

  public int hashCode() {
    return freeVariable.hashCode();
  }

  public boolean equals(Object o) {
    return (this == o) || (o instanceof ClassPattern &&
        equalsClassPattern((ClassPattern) o));
  }

  private boolean equalsClassPattern(ClassPattern o) {
    return freeVariable.getId() == o.freeVariable.getId();
  }

}
