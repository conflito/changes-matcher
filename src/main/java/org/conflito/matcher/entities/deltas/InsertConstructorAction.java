package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.ClassInstance;
import org.conflito.matcher.entities.ConstructorInstance;

public class InsertConstructorAction extends InsertAction {

  private final ConstructorInstance insertedConstructor;

  private final ClassInstance holderClass;

  public InsertConstructorAction(ConstructorInstance insertedConstructor,
      ClassInstance holderClass) {
    super();
    this.insertedConstructor = insertedConstructor;
    this.holderClass = holderClass;
  }

  public ConstructorInstance getInsertedConstructor() {
    return insertedConstructor;
  }

  public String getInsertedConstructorQualifiedName() {
    return insertedConstructor.getQualifiedName();
  }

  public ClassInstance getHolderClass() {
    return holderClass;
  }

  public String toString() {
    String result = "insert " +
        insertedConstructor.getVisibility().toString().toLowerCase() + " constructor "
        + insertedConstructor.getQualifiedName() + " in "
        + holderClass.getQualifiedName();
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((holderClass == null) ? 0 : holderClass.hashCode());
    result = prime * result + ((insertedConstructor == null) ? 0 : insertedConstructor.hashCode());
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
    if (!(obj instanceof InsertConstructorAction)) {
      return false;
    }
    InsertConstructorAction other = (InsertConstructorAction) obj;
    if (holderClass == null) {
      if (other.holderClass != null) {
        return false;
      }
    } else if (!holderClass.equals(other.holderClass)) {
      return false;
    }
    if (insertedConstructor == null) {
      return other.insertedConstructor == null;
    } else
      return insertedConstructor.equals(other.insertedConstructor);
  }

}
