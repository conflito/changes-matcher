package org.conflito.matcher.entities;

import spoon.reflect.factory.TypeFactory;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.reflect.reference.CtTypeReference;

/**
 * A class representing a Type in the system's domain
 * 
 * @author Nuno Castanho
 *
 */
public class Type {

	private CtTypeReference<?> typeRef;

	/**
	 * Creates an instance of Type
	 * @param type
	 * 			the Spoon CtTypeReference
	 */
	public Type(CtTypeReference<?> type) {
		super();
		this.typeRef = type;
	}
	
	/**
	 * Creates an instance of Type from another instance
	 * @param t
	 * 			the other instance
	 */
	public Type(Type t) {
		this.typeRef = t.typeRef;
	}
	
	/**
	 * Get this type's name
	 * @return the qualified name of this type
	 */
	public String getTypeName() {
		return typeRef.getQualifiedName();
	}
	
	/**
	 * Checks if this type is sub-type of another
	 * @param t
	 * 			the other type
	 * @return true if this type is sub-type of t; false otherwise
	 */
	public boolean isSubtypeOf(Type t) {
		return typeRef.isSubtypeOf(t.typeRef);
	}
	
	/**
	 * Checks if this type is a primitive type
	 * @return true if this type is primitive; false otherwise
	 */
	public boolean isPrimitive() {
		return typeRef.isPrimitive();
	}
	
	/**
	 * Checks if this type is an array
	 * @return true if this type is an array; false otherwise
	 */
	public boolean isArray() {
		return typeRef.isArray();
	}
	
	/**
	 * Checks if this type is the void (return type) type
	 * @return true if this type is the void type; false otherwise
	 */
	public boolean isVoid() {
		return typeRef.equals(new TypeFactory().VOID_PRIMITIVE);
	}
	
	/**
	 * Get the component type of this array type
	 * @return the component type of this array type; null if this type is not
	 * 		an array type
	 */
	public Type getArrayType() {
		if(!isArray())
			return null;
		CtArrayTypeReference<?> arrayTypeRef = (CtArrayTypeReference<?>) typeRef;
		Type result = new Type(arrayTypeRef.getComponentType());
		if(result.isArray())
			return result.getArrayType();
		else
			return result;
	}
	
	public boolean equals(Object o) {
		return (this == o) || (o instanceof Type && typeRef.equals(((Type)o).typeRef));
	}
	
	public int hashCode() {
		return typeRef.hashCode();
	}
	
	public String toString() {
		return getTypeName();
	}
	
	/**
	 * Calculates this type's descriptor as per the Java format
	 * 
	 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">
	 * 		https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html</a>
	 * @return this type's descriptor
	 */
	public String getDescriptor() {
		if(isVoid())
			return "V";
		else if(isPrimitive())
			return toPrimitiveDescriptor();
		else if(!isArray()) {
			return toComplexDescriptor();
		}
		else {
			return toArrayDescriptor();
		}
	}
	
	/**
	 * Calculates this type's descriptor if it's an array
	 * @return the array descriptor for this type
	 */
	private String toArrayDescriptor() {
		StringBuilder result = new StringBuilder();
		CtTypeReference<?> t = typeRef;
		while(t.isArray()) {
			CtArrayTypeReference<?> arrayTypeRef = (CtArrayTypeReference<?>) t;
			t = arrayTypeRef.getComponentType();
			result.append("[");
		}
		
		result.append(new Type(t).getDescriptor());
		
		return result.toString();
	}
	
	/**
	 * Calculates the descriptor of this non-primitive type
	 * @return the descriptor of this non-primitive type
	 */
	private String toComplexDescriptor() {
		return "L" + typeRef.getQualifiedName().replace('.', '/') + ";";
	}

	/**
	 * Calculates the descriptive of this primitive type
	 * @return the descriptor of this primitive type
	 */
	private String toPrimitiveDescriptor() {
		String result = null;
		TypeFactory f = new TypeFactory();
		if(typeRef.equals(f.INTEGER_PRIMITIVE)) {
			result = "I";
		}
		else if(typeRef.equals(f.BOOLEAN_PRIMITIVE)) {
			result = "Z";
		}
		else if(typeRef.equals(f.BYTE_PRIMITIVE)) {
			result = "B";
		}
		else if(typeRef.equals(f.SHORT_PRIMITIVE)) {
			result = "S";
		}
		else if(typeRef.equals(f.CHARACTER_PRIMITIVE)) {
			result = "C";
		}
		else if(typeRef.equals(f.LONG_PRIMITIVE)) {
			result = "J";
		}
		else if(typeRef.equals(f.FLOAT_PRIMITIVE)) {
			result = "F";
		}
		else if(typeRef.equals(f.DOUBLE_PRIMITIVE)){
			result = "D";
		}
		return result;
	}
	
	/**
	 * Obtains an independent wrapper for a given primitive type
	 * @param type
	 * 			the type to obtain the wrapper type
	 * @return the wrapper of type; null if type is not primitive
	 */
	public static Type primitiveToWrapper(Type type) {
		TypeFactory f = new TypeFactory();
		Type result = null;
		if(type.typeRef.equals(f.INTEGER_PRIMITIVE)) {
			result = new Type(f.INTEGER);
		}
		else if(type.typeRef.equals(f.BOOLEAN_PRIMITIVE)) {
			result = new Type(f.BOOLEAN);
		}
		else if(type.typeRef.equals(f.BYTE_PRIMITIVE)) {
			result = new Type(f.BYTE);
		}
		else if(type.typeRef.equals(f.SHORT_PRIMITIVE)) {
			result = new Type(f.SHORT);
		}
		else if(type.typeRef.equals(f.CHARACTER_PRIMITIVE)) {
			result = new Type(f.CHARACTER);
		}
		else if(type.typeRef.equals(f.LONG_PRIMITIVE)) {
			result = new Type(f.LONG);
		}
		else if(type.typeRef.equals(f.FLOAT_PRIMITIVE)) {
			result = new Type(f.FLOAT);
		}
		else if(type.typeRef.equals(f.DOUBLE_PRIMITIVE)){
			result = new Type(f.DOUBLE);
		}
		return result;
	}
}
