package org.conflito.matcher.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.conflito.matcher.entities.deltas.DeltaInstance;

public class ChangeInstance {

  private BaseInstance baseInstance;

  private final List<DeltaInstance> deltas;

  public ChangeInstance() {
    deltas = new ArrayList<>();
  }

  public void setBaseInstance(BaseInstance baseInstance) {
    this.baseInstance = baseInstance;
  }

  public void addDeltaInstance(DeltaInstance deltaInstance) {
    deltas.add(deltaInstance);
  }

  public void addDeltaInstances(List<DeltaInstance> deltaInstances) {
    deltaInstances.forEach(d -> addDeltaInstance(d));
  }

  public BaseInstance getBaseInstance() {
    return baseInstance;
  }

  public List<DeltaInstance> getDeltaInstances() {
    return deltas;
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(baseInstance.toStringDebug() + "\n---------\n");
    deltas.forEach(d -> result.append(d.toString() + "\n---------\n"));
    return result.toString();
  }

  public List<String> getFieldsQualifiedNames() {
    return baseInstance.getFieldsQualifiedNames();
  }

  public List<String> getFieldTypesQualifiedNames() {
    return baseInstance.getFieldTypesQualifiedNames();
  }

  public List<String> getMethodsQualifiedNames() {
    return baseInstance.getMethodsQualifiedNames();
  }

  public List<String> getInvocationsQualifiedNames() {
    return baseInstance.getInvocationsQualifiedNames();
  }

  public List<String> getConstructorsQualifiedNames() {
    return baseInstance.getConstructorsQualifiedNames();
  }

  public List<String> getInterfacesQualifiedNames() {
    return baseInstance.getInterfacesQualifiedNames();
  }

  public List<String> getClassQualifiedNames() {
    return baseInstance.getClassQualifiedNames();
  }

  public List<String> getFieldsAccessQualifiedNames() {
    return baseInstance.getFieldAccessesQualifiedNames();
  }

  public List<String> getDeltaClassesQualifiedNames() {
    List<String> result = new ArrayList<>();
    deltas.forEach(d -> result.addAll(d.getClassesQualifiedNames()));
    return result.stream().distinct().collect(Collectors.toList());
  }

  public List<String> getDeltaFieldsQualifiedNames() {
    List<String> result = new ArrayList<>();
    deltas.forEach(d -> result.addAll(d.getFieldsQualifiedNames()));
    return result.stream().distinct().collect(Collectors.toList());
  }

  public List<String> getDeltaFieldTypesQualifiedNames() {
    List<String> result = new ArrayList<>();
    deltas.forEach(d -> result.addAll(d.getFieldTypesQualifiedNames()));
    return result.stream().distinct().collect(Collectors.toList());
  }

  public List<String> getDeltaMethodsQualifiedNames() {
    List<String> result = new ArrayList<>();
    deltas.forEach(d -> result.addAll(d.getMethodsQualifiedNames()));
    return result.stream().distinct().collect(Collectors.toList());
  }

  public List<String> getDeltaConstructorsQualifiedNames() {
    List<String> result = new ArrayList<>();
    deltas.forEach(d -> result.addAll(d.getConstructorsQualifiedNames()));
    return result.stream().distinct().collect(Collectors.toList());
  }

  public List<String> getDeltaInvocationsQualifiedNames() {
    List<String> result = new ArrayList<>();
    deltas.forEach(d -> result.addAll(d.getInvocationsQualifiedNames()));
    return result.stream().distinct().collect(Collectors.toList());
  }

  public List<String> getDeltaFieldsAccessQualifiedNames() {
    List<String> result = new ArrayList<>();
    deltas.forEach(d -> result.addAll(d.getFieldAccessesQualifiedNames()));
    return result.stream().distinct().collect(Collectors.toList());

  }

  public List<String> getUpdatedQualifiedNames() {
    List<String> result = new ArrayList<>();
    deltas.forEach(d -> result.addAll(d.getUpdatesQualifiedNames()));
    return result.stream().distinct().collect(Collectors.toList());
  }

  public List<String> getUpdatedFieldsQualifiedNames() {
    List<String> result = new ArrayList<>();
    deltas.forEach(d -> result.addAll(d.getUpdatedFieldsQualifiedNames()));
    return result.stream().distinct().collect(Collectors.toList());
  }

  public List<String> getUpdatedInvocationsQualifiedNames() {
    List<String> result = new ArrayList<>();
    deltas.forEach(d -> result.addAll(d.getUpdatedInvocationsQualifiedNames()));
    return result.stream().distinct().collect(Collectors.toList());
  }

  public List<String> getVisibilityActionsQualifiedNames() {
    List<String> result = new ArrayList<>();
    deltas.forEach(d -> result.addAll(d.getVisibilityActionsQualifiedNames()));
    return result.stream().distinct().collect(Collectors.toList());
  }

  public boolean hasInsertInvocationActions() {
    return deltas.stream().anyMatch(d -> d.hasInsertInvocationActions());
  }

  public boolean hasInsertMethodActions() {
    return deltas.stream().anyMatch(d -> d.hasInsertMethodActions());
  }

  public boolean hasInsertConstructorActions() {
    return deltas.stream().anyMatch(d -> d.hasInsertConstructorActions());
  }

  public boolean hasInsertFieldActions() {
    return deltas.stream().anyMatch(d -> d.hasInsertFieldActions());
  }

  public boolean hasInsertFieldAccessActions() {
    return deltas.stream().anyMatch(d -> d.hasInsertFieldAccessActions());
  }

  public boolean hasInsertClassActions() {
    return deltas.stream().anyMatch(d -> d.hasInsertClassActions());
  }

  public boolean hasDeleteInvocationActions() {
    return deltas.stream().anyMatch(d -> d.hasDeleteInvocationActions());
  }

  public boolean hasDeleteFieldActions() {
    return deltas.stream().anyMatch(d -> d.hasDeleteFieldActions());
  }

  public boolean hasDeleteMethodActions() {
    return deltas.stream().anyMatch(d -> d.hasDeleteMethodActions());
  }

  public boolean hasDeleteConstructorsActions() {
    return deltas.stream().anyMatch(d -> d.hasDeleteConstructorsActions());
  }

  public boolean hasDeleteFieldAccessActions() {
    return deltas.stream().anyMatch(d -> d.hasDeleteFieldAccessActions());
  }

  public boolean hasUpdateActions() {
    return deltas.stream().anyMatch(d -> d.hasUpdateActions());
  }

  public boolean hasUpdateFieldTypeActions() {
    return deltas.stream().anyMatch(d -> d.hasUpdateFieldTypeActions());
  }

  public boolean hasUpdateFieldActions() {
    return deltas.stream().anyMatch(d -> d.hasUpdateFieldActions());
  }

  public boolean hasUpdateInvocationActions() {
    return deltas.stream().anyMatch(d -> d.hasUpdateInvocationActions());
  }

  public boolean hasVisibilityActions() {
    return deltas.stream().anyMatch(d -> d.hasVisibilityActions());
  }

  public boolean hasFields() {
    return baseInstance.hasFields();
  }

  public boolean hasMethods() {
    return baseInstance.hasMethods();
  }

  public boolean hasInterfaces() {
    return baseInstance.hasInterfaces();
  }

  public boolean hasConstructors() {
    return baseInstance.hasConstructors();
  }

  public boolean hasSuperClass() {
    return baseInstance.hasSuperClass();
  }

  public boolean hasCompatible() {
    return baseInstance.hasCompatible();
  }

  public boolean hasFieldAccesses() {
    return baseInstance.hasFieldAccesses();
  }

  public boolean hasInvocations() {
    return baseInstance.hasInvocations();
  }


}
