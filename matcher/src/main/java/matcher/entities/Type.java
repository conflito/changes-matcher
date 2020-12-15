package matcher.entities;

import spoon.reflect.factory.TypeFactory;
import spoon.reflect.reference.CtTypeReference;

public class Type {

	private CtTypeReference<?> typeRef;

	public Type(CtTypeReference<?> type) {
		super();
		this.typeRef = type;
	}

	public String toString() {
		return typeRef.toString();
	}
	
	public boolean isSubtypeOf(Type t) {
		return typeRef.isSubtypeOf(t.typeRef);
	}
	
	public boolean isPrimitive() {
		return typeRef.isPrimitive();
	}
	
	public boolean equals(Object o) {
		return (this == o) || (o instanceof Type && typeRef.equals(((Type)o).typeRef));
	}
	
	public int hashCode() {
		return typeRef.hashCode();
	}
	
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
