package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.ClassInstance;
import org.conflito.matcher.entities.FieldInstance;

public class DeleteFieldAction extends DeleteAction {

  private final FieldInstance deletedField;

  private final ClassInstance holderClass;

  public DeleteFieldAction(FieldInstance deletedField, ClassInstance holderClass) {
    super();
    this.deletedField = deletedField;
    this.holderClass = holderClass;
  }

  public FieldInstance getDeletedField() {
    return deletedField;
  }

  public ClassInstance getHolderClass() {
    return holderClass;
  }

  public String getDeletedFieldQualifiedName() {
    return deletedField.getQualifiedName();
  }

  public String toString() {
    String result = "delete " +
        deletedField.getVisibility().toString().toLowerCase() + " field "
        + deletedField.getQualifiedName() + " in "
        + holderClass.getQualifiedName();
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((deletedField == null) ? 0 : deletedField.hashCode());
    result = prime * result + ((holderClass == null) ? 0 : holderClass.hashCode());
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
    if (!(obj instanceof DeleteFieldAction)) {
      return false;
    }
    DeleteFieldAction other = (DeleteFieldAction) obj;
    if (deletedField == null) {
      if (other.deletedField != null) {
        return false;
      }
    } else if (!deletedField.equals(other.deletedField)) {
      return false;
    }
    if (holderClass == null) {
      return other.holderClass == null;
    } else
      return holderClass.equals(other.holderClass);
  }

}
