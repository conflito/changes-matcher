package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.ClassInstance;
import org.conflito.matcher.entities.FieldInstance;

public class InsertFieldAction extends InsertAction {

  private final FieldInstance insertedField;

  private final ClassInstance holderClass;

  public InsertFieldAction(FieldInstance insertedField, ClassInstance holderClass) {
    super();
    this.insertedField = insertedField;
    this.holderClass = holderClass;
  }

  public FieldInstance getInsertedField() {
    return insertedField;
  }

  public ClassInstance getHolderClass() {
    return holderClass;
  }

  public String getInsertedFieldQualifiedName() {
    return insertedField.getQualifiedName();
  }

  public String toString() {
    String result = "insert " +
        insertedField.getVisibility().toString().toLowerCase() + " field "
        + insertedField.getQualifiedName() + " in "
        + holderClass.getQualifiedName();
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((holderClass == null) ? 0 : holderClass.hashCode());
    result = prime * result + ((insertedField == null) ? 0 : insertedField.hashCode());
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
    if (!(obj instanceof InsertFieldAction)) {
      return false;
    }
    InsertFieldAction other = (InsertFieldAction) obj;
    if (holderClass == null) {
      if (other.holderClass != null) {
        return false;
      }
    } else if (!holderClass.equals(other.holderClass)) {
      return false;
    }
    if (insertedField == null) {
      return other.insertedField == null;
    } else
      return insertedField.equals(other.insertedField);
  }

}
