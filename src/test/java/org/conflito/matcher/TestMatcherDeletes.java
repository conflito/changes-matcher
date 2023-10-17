package org.conflito.matcher;

import org.conflito.matcher.entities.Visibility;
import org.conflito.matcher.patterns.BasePattern;
import org.conflito.matcher.patterns.ClassPattern;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.patterns.ConstructorPattern;
import org.conflito.matcher.patterns.FieldAccessPattern;
import org.conflito.matcher.patterns.FieldPattern;
import org.conflito.matcher.patterns.FreeVariable;
import org.conflito.matcher.patterns.MethodInvocationPattern;
import org.conflito.matcher.patterns.MethodPattern;
import org.conflito.matcher.patterns.deltas.DeleteConstructorPatternAction;
import org.conflito.matcher.patterns.deltas.DeleteFieldAccessPatternAction;
import org.conflito.matcher.patterns.deltas.DeleteFieldPatternAction;
import org.conflito.matcher.patterns.deltas.DeleteInvocationPatternAction;
import org.conflito.matcher.patterns.deltas.DeleteMethodPatternAction;
import org.conflito.matcher.patterns.deltas.DeltaPattern;
import org.conflito.matcher.utils.Pair;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TestMatcherDeletes extends AbstractTestMatcher {

	private static final String OPERATIONS_INSTANCES_DIR_PATH = buildPath(RELATIVE_TEST_RESOURCES_DIR_PATH, "OperationsInstances");

	@Test
	public void deletePrivateFieldAndPublicMethodTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndMethodDeleteInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndMethodDeleteInstance", BASE_BRANCH_NAME, "Square.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndMethodDeleteInstance", BRANCH_1_NAME, "Square.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndMethodDeleteInstance", BRANCH_2_NAME, "Square.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getDeletePrivateFieldAndPublicMethodPattern();

		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for deleting private "
				+ "field and public method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(3, assignments.size(), "Not 3 assignments with only 3 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Field is not t?");

		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m()", assignments.get(2).getSecond(), 
				"Method is not base.Square.m()?");
	}

	@Test
	public void deletePrivateFieldAndPublicConstructorTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndConstructorDeleteInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndConstructorDeleteInstance", BASE_BRANCH_NAME, "Square.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndConstructorDeleteInstance", BRANCH_1_NAME, "Square.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndConstructorDeleteInstance", BRANCH_2_NAME, "Square.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getDeletePrivateFieldAndPublicConstructorPattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for deleting private "
				+ "field and constructor method?");

		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(3, assignments.size(), "Not 3 assignments with only 3 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Field is not t?");

		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("base.Square.<init>()", assignments.get(2).getSecond(), 
				"Constructor is not base.Square.<init>()?");
	}

	@Test
	public void deleteFieldAndCompatibleMethodTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndCompatibleMethodDeleteInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndCompatibleMethodDeleteInstance", BASE_BRANCH_NAME, "Square.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndCompatibleMethodDeleteInstance", BRANCH_1_NAME, "Square.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndCompatibleMethodDeleteInstance", BRANCH_2_NAME, "Square.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getDeleteFieldAndCompatibleMethodPattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), 
				"Not one result for deleting field and compatible method?");

		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Deleted field is not t?");

		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m(java.lang.Number)", assignments.get(2).getSecond(), 
				"Top method is not m(java.lang.Number)?");

		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m(int)", assignments.get(3).getSecond(), 
				"Deleted and compatible method is not m(int)?");
	}

	@Test
	public void deleteFieldAndMethodInvocationTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodInvocationAndFieldDeleteInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodInvocationAndFieldDeleteInstance", BASE_BRANCH_NAME, "Shape.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodInvocationAndFieldDeleteInstance", BRANCH_1_NAME, "Shape.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodInvocationAndFieldDeleteInstance", BRANCH_2_NAME, "Shape.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getDeleteFieldAndMethodInvocationPattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		assertEquals(1, result.size(), 
				"Not one result for deleting field and method invocation?");

		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(3, assignments.size(), "Not 3 assignments with only 3 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Shape", assignments.get(0).getSecond(), 
				"Class is not base.Shape?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), 
				"Deleted field is not t?");

		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m()", assignments.get(2).getSecond(), 
				"Deleted method invoation is not m()?");
	}

	@Test
	public void deleteFieldAccessAndMethodTest() throws Exception {
		Matcher matcher = new Matcher(buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAccessAndMethodDeleteInstance", CONFIG_FILE_NAME));

		String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAccessAndMethodDeleteInstance", BASE_BRANCH_NAME, "Square.java");
		String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAccessAndMethodDeleteInstance", BRANCH_1_NAME, "Square.java");
		String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAccessAndMethodDeleteInstance", BRANCH_2_NAME, "Square.java");

		String[] bases = {basePath};
		assertTrue(checkIfFilesExist(bases));
		String[] variants1 = {firstVarPath};
		assertTrue(checkIfFilesExist(variants1));
		String[] variants2 = {secondVarPath};
		assertTrue(checkIfFilesExist(variants2));

		ConflictPattern cp = getDeleteFieldAccessAndMethodPattern();

		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), 
				"Not one result for deleting field and compatible method?");

		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), 
				"Not 4 assignments with only 4 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");

		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), 
				"Accessed field is not t?");

		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m()", assignments.get(2).getSecond(), 
				"Method with deleted access is not m()?");

		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m2()", assignments.get(3).getSecond(), 
				"Deleted method is not m2()?");
	}

	private ConflictPattern getDeletePrivateFieldAndPublicMethodPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, Visibility.PRIVATE);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		classPattern.addFieldPattern(fieldPattern);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		dp1.addActionPattern(new DeleteFieldPatternAction(fieldPattern, classPattern));
		dp2.addActionPattern(new DeleteMethodPatternAction(methodPattern, classPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Delete Pattern");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getDeletePrivateFieldAndPublicConstructorPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable constVar = new FreeVariable(2);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, Visibility.PRIVATE);
		ConstructorPattern cPattern =  new ConstructorPattern(constVar, Visibility.PUBLIC);
		classPattern.addFieldPattern(fieldPattern);
		classPattern.addConstructorPattern(cPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		dp1.addActionPattern(new DeleteFieldPatternAction(fieldPattern, classPattern));
		dp2.addActionPattern(new DeleteConstructorPatternAction(cPattern, classPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Delete Pattern 2");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getDeleteFieldAndCompatibleMethodPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable topMethodVar = new FreeVariable(2);
		FreeVariable subMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		MethodPattern topMethodPattern = new MethodPattern(topMethodVar, null);
		MethodPattern subMethodPattern = new MethodPattern(subMethodVar, null);
		classPattern.addFieldPattern(fieldPattern);
		classPattern.addMethodPattern(topMethodPattern);
		classPattern.addMethodPattern(subMethodPattern);
		classPattern.addCompatible(subMethodVar, topMethodVar);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		dp1.addActionPattern(new DeleteFieldPatternAction(fieldPattern, classPattern));
		dp2.addActionPattern(new DeleteMethodPatternAction(subMethodPattern, classPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Delete Pattern 3");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getDeleteFieldAndMethodInvocationPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		methodPattern.addDependency(methodVar);
		classPattern.addFieldPattern(fieldPattern);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodInvocationPattern deletedInvocation = new MethodInvocationPattern(methodVar);

		dp1.addActionPattern(new DeleteFieldPatternAction(fieldPattern, classPattern));
		dp2.addActionPattern(new DeleteInvocationPatternAction(deletedInvocation, methodPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Delete Pattern 4");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getDeleteFieldAccessAndMethodPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable methodVar1 = new FreeVariable(2);
		FreeVariable methodVar2 = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		MethodPattern methodPattern1 = new MethodPattern(methodVar1, null);
		MethodPattern methodPattern2 = new MethodPattern(methodVar2, null);
		FieldAccessPattern accessPattern = new FieldAccessPattern(fieldVar, null);
		methodPattern1.addFieldAccessPattern(accessPattern);
		classPattern.addFieldPattern(fieldPattern);
		classPattern.addMethodPattern(methodPattern1);
		classPattern.addMethodPattern(methodPattern2);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		dp1.addActionPattern(new DeleteFieldAccessPatternAction(accessPattern, methodPattern1));
		dp2.addActionPattern(new DeleteMethodPatternAction(methodPattern2, classPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Delete Pattern 4");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}
}
