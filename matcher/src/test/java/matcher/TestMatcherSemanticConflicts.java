package matcher;

import org.junit.jupiter.api.Test;

import matcher.Matcher;
import matcher.catalogs.PatternParser;
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
	private static final String OVERLOAD_ADDITION2_FOLDER = 
			"AddOverloadingMByAdditionAddCall2M_02" + File.separator;
	
	private static final String FIELD_HIDING_FOLDER = 
			"AddFieldHidingAddMethodThatUseDefFinChild" + File.separator;
	
	private static final String OVERLOAD_ACCESS_CHANGE_FOLDER = 
			"AddOverloadingMByChangeAccessibility1AddCall2M" + File.separator;
	private static final String OVERLOAD_ACCESS_CHANGE1_FOLDER = 
			"AddOverloadingMByChangeAccessibility1AddCall2M_01" + File.separator;
	private static final String OVERLOAD_ACCESS_CHANGE12_FOLDER = 
			"AddOverloadingMByChangeAccessibility1AddCall2M_02" + File.separator;
	
	private static final String OVERLOAD_ACCESS_CHANGE2_FOLDER = 
			"AddOverloadingMByChangeAccessibility2AddCall2M" + File.separator;
	private static final String OVERLOAD_ACCESS_CHANGE201_FOLDER = 
			"AddOverloadingMByChangeAccessibility2AddCall2M_01" + File.separator;
	private static final String OVERLOAD_ACCESS_CHANGE2012_FOLDER = 
			"AddOverloadingMByChangeAccessibility2AddCall2M_02" + File.separator;
	
	private static final String OVERRIDING_FOLDER = 
			"AddOverridingMAddCall2MinChild" + File.separator;
	private static final String OVERRIDING_FOLDER_01 = 
			"AddOverridingMAddCall2MinChild_01" + File.separator;
	private static final String OVERRIDING_FOLDER_02 = 
			"AddOverridingMAddCall2MinChild_02" + File.separator;
	
	
	private static final String REMOVE_OVERRIDER_FOLDER = 
			"RemoveOverridingMAddCall2M" + File.separator;
	
	private static final String CHANGE_METHOD1_FOLDER = 
			"ChangeMethod01" + File.separator;
	private static final String CHANGE_METHOD101_FOLDER = 
			"ChangeMethod01_01" + File.separator;
	
	private static final String CHANGE_METHOD2_FOLDER = 
			"ChangeMethod02" + File.separator;
	private static final String CHANGE_METHOD3_FOLDER = 
			"ChangeMethod03" + File.separator;
	
	private static final String DEPENDENCY_BASED1_FOLDER = 
			"DependencyBased01" + File.separator;
	private static final String DEPENDENCY_BASED101_FOLDER = 
			"DependencyBased01_01" + File.separator;
	private static final String DEPENDENCY_BASED2_FOLDER = 
			"DependencyBased02" + File.separator;
	private static final String DEPENDENCY_BASED201_FOLDER = 
			"DependencyBased02_01" + File.separator;
	private static final String DEPENDENCY_BASED3_FOLDER = 
			"DependencyBased03" + File.separator;
	private static final String DEPENDENCY_BASED4_FOLDER = 
			"DependencyBased04" + File.separator;

	private static final String DEPENDENCY_BASED7_FOLDER = 
			"DependencyBased07" + File.separator;
	
	private static final String UNEXPECTED_OVERIDE_FOLDER = 
			"UnexpectedOverriding01" + File.separator;
	private static final String UNEXPECTED_OVERIDE_01_FOLDER = 
			"UnexpectedOverriding01_01" + File.separator;
	private static final String UNEXPECTED_OVERIDE_02_FOLDER = 
			"UnexpectedOverriding01_02" + File.separator;
	
	private static final String UNEXPECTED_OVERIDE3_FOLDER = 
			"UnexpectedOverriding03" + File.separator;
	private static final String UNEXPECTED_OVERIDE301_FOLDER = 
			"UnexpectedOverriding03_01" + File.separator;
	private static final String UNEXPECTED_OVERIDE302_FOLDER = 
			"UnexpectedOverriding03_02" + File.separator;
	
	private static final String PARALLEL_CHANGED_FOLDER = 
			"ParallelChanges" + File.separator;
	
	private static final String PARALLEL_CHANGED_CONSTRUCTOR_FOLDER = 
			"ParallelChangesConstructor" + File.separator;


	@Test
	public void overloadByAdditionNewMethodTest() throws ApplicationException {
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "OverloadByAdditionNewMethod.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("base.A", assignments.get(1).getSecond(), "Holder class is not A");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("move(java.lang.Number, java.lang.Number)", 
				assignments.get(2).getSecond(), 
				"Method in class is not move(java.lang.Number, java.lang.Number)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("reset()", assignments.get(3).getSecond(), 
				"Inserted method with invocation is not reset()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("move(int, int)", assignments.get(4).getSecond(), 
				"Inserted compatible method is not move(int, int)?");

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
	public void overloadByAdditionNewClassTest() throws ApplicationException {
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
				
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "OverloadByAdditionNewClass.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("base.B", assignments.get(1).getSecond(), "New class is not B");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("move(java.lang.Number, java.lang.Number)", 
				assignments.get(2).getSecond(), 
				"Method in class is not move(java.lang.Number, java.lang.Number)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("reset()", assignments.get(3).getSecond(), 
				"Inserted method in new class with invocation is not reset()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("move(int, int)", assignments.get(4).getSecond(), 
				"Inserted compatible method is not move(int, int)?");
		
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
	public void overloadByAdditionNewCallTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ADDITION2_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + OVERLOAD_ADDITION2_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String firstVarPath = SRC_FOLDER + OVERLOAD_ADDITION2_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String secondVarPath = SRC_FOLDER + OVERLOAD_ADDITION2_FOLDER + 
				VAR2_BRANCH_FOLDER + "A.java";

		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "OverloadByAdditionNewDependency.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("base.A", assignments.get(1).getSecond(), "Holder class is not A");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("move(java.lang.Number, java.lang.Number)", 
				assignments.get(2).getSecond(), 
				"Method in class is not move(java.lang.Number, java.lang.Number)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("k()", assignments.get(3).getSecond(), 
				"Method that already exists is not k()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("move(int, int)", assignments.get(4).getSecond(), 
				"Inserted compatible method is not move(int, int)?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "base.A", "The target class to test is not base.A?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "base.A.k()V", 
				"Method to cover is not base.A.k()V?");
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "FieldHiding.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for overloading method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "Class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m()", assignments.get(2).getSecond(), 
				"Inserted method that writes to field is not m()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("size", assignments.get(3).getSecond(), "Field is not size?");
		
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
	public void overloadingAccessChangeNewMethodTest() throws ApplicationException {
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "OverloadByAccessChangeNewMethod.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

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
	public void overloadingAccessChangeNewCallTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ACCESS_CHANGE12_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE12_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE12_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String base2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE12_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE12_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "OverloadByAccessChangeNewDependency.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

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
	public void overloadingAccessChangeNewClassTest() throws ApplicationException {
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

//		ConflictPattern cp = ConflictPatternCatalog.getConflict(
//				ConflictPatternCatalog.OVERLOAD_BY_ACCESS_CHANGE_NEW_CLASS);
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "OverloadByAccessChangeNewClass.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

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
	public void overloadingAccessChange2NewMethodTest() throws ApplicationException {
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

		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "OverloadByAccessChange2NewMethod.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);
		
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
	public void overloadingAccessChange2NewCallTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ACCESS_CHANGE2012_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2012_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2012_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String base2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2012_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2012_FOLDER + 
				VAR2_BRANCH_FOLDER + "B.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "OverloadByAccessChange2NewDependency.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

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
		assertEquals(targetMethods.get(1), "A.move(Ljava/lang/Number;Ljava/lang/Number;)V", 
				"Method to cover is not A.move(Ljava/lang/Number;Ljava/lang/Number;)V?");
	}

	@Test
	public void overloadingAccessChange2NewClassTest() throws ApplicationException {
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

		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "OverloadByAccessChange2NewClass.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);
		
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
	public void addMethodOverridingNewMethodTest() throws ApplicationException {
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

		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "AddOverridingNewMethod.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for overriding method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "Class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("B", assignments.get(2).getSecond(), "Class that holds reset()"
				+ " is not B?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("move(int, int)", assignments.get(3).getSecond(), 
				"Method overwritten is not move(int, int)?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("reset()", assignments.get(4).getSecond(), 
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
	public void addMethodOverridingNewClassTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERRIDING_FOLDER_01 + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + OVERRIDING_FOLDER_01 + 
				BASE_BRANCH_FOLDER + "B.java";
		String var1Path = SRC_FOLDER + OVERRIDING_FOLDER_01 + 
				VAR1_BRANCH_FOLDER + "B.java";
		String newClassPath = SRC_FOLDER + OVERRIDING_FOLDER_01 + 
				VAR2_BRANCH_FOLDER + "C.java";

		String[] bases = {basePath, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "AddOverridingNewClass.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for overriding method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "Class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("C", assignments.get(2).getSecond(), "New "
				+ "class that holds k() is not C?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("move(int, int)", assignments.get(3).getSecond(), 
				"Method overwritten is not move(int, int)?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("k()", assignments.get(4).getSecond(), 
				"Method with invocation is not k()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "C", "The target class to test is not C?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "C.k()V", 
				"Method to cover is not C.k()V?");
		assertEquals(targetMethods.get(1), "B.move(II)V", 
				"Method to cover is not B.move(II)V?");
	}
	
	@Test
	public void addMethodOverridingNewCallTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERRIDING_FOLDER_02 + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + OVERRIDING_FOLDER_02 + 
				BASE_BRANCH_FOLDER + "B.java";
		String basePath2 = SRC_FOLDER + OVERRIDING_FOLDER_02 + 
				BASE_BRANCH_FOLDER + "C.java";
		
		String var1Path = SRC_FOLDER + OVERRIDING_FOLDER_02 + 
				VAR1_BRANCH_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + OVERRIDING_FOLDER_02 + 
				VAR2_BRANCH_FOLDER + "C.java";

		String[] bases = {basePath, basePath2};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "AddOverridingNewDependency.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for overriding method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(5, assignments.size(), "Not 5 assignments with only 5 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Superclass is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "Class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("C", assignments.get(2).getSecond(), "Class that "
				+ "holds k() is not C?");		
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("move(int, int)", assignments.get(3).getSecond(), 
				"Method overwritten is not move(int, int)?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("k()", assignments.get(4).getSecond(), 
				"Method with new invocation is not k()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "C", "The target class to test is not C?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "C.k()I", 
				"Method to cover is not C.k()I?");
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "RemoveOverriding.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "ChangeMethod.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(2, result.size(), "Not two results for method change?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("A", assignments.get(1).getSecond(),
				"Class that holds m1() isn't A?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("A", assignments.get(2).getSecond(), 
				"Class that holds m2() isn't A?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m()", assignments.get(3).getSecond(), 
				"Method with invocations is not m()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("m1()", assignments.get(4).getSecond(),
				"Method updated is not m1()?");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("m2()", assignments.get(5).getSecond(), 
				"Other method updated is not m2()?");
		
		assignments = result.get(1);
		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");

		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("A", assignments.get(1).getSecond(),
				"Class that holds m1() isn't A?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("A", assignments.get(2).getSecond(), 
				"Class that holds m2() isn't A?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m()", assignments.get(3).getSecond(), 
				"Method with invocations is not m()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("m2()", assignments.get(4).getSecond(),
				"Method updated is not m2()?");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("m1()", assignments.get(5).getSecond(), 
				"Other method updated is not m1()?");
				
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "ChangeMethod.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(2, result.size(), "Not two results for method change 1_01?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("B", assignments.get(0).getSecond(), "Dependant class is not B");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("A", assignments.get(1).getSecond(), 
				"Class that holds m1() isn't A?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("A", assignments.get(2).getSecond(), 
				"Class that holds m2() isn't A?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m()", assignments.get(3).getSecond(), 
				"Method with invocations is not m()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("m1()", assignments.get(4).getSecond(), 
				"Method updated is not m1()?");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("m2()", assignments.get(5).getSecond(), 
				"Other method updated is not m2()?");
		
		assignments = result.get(1);

		assertEquals(6, assignments.size(), "Not 6 assignments with only 6 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("B", assignments.get(0).getSecond(), "Dependant class is not B");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("A", assignments.get(1).getSecond(), 
				"Class that holds m1() isn't A?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("A", assignments.get(2).getSecond(), 
				"Class that holds m2() isn't A?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m()", assignments.get(3).getSecond(), 
				"Method with invocations is not m()?");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("m2()", assignments.get(4).getSecond(), 
				"Method updated is not m2()?");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("m1()", assignments.get(5).getSecond(), 
				"Other method updated is not m1()?");
		
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

		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "ChangeMethod2.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);
		
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
	public void changeMethod3Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ CHANGE_METHOD3_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + CHANGE_METHOD3_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + CHANGE_METHOD3_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String var2Path = SRC_FOLDER + CHANGE_METHOD3_FOLDER + 
				VAR2_BRANCH_FOLDER + "A.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "ChangeMethod3.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for change method 3?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("A", assignments.get(1).getSecond(), "Class is not A?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m()", assignments.get(2).getSecond(), 
				"Updated method with dependency is not m()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m1()", assignments.get(3).getSecond(), 
				"Updated method that's the dependency is not m1()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not B?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "A.m()I", 
				"Method to cover is not A.m()I?");
		assertEquals(targetMethods.get(1), "A.m1()I", 
				"Method to cover is not A.m1()I?");
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "DependencyBasedNewMethod.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for dependency based?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("A", assignments.get(1).getSecond(), 
				"Class that holds the new method is not A?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1()", assignments.get(2).getSecond(),
				"Updated method is not m1()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("k()", assignments.get(3).getSecond(), 
				"Method inserted with dependency is not k()?");
				
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

		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "DependencyBasedNewMethod.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for dependency based?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), 
				"Class that holds the new method is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1()", assignments.get(2).getSecond(), 
				"Updated method is not m1()?");
		
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "DependencyBasedNewClass.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for dependency based 2?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "New class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1()", assignments.get(2).getSecond(),
				"Method updated is not m1()?");
		
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "DependencyBasedNewClass.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(2, result.size(), "Not 2 results for dependency based 2_01?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "New class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1()", assignments.get(2).getSecond(), 
				"Method updated is not m1()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("k()", assignments.get(3).getSecond(), 
				"Method inserted with invocation is not k()?");

		assignments = result.get(1);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("C", assignments.get(1).getSecond(), "New class is not C?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1()", assignments.get(2).getSecond(), 
				"Method updated is not m1()?");
		
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

		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "DependencyBasedNewClass.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not than one result for dependency based 3?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), "New class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1(int, int)", assignments.get(2).getSecond(), 
				"Method in A is not m1(int, int)?");
		
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "DependencyBasedNewClass.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);				

		assertEquals(1, result.size(), "Not one result for dependency based 4?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B", assignments.get(1).getSecond(), 
				"New class is not B?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m1(double, int)", assignments.get(2).getSecond(), 
				"Method updated is not m1(doublt, int)?");
		
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "DependencyBasedNewDependency.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for dependency based 7?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), 
				"Class that holds m() is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("A", assignments.get(1).getSecond(), 
				"Class that holds n() is not A?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 3?"); 
		assertEquals("n1()", assignments.get(2).getSecond(), 
				"Updated method is not n1()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 5?"); 
		assertEquals("n4()", assignments.get(3).getSecond(), 
				"Method that receives a call to n3() is not n4()?");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "A.n4()I", 
				"Method to cover is not A.n4()I?");
		assertEquals(targetMethods.get(1), "A.n1()I", 
				"Method to cover is not A.n1()I?");
	}

	@Test
	public void unexpectedOverridingNewMethodTest() throws ApplicationException {
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "UnexpectedOverridingNewMethod.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for unexpected overriding 1?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B0", assignments.get(1).getSecond(), "Other class is not B0?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("n(A)", assignments.get(2).getSecond(), 
				"Inserted method is not n(A)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3? "); 
		assertEquals("equals(java.lang.Object)", assignments.get(3).getSecond(), 
				"Method overriden is not equals(java.lang.Object)");
		
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
	public void unexpectedOverridingNewClassTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UNEXPECTED_OVERIDE_01_FOLDER + CONFIG_FILE_NAME);
		
		String base1Path = SRC_FOLDER + UNEXPECTED_OVERIDE_01_FOLDER + 
				BASE_BRANCH_FOLDER + "B0.java";
		
		String newClassPath = SRC_FOLDER + UNEXPECTED_OVERIDE_01_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";

		String var1Path = SRC_FOLDER + UNEXPECTED_OVERIDE_01_FOLDER + 
				VAR2_BRANCH_FOLDER + "B0.java";

		String[] bases = {null, base1Path};
		String[] variants1 = {newClassPath, null};
		String[] variants2 = {null, var1Path};
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "UnexpectedOverridingNewClass.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for unexpected overriding 1_01?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "New class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B0", assignments.get(1).getSecond(), "Existing class is not B0?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("n(B)", assignments.get(2).getSecond(), 
				"Method that depends on overriden is not n(B)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3? "); 
		assertEquals("equals(java.lang.Object)", assignments.get(3).getSecond(), 
				"Method overriden is not equals(java.lang.Object)");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "A.n(LB;)Z", 
				"Method to cover is not A.n(LB;)Z?");
		assertEquals(targetMethods.get(1), "B0.equals(Ljava/lang/Object;)Z", 
				"Method to cover is not B0.equals(Ljava/lang/Object;)Z?");
	}
	
	@Test
	public void unexpectedOverridingNewCallTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UNEXPECTED_OVERIDE_02_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + UNEXPECTED_OVERIDE_02_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + UNEXPECTED_OVERIDE_02_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String base2Path = SRC_FOLDER + UNEXPECTED_OVERIDE_02_FOLDER + 
				BASE_BRANCH_FOLDER + "B0.java";
		String var2Path = SRC_FOLDER + UNEXPECTED_OVERIDE_02_FOLDER + 
				VAR2_BRANCH_FOLDER + "B0.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "UnexpectedOverridingNewDependency.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for unexpected overriding 1?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("B0", assignments.get(1).getSecond(), "Other class is not B0?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("n(B)", assignments.get(2).getSecond(), 
				"Inserted method is not n(B)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3? "); 
		assertEquals("equals(java.lang.Object)", assignments.get(3).getSecond(), 
				"Method overriden is not equals(java.lang.Object)");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "A.n(LB;)Z", 
				"Method to cover is not A.n(LB;)Z?");
		assertEquals(targetMethods.get(1), "B0.equals(Ljava/lang/Object;)Z", 
				"Method to cover is not B0.equals(Ljava/lang/Object;)Z?");
	}

	@Test
	public void unexpectedOverriding3NewMethodTest() throws ApplicationException {
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "UnexpectedOverriding3NewMethod.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

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
	public void unexpectedOverriding3NewClassTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UNEXPECTED_OVERIDE302_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + UNEXPECTED_OVERIDE302_FOLDER + 
				BASE_BRANCH_FOLDER + "B.java";
		String var1Path = SRC_FOLDER + UNEXPECTED_OVERIDE302_FOLDER + 
				VAR1_BRANCH_FOLDER + "B.java";
		String newClassPath = SRC_FOLDER + UNEXPECTED_OVERIDE302_FOLDER + 
				VAR2_BRANCH_FOLDER + "C.java";

		String[] bases = {base1Path, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

//		ConflictPattern cp = ConflictPatternCatalog.getConflict(
//				ConflictPatternCatalog.UNEXPECTED_OVERRIDING_3_NEW_CLASS);
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "UnexpectedOverriding3NewClass.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

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
		assertEquals("C", assignments.get(2).getSecond(), "New class is not C?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m(java.lang.Number)", assignments.get(3).getSecond(), 
				"Method in A is not m(java.lang.Number)");
		
		assertEquals(4, assignments.get(4).getFirst(), "Variable id is not 4?"); 
		assertEquals("m(int)", assignments.get(4).getSecond(), 
				"Compatible method inserted in B is not m(int)");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("k(A)", assignments.get(5).getSecond(), 
				"Method added that depends on m(java.lang.Number) is not k(A)");
		
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "C", "The target class to test is not C?");
		assertEquals(targetMethods.size(), 2, "There are not two methods to cover?");
		assertEquals(targetMethods.get(0), "C.k(LA;)V", 
				"Method to cover is not C.k(LA;)V?");
		assertEquals(targetMethods.get(1), "B.m(I)V", 
				"Method to cover is not B.m(I)V?");
	}

	@Test
	public void unexpectedOverriding3NewCallTest() throws ApplicationException {
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
		
		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "UnexpectedOverriding3NewDependency.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

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
		assertEquals("m(int)", assignments.get(4).getSecond(), 
				"Method added compatible with m(java.lang.Number) is not m(int)");
		
		assertEquals(5, assignments.get(5).getFirst(), "Variable id is not 5?"); 
		assertEquals("m1()", assignments.get(5).getSecond(), 
				"Method in B is not m1()");
		
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

		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "ParallelChanges.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

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
	
	@Test
	public void parallelChangesConstructorTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ PARALLEL_CHANGED_CONSTRUCTOR_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + PARALLEL_CHANGED_CONSTRUCTOR_FOLDER + 
				BASE_BRANCH_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + PARALLEL_CHANGED_CONSTRUCTOR_FOLDER + 
				VAR1_BRANCH_FOLDER + "A.java";
		String var2Path = SRC_FOLDER + PARALLEL_CHANGED_CONSTRUCTOR_FOLDER + 
				VAR2_BRANCH_FOLDER + "A.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		String conflictPath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "ParallelChangesConstructor.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for parallel changes?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("A", assignments.get(0).getSecond(), "Class is not A");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("A.<init>()", assignments.get(1).getSecond(),
				"Updated constructor is not A.<init>()?");
				
		List<Pair<String, List<String>>> goals = matcher.getTestingGoals();
		
		assertEquals(1, goals.size(), "There is not one goal to test?");
		
		Pair<String, List<String>> goal = goals.get(0);
		String targetClass = goal.getFirst();
		List<String> targetMethods = goal.getSecond();
		assertEquals(targetClass, "A", "The target class to test is not A?");
		assertEquals(targetMethods.size(), 1, "There is not one constructor to cover?");
		assertEquals(targetMethods.get(0), "A.<init>()V", 
				"Constructor to cover is not A.<init>()V?");
	}
}
