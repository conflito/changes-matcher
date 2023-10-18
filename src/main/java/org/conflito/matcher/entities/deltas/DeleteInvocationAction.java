package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.ConstructorInstance;
import org.conflito.matcher.entities.MethodInstance;
import org.conflito.matcher.entities.MethodInvocationInstance;

public class DeleteInvocationAction extends DeleteAction {

  private final MethodInvocationInstance deletedInvocation;

  private ConstructorInstance holderConstructor;

  private MethodInstance holderMethod;

  public DeleteInvocationAction(MethodInvocationInstance deletedInvocation,
      ConstructorInstance holderConstructor) {
    super();
    this.deletedInvocation = deletedInvocation;
    this.holderConstructor = holderConstructor;
  }

  public DeleteInvocationAction(MethodInvocationInstance deletedInvocation,
      MethodInstance holderMethod) {
    super();
    this.deletedInvocation = deletedInvocation;
    this.holderMethod = holderMethod;
  }

  public MethodInvocationInstance getDeletedInvocation() {
    return deletedInvocation;
  }

  public ConstructorInstance getHolderConstructor() {
    return holderConstructor;
  }

  public MethodInstance getHolderMethod() {
    return holderMethod;
  }

  public String getDeletedInvocationQualifiedName() {
    return deletedInvocation.getQualifiedName();
  }

  public boolean insertedInMethod() {
    return holderMethod != null;
  }

  public boolean insertedInConstructor() {
    return holderConstructor != null;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder("insert ");
    result.append("method invocation of ");
    result.append(deletedInvocation.getQualifiedName() + " in ");
    if (insertedInMethod()) {
      result.append(holderMethod.getQualifiedName());
    }
    if (insertedInConstructor()) {
      result.append(holderConstructor.getQualifiedName());
    }
    return result.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((deletedInvocation == null) ? 0 : deletedInvocation.hashCode());
    result = prime * result + ((holderConstructor == null) ? 0 : holderConstructor.hashCode());
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
    if (!(obj instanceof DeleteInvocationAction)) {
      return false;
    }
    DeleteInvocationAction other = (DeleteInvocationAction) obj;
    if (deletedInvocation == null) {
      if (other.deletedInvocation != null) {
        return false;
      }
    } else if (!deletedInvocation.equals(other.deletedInvocation)) {
      return false;
    }
    if (holderConstructor == null) {
      if (other.holderConstructor != null) {
        return false;
      }
    } else if (!holderConstructor.equals(other.holderConstructor)) {
      return false;
    }
    if (holderMethod == null) {
      return other.holderMethod == null;
    } else
      return holderMethod.equals(other.holderMethod);
  }

}
