package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.FieldInstance;
import org.conflito.matcher.entities.Type;

public class UpdateFieldTypeAction extends UpdateAction {

  private final FieldInstance updatedField;

  private final Type newType;

  public UpdateFieldTypeAction(FieldInstance updatedField, Type newType) {
    super();
    this.updatedField = updatedField;
    this.newType = newType;
  }

  public FieldInstance getUpdatedField() {
    return updatedField;
  }

  public Type getNewType() {
    return newType;
  }

  public String getNewTypeName() {
    if (newType.isArray()) {
      return newType.getArrayType().toString();
    }
    return newType.toString();
  }

  @Override
  public String getUpdatedEntityQualifiedName() {
    return updatedField.getQualifiedName();
  }

  @Override
  public String toString() {
    String result = "update field " + updatedField.getQualifiedName() + " type from "
        + updatedField.getType() + " to " + newType;
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((newType == null) ? 0 : newType.hashCode());
    result = prime * result + ((updatedField == null) ? 0 : updatedField.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof UpdateFieldTypeAction)) {
      return false;
    }
    UpdateFieldTypeAction other = (UpdateFieldTypeAction) obj;
    if (newType == null) {
      if (other.newType != null) {
        return false;
      }
    } else if (!newType.equals(other.newType)) {
      return false;
    }
    if (updatedField == null) {
      return other.updatedField == null;
    } else
      return updatedField.equals(other.updatedField);
  }


}
