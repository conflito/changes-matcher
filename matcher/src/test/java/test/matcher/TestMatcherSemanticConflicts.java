package test.matcher;

import org.junit.jupiter.api.Test;

import matcher.Matcher;
import matcher.catalogs.ConflictPatternCatalog;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;
import matcher.utils.Pair;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;


public class TestMatcherSemanticConflicts {

	private static final String SRC_FOLDER = "src"+ File.separator + "test"+ File.separator +
			"resources" + File.separator + "SemanticConflictsInstances" + File.separator;
	private static final String CONFIG_FILE_NAME = "config.properties";
	private static final String BASE_BRANCH_FOLDER = "base" + File.separator;
	private static final String VAR1_BRANCH_FOLDER = "branch01" + File.separator;
	private static final String VAR2_BRANCH_FOLDER = "branch02" + File.separator;

	private static final String OVERLOAD_ADDITION_FOLDER = 
			"AddOverloadingMByAdditionAddCall2M" + File.separator;
	private static final String OVERLOAD_ADDITION1_FOLDER = 
			"AddOverloadingMByAdditionAddCall2M_01" + File.separator;
	private static final String FIELD_HIDING_FOLDER = 
			"AddFieldHidingAddMethodThatUseDefFinChild" + File.separator;
	private static final String METHOD_OVERIDING_FOLDER = 
			"AddOveridingMAddCall2MInParent" + File.separator;
	private static final String OVERLOAD_ACCESS_CHANGE_FOLDER = 
			"AddOverloadingMByChangeAccessibility1AddCall2M" + File.separator;
	private static final String OVERLOAD_ACCESS_CHANGE1_FOLDER = 
			"AddOverloadingMByChangeAccessibility1AddCall2M_01" + File.separator;
	private static final String OVERLOAD_ACCESS_CHANGE2_FOLDER = 
			"AddOverloadingMByChangeAccessibility2AddCall2M" + File.separator;
	private static final String OVERLOAD_ACCESS_CHANGE201_FOLDER = 
			"AddOverloadingMByChangeAccessibility2AddCall2M_01" + File.separator;
	private static final String OVERRIDING_FOLDER = 
			"AddOverridingMAddCall2MinChild" + File.separator;
	private static final String REMOVE_OVERRIDER_FOLDER = 
			"RemoveOverridingMAddCall2M" + File.separator;
	private static final String CHANGE_METHOD1_FOLDER ="ChangeMethod01" + File.separator;
	private static final String CHANGE_METHOD101_FOLDER ="ChangeMethod01_01" + File.separator;
	private static final String CHANGE_METHOD2_FOLDER ="ChangeMethod02" + File.separator;
	private static final String DEPENDENCY_BASED1_FOLDER = "DependencyBased01" + File.separator;
	private static final String DEPENDENCY_BASED101_FOLDER = "DependencyBased01_01" + File.separator;
	private static final String DEPENDENCY_BASED2_FOLDER = "DependencyBased02" + File.separator;
	private static final String DEPENDENCY_BASED201_FOLDER = "DependencyBased02_01" + File.separator;
	private static final String DEPENDENCY_BASED3_FOLDER = "DependencyBased03" + File.separator;
	private static final String DEPENDENCY_BASED4_FOLDER = "DependencyBased04" + File.separator;
	private static final String DEPENDENCY_BASED5_FOLDER = "DependencyBased05" + File.separator;
	private static final String DEPENDENCY_BASED6_FOLDER = "DependencyBased06" + File.separator;
	private static final String DEPENDENCY_BASED601_FOLDER = "DependencyBased06_01" + File.separator;
	private static final String DEPENDENCY_BASED7_FOLDER = "DependencyBased07" + File.separator;
	private static final String UNEXPECTED_OVERIDE_FOLDER = 
			"UnexpectedOverriding01" + File.separator;
	private static final String UNEXPECTED_OVERIDE2_FOLDER = 
			"UnexpectedOverriding02" + File.separator;
	private static final String UNEXPECTED_OVERIDE3_FOLDER = 
			"UnexpectedOverriding03" + File.separator;
	private static final String UNEXPECTED_OVERIDE301_FOLDER = 
			"UnexpectedOverriding03_01" + File.separator;
	private static final String PARALLEL_CHANGED_FOLDER = "ParallelChanges" + File.separator;


	@Test
	public void overloadByAdditionTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ADDITION_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String firstVarPath = SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String secondVarPath = SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + 
				VAR2_BRANCH_FOLDER + "A.java";

		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};

		ConflictPattern cp = 
				ConflictPatternCatalog.getConflict(
						ConflictPatternCatalog.OVERLOAD_BY_ADDITION_CLASS_EXISTS);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("move(java.lang.Number, java.lang.Number)", 
				assignments.get(1).getSecond(), 
				"Method in class is not move(java.lang.Number, java.lang.Number)?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("reset()", assignments.get(2).getSecond(), 
				"Inserted method with invocation is not reset()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("move(int, int)", assignments.get(3).getSecond(), 
				"Inserted compatible method is not move(int, int)?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("base.A", assignments.get(4).getSecond(), "Class is not A");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "base.A", "The target class to test is not base.A?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "base.A.reset()V", 
				"Method to cover is not base.A.reset()V?");
		assertEquals(targetMethods.get(1), "base.A.move(II)V", 
				"Method to cover is not base.A.move(II)V?");
	}

	@Test
	public void overloadByAddition2Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ADDITION1_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + OVERLOAD_ADDITION1_FOLDER +
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + OVERLOAD_ADDITION1_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String newPath = SRC_FOLDER + OVERLOAD_ADDITION1_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {base1Path, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newPath};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.OVERLOAD_BY_ADDITION_CLASS_NEW);
				

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("move(java.lang.Number, java.lang.Number)", 
				assignments.get(1).getSecond(), 
				"Method in class is not move(java.lang.Number, java.lang.Number)?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("move(int, int)", assignments.get(2).getSecond(), 
				"Inserted compatible method is not move(int, int)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("reset()", assignments.get(3).getSecond(), 
				"Inserted method in new class with invocation is not reset()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("base.B", assignments.get(4).getSecond(), "New class is not B");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "base.B", "The target class to test is not base.B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "base.B.reset()V", 
				"Method to cover is not base.B.reset()V?");
		assertEquals(targetMethods.get(1), "base.A.move(II)V", 
				"Method to cover is not base.A.move(II)V?");
	}

	@Test
	public void addFieldHidingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ FIELD_HIDING_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + FIELD_HIDING_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String firstVarPath = SRC_FOLDER + FIELD_HIDING_FOLDER + 
				VAR1_BRANCH_FOLDER + "B.java";
		String secondVarPath = SRC_FOLDER + FIELD_HIDING_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.FIELD_HIDING);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "Class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("size", assignments.get(2).getSecond(), "Field is not size?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m()", assignments.get(3).getSecond(), 
				"Inserted method that writes to field is not m()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("size", assignments.get(4).getSecond(), "Inserted field not size?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 1, "There is not one method to cover?");
		assertEquals(targetMethods.get(0), "B.m()V", 
				"Method to cover is not B.m()V?");
	}

	@Test
	public void methodOveridingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ METHOD_OVERIDING_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + METHOD_OVERIDING_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + METHOD_OVERIDING_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String base2Path = SRC_FOLDER + METHOD_OVERIDING_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + METHOD_OVERIDING_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.ADD_METHOD_OVERRIDING);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "Class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("A.<init>()", assignments.get(2).getSecond(), 
				"Constructor is not A.<init>()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("resize()", assignments.get(3).getSecond(), 
				"Inserted method that writes to field is not resize()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("h", assignments.get(4).getSecond(), "Field is not h?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "A.<init>()V", 
				"Method to cover is not A.<init>()V?");
		assertEquals(targetMethods.get(1), "B.resize()V", 
				"Method to cover is not B.resize()V?");
	}

	@Test
	public void overloadingAccessChangeTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ACCESS_CHANGE_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String base2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.OVERLOAD_BY_ACCESS_CHANGE_CLASS_EXISTS);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "Class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("move(java.lang.Number, java.lang.Number)", 
				assignments.get(2).getSecond(), 
				"Top method is not move(java.lang.Number, java.lang.Number)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("move(int, int)", assignments.get(3).getSecond(), 
				"Sub method is not move(int, int)?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("reset()", assignments.get(4).getSecond(), 
				"Inserted method with invocation is not reset()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.reset()V", 
				"Method to cover is not B.reset()V?");
		assertEquals(targetMethods.get(1), "A.move(II)V", 
				"Method to cover is not A.move(II)V?");
	}

	@Test
	public void overloadingAccessChange_1Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ACCESS_CHANGE1_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE1_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE1_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String newClassPath = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE1_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {base1Path, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.OVERLOAD_BY_ACCESS_CHANGE_CLASS_NEW);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "New class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("move(java.lang.Number, java.lang.Number)", 
				assignments.get(2).getSecond(), 
				"Top method is not move(java.lang.Number, java.lang.Number)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("move(int, int)", assignments.get(3).getSecond(), 
				"Sub method is not move(int, int)?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("reset()", assignments.get(4).getSecond(), 
				"Inserted method in new class with invocation is not reset()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.reset()V", 
				"Method to cover is not B.reset()V?");
		assertEquals(targetMethods.get(1), "A.move(II)V", 
				"Method to cover is not A.move(II)V?");
	}

	@Test
	public void overloadingAccessChange2Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ACCESS_CHANGE2_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String base2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.OVERLOAD_BY_ACCESS_CHANGE_2_CLASS_EXISTS);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "Class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("move(java.lang.Number, java.lang.Number)", 
				assignments.get(2).getSecond(), 
				"Top method is not move(java.lang.Number, java.lang.Number)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("move(int, int)", assignments.get(3).getSecond(), 
				"Sub method is not move(int, int)?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("reset()", assignments.get(4).getSecond(), 
				"Inserted method with invocation is not reset()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.reset()V", 
				"Method to cover is not B.reset()V?");
		assertEquals(targetMethods.get(1), "A.move(Ljava/lang/Number;Ljava/lang/Number;)V", 
				"Method to cover is not A.move(Ljava/lang/Number;Ljava/lang/Number;)V?");
	}

	@Test
	public void overloadingAccessChange2_1Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ACCESS_CHANGE201_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE201_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE201_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String newClassPath = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE201_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {base1Path, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.OVERLOAD_BY_ACCESS_CHANGE_2_CLASS_NEW);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "New class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("move(java.lang.Number, java.lang.Number)", 
				assignments.get(2).getSecond(), 
				"Top method is not move(java.lang.Number, java.lang.Number)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("move(int, int)", assignments.get(3).getSecond(), 
				"Sub method is not move(int, int)?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("reset()", assignments.get(4).getSecond(), 
				"Inserted method with invocation is not reset()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.reset()V", 
				"Method to cover is not B.reset()V?");
		assertEquals(targetMethods.get(1), "A.move(Ljava/lang/Number;Ljava/lang/Number;)V", 
				"Method to cover is not A.move(Ljava/lang/Number;Ljava/lang/Number;)V?");
	}

	@Test
	public void overridingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERRIDING_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + OVERRIDING_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var1Path = SRC_FOLDER + OVERRIDING_FOLDER + 
				VAR1_BRANCH_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + OVERRIDING_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.ADD_METHOD_OVERRIDING_2);

		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for overriding method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "Class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("move(int, int)", assignments.get(2).getSecond(), 
				"Method overwritten is not move(int, int)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("reset()", assignments.get(3).getSecond(), 
				"Method with invocation is not reset()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.reset()V", 
				"Method to cover is not B.reset()V?");
		assertEquals(targetMethods.get(1), "B.move(II)V", 
				"Method to cover is not B.move(II)V?");
	}

	@Test
	public void removeOverridingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ REMOVE_OVERRIDER_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + REMOVE_OVERRIDER_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var1Path = SRC_FOLDER + REMOVE_OVERRIDER_FOLDER + 
				VAR1_BRANCH_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + REMOVE_OVERRIDER_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.REMOVE_OVERRIDING);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for remove overriding method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "Class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("move(int, int)", assignments.get(2).getSecond(), 
				"Method removed is not move(int, int)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("reset()", assignments.get(3).getSecond(), 
				"Method with invocation is not reset()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.reset()V", 
				"Method to cover is not B.reset()V?");
		assertEquals(targetMethods.get(1), "A.move(II)V", 
				"Method to cover is not A.move(II)V?");
	}

	@Test
	public void changeMethod1Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ CHANGE_METHOD1_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + CHANGE_METHOD1_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + CHANGE_METHOD1_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String var2Path = SRC_FOLDER + CHANGE_METHOD1_FOLDER + 
				VAR2_BRANCH_FOLDER + "A.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.CHANGE_METHOD);

		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(2, result.size(), "Not two results for method change?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m()", assignments.get(1).getSecond(), 
				"Method with invocations is not m()?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1()", assignments.get(2).getSecond(),
				"Method updated is not m1()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m2()", assignments.get(3).getSecond(), 
				"Other method updated is not m2()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("A", assignments.get(4).getSecond(),
				"Class that holds m1() isn't A?");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("A", assignments.get(5).getSecond(), 
				"Class that holds m2() isn't A?");

		assignments = result.get(1);
		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m()", assignments.get(1).getSecond(), 
				"Method with invocations is not m()?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m2()", assignments.get(2).getSecond(),
				"Method updated is not m2()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m1()", assignments.get(3).getSecond(), 
				"Other method updated is not m1()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("A", assignments.get(4).getSecond(),
				"Class that holds m2() isn't A?");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("A", assignments.get(5).getSecond(), 
				"Class that holds m1() isn't A?");
				
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(2, goals.size(), "There are not two goals to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 3, "There are not three methods to cover?");
		assertEquals(targetMethods.get(0), "A.m()I", 
				"Method to cover is not A.m()I?");
		assertEquals(targetMethods.get(1), "A.m1()I", 
				"Method to cover is not A.m1()I?");
		assertEquals(targetMethods.get(2), "A.m2()I", 
				"Method to cover is not A.m2()I?");
		
		goal = goals.get(1);
		targetClass = goal.getFirst();
		targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 3, "There are not three methods to cover?");
		assertEquals(targetMethods.get(0), "A.m()I", 
				"Method to cover is not A.m()I?");
		assertEquals(targetMethods.get(1), "A.m2()I", 
				"Method to cover is not A.m2()I?");
		assertEquals(targetMethods.get(2), "A.m1()I", 
				"Method to cover is not A.m1()I?");
	}
	
	@Test
	public void changeMethod1_1Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ CHANGE_METHOD101_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + CHANGE_METHOD101_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + CHANGE_METHOD101_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String var2Path = SRC_FOLDER + CHANGE_METHOD101_FOLDER + 
				VAR2_BRANCH_FOLDER + "A.java";
		
		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.CHANGE_METHOD);

		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(2, result.size(), "Not two results for method change 1_01?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("B", assignments.get(0).getSecond(), "Dependant class is not B");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m()", assignments.get(1).getSecond(), 
				"Method with invocations is not m()?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1()", assignments.get(2).getSecond(), 
				"Method updated is not m1()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m2()", assignments.get(3).getSecond(), 
				"Other method updated is not m2()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("A", assignments.get(4).getSecond(), 
				"Class that holds m1() isn't A?");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("A", assignments.get(5).getSecond(), 
				"Class that holds m2() isn't A?");

		assignments = result.get(1);
		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("B", assignments.get(0).getSecond(), "Dependant class is not B");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m()", assignments.get(1).getSecond(), 
				"Method with invocations is not m()?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m2()", assignments.get(2).getSecond(), 
				"Method updated is not m2()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m1()", assignments.get(3).getSecond(), 
				"Other method updated is not m1()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("A", assignments.get(4).getSecond(), 
				"Class that holds m2() isn't A?");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("A", assignments.get(5).getSecond(), 
				"Class that holds m1() isn't A?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(2, goals.size(), "There are not two goals to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 3, "There are not three methods to cover?");
		assertEquals(targetMethods.get(0), "B.m()I", 
				"Method to cover is not B.m()I?");
		assertEquals(targetMethods.get(1), "A.m1()I", 
				"Method to cover is not A.m1()I?");
		assertEquals(targetMethods.get(2), "A.m2()I", 
				"Method to cover is not A.m2()I?");
		
		goal = goals.get(1);
		targetClass = goal.getFirst();
		targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 3, "There are not three methods to cover?");
		assertEquals(targetMethods.get(0), "B.m()I", 
				"Method to cover is not B.m()I?");
		assertEquals(targetMethods.get(1), "A.m2()I", 
				"Method to cover is not A.m2()I?");
		assertEquals(targetMethods.get(2), "A.m1()I", 
				"Method to cover is not A.m1()I?");
	}

	@Test
	public void changeMethod2Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ CHANGE_METHOD2_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + CHANGE_METHOD2_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + CHANGE_METHOD2_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String var2Path = SRC_FOLDER + CHANGE_METHOD2_FOLDER + 
				VAR2_BRANCH_FOLDER + "A.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.CHANGE_METHOD_2);


		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for change method 2?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(9, assignments.size(), "Not 9 assignments with only 9 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "Interface is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("B1", assignments.get(2).getSecond(), 
				"Class that implements hashCode is not B1?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("B2", assignments.get(3).getSecond(), 
				"Class that does not implement hashCode is not B2?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("n()", assignments.get(4).getSecond(), 
				"Method that has its invocation updated is not n()?");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("m1(B[])", assignments.get(5).getSecond(), 
				"The new invocation is not of method m1(B[])?");
		
		assertEquals(6, assignments.get(6).getFirst(), "Variable id is not 6?"); 
		assertEquals("m2(B[])", assignments.get(6).getSecond(), 
				"The old invocation is not of method m2(B[])?");
		
		assertEquals(7, assignments.get(7).getFirst(), "Variable id is not 7?"); 
		assertEquals("hashCode()", assignments.get(7).getSecond(), 
				"The method that B1 has that B2 doesn't isn't hashCode()?");
		
		assertEquals(8, assignments.get(8).getFirst(), "Variable id is not 8?"); 
		assertEquals("v", assignments.get(8).getSecond(), "The updated field is not v");
				
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 3, "There are not three methods to cover?");
		assertEquals(targetMethods.get(0), "A.n()Z", 
				"Method to cover is not A.n()Z?");
		assertEquals(targetMethods.get(1), "A.m1([LB;)Z", 
				"Method to cover is not A.m1([LB;)Z?");
		assertEquals(targetMethods.get(2), "B2.hashCode()I", 
				"Method to cover is not B2.hasCode()I?");
	}

	@Test
	public void dependencyBased1Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED1_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + DEPENDENCY_BASED1_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED1_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String var2Path = SRC_FOLDER + DEPENDENCY_BASED1_FOLDER + 
				VAR2_BRANCH_FOLDER + "A.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.DEPENDENCY_BASED_CLASS_EXISTS);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for dependency based?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m1()", assignments.get(1).getSecond(),
				"Updated method is not m1()?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("A", assignments.get(2).getSecond(), 
				"Class that holds the new method is not A?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("k()", assignments.get(3).getSecond(), 
				"Method inserted with t is not k()?");
				
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "A.k()I", 
				"Method to cover is not A.k()I?");
		assertEquals(targetMethods.get(1), "A.m1()I", 
				"Method to cover is not A.m1()I?");
	}

	@Test
	public void dependencyBased1_01Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED101_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + DEPENDENCY_BASED101_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED101_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String base2Path = SRC_FOLDER + DEPENDENCY_BASED101_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + DEPENDENCY_BASED101_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.DEPENDENCY_BASED_CLASS_EXISTS);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for dependency based?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m1()", assignments.get(1).getSecond(), 
				"Updated method is not m1()?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("B", assignments.get(2).getSecond(), 
				"Class that holds the new method is not B?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("k()", assignments.get(3).getSecond(), 
				"Method inserted with t is not k()?");
				
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.k()I", 
				"Method to cover is not B.k()I?");
		assertEquals(targetMethods.get(1), "A.m1()I", 
				"Method to cover is not A.m1()I?");
	}

	@Test
	public void dependencyBased2Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED2_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + DEPENDENCY_BASED2_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED2_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED2_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {basePath, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.DEPENDENCY_BASED_CLASS_NEW);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for dependency based 2?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m1()", assignments.get(1).getSecond(),
				"Method updated is not m1()?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("B", assignments.get(2).getSecond(), "New class is not B?");
		
		assertEquals(3,assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("k()", assignments.get(3).getSecond(), 
				"New method that depends on m1() is not k()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.k()I", 
				"Method to cover is not B.k()I?");
		assertEquals(targetMethods.get(1), "A.m1()I", 
				"Method to cover is not A.m1()I?");
	}

	@Test
	public void dependencyBased2_01Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED201_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + DEPENDENCY_BASED201_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED201_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED201_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";
		String newClass2Path = SRC_FOLDER + DEPENDENCY_BASED201_FOLDER + 
				VAR2_BRANCH_FOLDER + "C.java";

		String[] bases = {basePath, null, null};
		String[] variants1 = {var1Path, null, null};
		String[] variants2 = {null, newClassPath, newClass2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.DEPENDENCY_BASED_CLASS_NEW);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(2, result.size(), "Not 2 results for dependency based 2_01?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m1()", assignments.get(1).getSecond(), 
				"Method updated is not m1()?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("B", assignments.get(2).getSecond(), "New class is not B?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("k()", assignments.get(3).getSecond(), 
				"Method inserted with invocation is not k()?");

		assignments = result.get(1);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m1()", assignments.get(1).getSecond(), 
				"Method updated is not m1()?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("C", assignments.get(2).getSecond(), "New class is not C?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("method()", assignments.get(3).getSecond(), 
				"Method inserted with invocation is not method()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(2, goals.size(), "There are not two goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.k()I", 
				"Method to cover is not B.k()I?");
		assertEquals(targetMethods.get(1), "A.m1()I", 
				"Method to cover is not A.m1()I?");
		
		goal = goals.get(1);
		targetClass = goal.getFirst();
		targetMethods = goal.getSecond();
		assertEquals(targetClass, "C", "The target class to test is not C?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "C.method()I", 
				"Method to cover is not C.method()I?");
		assertEquals(targetMethods.get(1), "A.m1()I", 
				"Method to cover is not A.m1()I?");
	}

	@Test
	public void dependencyBased3Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED3_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + DEPENDENCY_BASED3_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED3_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED3_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {basePath, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.DEPENDENCY_BASED_CLASS_NEW);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not than one result for dependency based 3?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m1(int, int)", assignments.get(1).getSecond(), 
				"Method in A is not m1(int, int)?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("B", assignments.get(2).getSecond(), "New class is not B?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("k()", assignments.get(3).getSecond(), 
				"New method with invocation is not k()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.k()I", 
				"Method to cover is not B.k()I?");
		assertEquals(targetMethods.get(1), "A.m1(II)I", 
				"Method to cover is not A.m1(II)I?");
	}

	@Test
	public void dependencyBased4Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED4_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + DEPENDENCY_BASED4_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED4_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED4_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {basePath, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.DEPENDENCY_BASED_CLASS_NEW);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);				

		assertEquals(1, result.size(), "Not one result for dependency based 4?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m1(double, int)", assignments.get(1).getSecond(), 
				"Method updated is not m1(doublt, int)?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("B", assignments.get(2).getSecond(), 
				"New class is not B?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("k()", assignments.get(3).getSecond(), 
				"Method inserted with invocation is not k()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.k()I", 
				"Method to cover is not B.k()I?");
		assertEquals(targetMethods.get(1), "A.m1(DI)I", 
				"Method to cover is not A.m1(DI)I?");
	}

	@Test
	public void dependencyBased5Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED5_FOLDER + CONFIG_FILE_NAME);


		String basePath = SRC_FOLDER + DEPENDENCY_BASED5_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED5_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED5_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {basePath, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.DEPENDENCY_BASED_FIELD_TYPE_UPDATE_CLASS_NEW);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for dependency based 5?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("x", assignments.get(1).getSecond(), "Updated field is not x?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1()", assignments.get(2).getSecond(), 
				"Method that reads x is not m1()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("B", assignments.get(3).getSecond(), "New class is not B?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("k()", assignments.get(4).getSecond(), 
				"Method inserted with invocation is not k()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.k()I", 
				"Method to cover is not B.k()I?");
		assertEquals(targetMethods.get(1), "A.m1()I", 
				"Method to cover is not A.m1()I?");
	}

	@Test
	public void dependencyBased6Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED6_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + DEPENDENCY_BASED6_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED6_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String base2Path = SRC_FOLDER + DEPENDENCY_BASED6_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + DEPENDENCY_BASED6_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.DEPENDENCY_BASED_FIELD_TYPE_UPDATE_CLASS_EXISTS);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for dependency based 5?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("x", assignments.get(1).getSecond(), "Updated field is not x?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1()", assignments.get(2).getSecond(), 
				"Method that reads x is not m1()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("B", assignments.get(3).getSecond(), 
				"Class is not B?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("k()", assignments.get(4).getSecond(), 
				"Method that depends on m1() is not k()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.k()I", 
				"Method to cover is not B.k()I?");
		assertEquals(targetMethods.get(1), "A.m1()I", 
				"Method to cover is not A.m1()I?");
	}

	@Test
	public void dependencyBased6_01Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED601_FOLDER + CONFIG_FILE_NAME);


		String basePath = SRC_FOLDER + DEPENDENCY_BASED601_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED601_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String var2Path = SRC_FOLDER + DEPENDENCY_BASED601_FOLDER + 
				VAR2_BRANCH_FOLDER + "A.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.DEPENDENCY_BASED_FIELD_TYPE_UPDATE_CLASS_EXISTS);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for dependency based 5?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("x", assignments.get(1).getSecond(), "Updated field is not x?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1()", assignments.get(2).getSecond(), 
				"Method that reads x is not m1()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("A", assignments.get(3).getSecond(), "Class is not A?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("m2()", assignments.get(4).getSecond(), 
				"Method that depends on m1() is not m2()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "A.m2()I", 
				"Method to cover is not A.m2()I?");
		assertEquals(targetMethods.get(1), "A.m1()I", 
				"Method to cover is not A.m1()I?");
	}

	@Test
	public void dependencyBased7Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED7_FOLDER + CONFIG_FILE_NAME);


		String basePath = SRC_FOLDER + DEPENDENCY_BASED7_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED7_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String var2Path = SRC_FOLDER + DEPENDENCY_BASED7_FOLDER + 
				VAR2_BRANCH_FOLDER + "A.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.DEPENDENCY_BASED_DEPENDENCY_INSERT);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		assertEquals(1, result.size(), "Not one result for dependency based 7?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), 
				"Class that holds m() is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("A", assignments.get(1).getSecond(), 
				"Class that holds n() is not A?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("A", assignments.get(2).getSecond(), 
				"Class that holds m1() is not A?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m()", assignments.get(3).getSecond(), 
				"Updated method is not m()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("n()", assignments.get(4).getSecond(), 
				"Method that depends on m() is not n()?");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("m1()", assignments.get(5).getSecond(), 
				"Method that receives a call to n() is not m1()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "A.m1()I", 
				"Method to cover is not A.m1()I?");
		assertEquals(targetMethods.get(1), "A.m()I", 
				"Method to cover is not A.m()I?");
	}

	@Test
	public void unexpectedOverridingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UNEXPECTED_OVERIDE_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String base2Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + 
				BASE_BRANCH_FOLDER + "B0.java";
		String var2Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + 
				VAR2_BRANCH_FOLDER + "B0.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.UNEXPECTED_OVERRIDING);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		assertEquals(1, result.size(), "Not one result for unexpected overriding 1?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B0", assignments.get(1).getSecond(), "Other class is not B0?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("b", assignments.get(2).getSecond(), "Field is not b?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("n(A)", assignments.get(3).getSecond(), 
				"Inserted method is not n(A)?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4? "); 
		assertEquals("equals(java.lang.Object)", assignments.get(4).getSecond(), 
				"Method overriden is not equals(java.lang.Object)");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("B", assignments.get(5).getSecond(), "Interface is not B?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "A.n(LA;)Z", 
				"Method to cover is not A.n(LA;)Z?");
		assertEquals(targetMethods.get(1), "B0.equals(Ljava/lang/Object;)Z", 
				"Method to cover is not B0.equals(Ljava/lang/Object;)Z?");
	}

	@Test
	public void unexpectedOverriding2Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UNEXPECTED_OVERIDE2_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + UNEXPECTED_OVERIDE2_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + UNEXPECTED_OVERIDE2_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String base2Path = SRC_FOLDER + UNEXPECTED_OVERIDE2_FOLDER + 
				BASE_BRANCH_FOLDER + "B2.java";
		String var2Path = SRC_FOLDER + UNEXPECTED_OVERIDE2_FOLDER + 
				VAR2_BRANCH_FOLDER + "B2.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.UNEXPECTED_OVERRIDING_2);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		assertEquals(1, result.size(), "Not one result for unexpected overriding 2?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(7, assignments.size(), "Not 7 assignments with only 7 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B1", assignments.get(1).getSecond(), "Other class is not B1?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("B2", assignments.get(2).getSecond(), 
				"Third class that extends second is not B2?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("b", assignments.get(3).getSecond(), "Field is not b?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("m()", assignments.get(4).getSecond(), 
				"Method overriden is not m()");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("n()", assignments.get(5).getSecond(), 
				"Method that invokes overriden method is not n()");
		
		assertEquals(6, assignments.get(6).getFirst(), "Variable id is not 6?"); 
		assertEquals("B", assignments.get(6).getSecond(), "Interface is not B?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "A.n()I", 
				"Method to cover is not A.n()I?");
		assertEquals(targetMethods.get(1), "B2.m()I", 
				"Method to cover is not B2.m()I");
	}

	@Test
	public void unexpectedOverriding3Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UNEXPECTED_OVERIDE3_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + UNEXPECTED_OVERIDE3_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var1Path = SRC_FOLDER + UNEXPECTED_OVERIDE3_FOLDER + 
				VAR1_BRANCH_FOLDER + "B.java";
		String base2Path = SRC_FOLDER + UNEXPECTED_OVERIDE3_FOLDER + 
				BASE_BRANCH_FOLDER + "C.java";
		String var2Path = SRC_FOLDER + UNEXPECTED_OVERIDE3_FOLDER + 
				VAR2_BRANCH_FOLDER + "C.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.UNEXPECTED_OVERRIDING_3_NEW_METHOD);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		assertEquals(1, result.size(), "Not one result for unexpected overriding 2?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), 
				"Class that extends A is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("C", assignments.get(2).getSecond(), "Third class is not C?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m(java.lang.Number)", assignments.get(3).getSecond(), 
				"Method in A is not m(java.lang.Number)");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("m(int)", assignments.get(4).getSecond(), 
				"Compatible method inserted in B is not m(int)");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("k()", assignments.get(5).getSecond(), 
				"Method added that depends on m(java.lang.Number) is not k()");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "C", "The target class to test is not C?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "C.k()V", 
				"Method to cover is not C.k()V?");
		assertEquals(targetMethods.get(1), "B.m(I)V", 
				"Method to cover is not B.m(I)V?");
	}

	@Test
	public void unexpectedOverriding3_1Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UNEXPECTED_OVERIDE301_FOLDER + CONFIG_FILE_NAME);


		String basePath = SRC_FOLDER + UNEXPECTED_OVERIDE301_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var1Path = SRC_FOLDER + UNEXPECTED_OVERIDE301_FOLDER + 
				VAR1_BRANCH_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + UNEXPECTED_OVERIDE301_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.UNEXPECTED_OVERRIDING_3_NEW_DEPENDENCY);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not than one result for unexpected overriding 3_1?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), 
				"Class that extends A is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("B", assignments.get(2).getSecond(), 
				"Class that has m1() class is not B?");
		
		assertEquals(3,assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m(java.lang.Number)", assignments.get(3).getSecond(), 
				"Method in A is not m(java.lang.Number)");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("m1()", assignments.get(4).getSecond(), 
				"Method in B is not m1()");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("m(int)", assignments.get(5).getSecond(), 
				"Method added compatible with m(java.lang.Number) is not m(int)");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "B", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "B.m1()V", 
				"Method to cover is not B.m1()V?");
		assertEquals(targetMethods.get(1), "B.m(I)V", 
				"Method to cover is not B.m(I)V");
	}

	@Test
	public void parallelChangesTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ PARALLEL_CHANGED_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + PARALLEL_CHANGED_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + PARALLEL_CHANGED_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String var2Path = SRC_FOLDER + PARALLEL_CHANGED_FOLDER + 
				VAR2_BRANCH_FOLDER + "A.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = ConflictPatternCatalog.getConflict(
				ConflictPatternCatalog.PARALLEL_CHANGES);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for parallel changes?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m()", assignments.get(1).getSecond(),
				"Updated method is not m()?");
				
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 1, "There is not one method to cover?");
		assertEquals(targetMethods.get(0), "A.m()I", 
				"Method to cover is not A.m()I?");
	}
}
