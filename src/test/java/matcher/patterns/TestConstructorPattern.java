package matcher.patterns;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.MethodInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FreeVariable;
import spoon.reflect.factory.TypeFactory;

public class TestConstructorPattern {

	private final String CLASS_NAME = "Test";
	private final String CLASS_QUALIFIED_NAME = "a.b.Test";
	private final ClassInstance CLASS_INSTANCE = new ClassInstance(CLASS_NAME, CLASS_QUALIFIED_NAME);
	
	private final TypeFactory FACTORY = new TypeFactory();
	private final Type STRING_TYPE = new Type(FACTORY.get(String.class).getReference());
	
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
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		MethodInstance invocation = new MethodInstance("m", Visibility.PUBLIC, STRING_TYPE);
		instance.addDirectDependency(invocation);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ConstructorPattern constructorPattern = new ConstructorPattern(freeVar0, Visibility.PUBLIC);
		constructorPattern.addDependency(freeVar1);
		
		constructorPattern.setVariableValue(0, instance.getQualifiedName());
		constructorPattern.setVariableValue(1, "m()");
		
		assertTrue(constructorPattern.matches(instance), "Constructor with one invocation doesn't match?");
	}
	
	@Test
	public void matchingConstructorWithOneInvocationNotFullyFilled() {
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		MethodInstance invocation = new MethodInstance("m", Visibility.PUBLIC, STRING_TYPE);
		instance.addDirectDependency(invocation);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ConstructorPattern constructorPattern = new ConstructorPattern(freeVar0, Visibility.PUBLIC);
		constructorPattern.addDependency(freeVar1);
		
		constructorPattern.setVariableValue(0, instance.getQualifiedName());
		
		assertFalse(constructorPattern.matches(instance), "Constructor with one invocation "
				+ "not filled matches?");

	}
	
	@Test
	public void matchingConstructorWithTwoInvocationPatternRequiresOne() {
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		MethodInstance invocation = new MethodInstance("m", Visibility.PUBLIC, STRING_TYPE);
		MethodInstance invocation2 = new MethodInstance("n", Visibility.PUBLIC, STRING_TYPE);
		instance.addDirectDependency(invocation);
		instance.addDirectDependency(invocation2);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ConstructorPattern constructorPattern = new ConstructorPattern(freeVar0, Visibility.PUBLIC);
		constructorPattern.addDependency(freeVar1);
		
		constructorPattern.setVariableValue(0, instance.getQualifiedName());
		constructorPattern.setVariableValue(1, "m()");
		
		assertTrue(constructorPattern.matches(instance), "Constructor with two invocations "
				+ "and pattern requiring one doesn't match first invocation?");

		constructorPattern.setVariableValue(1, "n()");
		
		assertTrue(constructorPattern.matches(instance), "Constructor with two invocations "
				+ "and pattern requiring one doesn't match second invocation?");
	}
	
	@Test
	public void matchingConstructorWithOneInvocationButNoMatch() {
		ConstructorInstance instance =  new ConstructorInstance(Visibility.PUBLIC);
		MethodInstance invocation = new MethodInstance("m", Visibility.PUBLIC, STRING_TYPE);
		instance.addDirectDependency(invocation);
		instance.setClassInstance(CLASS_INSTANCE);
		
		FreeVariable freeVar0 = new FreeVariable(0);
		FreeVariable freeVar1 = new FreeVariable(1);
		
		ConstructorPattern constructorPattern = new ConstructorPattern(freeVar0, Visibility.PUBLIC);
		constructorPattern.addDependency(freeVar1);
		
		constructorPattern.setVariableValue(0, instance.getQualifiedName());
		constructorPattern.setVariableValue(1, "n()");
		
		assertFalse(constructorPattern.matches(instance), "Constructor with one invocation and "
				+ "pattern with a different value doesn't match?");
	}
}
