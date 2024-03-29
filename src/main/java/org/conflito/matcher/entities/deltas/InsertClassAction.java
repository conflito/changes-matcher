package org.conflito.matcher.entities.deltas;

import java.util.List;
import org.conflito.matcher.entities.ClassInstance;

public class InsertClassAction extends InsertAction {

  private final ClassInstance insertedClass;

  public InsertClassAction(ClassInstance insertedClass) {
    super();
    this.insertedClass = insertedClass;
  }

  public ClassInstance getInsertedClass() {
    return insertedClass;
  }

  public String getInsertedClassQualifiedName() {
    return insertedClass.getQualifiedName();
  }

  public List<String> getInsertedMethodsQualifiedName() {
    return insertedClass.getMethodsQualifiedNames();
  }

  @Override
  public String toString() {
    String result = "insert new class " + insertedClass.getQualifiedName()
        + "\n" + insertedClass.toStringDebug();
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((insertedClass == null) ? 0 : insertedClass.hashCode());
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
    if (!(obj instanceof InsertClassAction)) {
      return false;
    }
    InsertClassAction other = (InsertClassAction) obj;
    if (insertedClass == null) {
      return other.insertedClass == null;
    } else
      return insertedClass.equals(other.insertedClass);
  }

}
