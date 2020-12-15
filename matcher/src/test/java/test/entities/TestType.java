package test.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import matcher.entities.Type;
import spoon.reflect.factory.TypeFactory;

public class TestType {
	
	private TypeFactory factory = new TypeFactory();

	@Test
	public void isSubtypeWithTrueSubtypeTest() {
		Type subType = new Type(factory.INTEGER);
		Type topType = new Type(factory.get(Number.class).getReference());
		assertTrue(subType.isSubtypeOf(topType), "Integer not subtype of Number?");
	}
	
	@Test
	public void isSubtypeWithFalseSubtypeTest() {
		Type subType = new Type(factory.INTEGER);
		Type topType = new Type(factory.STRING);
		assertFalse(subType.isSubtypeOf(topType), "Integer subtype of String?");
	}
	
	@Test
	public void primitiveToWrapperWithPrimitiveTest() {
		Type primitive = new Type(factory.BOOLEAN_PRIMITIVE);
		Type wrapper = Type.primitiveToWrapper(primitive);
		Type expected = new Type(factory.BOOLEAN);
		assertEquals(expected, wrapper, "Not the Boolean type?");
	}
	
	@Test
	public void primitiToWrapperWithNonPrimitiveTest() {
		Type type = new Type(factory.get(Number.class).getReference());
		Type wrapper = Type.primitiveToWrapper(type);
		Type expected = null;
		assertEquals(expected, wrapper, "Number converted to wrapper class?");
	}
}
