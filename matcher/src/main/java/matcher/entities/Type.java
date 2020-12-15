package matcher.entities;

import spoon.reflect.factory.TypeFactory;
import spoon.reflect.reference.CtTypeReference;

public class Type {

	private CtTypeReference<?> type;

	public Type(CtTypeReference<?> type) {
		super();
		this.type = type;
	}

	public String toString() {
		return type.toString();
	}
	
	public boolean isSubtypeOf(Type t) {
		return type.isSubtypeOf(t.type);
	}
	
	public boolean isPrimitive() {
		return type.isPrimitive();
	}
	
	public static Type primitiveToWrapper(Type type) {
		TypeFactory f = new TypeFactory();
		Type result;
		if(type.type.equals(f.INTEGER_PRIMITIVE)) {
			result = new Type(f.INTEGER);
		}
		else if(type.type.equals(f.BOOLEAN_PRIMITIVE)) {
			result = new Type(f.BOOLEAN);
		}
		else if(type.type.equals(f.BYTE_PRIMITIVE)) {
			result = new Type(f.BYTE);
		}
		else if(type.type.equals(f.SHORT_PRIMITIVE)) {
			result = new Type(f.SHORT);
		}
		else if(type.type.equals(f.CHARACTER_PRIMITIVE)) {
			result = new Type(f.CHARACTER);
		}
		else if(type.type.equals(f.LONG_PRIMITIVE)) {
			result = new Type(f.LONG);
		}
		else if(type.type.equals(f.FLOAT_PRIMITIVE)) {
			result = new Type(f.FLOAT);
		}
		else{
			result = new Type(f.DOUBLE);
		}
		return result;
	}
}
