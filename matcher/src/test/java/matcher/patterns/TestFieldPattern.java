package matcher.patterns;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import matcher.entities.FieldInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import spoon.reflect.factory.TypeFactory;

public class TestFieldPattern {
	
	private final FreeVariable FREE_VAR = new FreeVariable(0);
	private final String FIELD_NAME = "x";
	
	private final TypeFactory FACTORY = new TypeFactory();
	private final Type INT_TYPE = new Type(FACTORY.get(int.class).getReference());

	@Test
	public void matchingFieldSameVisibilityTest() {
		FieldInstance instance = new FieldInstance(FIELD_NAME, Visibility.PUBLIC, INT_TYPE);
		FieldPattern pattern =  new FieldPattern(FREE_VAR, Visibility.PUBLIC);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertTrue(pattern.matches(instance), "Same visibility doesn't match?");
	}
	
	@Test
	public void matchingFieldDifferentVisibilityTest() {
		FieldInstance instance = new FieldInstance(FIELD_NAME, Visibility.PUBLIC, INT_TYPE);
		FieldPattern pattern =  new FieldPattern(FREE_VAR, Visibility.PRIVATE);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertFalse(pattern.matches(instance), "Different visibility matches?");
	}
	
	@Test
	public void matchingFieldDifferentQualifiedNamesTest() {
		FieldInstance instance = new FieldInstance(FIELD_NAME, Visibility.PUBLIC, INT_TYPE);
		FieldPattern pattern =  new FieldPattern(FREE_VAR, Visibility.PUBLIC);
		pattern.setVariableValue(0, "y");
		assertFalse(pattern.matches(instance), "Different name matches?");
	}
	
	@Test
	public void matchingFieldDifferentQualifiedNamesDifferentVisibilityTest() {
		FieldInstance instance = new FieldInstance(FIELD_NAME, Visibility.PUBLIC, INT_TYPE);
		FieldPattern pattern =  new FieldPattern(FREE_VAR, Visibility.PRIVATE);
		pattern.setVariableValue(0, "y");
		assertFalse(pattern.matches(instance), "Different name and visibilities matches?");
	}
	
	@Test
	public void matchingFieldAnyVisibilityTest() {
		FieldInstance instance = new FieldInstance(FIELD_NAME, Visibility.PUBLIC, INT_TYPE);
		FieldPattern pattern =  new FieldPattern(FREE_VAR, null);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertTrue(pattern.matches(instance), "Any visibility doesn't match with public visibility?");
		
		instance = new FieldInstance(FIELD_NAME, Visibility.PACKAGE, INT_TYPE);
		assertTrue(pattern.matches(instance), "Any visibility doesn't match with package visibility?");
		
		instance = new FieldInstance(FIELD_NAME, Visibility.PRIVATE, INT_TYPE);
		assertTrue(pattern.matches(instance), "Any visibility doesn't match with private visibility?");
		
		instance = new FieldInstance(FIELD_NAME, Visibility.PROTECTED, INT_TYPE);
		assertTrue(pattern.matches(instance), "Any visibility doesn't match with protected visibility?");
	}	
}
