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
		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("move(java.lang.Number, "
						+ "java.lang.Number)"), 
				"Method in class is not move(java.lang.Number, java.lang.Number)?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("reset()"), 
				"Inserted method with invocation is not reset()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("move(int, int)"), 
				"Inserted compatible method is not move(int, int)?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("base.A"), "Class is not A");
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
		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("move(java.lang.Number, "
						+ "java.lang.Number)"), 
				"Method in class is not move(java.lang.Number, java.lang.Number)?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("move(int, int)"), 
				"Inserted compatible method is not move(int, int)?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("reset()"), 
				"Inserted method in new class with invocation is not reset()?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("base.B"), "New class is not B");
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
		
		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Superclass is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B"), "Class is not B?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("size"), 
				"Field is not size?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("m()"), 
				"Inserted method that writes to field is not m()?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("size"), 
				"Inserted field not size?");
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

		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Superclass is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B"), "Class is not B?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("A.<init>()"), 
				"Constructor is not A.<init>()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("resize()"), 
				"Inserted method that writes to field is not resize()?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("h"), 
				"Field is not h?");
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

		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Superclass is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B"), "Class is not B?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("move(java.lang.Number, java.lang.Number)"), 
				"Top method is not move(java.lang.Number, java.lang.Number)?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("move(int, int)"), 
				"Sub method is not move(int, int)?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("reset()"), 
				"Inserted method with invocation is not reset()?");
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
		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Superclass is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B"), "New class is not B?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("move(java.lang.Number, java.lang.Number)"), 
				"Top method is not move(java.lang.Number, java.lang.Number)?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("move(int, int)"), 
				"Sub method is not move(int, int)?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("reset()"), 
				"Inserted method in new class with invocation is not reset()?");
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

		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Superclass is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B"), "Class is not B?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("move(java.lang.Number, java.lang.Number)"), 
				"Top method is not move(java.lang.Number, java.lang.Number)?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("move(int, int)"), 
				"Sub method is not move(int, int)?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("reset()"), 
				"Inserted method with invocation is not reset()?");
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
		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Superclass is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B"), "New class is not B?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("move(java.lang.Number, java.lang.Number)"), 
				"Top method is not move(java.lang.Number, java.lang.Number)?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("move(int, int)"), 
				"Sub method is not move(int, int)?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("reset()"), 
				"Inserted method with invocation is not reset()?");
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

		assertTrue(result.size() == 1, "More than one result for overriding method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Superclass is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B"), "Class is not B?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("move(int, int)"), 
				"Method overwritten is not move(int, int)?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("reset()"), 
				"Method with invocation is not reset()?");
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

		assertTrue(result.size() == 1, "More than one result for remove overriding method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Superclass is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B"), "Class is not B?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("move(int, int)"), 
				"Method removed is not move(int, int)?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("reset()"), 
				"Method with invocation is not reset()?");
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

		assertTrue(result.size() == 2, "Not two results for method change?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 6, "Not 6 assignments with only 6 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Method with invocations is not m()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m1()"), 
				"Method updated is not m1()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("m2()"), 
				"Other method updated is not m2()?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("A"), 
				"Class that holds m1() isn't A?");
		assertTrue(assignments.get(5).getFirst() == 5 && 
				assignments.get(5).getSecond().equals("A"), 
				"Class that holds m2() isn't A?");

		assignments = result.get(1);

		assertTrue(assignments.size() == 6, "Not 6 assignments with only 6 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Method with invocations is not m()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m2()"), 
				"Method updated is not m2()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("m1()"), 
				"Other method updated is not m1()?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("A"), 
				"Class that holds m1() isn't A?");
		assertTrue(assignments.get(5).getFirst() == 5 && 
				assignments.get(5).getSecond().equals("A"), 
				"Class that holds m2() isn't A?");
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
		assertTrue(result.size() == 2, "Not two results for method change 1_01?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 6, "Not 6 assignments with only 6 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("B"), 
				"Dependant class is not B");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Method with invocations is not m()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m1()"), 
				"Method updated is not m1()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("m2()"), 
				"Other method updated is not m2()?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("A"), 
				"Class that holds m1() isn't A?");
		assertTrue(assignments.get(5).getFirst() == 5 && 
				assignments.get(5).getSecond().equals("A"), 
				"Class that holds m2() isn't A?");

		assignments = result.get(1);

		assertTrue(assignments.size() == 6, "Not 6 assignments with only 6 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("B"), 
				"Dependant class is not B");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Method with invocations is not m()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m2()"), 
				"Method updated is not m2()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("m1()"), 
				"Other method updated is not m1()?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("A"), 
				"Class that holds m1() isn't A?");
		assertTrue(assignments.get(5).getFirst() == 5 && 
				assignments.get(5).getSecond().equals("A"), 
				"Class that holds m2() isn't A?");
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

		assertTrue(result.size() == 1, "More than one result for change method 2?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 9, "Not 9 assignments with only 9 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B"), 
				"Interface is not B?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("B1"), 
				"Class that implements hashCode is not B1?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("B2"), 
				"Class that does not implement hashCode is not B2?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("n()"), 
				"Method that has its invocation updated is not n()?");
		assertTrue(assignments.get(5).getFirst() == 5 && 
				assignments.get(5).getSecond().equals("m1(B[])"), 
				"The new invocation is not of method m1(B[])?");
		assertTrue(assignments.get(6).getFirst() == 6 && 
				assignments.get(6).getSecond().equals("m2(B[])"), 
				"The old invocation is not of method m2(B[])?");
		assertTrue(assignments.get(7).getFirst() == 7 && 
				assignments.get(7).getSecond().equals("hashCode()"), 
				"The method that B1 has that B2 doesn't isn't hashCode()?");
		assertTrue(assignments.get(8).getFirst() == 8 && 
				assignments.get(8).getSecond().equals("v"), 
				"The updated field is not v");
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

		assertTrue(result.size() == 1, "More than one result for dependency based?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m1()"), 
				"Updated method is not m1()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("A"), 
				"Class that holds the new method is not A?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("k()"), 
				"Method inserted with t is not k()?");
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
		
		assertTrue(result.size() == 1, "More than one result for dependency based?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m1()"), 
				"Updated method is not m1()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("B"), 
				"Class that holds the new method is not B?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("k()"), 
				"Method inserted with t is not k()?");
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
		assertTrue(result.size() == 1, "More than one result for dependency based 2?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m1()"), 
				"Method updated is not m1()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("B"), 
				"New class is not B?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("k()"), 
				"New method that depends on m1() is not k()?");
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

		assertTrue(result.size() == 2, "Not 2 results for dependency based 2_01?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m1()"), 
				"Method updated is not m1()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("B"), 
				"New class is not B?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("k()"), 
				"Method inserted with invocation is not k()?");

		assignments = result.get(1);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m1()"), 
				"Method updated is not m1()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("C"), 
				"New class is not C?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("method()"), 
				"Method inserted with invocation is not method()?");
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

		assertTrue(result.size() == 1, "More than one result for dependency based 3?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m1(int, int)"), 
				"Method in A is not m1(int, int)?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("B"), 
				"New class is not B?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("k()"), 
				"New method with invocation is not k()?");
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

		assertTrue(result.size() == 1, "More than one result for dependency based 4?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m1(double, int)"), 
				"Method updated is not m1(doublt, int)?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("B"), 
				"New class is not B?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("k()"), 
				"Method inserted with invocation is not k()?");
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

		assertTrue(result.size() == 1, "More than one result for dependency based 5?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("x"), 
				"Updated field is not x?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m1()"), 
				"Method that reads x is not m1()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("B"), 
				"New class is not B?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("k()"), 
				"Method inserted with invocation is not k()?");
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
		assertTrue(result.size() == 1, "More than one result for dependency based 5?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("x"), 
				"Updated field is not x?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m1()"), 
				"Method that reads x is not m1()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("B"), 
				"Class is not B?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("k()"), 
				"Method that depends on m1() is not k()?");
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
		assertTrue(result.size() == 1, "More than one result for dependency based 5?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("x"), 
				"Updated field is not x?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m1()"), 
				"Method that reads x is not m1()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("A"), 
				"Class is not B?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("m2()"), 
				"Method that depends on m1() is not m2()?");
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
		assertTrue(result.size() == 1, "More than one result for dependency based 7?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 6, "Not 6 assignments with only 6 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), 
				"Class that holds m() is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("A"), 
				"Class that holds n() is not A?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("A"), 
				"Class that holds m1() is not A?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("m()"), 
				"Updated method is not m()?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("n()"), 
				"Method that depends on m() is not n()?");
		assertTrue(assignments.get(5).getFirst() == 5 && 
				assignments.get(5).getSecond().equals("m1()"), 
				"Method that receives a call to n() is not m1()?");
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
		assertTrue(result.size() == 1, "More than one result for unexpected overriding 1?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 6, "Not 6 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B0"), 
				"Other class is not B0?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("b"), 
				"Field is not b?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("n(A)"), 
				"Inserted method is not n(A)?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("equals(java.lang.Object)"), 
				"Method overriden is not equals(java.lang.Object)");
		assertTrue(assignments.get(5).getFirst() == 5 && 
				assignments.get(5).getSecond().equals("B"), 
				"Interface is not B?");
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
		assertTrue(result.size() == 1, "More than one result for unexpected overriding 2?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 7, "Not 7 assignments with only 7 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B1"), 
				"Other class is not B1?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("B2"), 
				"Third class that extends second is not B2?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("b"), 
				"Field is not b?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("m()"), 
				"Method overriden is not m()");
		assertTrue(assignments.get(5).getFirst() == 5 && 
				assignments.get(5).getSecond().equals("n()"), 
				"Method that invokes overriden method is not n()");
		assertTrue(assignments.get(6).getFirst() == 6 && 
				assignments.get(6).getSecond().equals("B"), 
				"Interface is not B?");
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
		assertTrue(result.size() == 1, "More than one result for unexpected overriding 2?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 6, "Not 6 assignments with only 6 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B"), 
				"Class that extends A is not B?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("C"), 
				"Third class is not C?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("m(java.lang.Number)"), 
				"Method in A is not m(java.lang.Number)");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("m(int)"), 
				"Compatible method inserted in B is not m(int)");
		assertTrue(assignments.get(5).getFirst() == 5 && 
				assignments.get(5).getSecond().equals("k()"), 
				"Method added that depends on m(java.lang.Number) is not k()");
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
		
		assertTrue(result.size() == 1, "More than one result for unexpected overriding 3_1?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 6, "Not 6 assignments with only 6 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("B"), 
				"Class that extends A is not B?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("B"), 
				"Class that has m1() class is not B?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("m(java.lang.Number)"), 
				"Method in A is not m(java.lang.Number)");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("m1()"), 
				"Method in B is not m1()");
		assertTrue(assignments.get(5).getFirst() == 5 && 
				assignments.get(5).getSecond().equals("m(int)"), 
				"Method added compatible with m(java.lang.Number) is not m(int)");
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

		assertTrue(result.size() == 1, "More than one result for parallel changes?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 2, "Not 2 assignments with only 2 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Updated method is not m()?");
	}
}
