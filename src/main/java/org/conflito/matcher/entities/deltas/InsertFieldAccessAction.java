package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.FieldAccessInstance;
import org.conflito.matcher.entities.MethodInstance;

public class InsertFieldAccessAction extends InsertAction {

  private final FieldAccessInstance insertedFieldAccess;

  private final MethodInstance holderMethod;

  public InsertFieldAccessAction(FieldAccessInstance insertedFieldAccess,
      MethodInstance holderMethod) {
    super();
    this.insertedFieldAccess = insertedFieldAccess;
    this.holderMethod = holderMethod;
  }

  public FieldAccessInstance getInsertedFieldAccess() {
    return insertedFieldAccess;
  }

  public String getInsertedFieldAccessQualifiedName() {
    return insertedFieldAccess.getQualifiedName();
  }

  public MethodInstance getHolderMethod() {
    return holderMethod;
  }

  public String toString() {
    String result = "insert " +
        insertedFieldAccess.getAccessType().toString().toLowerCase() + " of field "
        + insertedFieldAccess.getQualifiedName() + " in "
        + holderMethod.getQualifiedName();
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((holderMethod == null) ? 0 : holderMethod.hashCode());
    result = prime * result + ((insertedFieldAccess == null) ? 0 : insertedFieldAccess.hashCode());
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
    if (!(obj instanceof InsertFieldAccessAction)) {
      return false;
    }
    InsertFieldAccessAction other = (InsertFieldAccessAction) obj;
    if (holderMethod == null) {
      if (other.holderMethod != null) {
        return false;
      }
    } else if (!holderMethod.equals(other.holderMethod)) {
      return false;
    }
    if (insertedFieldAccess == null) {
      return other.insertedFieldAccess == null;
    } else
      return insertedFieldAccess.equals(other.insertedFieldAccess);
  }

}
