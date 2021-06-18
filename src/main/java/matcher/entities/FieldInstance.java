package matcher.entities;

import matcher.entities.deltas.Visible;

/**
 * A class representing a field in the system's domain
 * 
 * @author Nuno Castanho
 *
 */
public class FieldInstance implements Visible {

	private String name;
	
	private Visibility visibility;
	
	private Type type;

	/**
	 * Creates an instance of FieldInstance
	 * @param name
	 * 			the name of this field
	 * @param visibility
	 * 			the visibility this field
	 * @param type
	 * 			the type of this field
	 */
	public FieldInstance(String name, Visibility visibility, Type type) {
		super();
		this.name = name;
		this.visibility = visibility;
		this.type = type;
	}
	
	/**
	 * Creates an instance of FieldInstance from another instance
	 * @param field
	 * 			the other instance
	 */
	public FieldInstance(FieldInstance field) {
		this.name = field.name;
		this.visibility = field.visibility;
		this.type = new Type(field.type);
	}

	/**
	 * Get the name of this field
	 * @return the name of this field
	 */
	public String getQualifiedName() {
		return name;
	}

	/**
	 * Get the visibility of this field
	 * @return the visibility of this field
	 */
	public Visibility getVisibility() {
		return visibility;
	}

	/**
	 * Get the type of this field
	 * @return the type of this field
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Get the name of this field's type
	 * @return the name of this field's type 
	 */
	public String getTypeName() {
		if(type.isArray())
			return type.getArrayType().getTypeName();
		return type.getTypeName();
	}
	
	/**
	 * Checks if this field has a primitive type
	 * @return true if this field has a primitive type; false otherwise
	 */
	public boolean primitiveType() {
		return type.isPrimitive();
	}
	
	public boolean equals(Object o) {
		return (this == o) || (o instanceof FieldInstance && equalsFieldInstance((FieldInstance)o));
	}

	private boolean equalsFieldInstance(FieldInstance o) {
		return getQualifiedName().equals(o.getQualifiedName());
	}
	
	public int hashCode() {
		return getQualifiedName().hashCode();
	}
	
}
