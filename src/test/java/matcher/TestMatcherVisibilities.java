package matcher;

import org.junit.jupiter.api.Test;

import matcher.Matcher;
import matcher.entities.Visibility;
import matcher.entities.deltas.Action;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.VisibilityPatternAction;
import matcher.utils.Pair;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class TestMatcherVisibilities extends AbstractTestMatcher {

	private static final String OPERATIONS_INSTANCES_DIR_PATH = buildPath(RELATIVE_TEST_RESOURCES_DIR_PATH, "OperationsInstances");

	@Test
	public void insertVisibilityInFieldTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldInsertInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldInsertInstance", BASE_BRANCH_NAME, "Square.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldInsertInstance", BRANCH_1_NAME, "Square.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldInsertInstance", BRANCH_2_NAME, "Square.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getInsertFieldVisibilityPattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "More than one result for updating method?");

		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Updated field is not t?");
	}

	@Test
	public void deleteVisibilityInFieldTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldDeleteInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldDeleteInstance", BASE_BRANCH_NAME, "Square.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldDeleteInstance", BRANCH_1_NAME, "Square.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldDeleteInstance", BRANCH_2_NAME, "Square.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getDeleteFieldVisibilityPattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "More than one result for updating method?");

		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Updated field is not t?");
	}

	@Test
	public void updateVisibilityInFieldTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldUpdateInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldUpdateInstance", BASE_BRANCH_NAME, "Square.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldUpdateInstance", BRANCH_1_NAME, "Square.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "VisibilityFieldUpdateInstance", BRANCH_2_NAME, "Square.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getUpdateFieldVisibilityPattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "More than one result for updating method?");

		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Updated field is not t?");
	}

	private ConflictPattern getInsertFieldVisibilityPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldvar = new FreeVariable(1);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldvar, Visibility.PACKAGE);
		classPattern.addFieldPattern(fieldPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new VisibilityPatternAction(Action.INSERT, Visibility.PRIVATE, 
				null, fieldvar));

		ConflictPattern conflict = new ConflictPattern("Dummy Visibility Pattern");
		conflict.setBasePattern(basePattern);
//		conflict.addDeltaPattern(dp1);
//		conflict.addDeltaPattern(dp2);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getDeleteFieldVisibilityPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldvar = new FreeVariable(1);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldvar, Visibility.PRIVATE);
		classPattern.addFieldPattern(fieldPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new VisibilityPatternAction(Action.DELETE, null, 
				Visibility.PRIVATE, fieldvar));

		ConflictPattern conflict = new ConflictPattern("Dummy Visibility Pattern 2");
		conflict.setBasePattern(basePattern);
//		conflict.addDeltaPattern(dp1);
//		conflict.addDeltaPattern(dp2);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getUpdateFieldVisibilityPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldvar = new FreeVariable(1);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldvar, Visibility.PRIVATE);
		classPattern.addFieldPattern(fieldPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new VisibilityPatternAction(Action.UPDATE, Visibility.PROTECTED, 
				Visibility.PRIVATE, fieldvar));

		ConflictPattern conflict = new ConflictPattern("Dummy Visibility Pattern 2");
		conflict.setBasePattern(basePattern);
//		conflict.addDeltaPattern(dp1);
//		conflict.addDeltaPattern(dp2);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}

}
