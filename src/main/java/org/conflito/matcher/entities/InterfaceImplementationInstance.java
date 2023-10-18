package org.conflito.matcher.entities;

/**
 * A class representing the implementation of an interface in the system's domain
 *
 * @author Nuno Castanho
 */
public class InterfaceImplementationInstance {

  private final String name;

  /**
   * Creates an instance of InterfaceImplementationInstance
   *
   * @param name the name of the implemented interface
   */
  public InterfaceImplementationInstance(String name) {
    super();
    this.name = name;
  }

  /**
   * Creates an instance of InterfaceImplementationInstance from another instance
   *
   * @param i the other instance
   */
  public InterfaceImplementationInstance(InterfaceImplementationInstance i) {
    this.name = i.name;
  }

  /**
   * Get the implemented interface's name
   *
   * @return the implemented interface's name
   */
  public String getName() {
    return name;
  }
}
