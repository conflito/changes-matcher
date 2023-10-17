package org.conflito.matcher;

import org.junit.jupiter.api.Test;

import org.conflito.matcher.patterns.BasePattern;
import org.conflito.matcher.patterns.ClassPattern;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.patterns.ConstructorPattern;
import org.conflito.matcher.patterns.FreeVariable;
import org.conflito.matcher.patterns.MethodPattern;
import org.conflito.matcher.patterns.deltas.DeltaPattern;
import org.conflito.matcher.patterns.deltas.UpdateConstructorPatternAction;
import org.conflito.matcher.patterns.deltas.UpdateMethodPatternAction;
import org.conflito.matcher.utils.Pair;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TestMatcherUpdates extends AbstractTestMatcher {

	private static final String OPERATIONS_INSTANCES_DIR_PATH = buildPath(RELATIVE_TEST_RESOURCES_DIR_PATH, "OperationsInstances");

	@Test
	public void methodUpdateWithInsertOperationTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodUpdateWithInsertOperationInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodUpdateWithInsertOperationInstance", BASE_BRANCH_NAME, "Square.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodUpdateWithInsertOperationInstance", BRANCH_1_NAME, "Square.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodUpdateWithInsertOperationInstance", BRANCH_2_NAME, "Square.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getUpdateMethodPattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for updating method?");

		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m()", assignments.get(1).getSecond(),
				"Updated method is not m()?");
	}

	@Test
	public void methodUpdateWithDeleteOperationTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodUpdateWithDeleteOperationInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodUpdateWithDeleteOperationInstance", BASE_BRANCH_NAME, "Shape.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodUpdateWithDeleteOperationInstance", BRANCH_1_NAME, "Shape.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodUpdateWithDeleteOperationInstance", BRANCH_2_NAME, "Shape.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getUpdateMethodPattern();

		List<List<Pair<Integer, String>>> result = 
			matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for updating method?");

		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Shape", assignments.get(0).getSecond(), 
				"Class is not base.Shape?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m()", assignments.get(1).getSecond(),
				"Updated method is not m()?");
	}

	@Test
	public void constructorUpdateWithInsertOperationTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "ConstructorUpdateWithInsertOperationInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "ConstructorUpdateWithInsertOperationInstance", BASE_BRANCH_NAME, "Square.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "ConstructorUpdateWithInsertOperationInstance", BRANCH_1_NAME, "Square.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "ConstructorUpdateWithInsertOperationInstance", BRANCH_2_NAME, "Square.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getUpdateConstructorPattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for updating method?");

		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("base.Square.<init>()", assignments.get(1).getSecond(), 
				"Updated constructor is not base.Square.<init>()?");
	}

	@Test
	public void constructorUpdateWithDeleteOperationTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "ConstructorUpdateWithDeleteOperationInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "ConstructorUpdateWithDeleteOperationInstance", BASE_BRANCH_NAME, "Square.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "ConstructorUpdateWithDeleteOperationInstance", BRANCH_1_NAME, "Square.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "ConstructorUpdateWithDeleteOperationInstance", BRANCH_2_NAME, "Square.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getUpdateConstructorPattern();

		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for updating method?");

		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("base.Square.<init>()", assignments.get(1).getSecond(), 
				"Updated constructor is not base.Square.<init>()?");
	}

	private ConflictPattern getUpdateMethodPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar = new FreeVariable(1);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(
				new UpdateMethodPatternAction(methodPattern, classPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Update Pattern");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getUpdateConstructorPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable cVar = new FreeVariable(1);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		ConstructorPattern cPattern = new ConstructorPattern(cVar, null);
		classPattern.addConstructorPattern(cPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new UpdateConstructorPatternAction(cPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Update Pattern 2");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}
}
