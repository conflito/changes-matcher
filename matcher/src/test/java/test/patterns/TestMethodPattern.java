package test.patterns;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import matcher.entities.ClassInstance;
import matcher.entities.FieldAccessInstance;
import matcher.entities.FieldAccessType;
import matcher.entities.MethodInstance;
import matcher.entities.MethodInvocationInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import matcher.patterns.FieldAccessPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodInvocationPattern;
import matcher.patterns.MethodPattern;
import spoon.reflect.factory.TypeFactory;

public class TestMethodPattern {

	private final String CLASS_NAME = "Test";
	private final String CLASS_QUALIFIED_NAME = "a.b.Test";
	private final ClassInstance CLASS_INSTANCE = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
	
	private final TypeFactory FACTORY = new TypeFactory();
	private final Type INT_TYPE = new Type(FACTORY.get(int.class).getReference());
	
	@Test
	public void matchingSimpleMethod() {
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.setClassInstance(CLASS_INSTANCE);
		FreeVariable freeVar = new FreeVariable(0);
		MethodPattern pattern = new MethodPattern(freeVar, Visibility.PUBLIC);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertTrue(pattern.matches(instance), "Simple method doesn't match?");
	}
	
	@Test
	public void matchingSimpleMethodAnyVisibility() {
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.setClassInstance(CLASS_INSTANCE);
		FreeVariable freeVar = new FreeVariable(0);
		MethodPattern pattern = new MethodPattern(freeVar, null);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertTrue(pattern.matches(instance), "Simple method with any visibility doesn't match?");
	}
	
	@Test
	public void matchingSimpleMethodDifferentVisibility() {
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.setClassInstance(CLASS_INSTANCE);
		FreeVariable freeVar = new FreeVariable(0);
		MethodPattern pattern = new MethodPattern(freeVar, Visibility.PRIVATE);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertFalse(pattern.matches(instance), "Simple method different visibility matches?");
	}
	
	@Test
	public void matchingSimpleMethodDifferentName() {
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.setClassInstance(CLASS_INSTANCE);
		FreeVariable freeVar = new FreeVariable(0);
		MethodPattern pattern = new MethodPattern(freeVar, Visibility.PUBLIC);
		pattern.setVariableValue(0, "a.b.Test.n()");
		assertFalse(pattern.matches(instance), "Simple method with different name and matches?");
	}
	
	@Test
	public void matchingSimpleMethodDifferentNameAndVisibility() {
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.setClassInstance(CLASS_INSTANCE);
		FreeVariable freeVar = new FreeVariable(0);
		MethodPattern pattern = new MethodPattern(freeVar, Visibility.PRIVATE);
		pattern.setVariableValue(0, "a.b.Test.n()");
		assertFalse(pattern.matches(instance), "Simple method with different name and "
													+ "visibility matches?");
	}
	
	@Test
	public void matchingMethodWithOneInvocation() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		pattern.addMethodInvocationPattern(methodInvocationPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, "a.b.C.m()");
		
		assertTrue(pattern.matches(instance), "Method with one invocation doesn't match?");
	}
	
	@Test
	public void matchingMethodWithOneFieldAccess() {
		FieldAccessInstance fieldAccessInstance = 
				new FieldAccessInstance("a.b.Test.x", FieldAccessType.READ);
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addFieldAccess(fieldAccessInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		FieldAccessPattern fieldAccessPattern = 
				new FieldAccessPattern(freeVar1, FieldAccessType.READ);
		pattern.addFieldAccessPattern(fieldAccessPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, "a.b.Test.x");
		
		assertTrue(pattern.matches(instance), "Method with one field access doesn't match?");
	}
	
	@Test
	public void matchingMethodWithOneAnyFieldAccess() {
		FieldAccessInstance fieldAccessInstance = 
				new FieldAccessInstance("a.b.Test.x", FieldAccessType.READ);
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addFieldAccess(fieldAccessInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		FieldAccessPattern fieldAccessPattern = 
				new FieldAccessPattern(freeVar1, null);
		pattern.addFieldAccessPattern(fieldAccessPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, "a.b.Test.x");
		
		assertTrue(pattern.matches(instance), "Method with one any field access doesn't match?");
	}
	
	@Test
	public void matchingMethodWithOneInvocationNotFullyFilled() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		pattern.addMethodInvocationPattern(methodInvocationPattern);
		
		pattern.setVariableValue(1, "a.b.C.m()");
		
		assertFalse(pattern.matches(instance), "Method with one invocation "
										+ "not filled matches?");
	}
	
	@Test
	public void matchingMethodWithOneFieldAccessNotFullyFilled() {
		FieldAccessInstance fieldAccessInstance = 
				new FieldAccessInstance("a.b.Test.x", FieldAccessType.READ);
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addFieldAccess(fieldAccessInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		FieldAccessPattern fieldAccessPattern = 
				new FieldAccessPattern(freeVar1, FieldAccessType.READ);
		pattern.addFieldAccessPattern(fieldAccessPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		
		assertFalse(pattern.matches(instance), "Method with one field access "
				   								+ "not filled matches?");
	}
	
	@Test
	public void matchingMethodWithTwoInvocationPatternRequiresOne() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		MethodInvocationInstance methodInvocationInstance2 = new MethodInvocationInstance("a.b.C.n()");
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.addMethodInvocation(methodInvocationInstance2);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		pattern.addMethodInvocationPattern(methodInvocationPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, "a.b.C.m()");
		
		assertTrue(pattern.matches(instance), "Method with two invocations "
				+ "and pattern requiring one doesn't match first invocation?");
		
		pattern.setVariableValue(1, "a.b.C.n()");
		
		assertTrue(pattern.matches(instance), "Method with two invocations "
				+ "and pattern requiring one doesn't match second invocation?");
	}
	
	@Test
	public void matchingMethodWithTwoFieldAccessesPatternRequiresOne() {
		FieldAccessInstance fieldAccessInstance = 
				new FieldAccessInstance("a.b.Test.x", FieldAccessType.READ);
		FieldAccessInstance fieldAccessInstance2 = 
				new FieldAccessInstance("a.b.Test.y", FieldAccessType.WRITE);
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addFieldAccess(fieldAccessInstance);
		instance.addFieldAccess(fieldAccessInstance2);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		FieldAccessPattern fieldAccessPattern = 
				new FieldAccessPattern(freeVar1, null);
		pattern.addFieldAccessPattern(fieldAccessPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, "a.b.Test.x");
		
		assertTrue(pattern.matches(instance), "Method with two accesses "
				+ "and pattern requiring one doesn't match first access?");
		
		pattern.setVariableValue(1, "a.b.Test.y");
		
		assertTrue(pattern.matches(instance), "Method with two accesses "
				+ "and pattern requiring one doesn't match second access?");
	}
	
	@Test
	public void matchingMethodWithOneInvocationButNoMatch() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.setClassInstance(CLASS_INSTANCE);

		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		pattern.addMethodInvocationPattern(methodInvocationPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, "a.b.C.n()");
		
		assertFalse(pattern.matches(instance), "Method with one invocation and "
				+ "pattern with a different value matches?");
	}
	
	@Test
	public void matchingMethodWithOneFieldAccessButNoMatch() {
		FieldAccessInstance fieldAccessInstance = 
				new FieldAccessInstance("a.b.Test.x", FieldAccessType.READ);
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addFieldAccess(fieldAccessInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		FieldAccessPattern fieldAccessPattern = 
				new FieldAccessPattern(freeVar1, null);
		pattern.addFieldAccessPattern(fieldAccessPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, "a.b.Test.y");
		
		assertFalse(pattern.matches(instance), "Method with one field access and "
				+ "pattern with a different value matches?");
	}
	
	@Test
	public void matchingMethodWithInvocationAndFieldAccess() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		FieldAccessInstance fieldAccessInstance = 
				new FieldAccessInstance("a.b.Test.x", FieldAccessType.READ);
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.addFieldAccess(fieldAccessInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		FreeVariable freeVar2 = new FreeVariable(2);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		FieldAccessPattern fieldAccessPattern = 
				new FieldAccessPattern(freeVar2, null);
		pattern.addMethodInvocationPattern(methodInvocationPattern);
		pattern.addFieldAccessPattern(fieldAccessPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, "a.b.C.m()");
		pattern.setVariableValue(2, "a.b.Test.x");
		
		assertTrue(pattern.matches(instance), "Method with one invocation and "
				+ "access doesn't match?");
		
	}
	
	@Test
	public void matchingMethodWithInvocationAndWrongFieldAccess() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		FieldAccessInstance fieldAccessInstance = 
				new FieldAccessInstance("a.b.Test.x", FieldAccessType.READ);
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.addFieldAccess(fieldAccessInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		FreeVariable freeVar2 = new FreeVariable(2);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		FieldAccessPattern fieldAccessPattern = 
				new FieldAccessPattern(freeVar2, null);
		pattern.addMethodInvocationPattern(methodInvocationPattern);
		pattern.addFieldAccessPattern(fieldAccessPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, "a.b.C.m()");
		pattern.setVariableValue(2, "a.b.Test.y");
		
		assertFalse(pattern.matches(instance), "Method with one invocation and "
				+ "wrong access matches?");
		
	}
	
	@Test
	public void matchingMethodWithWrongInvocationAndFieldAccess() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		FieldAccessInstance fieldAccessInstance = 
				new FieldAccessInstance("a.b.Test.x", FieldAccessType.READ);
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.addFieldAccess(fieldAccessInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		FreeVariable freeVar2 = new FreeVariable(2);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		FieldAccessPattern fieldAccessPattern = 
				new FieldAccessPattern(freeVar2, null);
		pattern.addMethodInvocationPattern(methodInvocationPattern);
		pattern.addFieldAccessPattern(fieldAccessPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, "a.b.C.n()");
		pattern.setVariableValue(2, "a.b.Test.x");
		
		assertFalse(pattern.matches(instance), "Method with wrong one invocation and "
				+ "access matches?");
	}
	
	@Test
	public void matchingMethodWithInvocationAndFieldAccessBothWrong() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		FieldAccessInstance fieldAccessInstance = 
				new FieldAccessInstance("a.b.Test.x", FieldAccessType.READ);
		MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.addFieldAccess(fieldAccessInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		FreeVariable freeVar2 = new FreeVariable(2);
		
		MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		FieldAccessPattern fieldAccessPattern = 
				new FieldAccessPattern(freeVar2, null);
		pattern.addMethodInvocationPattern(methodInvocationPattern);
		pattern.addFieldAccessPattern(fieldAccessPattern);
		
		pattern.setVariableValue(0, instance.getQualifiedName());
		pattern.setVariableValue(1, "a.b.C.n()");
		pattern.setVariableValue(2, "a.b.Test.y");
		
		assertFalse(pattern.matches(instance), "Method with wrong one invocation and "
				+ "wrong access matches?");
	}
	
}