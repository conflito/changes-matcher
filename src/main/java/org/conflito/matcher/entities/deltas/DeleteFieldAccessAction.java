package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.FieldAccessInstance;
import org.conflito.matcher.entities.MethodInstance;

public class DeleteFieldAccessAction extends DeleteAction {

  private final FieldAccessInstance deletedFieldAccess;

  private final MethodInstance holderMethod;

  public DeleteFieldAccessAction(FieldAccessInstance deletedFieldAccess,
      MethodInstance holderMethod) {
    super();
    this.deletedFieldAccess = deletedFieldAccess;
    this.holderMethod = holderMethod;
  }

  public FieldAccessInstance getDeletedFieldAccess() {
    return deletedFieldAccess;
  }

  public MethodInstance getHolderMethod() {
    return holderMethod;
  }

  public String getDeletedFieldAccessQualifiedName() {
    return deletedFieldAccess.getQualifiedName();
  }

  public String toString() {
    String result = "delete " +
        deletedFieldAccess.getAccessType().toString().toLowerCase() + " of field "
        + deletedFieldAccess.getQualifiedName() + " in "
        + holderMethod.getQualifiedName();
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((deletedFieldAccess == null) ? 0 : deletedFieldAccess.hashCode());
    result = prime * result + ((holderMethod == null) ? 0 : holderMethod.hashCode());
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
    if (!(obj instanceof DeleteFieldAccessAction)) {
      return false;
    }
    DeleteFieldAccessAction other = (DeleteFieldAccessAction) obj;
    if (deletedFieldAccess == null) {
      if (other.deletedFieldAccess != null) {
        return false;
      }
    } else if (!deletedFieldAccess.equals(other.deletedFieldAccess)) {
      return false;
    }
    if (holderMethod == null) {
      return other.holderMethod == null;
    } else
      return holderMethod.equals(other.holderMethod);
  }
}
