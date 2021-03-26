package matcher.entities;

import spoon.reflect.factory.TypeFactory;
import spoon.reflect.reference.CtArrayTypeReference;
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
	
	public boolean isArray() {
		return typeRef.isArray();
	}
	
	public boolean isVoid() {
		return typeRef.equals(new TypeFactory().VOID_PRIMITIVE);
	}
	
	public Type getArrayType() {
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
	
	private String toComplexDescriptor() {
		return "L" + typeRef.getQualifiedName().replace('.', '/') + ";";
	}

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
