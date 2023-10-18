package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.ConstructorInstance;
import org.conflito.matcher.entities.MethodInstance;

public class UpdateDependencyAction extends UpdateAction {

  private MethodInstance previousHolderMethod;

  private ConstructorInstance previousHolderConstructor;

  private MethodInstance newHolderMethod;

  private ConstructorInstance newHolderConstructor;

  private final MethodInstance oldDependency;

  private final MethodInstance newDependency;

  public UpdateDependencyAction(ConstructorInstance previousHolderConstructor,
      ConstructorInstance newHolderConstructor, MethodInstance oldDependency,
      MethodInstance newDependency) {
    super();
    this.previousHolderConstructor = previousHolderConstructor;
    this.newHolderConstructor = newHolderConstructor;
    this.oldDependency = oldDependency;
    this.newDependency = newDependency;
  }

  public UpdateDependencyAction(MethodInstance previousHolderMethod, MethodInstance newHolderMethod,
      MethodInstance oldDependency, MethodInstance newDependency) {
    super();
    this.previousHolderMethod = previousHolderMethod;
    this.newHolderMethod = newHolderMethod;
    this.oldDependency = oldDependency;
    this.newDependency = newDependency;
  }

  public MethodInstance getOldDependency() {
    return oldDependency;
  }

  public MethodInstance getNewDependency() {
    return newDependency;
  }

  public MethodInstance getPreviousHolderMethod() {
    return previousHolderMethod;
  }

  public ConstructorInstance getPreviousHolderConstructor() {
    return previousHolderConstructor;
  }

  public MethodInstance getNewHolderMethod() {
    return newHolderMethod;
  }

  public ConstructorInstance getNewHolderConstructor() {
    return newHolderConstructor;
  }

  @Override
  public String getUpdatedEntityQualifiedName() {
    return newDependency.getQualifiedName();
  }

  public ConstructorInstance getHolderConstructor() {
    return previousHolderConstructor;
  }

  public MethodInstance getHolderMethod() {
    return previousHolderMethod;
  }

  public boolean updatedInMethod() {
    return previousHolderMethod != null;
  }

  public boolean updatedInConstructor() {
    return previousHolderConstructor != null;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder("update invocation ");
    result.append(oldDependency.getQualifiedName() + " in ");
    if (updatedInConstructor()) {
      result.append(previousHolderConstructor.getQualifiedName() + " to ");
    }
    if (updatedInMethod()) {
      result.append(previousHolderMethod.getQualifiedName() + " to ");
    }
    result.append(newDependency.getQualifiedName());
    return result.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((newDependency == null) ? 0 : newDependency.hashCode());
    result =
        prime * result + ((newHolderConstructor == null) ? 0 : newHolderConstructor.hashCode());
    result = prime * result + ((newHolderMethod == null) ? 0 : newHolderMethod.hashCode());
    result = prime * result + ((oldDependency == null) ? 0 : oldDependency.hashCode());
    result = prime * result + ((previousHolderConstructor == null) ? 0
        : previousHolderConstructor.hashCode());
    result =
        prime * result + ((previousHolderMethod == null) ? 0 : previousHolderMethod.hashCode());
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
    if (!(obj instanceof UpdateDependencyAction)) {
      return false;
    }
    UpdateDependencyAction other = (UpdateDependencyAction) obj;
    if (newDependency == null) {
      if (other.newDependency != null) {
        return false;
      }
    } else if (!newDependency.equals(other.newDependency)) {
      return false;
    }
    if (newHolderConstructor == null) {
      if (other.newHolderConstructor != null) {
        return false;
      }
    } else if (!newHolderConstructor.equals(other.newHolderConstructor)) {
      return false;
    }
    if (newHolderMethod == null) {
      if (other.newHolderMethod != null) {
        return false;
      }
    } else if (!newHolderMethod.equals(other.newHolderMethod)) {
      return false;
    }
    if (oldDependency == null) {
      if (other.oldDependency != null) {
        return false;
      }
    } else if (!oldDependency.equals(other.oldDependency)) {
      return false;
    }
    if (previousHolderConstructor == null) {
      if (other.previousHolderConstructor != null) {
        return false;
      }
    } else if (!previousHolderConstructor.equals(other.previousHolderConstructor)) {
      return false;
    }
    if (previousHolderMethod == null) {
      return other.previousHolderMethod == null;
    } else
      return previousHolderMethod.equals(other.previousHolderMethod);
  }

}
