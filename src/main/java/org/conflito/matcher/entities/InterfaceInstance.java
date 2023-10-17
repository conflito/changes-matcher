package org.conflito.matcher.entities;

/**
 * A class representing an interface in the system's domain
 * 
 * @author Nuno Castanho
 *
 */
public class InterfaceInstance {

	private String name;

	/**
	 * Creates an instance of InterfaceInstance
	 * @param name
	 * 			the name of the interface
	 */
	public InterfaceInstance(String name) {
		super();
		this.name = name;
	}
	
	/**
	 * Creates an instance of InterfaceInstance from another instance
	 * @param i
	 * 			the other instance
	 */
	public InterfaceInstance(InterfaceInstance i) {
		this.name = i.name;
	}

	/**
	 * Get the interface's qualified name
	 * @return the interface's qualified name
	 */
	public String getQualifiedName() {
		return name;
	}
	
	/**
	 * Debug toString method for this interface
	 * @return a string representing this interface
	 */
	public String toStringDebug() {
		return "Interface " + name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof InterfaceInstance 
				&& equalsInterface((InterfaceInstance) obj));
	}
	
	private boolean equalsInterface(InterfaceInstance i) {
		return name.equals(i.getQualifiedName());
	}
	
	
}
