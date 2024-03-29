package org.conflito.matcher.patterns;

import org.conflito.matcher.entities.FieldAccessInstance;
import org.conflito.matcher.entities.FieldAccessType;

public class FieldAccessPattern {

  private final FreeVariable freeVariable;

  private final FieldAccessType accessType;

  public FieldAccessPattern(FreeVariable freeVariable, FieldAccessType accessType) {
    super();
    this.freeVariable = freeVariable;
    this.accessType = accessType;
  }

  public FieldAccessPattern(FieldAccessPattern fieldAccessPattern) {
    super();
    this.freeVariable = new FreeVariable(fieldAccessPattern.freeVariable);
    this.accessType = fieldAccessPattern.accessType;
  }

  public FreeVariable getFreeVariable() {
    return freeVariable;
  }

  public FieldAccessType getType() {
    return accessType;
  }

  public int getVariableId() {
    return freeVariable.getId();
  }

  public boolean isVariableId(int id) {
    return freeVariable.isId(id);
  }

  public void setVariableValue(int id, String value) {
    if (isVariableId(id)) {
      freeVariable.setValue(value);
    }
  }

  public void clean() {
    freeVariable.clean();
  }

  public boolean isFieldRead() {
    return accessType == FieldAccessType.READ;
  }

  public boolean isFieldWrite() {
    return accessType == FieldAccessType.WRITE;
  }

  public boolean isAnyAccess() {
    return !isFieldRead() && !isFieldWrite();
  }

  public boolean filled() {
    return freeVariable.hasValue();
  }

  public boolean matches(FieldAccessInstance accessInstance) {
    return filled() && (isAnyAccess() || sameAccess(accessInstance)) && sameName(accessInstance);
  }

  private boolean sameName(FieldAccessInstance accessInstance) {
    return accessInstance.getQualifiedName().equals(freeVariable.getValue());
  }

  private boolean sameAccess(FieldAccessInstance accessInstance) {
    return (isFieldRead() && accessInstance.isFieldRead()) ||
        (isFieldWrite() && accessInstance.isFieldWrite());
  }
}
