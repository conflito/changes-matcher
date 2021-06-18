package matcher.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import matcher.entities.MethodInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import spoon.reflect.factory.TypeFactory;

public class TestMethodInstance {
	
	private TypeFactory factory = new TypeFactory();

	@Test
	public void isCompatibleWithNull() {
		Type returnType = new Type(factory.get(int.class).getReference());
		MethodInstance m1 = new MethodInstance("m", Visibility.PUBLIC, returnType);
		assertFalse(m1.isCompatibleWith(null), "Method m() compatible with null?");
	}
	
	@Test
	public void isCompatibleWithEqualMethodsTest() {
		Type returnType = new Type(factory.get(int.class).getReference());
		MethodInstance m1 = new MethodInstance("m", Visibility.PUBLIC, returnType);
		MethodInstance m2 = new MethodInstance("m", Visibility.PUBLIC, returnType);
		assertTrue(m1.isCompatibleWith(m2), "Method m() not compatible with m()?");
	}
	
	@Test
	public void isCompatibleWithDifferentNameMethodsTest() {
		Type returnType = new Type(factory.get(int.class).getReference());
		MethodInstance m1 = new MethodInstance("m", Visibility.PUBLIC, returnType);
		MethodInstance m2 = new MethodInstance("n", Visibility.PUBLIC, returnType);
		assertFalse(m1.isCompatibleWith(m2), "Method m() compatible with n()?");
	}
	
	@Test
	public void isCompatibleWithDifferentParameterCountTest() {
		Type intType = new Type(factory.get(int.class).getReference());
		MethodInstance m1 = new MethodInstance("m", Visibility.PUBLIC, intType);
		m1.addParameter(intType);
		MethodInstance m2 = new MethodInstance("m", Visibility.PUBLIC, intType);
		assertFalse(m1.isCompatibleWith(m2), "Method m(int) compatible with m()?");
	}
	
	@Test
	public void isCompatibleWithCompatibleMethodsTest() {
		Type intType = new Type(factory.get(int.class).getReference());
		Type numberType = new Type(factory.get(Number.class).getReference());
		MethodInstance m1 = new MethodInstance("m", Visibility.PUBLIC, intType);
		m1.addParameter(intType);
		MethodInstance m2 = new MethodInstance("m", Visibility.PUBLIC, intType);
		m2.addParameter(numberType);
		assertTrue(m1.isCompatibleWith(m2), "Method m(int) not compatible with m(Number)?");
	}
	
	@Test
	public void isCompatibleWithComplexSignatureMethodsTest() {
		Type intType = new Type(factory.get(int.class).getReference());
		Type numberType = new Type(factory.get(Number.class).getReference());
		MethodInstance m1 = new MethodInstance("m", Visibility.PUBLIC, intType);
		m1.addParameter(intType);
		m1.addParameter(intType);
		MethodInstance m2 = new MethodInstance("m", Visibility.PUBLIC, intType);
		m2.addParameter(intType);
		m2.addParameter(numberType);
		assertTrue(m1.isCompatibleWith(m2), "Method m(int, int) not compatible with m(int, Number)?");
	}
	
	@Test
	public void dependsOnSimpleTest() {
		Type intType = new Type(factory.get(int.class).getReference());
		MethodInstance m = new MethodInstance("m", Visibility.PUBLIC, intType);
		MethodInstance m1 = new MethodInstance("m1", Visibility.PUBLIC, intType);
		m.addDirectDependency(m1);
		assertTrue(m.dependsOn(m1.getQualifiedName()));
	}
	
	@Test
	public void dependsOnTwoStepTest() {
		Type intType = new Type(factory.get(int.class).getReference());
		MethodInstance m = new MethodInstance("m", Visibility.PUBLIC, intType);
		MethodInstance m1 = new MethodInstance("m1", Visibility.PUBLIC, intType);
		MethodInstance m2 = new MethodInstance("m2", Visibility.PUBLIC, intType);
		m.addDirectDependency(m1);
		m1.addDirectDependency(m2);
		assertTrue(m.dependsOn(m2.getQualifiedName()));
	}
	
	@Test
	public void dependsOnWithLoopTest() {
		Type intType = new Type(factory.get(int.class).getReference());
		MethodInstance m = new MethodInstance("m", Visibility.PUBLIC, intType);
		MethodInstance m1 = new MethodInstance("m1", Visibility.PUBLIC, intType);
		MethodInstance m2 = new MethodInstance("m2", Visibility.PUBLIC, intType);
		MethodInstance m3 = new MethodInstance("m3", Visibility.PUBLIC, intType);
		m.addDirectDependency(m1);
		m1.addDirectDependency(m2);
		m1.addDirectDependency(m3);
		m2.addDirectDependency(m);
		assertTrue(m.dependsOn(m3.getQualifiedName()));
	}
	
	@Test
	public void dependsOnFailingTest() {
		Type intType = new Type(factory.get(int.class).getReference());
		MethodInstance m = new MethodInstance("m", Visibility.PUBLIC, intType);
		MethodInstance m1 = new MethodInstance("m1", Visibility.PUBLIC, intType);
		MethodInstance m2 = new MethodInstance("m2", Visibility.PUBLIC, intType);
		m.addDirectDependency(m1);
		assertFalse(m.dependsOn(m2.getQualifiedName()));
	}
	
	@Test
	public void simpleMethodDescriptorTest() {
		Type intType = new Type(factory.get(int.class).getReference());
		MethodInstance m = new MethodInstance("m", Visibility.PUBLIC, intType);
		String expected = "()I";
		String result = m.getDescriptor();
		assertEquals(expected, result, "Descriptor is not ()I?");
	}
	
	@Test
	public void simpleVoidMethodDescriptorTest() {
		Type voidType = new Type(factory.VOID_PRIMITIVE);
		MethodInstance m = new MethodInstance("m", Visibility.PUBLIC, voidType);
		String expected = "()V";
		String result = m.getDescriptor();
		assertEquals(expected, result, "Descriptor is not ()V?");
	}
	
	@Test
	public void methodWithIntParametersDescriptorTest() {
		Type intType = new Type(factory.get(int.class).getReference());
		MethodInstance m = new MethodInstance("m", Visibility.PUBLIC, intType);
		m.addParameter(intType);
		m.addParameter(intType);
		String expected = "(II)I";
		String result = m.getDescriptor();
		assertEquals(expected, result, "Descriptor is not (II)I?");
	}
	
	@Test
	public void methodWithNumberParametersAndReturnDescriptorTest() {
		Type intType = new Type(factory.get(int.class).getReference());
		Type numberType = new Type(factory.get(Number.class).getReference());
		MethodInstance m = new MethodInstance("m", Visibility.PUBLIC, numberType);
		m.addParameter(intType);
		m.addParameter(numberType);
		String expected = "(ILjava/lang/Number;)Ljava/lang/Number;";
		String result = m.getDescriptor();
		assertEquals(expected, result, "Descriptor is not (ILjava/lang/Number;)Ljava/lang/Number;?");
	}
	
	@Test
	public void methodWithArrayParametersDescriptorTest() {
		Type doubleArrayType = new Type(
				factory.createArrayReference(factory.get(double.class).getReference()));
		Type intType = new Type(factory.get(int.class).getReference());
		MethodInstance m = new MethodInstance("m", Visibility.PUBLIC, intType);
		m.addParameter(doubleArrayType);
		m.addParameter(intType);
		String expected = "([DI)I";
		String result = m.getDescriptor();
		assertEquals(expected, result, "Descriptor is not ([DI)I?");
	}
	
	@Test
	public void methodWithArrayParametersAndReturnDescriptorTest() {
		Type doubleArrayType = new Type(
				factory.createArrayReference(factory.get(double.class).getReference()));
		Type numberArrayType = new Type(
				factory.createArrayReference(factory.get(Number.class).getReference(), 2));
		MethodInstance m = new MethodInstance("m", Visibility.PUBLIC, numberArrayType);
		m.addParameter(numberArrayType);
		m.addParameter(doubleArrayType);
		String expected = "([[Ljava/lang/Number;[D)[[Ljava/lang/Number;";
		String result = m.getDescriptor();
		assertEquals(expected, result, "Descriptor is not "
				+ "([[Ljava/lang/Number;[D)[[Ljava/lang/Number;?");
	}
	
}
