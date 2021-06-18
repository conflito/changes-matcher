package matcher.patterns;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import matcher.entities.FieldAccessInstance;
import matcher.entities.FieldAccessType;
import matcher.patterns.FieldAccessPattern;
import matcher.patterns.FreeVariable;

public class TestFieldAccessPattern {

	private final FreeVariable FREE_VAR = new FreeVariable(0);
	private final String ACCESS_QUALIFIED_NAME = "x";
	
	@Test
	public void matchingFieldAccessSameAccessTest() {
		FieldAccessInstance instance = new FieldAccessInstance(ACCESS_QUALIFIED_NAME, 
				FieldAccessType.READ);
		FieldAccessPattern pattern = new FieldAccessPattern(FREE_VAR, FieldAccessType.READ);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertTrue(pattern.matches(instance), "Same access doesn't match?");
	}
	
	@Test
	public void matchingFieldAccessAnyAccessTest() {
		FieldAccessInstance instance = new FieldAccessInstance(ACCESS_QUALIFIED_NAME, 
				FieldAccessType.READ);
		FieldAccessPattern pattern = new FieldAccessPattern(FREE_VAR, null);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertTrue(pattern.matches(instance), "Any access doesn't match?");
	}
	
	@Test
	public void matchingFieldAccessDifferedAccessTest() {
		FieldAccessInstance instance = new FieldAccessInstance(ACCESS_QUALIFIED_NAME, 
				FieldAccessType.READ);
		FieldAccessPattern pattern = new FieldAccessPattern(FREE_VAR, FieldAccessType.WRITE);
		pattern.setVariableValue(0, instance.getQualifiedName());
		assertFalse(pattern.matches(instance), "Different accesses match?");
	}
	
	@Test
	public void matchingFieldAccessDifferentNameTest() {
		FieldAccessInstance instance = new FieldAccessInstance(ACCESS_QUALIFIED_NAME, 
				FieldAccessType.READ);
		FieldAccessPattern pattern = new FieldAccessPattern(FREE_VAR, FieldAccessType.READ);
		pattern.setVariableValue(0, "a.b.C.d");
		assertFalse(pattern.matches(instance), "Different names match?");
	}
	
	@Test
	public void matchingFieldAccessDifferentNameWithAnyAccessTest() {
		FieldAccessInstance instance = new FieldAccessInstance(ACCESS_QUALIFIED_NAME, 
				FieldAccessType.READ);
		FieldAccessPattern pattern = new FieldAccessPattern(FREE_VAR, null);
		pattern.setVariableValue(0, "a.b.C.d");
		assertFalse(pattern.matches(instance), "Different names (with any access) match?");
	}
}
