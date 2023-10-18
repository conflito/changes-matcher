package org.conflito.matcher.entities.deltas;


import org.conflito.matcher.entities.ClassInstance;
import org.conflito.matcher.entities.MethodInstance;

public class DeleteMethodAction extends DeleteAction {

  private final MethodInstance deletedMethod;

  private final ClassInstance holderClass;

  public DeleteMethodAction(MethodInstance insertedMethod, ClassInstance holderClass) {
    super();
    this.deletedMethod = insertedMethod;
    this.holderClass = holderClass;
  }

  public MethodInstance getDeletedMethod() {
    return deletedMethod;
  }

  public ClassInstance getHolderClass() {
    return holderClass;
  }

  public String getDeletedMethodQualifiedName() {
    return deletedMethod.getQualifiedName();
  }

  public String toString() {
    String result = "delete " +
        deletedMethod.getVisibility().toString().toLowerCase() + " method "
        + deletedMethod.getQualifiedName() + " in "
        + deletedMethod.getQualifiedName();
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((deletedMethod == null) ? 0 : deletedMethod.hashCode());
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
    if (!(obj instanceof DeleteMethodAction)) {
      return false;
    }
    DeleteMethodAction other = (DeleteMethodAction) obj;
    if (deletedMethod == null) {
      if (other.deletedMethod != null) {
        return false;
      }
    } else if (!deletedMethod.equals(other.deletedMethod)) {
      return false;
    }
    if (holderClass == null) {
      return other.holderClass == null;
    } else
      return holderClass.equals(other.holderClass);
  }
}
