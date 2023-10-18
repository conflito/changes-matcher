package org.conflito.matcher.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.conflito.matcher.entities.deltas.Visible;

/**
 * A class representing a constructor in the system's domain
 *
 * @author Nuno Castanho
 */
public class ConstructorInstance implements Visible {

  private final Visibility visibility;

  private final List<Type> parameters;

  private ClassInstance classInstance;

  private final List<MethodInstance> directDependencies;

  /**
   * Creates an instance of ConstructorInstance
   *
   * @param visibility the visibility of the constructor
   */
  public ConstructorInstance(Visibility visibility) {
    super();
    this.visibility = visibility;
    this.parameters = new ArrayList<>();
    this.directDependencies = new ArrayList<>();
  }

  /**
   * Creates an instance of ConstructorInstance from another instance
   *
   * @param c the other instance
   */
  public ConstructorInstance(ConstructorInstance c) {
    this.visibility = c.visibility;
    this.parameters = new ArrayList<>();
    this.directDependencies = new ArrayList<>();

    for (Type t : c.parameters) {
      this.parameters.add(new Type(t));
    }

    for (MethodInstance m : c.directDependencies) {
      this.directDependencies.add(new MethodInstance(m));
    }
  }

  /**
   * Creates an instance of ConstructorInstance
   *
   * @param visibility the visibility of the constructor
   * @param parameters the parameters of the constructor
   */
  public ConstructorInstance(Visibility visibility, List<Type> parameters) {
    super();
    this.visibility = visibility;
    this.parameters = parameters;
    this.directDependencies = new ArrayList<>();
  }

  /**
   * Get this constructor's direct method dependencies
   *
   * @return this constructor's direct method dependencies
   */
  public List<MethodInstance> getDirectDependencies() {
    return directDependencies;
  }

  /**
   * Adds a method direct method dependency to this constructor
   *
   * @param m the direct dependency
   */
  public void addDirectDependency(MethodInstance m) {
    directDependencies.add(m);
  }

  /**
   * Checks if this constructor has direct method dependencies
   *
   * @return true if this constructor has direct method dependencies; false otherwise
   */
  public boolean hasDependencies() {
    return !directDependencies.isEmpty();
  }

  /**
   * Checks if this constructor depends (directly or not) on a method with the given name
   *
   * @param methodName the name of the method to test dependency
   * @return true if this constructor depends on a method with a name of methodName; false otherwise
   */
  public boolean dependsOn(String methodName) {
    boolean result =
        directDependencies.stream()
            .anyMatch(m -> m.getQualifiedName().equals(methodName));
    if (!result) {
      for (MethodInstance m : directDependencies) {
        if (m.dependsOn(methodName)) {
          result = true;
        }
      }
    }
    return result;
  }

  /**
   * Get this constructor's visibility
   *
   * @return this constructor's visibility
   */
  public Visibility getVisibility() {
    return visibility;
  }

  /**
   * Get this constructor's parameters' types
   *
   * @return this constructor's parameter's types
   */
  public List<Type> getParameters() {
    return parameters;
  }

  /**
   * Sets this constructor's class
   *
   * @param classInstance the class that contains this constructor
   */
  public void setClassInstance(ClassInstance classInstance) {
    this.classInstance = classInstance;
  }

  /**
   * Get this constructor's simple name
   *
   * @return the return is always '&lt;init&gt;'
   */
  public String getName() {
    return "<init>";
  }

  /**
   * Get this constructor's qualified name<br> A constructor's qualified name is made up of it's
   * class' name, its name and its parameters
   *
   * @return this constructor's qualified name
   */
  public String getQualifiedName() {
    return classInstance.getQualifiedName() + "."
        + getName()
        + parametersToString();
  }

  /**
   * Calculates this constructor's descriptor as per the Java format
   *
   * @return this constructor's descriptor
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">
   * https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html</a>
   */
  public String getDescriptor() {
    StringBuilder result = new StringBuilder("(");
    parameters.stream()
        .map(Type::getDescriptor)
        .forEach(s -> result.append(s));
    result.append(")V");
    return result.toString();
  }

  /**
   * Get the qualified names of the methods invoked in this constructor
   *
   * @return the qualified names of the methods invoked in this constructor
   */
  public List<String> getInvocationsQualifiedNames() {
    return directDependencies.stream()
        .map(MethodInstance::getQualifiedName)
        .collect(Collectors.toList());
  }

  /**
   * Get this constructor's simple name<br> A constructor's simple name is its class name and its
   * parameters
   *
   * @return
   */
  public String getSimpleName() {
    return classInstance.getName() + parametersToString();
  }

  /**
   * Calculates the string form for this constructor's parameters
   *
   * @return the string of the parameters of this constructor
   */
  private String parametersToString() {
    return getParameters().toString().replace("[", "(").replace("]", ")");
  }

  public boolean equals(Object o) {
    return (this == o) || (o instanceof ConstructorInstance
        && equalsConstructorInstance((ConstructorInstance) o));
  }

  private boolean equalsConstructorInstance(ConstructorInstance o) {
    return getQualifiedName().equals(o.getQualifiedName());
  }

  public int hashCode() {
    return getQualifiedName().hashCode();
  }

}
