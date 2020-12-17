package test.patterns;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.MethodInvocationInstance;
import matcher.entities.Visibility;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodInvocationPattern;

public class TestConstructorPattern {

	private final String CLASS_NAME = "Test";
	private final String CLASS_QUALIFIED_NAME = "a.b.Test";
	private final ClassInstance CLASS_INSTANCE = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
	
	@Test
	public void matchingSimpleConstructor() {
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		instance.setClassInstance(CLASS_INSTANCE);
		FreeVariable freeVar = new FreeVariable(0);
		ConstructorPattern pattern = new ConstructorPattern(freeVar, Visibility.PUBLIC);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertTrue(pattern.matches(instance), "Simple constructor doesn't match?");
	}
	
	@Test
	public void matchingSimpleConstructorAnyVisibility() {
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		instance.setClassInstance(CLASS_INSTANCE);
		FreeVariable freeVar = new FreeVariable(0);
		ConstructorPattern pattern = new ConstructorPattern(freeVar, null);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertTrue(pattern.matches(instance), "Simple constructor with any visibility doesn't match?");
	}
	
	@Test
	public void matchingSimpleConstructorDifferentVisibility() {
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		instance.setClassInstance(CLASS_INSTANCE);
		FreeVariable freeVar = new FreeVariable(0);
		ConstructorPattern pattern = new ConstructorPattern(freeVar, Visibility.PRIVATE);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertFalse(pattern.matches(instance), "Simple constructor with different visibility matches?");
	}
	
	@Test
	public void matchingSimpleConstructorDifferentName() {
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		instance.setClassInstance(CLASS_INSTANCE);
		FreeVariable freeVar = new FreeVariable(0);
		ConstructorPattern pattern = new ConstructorPattern(freeVar, Visibility.PUBLIC);
		pattern.setVariableValue(0, "a.b.Test(int)");
		assertFalse(pattern.matches(instance), "Simple constructor with different name and matches?");
	}
	
	@Test
	public void matchingSimpleConstructorDifferentNameAndVisibility() {
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		instance.setClassInstance(CLASS_INSTANCE);
		FreeVariable freeVar = new FreeVariable(0);
		ConstructorPattern pattern = new ConstructorPattern(freeVar, Visibility.PRIVATE);
		pattern.setVariableValue(0, "a.b.Test(int)");
		assertFalse(pattern.matches(instance), "Simple constructor with different name and "
												+ "visibility matches?");
	}
	
	@Test
	public void matchingConstructorWithOneInvocation() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ConstructorPattern constructorPattern = new ConstructorPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		constructorPattern.addMethodInvocationPattern(methodInvocationPattern);
		
		constructorPattern.setVariableValue(0, instance.getQualifiedName());
		constructorPattern.setVariableValue(1, "a.b.C.m()");
		
		assertTrue(constructorPattern.matches(instance), "Constructor with one invocation doesn't match?");
	}
	
	@Test
	public void matchingConstructorWithOneInvocationNotFullyFilled() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ConstructorPattern constructorPattern = new ConstructorPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		constructorPattern.addMethodInvocationPattern(methodInvocationPattern);
		
		constructorPattern.setVariableValue(0, instance.getQualifiedName());
		
		assertFalse(constructorPattern.matches(instance), "Constructor with one invocation "
				+ "not filled matches?");

	}
	
	@Test
	public void matchingConstructorWithTwoInvocationPatternRequiresOne() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		MethodInvocationInstance methodInvocationInstance2 = new MethodInvocationInstance("a.b.C.n()");
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.addMethodInvocation(methodInvocationInstance2);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ConstructorPattern constructorPattern = new ConstructorPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		constructorPattern.addMethodInvocationPattern(methodInvocationPattern);
		
		constructorPattern.setVariableValue(0, instance.getQualifiedName());
		constructorPattern.setVariableValue(1, "a.b.C.m()");
		
		assertTrue(constructorPattern.matches(instance), "Constructor with two invocations "
				+ "and pattern requiring one doesn't match first invocation?");

		constructorPattern.setVariableValue(1, "a.b.C.n()");
		
		assertTrue(constructorPattern.matches(instance), "Constructor with two invocations "
				+ "and pattern requiring one doesn't match second invocation?");
	}
	
	@Test
	public void matchingConstructorWithOneInvocationButNoMatch() {
		MethodInvocationInstance methodInvocationInstance = new MethodInvocationInstance("a.b.C.m()");
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		instance.addMethodInvocation(methodInvocationInstance);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ConstructorPattern constructorPattern = new ConstructorPattern(freeVar0, Visibility.PUBLIC);
		MethodInvocationPattern methodInvocationPattern = new MethodInvocationPattern(freeVar1);
		constructorPattern.addMethodInvocationPattern(methodInvocationPattern);
		
		constructorPattern.setVariableValue(0, instance.getQualifiedName());
		constructorPattern.setVariableValue(1, "a.b.C.n()");
		
		assertFalse(constructorPattern.matches(instance), "Constructor with one invocation and "
				+ "pattern with a different valuedoesn't match?");
	}
}
