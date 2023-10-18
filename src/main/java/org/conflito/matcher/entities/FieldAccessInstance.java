package org.conflito.matcher.entities;

/**
 * A class representing an access to a field
 *
 * @author Nuno Castanho
 */
public class FieldAccessInstance {

  private final String qualifiedName;

  private final FieldAccessType accessType;

  /**
   * Creates an instance of FieldAccessInstance
   *
   * @param qualifiedName the accessed field's name
   * @param accessType    the type of access
   */
  public FieldAccessInstance(String qualifiedName, FieldAccessType accessType) {
    super();
    this.qualifiedName = qualifiedName;
    this.accessType = accessType;
  }

  /**
   * Creates an instance of FieldAccessInstance from another instance
   *
   * @param access the other instance
   */
  public FieldAccessInstance(FieldAccessInstance access) {
    this.qualifiedName = access.qualifiedName;
    this.accessType = access.accessType;
  }

  /**
   * Get the name of the accessed field
   *
   * @return the name of the accessed field
   */
  public String getQualifiedName() {
    return qualifiedName;
  }

  /**
   * Get the type of the access to the field
   *
   * @return the type of the access
   */
  public FieldAccessType getAccessType() {
    return accessType;
  }

  /**
   * Checks if this access is a read
   *
   * @return true if this access is a read to the field; false otherwise
   */
  public boolean isFieldRead() {
    return getAccessType() == FieldAccessType.READ;
  }

  /**
   * Checks if this access is a write
   *
   * @return true if this access is a write to the field; false otherwise
   */
  public boolean isFieldWrite() {
    return getAccessType() == FieldAccessType.WRITE;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accessType == null) ? 0 : accessType.hashCode());
    result = prime * result + ((qualifiedName == null) ? 0 : qualifiedName.hashCode());
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
    if (!(obj instanceof FieldAccessInstance)) {
      return false;
    }
    FieldAccessInstance other = (FieldAccessInstance) obj;
    if (accessType != other.accessType) {
      return false;
    }
    if (qualifiedName == null) {
      return other.qualifiedName == null;
    } else
      return qualifiedName.equals(other.qualifiedName);
  }


}
