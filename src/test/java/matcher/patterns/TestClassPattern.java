package matcher.patterns;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.FieldAccessInstance;
import matcher.entities.FieldAccessType;
import matcher.entities.FieldInstance;
import matcher.entities.MethodInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FieldAccessPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodPattern;
import spoon.reflect.factory.TypeFactory;

public class TestClassPattern {

	private final TypeFactory FACTORY = new TypeFactory();
	private final Type INT_TYPE = new Type(FACTORY.get(int.class).getReference());
	private final Type NUMBER_TYPE = new Type(FACTORY.get(Number.class).getReference());
	private final Type STRING_TYPE = new Type(FACTORY.get(String.class).getReference());
	
	private final String CLASS_NAME = "Test";
	private final String CLASS_QUALIFIED_NAME = "a.b.Test";
	
	private final String SUPER_CLASS_NAME = "Parent";
	private final String SUPER_CLASS_QUALIFIED_NAME = "a.b.Parent";
	
	@Test
	public void matchingSimpleClassTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		
		FreeVariable freeVar = new FreeVariable(0);
		
		ClassPattern pattern = new ClassPattern(freeVar);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Simple class doesn't match?");
	}
	
	@Test
	public void matchingClassWithPrivateFieldTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		FieldInstance field = new FieldInstance("x", Visibility.PRIVATE, INT_TYPE);
		instance.addField(field);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		FieldPattern fieldPattern = new FieldPattern(freeVar1, Visibility.PRIVATE);
		pattern.addFieldPattern(fieldPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, field.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Class with private field doesn't match?");
	}
	
	@Test
	public void matchingClassWithAnyVisibilityFieldTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		FieldInstance field = new FieldInstance("x", Visibility.PRIVATE, INT_TYPE);
		instance.addField(field);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		FieldPattern fieldPattern = new FieldPattern(freeVar1, null);
		pattern.addFieldPattern(fieldPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, field.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Class with any visibility field doesn't match?");
	}
	
	@Test
	public void matchingClassWithWrongVisibilityFieldTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		FieldInstance field = new FieldInstance("x", Visibility.PRIVATE, INT_TYPE);
		instance.addField(field);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		FieldPattern fieldPattern = new FieldPattern(freeVar1, Visibility.PUBLIC);
		pattern.addFieldPattern(fieldPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, field.getQualifiedName());
		
		assertFalse(pattern.matches(instance), "Class with wrong visibility in "
				+ "field matches?");
	}
	
	@Test
	public void matchingClassWithSimpleSuperClassTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		ClassInstance parent = new ClassInstance(SUPER_CLASS_NAME, SUPER_CLASS_QUALIFIED_NAME);
		instance.setSuperClass(parent);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		ClassPattern patternParent = new ClassPattern(freeVar1);
		pattern.setSuperClass(patternParent);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, parent.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Class with simple superclass doesn't match?");
	}
	
	@Test
	public void matchingClassWithSuperClassWithFieldsTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		FieldInstance field = new FieldInstance("x", Visibility.PRIVATE, INT_TYPE);
		instance.addField(field);
		
		ClassInstance parent = new ClassInstance(SUPER_CLASS_NAME, SUPER_CLASS_QUALIFIED_NAME);
		FieldInstance parentField = new FieldInstance("t", Visibility.PROTECTED, INT_TYPE);
		parent.addField(parentField);
		
		instance.setSuperClass(parent);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		FreeVariable freeVar2 = new FreeVariable(2);
		FreeVariable freeVar3 = new FreeVariable(3);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		FieldPattern fieldPattern = new FieldPattern(freeVar1, Visibility.PRIVATE);
		pattern.addFieldPattern(fieldPattern);
		
		ClassPattern patternParent = new ClassPattern(freeVar2);
		FieldPattern fieldParentPattern = new FieldPattern(freeVar3, Visibility.PROTECTED);
		patternParent.addFieldPattern(fieldParentPattern);
		
		pattern.setSuperClass(patternParent);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, field.getQualifiedName());
		pattern.setVariableValue(2, parent.getQualifiedName());
		pattern.setVariableValue(3, parentField.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Class with fields and superclass doesn't match?");
	}
	
	@Test
	public void matchingClassWithSimplePublicMethodTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		MethodInstance method = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addMethod(method);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		MethodPattern methodPattern = new MethodPattern(freeVar1, Visibility.PUBLIC);
		pattern.addMethodPattern(methodPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, method.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Class with public method doesn't match?");
	}
	
	@Test
	public void matchingClassWithSimpleAnyVisibilityMethodTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		MethodInstance method = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addMethod(method);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		MethodPattern methodPattern = new MethodPattern(freeVar1, null);
		pattern.addMethodPattern(methodPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, method.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Class with any visibility method doesn't match?");
	}
	
	@Test
	public void matchingClassWithSimpleWrongVisibilityMethodTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		MethodInstance method = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addMethod(method);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		MethodPattern methodPattern = new MethodPattern(freeVar1, Visibility.PRIVATE);
		pattern.addMethodPattern(methodPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, method.getQualifiedName());
		
		assertFalse(pattern.matches(instance), "Class with wrong visibility method matches?");
	}
	
	@Test
	public void matchingClassWithMethodWithInvocationTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		MethodInstance method = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		method.addDirectDependency(method);
		instance.addMethod(method);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		MethodPattern methodPattern = new MethodPattern(freeVar1, null);
		methodPattern.addDependency(freeVar1);
		pattern.addMethodPattern(methodPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, method.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Class with method with invocation "
				+ "doesn't match?");
	}
	
	@Test
	public void matchingClassWithMethodWithReadFieldAccessTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		MethodInstance method = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		FieldAccessInstance access = new FieldAccessInstance("z", FieldAccessType.READ);
		method.addFieldAccess(access);
		instance.addMethod(method);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		FreeVariable freeVar2 = new FreeVariable(2);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		MethodPattern methodPattern = new MethodPattern(freeVar1, null);
		FieldAccessPattern accessPattern = new FieldAccessPattern(freeVar2, FieldAccessType.READ);
		methodPattern.addFieldAccessPattern(accessPattern);
		pattern.addMethodPattern(methodPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, method.getQualifiedName());
		pattern.setVariableValue(2, "z");
		
		assertTrue(pattern.matches(instance), "Class with method with read access "
				+ "doesn't match?");
	}
	
	@Test
	public void matchingClassWithMethodWithAnyFieldAccessTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		MethodInstance method = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		FieldAccessInstance access = new FieldAccessInstance("z", FieldAccessType.READ);
		method.addFieldAccess(access);
		instance.addMethod(method);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		FreeVariable freeVar2 = new FreeVariable(2);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		MethodPattern methodPattern = new MethodPattern(freeVar1, null);
		FieldAccessPattern accessPattern = new FieldAccessPattern(freeVar2, null);
		methodPattern.addFieldAccessPattern(accessPattern);
		pattern.addMethodPattern(methodPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, method.getQualifiedName());
		pattern.setVariableValue(2, "z");
		
		assertTrue(pattern.matches(instance), "Class with method with any access "
				+ "doesn't match?");
	}
	
	@Test
	public void matchingClassWithSimplePublicConstructorTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		ConstructorInstance constructor = new ConstructorInstance(Visibility.PUBLIC);
		constructor.setClassInstance(instance);
		instance.addConstructor(constructor);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		ConstructorPattern constructorPattern = new ConstructorPattern(freeVar1, 
				Visibility.PUBLIC);
		pattern.addConstructorPattern(constructorPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, constructor.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Class with public constructor doesn't match?");
	}
	
	@Test
	public void matchingClassWithSimpleAnyVisibilityConstructorTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		ConstructorInstance constructor = new ConstructorInstance(Visibility.PUBLIC);
		constructor.setClassInstance(instance);
		instance.addConstructor(constructor);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		ConstructorPattern constructorPattern = new ConstructorPattern(freeVar1, null);
		pattern.addConstructorPattern(constructorPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, constructor.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Class with any visibility constructor "
				+ "doesn't match?");
	}
	
	@Test
	public void matchingClassWithConstructorWithInvocationTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		ConstructorInstance constructor = new ConstructorInstance(Visibility.PUBLIC);
		MethodInstance invocation = new MethodInstance("m", Visibility.PUBLIC, STRING_TYPE);
		constructor.addDirectDependency(invocation);
		constructor.setClassInstance(instance);
		instance.addConstructor(constructor);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		FreeVariable freeVar2 = new FreeVariable(2);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		ConstructorPattern constructorPattern = new ConstructorPattern(freeVar1, null);
		constructorPattern.addDependency(freeVar2);
		pattern.addConstructorPattern(constructorPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, constructor.getQualifiedName());
		pattern.setVariableValue(2, "m()");
		
		assertTrue(pattern.matches(instance), "Method with constructor with invocation "
				+ "doesn't match?");
	}
	
	@Test
	public void matchingClassWithCompatibleMethodsTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		MethodInstance method1 = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		method1.addParameter(INT_TYPE);
		MethodInstance method2 = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		method2.addParameter(NUMBER_TYPE);
		instance.addMethod(method1);
		instance.addMethod(method2);
		instance.addCompatible(method1, method2);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		FreeVariable freeVar2 = new FreeVariable(2);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		MethodPattern methodPattern1 = new MethodPattern(freeVar1, null);
		MethodPattern methodPattern2 = new MethodPattern(freeVar2, null);
		pattern.addMethodPattern(methodPattern1);
		pattern.addMethodPattern(methodPattern2);
		pattern.addCompatible(freeVar1, freeVar2);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, method1.getQualifiedName());
		pattern.setVariableValue(2, method2.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Class with compatibles doesn't match?");
	}
	
	@Test
	public void matchingClassWithCompatibleMethodsFailTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		MethodInstance method1 = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		method1.addParameter(INT_TYPE);
		MethodInstance method2 = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		method2.addParameter(STRING_TYPE);
		instance.addMethod(method1);
		instance.addMethod(method2);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		FreeVariable freeVar2 = new FreeVariable(2);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		MethodPattern methodPattern1 = new MethodPattern(freeVar1, null);
		MethodPattern methodPattern2 = new MethodPattern(freeVar2, null);
		pattern.addMethodPattern(methodPattern1);
		pattern.addMethodPattern(methodPattern2);
		pattern.addCompatible(freeVar1, freeVar2);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, method1.getQualifiedName());
		pattern.setVariableValue(2, method2.getQualifiedName());
		
		assertFalse(pattern.matches(instance), "Class without compatibles matches?");
	}
	
	@Test
	public void matchingClassWithTwoCompatibleMethodsTest() {
		ClassInstance instance = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
		MethodInstance method1 = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		method1.addParameter(INT_TYPE);
		method1.addParameter(INT_TYPE);
		MethodInstance method2 = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		method2.addParameter(INT_TYPE);
		method2.addParameter(NUMBER_TYPE);
		MethodInstance method3 = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		method3.addParameter(NUMBER_TYPE);
		method3.addParameter(NUMBER_TYPE);		
		instance.addMethod(method1);
		instance.addMethod(method2);
		instance.addMethod(method3);
		
		instance.addCompatible(method1, method2);
		instance.addCompatible(method1, method3);
		instance.addCompatible(method2, method3);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		FreeVariable freeVar2 = new FreeVariable(2);
		FreeVariable freeVar3 = new FreeVariable(3);
		
		ClassPattern pattern = new ClassPattern(freeVar0);
		MethodPattern methodPattern1 = new MethodPattern(freeVar1, null);
		MethodPattern methodPattern2 = new MethodPattern(freeVar2, null);
		MethodPattern methodPattern3 = new MethodPattern(freeVar3, null);
		pattern.addMethodPattern(methodPattern1);
		pattern.addMethodPattern(methodPattern2);
		pattern.addMethodPattern(methodPattern3);
		
		pattern.addCompatible(freeVar1, freeVar2);
		pattern.addCompatible(freeVar1, freeVar3);
		pattern.addCompatible(freeVar2, freeVar3);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, method1.getQualifiedName());
		pattern.setVariableValue(2, method2.getQualifiedName());
		pattern.setVariableValue(3, method3.getQualifiedName());
		
		assertTrue(pattern.matches(instance), "Class with compatibles doesn't match?");
	}
	
}
