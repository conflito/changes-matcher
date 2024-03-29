package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.ClassInstance;
import org.conflito.matcher.entities.ConstructorInstance;

public class DeleteConstructorAction extends DeleteAction {

  private final ConstructorInstance deletedConstructor;

  private final ClassInstance holderClass;

  public DeleteConstructorAction(ConstructorInstance deletedConstructor,
      ClassInstance holderClass) {
    super();
    this.deletedConstructor = deletedConstructor;
    this.holderClass = holderClass;
  }

  public ConstructorInstance getDeletedConstructor() {
    return deletedConstructor;
  }

  public ClassInstance getHolderClass() {
    return holderClass;
  }

  public String getDeletedConstructorQualifiedName() {
    return deletedConstructor.getQualifiedName();
  }

  public String toString() {
    String result = "delete " +
        deletedConstructor.getVisibility().toString().toLowerCase() + " constructor "
        + deletedConstructor.getQualifiedName() + " in "
        + holderClass.getQualifiedName();
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((deletedConstructor == null) ? 0 : deletedConstructor.hashCode());
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
    if (!(obj instanceof DeleteConstructorAction)) {
      return false;
    }
    DeleteConstructorAction other = (DeleteConstructorAction) obj;
    if (deletedConstructor == null) {
      if (other.deletedConstructor != null) {
        return false;
      }
    } else if (!deletedConstructor.equals(other.deletedConstructor)) {
      return false;
    }
    if (holderClass == null) {
      return other.holderClass == null;
    } else
      return holderClass.equals(other.holderClass);
  }


}
