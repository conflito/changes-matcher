package test.matcher;

import org.junit.jupiter.api.Test;

import matcher.Matcher;
import matcher.entities.FieldAccessType;
import matcher.entities.Visibility;
import matcher.entities.deltas.Action;
import matcher.exceptions.ApplicationException;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FieldAccessPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.InterfaceImplementationPattern;
import matcher.patterns.InterfacePattern;
import matcher.patterns.MethodInvocationPattern;
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeleteMethodPatternAction;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertClassPatternAction;
import matcher.patterns.deltas.InsertFieldAccessPatternAction;
import matcher.patterns.deltas.InsertFieldPatternAction;
import matcher.patterns.deltas.InsertInvocationPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.patterns.deltas.UpdateFieldTypePatternAction;
import matcher.patterns.deltas.UpdateInvocationPatternAction;
import matcher.patterns.deltas.UpdatePatternAction;
import matcher.patterns.deltas.VisibilityActionPattern;
import matcher.utils.Pair;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class TestMatcherSemanticConflicts {

	private static final String SRC_FOLDER = "src"+ File.separator + "test"+ File.separator +
			"resources" + File.separator + "SemanticConflictsInstances" + File.separator;
	private static final String CONFIG_FILE_NAME = "config.properties";

	private static final String OVERLOAD_ADDITION_FOLDER = 
			"AddOverloadingMByAdditionAddCall2M" + File.separator;
	private static final String FIELD_HIDING_FOLDER = 
			"AddFieldHidingAddMethodThatUseDefFinChild" + File.separator;
	private static final String METHOD_OVERIDING_FOLDER = 
			"AddOveridingMAddCall2MInParent" + File.separator;
	private static final String OVERLOAD_ACCESS_CHANGE_FOLDER = 
			"AddOverloadingMByChangeAccessibility1AddCall2M" + File.separator;
	private static final String OVERLOAD_ACCESS_CHANGE2_FOLDER = 
			"AddOverloadingMByChangeAccessibility2AddCall2M" + File.separator;
	private static final String OVERRIDING_FOLDER = 
			"AddOverridingMAddCall2MinChild" + File.separator;
	private static final String REMOVE_OVERRIDER_FOLDER = 
			"RemoveOverridingMAddCall2M" + File.separator;
	private static final String CHANGE_METHOD1_FOLDER ="ChangeMethod01" + File.separator;
	private static final String CHANGE_METHOD2_FOLDER ="ChangeMethod02" + File.separator;
	private static final String DEPENDENCY_BASED1_FOLDER = "DependencyBased01" + File.separator;
	private static final String DEPENDENCY_BASED2_FOLDER = "DependencyBased02" + File.separator;
	private static final String DEPENDENCY_BASED201_FOLDER = "DependencyBased02_01" + File.separator;
	private static final String DEPENDENCY_BASED3_FOLDER = "DependencyBased03" + File.separator;
	private static final String DEPENDENCY_BASED4_FOLDER = "DependencyBased04" + File.separator;
	private static final String DEPENDENCY_BASED5_FOLDER = "DependencyBased05" + File.separator;
	private static final String UNEXPECTED_OVERIDE_FOLDER = 
			"UnexpectedOverriding01" + File.separator;
	private static final String UNEXPECTED_OVERIDE2_FOLDER = 
			"UnexpectedOverriding02" + File.separator;


	@Test
	public void overloadByAdditionTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ADDITION_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "A.java";
		String firstVarPath = SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "A01.java";
		String secondVarPath = SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "A02.java";

		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};

		ConflictPattern cp = getOverloadByAdditionPattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
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
	}

	@Test
	public void addFieldHidingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ FIELD_HIDING_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + FIELD_HIDING_FOLDER + "B.java";
		String firstVarPath = SRC_FOLDER + FIELD_HIDING_FOLDER + "B01.java";
		String secondVarPath = SRC_FOLDER + FIELD_HIDING_FOLDER + "B02.java";

		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};

		ConflictPattern cp = getFieldHidingPattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
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
	}

	@Test
	public void methodOveridingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ METHOD_OVERIDING_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + METHOD_OVERIDING_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + METHOD_OVERIDING_FOLDER + "A01.java";
		String base2Path = SRC_FOLDER + METHOD_OVERIDING_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + METHOD_OVERIDING_FOLDER + "B01.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = getMethodOveridingPattern();

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
				assignments.get(2).getSecond().equals("A.A()"), 
				"Constructor is not A.A()?");
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

		String base1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + "A01.java";
		String base2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + "B01.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = getOverloadAccessChangePattern();

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
	public void overloadingAccessChange2Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ACCESS_CHANGE2_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2_FOLDER + "A01.java";
		String base2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2_FOLDER + "B.java";
		String var2Path = SRC_FOLDER + OVERLOAD_ACCESS_CHANGE2_FOLDER + "B01.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = getOverloadAccessChange2Pattern();

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
	public void overridingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERRIDING_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + OVERRIDING_FOLDER + "B.java";
		String var1Path = SRC_FOLDER + OVERRIDING_FOLDER + "B01.java";
		String var2Path = SRC_FOLDER + OVERRIDING_FOLDER + "B02.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = getOverridingPattern();

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

		String basePath = SRC_FOLDER + REMOVE_OVERRIDER_FOLDER + "B.java";
		String var1Path = SRC_FOLDER + REMOVE_OVERRIDER_FOLDER + "B01.java";
		String var2Path = SRC_FOLDER + REMOVE_OVERRIDER_FOLDER + "B02.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = getRemoveOverridingPattern();

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

		String basePath = SRC_FOLDER + CHANGE_METHOD1_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + CHANGE_METHOD1_FOLDER + "A01.java";
		String var2Path = SRC_FOLDER + CHANGE_METHOD1_FOLDER + "A02.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = getChangeMethodPattern();

		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertTrue(result.size() == 2, "Not two results for method change?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
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

		assignments = result.get(1);

		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
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
	}

//	@Test
	public void changeMethod2Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ CHANGE_METHOD2_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + CHANGE_METHOD2_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + CHANGE_METHOD2_FOLDER + "A01.java";
		String var2Path = SRC_FOLDER + CHANGE_METHOD2_FOLDER + "A02.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = getChangeMethod2Pattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		result.forEach(l -> System.out.println(l));
	}

	@Test
	public void dependencyBased1Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED1_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + DEPENDENCY_BASED1_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED1_FOLDER + "A01.java";
		String var2Path = SRC_FOLDER + DEPENDENCY_BASED1_FOLDER + "A02.java";

		String[] bases = {basePath};
		String[] variants1 = {var1Path};
		String[] variants2 = {var2Path};

		ConflictPattern cp = getDependencyBased1Pattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertTrue(result.size() == 1, "More than one result for dependency based?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Method with invocation is not m()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m1()"), 
				"Method updated is not m1()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("k()"), 
				"Method inserted with invocation is not k()?");
	}

//	@Test
	public void dependencyBased2Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED2_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + DEPENDENCY_BASED2_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED2_FOLDER + "A01.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED2_FOLDER + "B.java";

		String[] bases = {basePath, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

		ConflictPattern cp = getDependencyBased2Pattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertTrue(result.size() == 1, "More than one result for dependency based 2?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Method with invocation is not m()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m1()"), 
				"Method updated is not m1()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("B"), 
				"New class is not B?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("k()"), 
				"Method inserted with invocation is not k()?");
	}

	@Test
	public void dependencyBased2_01Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED201_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + DEPENDENCY_BASED201_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED201_FOLDER + "A01.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED201_FOLDER + "B.java";
		String newClass2Path = SRC_FOLDER + DEPENDENCY_BASED201_FOLDER + "C.java";

		String[] bases = {basePath, null, null};
		String[] variants1 = {var1Path, null, null};
		String[] variants2 = {null, newClassPath, newClass2Path};

		ConflictPattern cp = getDependencyBased2Pattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertTrue(result.size() == 2, "Not 2 results for dependency based 2_01?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Method with invocation is not m()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m1()"), 
				"Method updated is not m1()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("B"), 
				"New class is not B?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("k()"), 
				"Method inserted with invocation is not k()?");
		
		assignments = result.get(1);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Method with invocation is not m()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m1()"), 
				"Method updated is not m1()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("C"), 
				"New class is not C?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("method()"), 
				"Method inserted with invocation is not method()?");
	}

	@Test
	public void dependencyBased3Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED3_FOLDER + CONFIG_FILE_NAME);

		String basePath = SRC_FOLDER + DEPENDENCY_BASED3_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED3_FOLDER + "A01.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED3_FOLDER + "B.java";

		String[] bases = {basePath, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

		ConflictPattern cp = getDependencyBased3Pattern();

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

		String basePath = SRC_FOLDER + DEPENDENCY_BASED4_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED4_FOLDER + "A01.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED4_FOLDER + "B.java";

		String[] bases = {basePath, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

		ConflictPattern cp = getDependencyBased2Pattern();

		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);				

		assertTrue(result.size() == 1, "More than one result for dependency based 2?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("A"), "Class is not A");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Method with invocation is not m()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m1(double, int)"), 
				"Method updated is not m1(doublt, int)?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("B"), 
				"New class is not B?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("k()"), 
				"Method inserted with invocation is not k()?");
	}

	@Test
	public void dependencyBased5Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED5_FOLDER + CONFIG_FILE_NAME);


		String basePath = SRC_FOLDER + DEPENDENCY_BASED5_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED5_FOLDER + "A01.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED5_FOLDER + "B.java";

		String[] bases = {basePath, null};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, newClassPath};

		ConflictPattern cp = getDependencyBased5Pattern();

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
	public void unexpectedOverridingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UNEXPECTED_OVERIDE_FOLDER + CONFIG_FILE_NAME);

		String base1Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + "A01.java";
		String base2Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + "B0.java";
		String var2Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + "B0_1.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = getUnexpectedOverriding1Pattern();

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

		String base1Path = SRC_FOLDER + UNEXPECTED_OVERIDE2_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + UNEXPECTED_OVERIDE2_FOLDER + "A01.java";
		String base2Path = SRC_FOLDER + UNEXPECTED_OVERIDE2_FOLDER + "B2.java";
		String var2Path = SRC_FOLDER + UNEXPECTED_OVERIDE2_FOLDER + "B2_01.java";

		String[] bases = {base1Path, base2Path};
		String[] variants1 = {var1Path, null};
		String[] variants2 = {null, var2Path};

		ConflictPattern cp = getUnexpectedOverriding2Pattern();

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

	private ConflictPattern getOverloadByAdditionPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar = new FreeVariable(1);
		FreeVariable insertedMethodVar1 = new FreeVariable(2);
		FreeVariable insertedMethodVar2 = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertMethodPatternAction(insertedMethodVar1, classVar, 
				Visibility.PUBLIC));
		dp1.addActionPattern(new InsertInvocationPatternAction(methodVar, insertedMethodVar1));
		InsertMethodPatternAction impa = new InsertMethodPatternAction(insertedMethodVar2, classVar, 
				Visibility.PUBLIC);
		impa.addCompatible(methodVar);
		dp2.addActionPattern(impa);

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getFieldHidingPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable fieldVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, Visibility.PACKAGE);
		superClassPattern.addFieldPattern(fieldPattern);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		dp1.addActionPattern(new InsertMethodPatternAction(insertedMethodVar, classVar, null));
		dp1.addActionPattern(new InsertFieldAccessPatternAction(fieldVar, insertedMethodVar, FieldAccessType.WRITE));
		dp2.addActionPattern(new InsertFieldPatternAction(fieldVar, classVar, null));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getMethodOveridingPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable cVar = new FreeVariable(2);
		FreeVariable methodVar = new FreeVariable(3);
		FreeVariable fieldVar =  new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PACKAGE);
		ConstructorPattern cPattern = new ConstructorPattern(cVar, Visibility.PACKAGE);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, Visibility.PACKAGE);
		superClassPattern.addConstructorPattern(cPattern);
		superClassPattern.addMethodPattern(methodPattern);
		classPattern.addFieldPattern(fieldPattern);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertInvocationPatternAction(methodVar, cVar));
		dp2.addActionPattern(new InsertMethodPatternAction(methodVar, classVar, Visibility.PACKAGE));
		dp2.addActionPattern(new InsertFieldAccessPatternAction(fieldVar, methodVar, 
				FieldAccessType.WRITE));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getOverloadAccessChangePattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable topMethodVar = new FreeVariable(2);
		FreeVariable subMethodVar = new FreeVariable(3);
		FreeVariable insertedMethodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern topMethodPattern = new MethodPattern(topMethodVar, Visibility.PUBLIC);
		MethodPattern subMethodPattern = new MethodPattern(subMethodVar, Visibility.PRIVATE);
		superClassPattern.addMethodPattern(topMethodPattern);
		superClassPattern.addMethodPattern(subMethodPattern);
		superClassPattern.addCompatible(subMethodVar, topMethodVar);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertMethodPatternAction(insertedMethodVar, classVar, 
				Visibility.PUBLIC));
		dp1.addActionPattern(new InsertInvocationPatternAction(topMethodVar, insertedMethodVar));
		dp2.addActionPattern(new VisibilityActionPattern(Action.UPDATE, Visibility.PUBLIC, 
				Visibility.PRIVATE, subMethodVar));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getOverloadAccessChange2Pattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable topMethodVar = new FreeVariable(2);
		FreeVariable subMethodVar = new FreeVariable(3);
		FreeVariable insertedMethodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern topMethodPattern = new MethodPattern(topMethodVar, Visibility.PUBLIC);
		MethodPattern subMethodPattern = new MethodPattern(subMethodVar, Visibility.PUBLIC);
		superClassPattern.addMethodPattern(topMethodPattern);
		superClassPattern.addMethodPattern(subMethodPattern);
		superClassPattern.addCompatible(subMethodVar, topMethodVar);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertMethodPatternAction(insertedMethodVar, classVar, 
				Visibility.PUBLIC));
		dp1.addActionPattern(new InsertInvocationPatternAction(subMethodVar, insertedMethodVar));
		dp2.addActionPattern(new VisibilityActionPattern(Action.UPDATE, Visibility.PRIVATE, 
				Visibility.PUBLIC, subMethodVar));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getOverridingPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		superClassPattern.addMethodPattern(methodPattern);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertMethodPatternAction(insertedMethodVar, classVar, 
				Visibility.PUBLIC));
		dp1.addActionPattern(new InsertInvocationPatternAction(methodVar, insertedMethodVar));
		dp2.addActionPattern(new InsertMethodPatternAction(methodVar, classVar, 
				Visibility.PUBLIC));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getRemoveOverridingPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		superClassPattern.addMethodPattern(methodPattern);
		classPattern.addMethodPattern(methodPattern);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertMethodPatternAction(insertedMethodVar, classVar, 
				Visibility.PUBLIC));
		dp1.addActionPattern(new InsertInvocationPatternAction(methodVar, insertedMethodVar));
		dp2.addActionPattern(new DeleteMethodPatternAction(methodVar, classVar, null));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}


	private ConflictPattern getChangeMethodPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar1 = new FreeVariable(1);
		FreeVariable methodVar2 = new FreeVariable(2);
		FreeVariable methodVar3 = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern1 = new MethodPattern(methodVar1, null);
		MethodPattern methodPattern2 = new MethodPattern(methodVar2, null);
		MethodPattern methodPattern3 = new MethodPattern(methodVar3, null);
		methodPattern1.addMethodInvocationPattern(new MethodInvocationPattern(methodVar2));
		methodPattern1.addMethodInvocationPattern(new MethodInvocationPattern(methodVar3));
		classPattern.addMethodPattern(methodPattern1);
		classPattern.addMethodPattern(methodPattern2);
		classPattern.addMethodPattern(methodPattern3);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new UpdatePatternAction(methodVar2));
		dp2.addActionPattern(new UpdatePatternAction(methodVar3));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		conflict.addDifferentVariablesRule(methodVar2, methodVar3);

		return conflict;
	}


	private ConflictPattern getChangeMethod2Pattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable iVar = new FreeVariable(1);
		FreeVariable b1ClassVar = new FreeVariable(2);
		FreeVariable b2ClassVar = new FreeVariable(3);
		FreeVariable methodNVar = new FreeVariable(4);
		FreeVariable methodM1Var = new FreeVariable(5);
		FreeVariable methodM2Var = new FreeVariable(6);
		FreeVariable methodHashVar = new FreeVariable(7);
		FreeVariable fieldVar = new FreeVariable(8);

		BasePattern basePattern = new BasePattern();
		
		ClassPattern classAPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		fieldPattern.setType(b1ClassVar);
		classAPattern.addFieldPattern(fieldPattern);
		MethodPattern methodNPattern = new MethodPattern(methodNVar, null);
		methodNPattern.addMethodInvocationPattern(new MethodInvocationPattern(methodM2Var));
		classAPattern.addMethodPattern(methodNPattern);
		MethodPattern methodM1Pattern = new MethodPattern(methodM1Var, null);
		methodM1Pattern.addMethodInvocationPattern(new MethodInvocationPattern(methodHashVar));
		MethodPattern methodM2Pattern = new MethodPattern(methodM2Var, null);
		classAPattern.addMethodPattern(methodM1Pattern);
		classAPattern.addMethodPattern(methodM2Pattern);

		InterfacePattern interfacePattern = new InterfacePattern(iVar);
		
		ClassPattern classB1Pattern = new ClassPattern(b1ClassVar);
		//		ClassPattern classB2Pattern = new ClassPattern(b2ClassVar);
		InterfaceImplementationPattern iPattern = new InterfaceImplementationPattern(iVar);
		classB1Pattern.addInterface(iPattern);
		//		classB2Pattern.addInterface(iPattern);
		MethodPattern methodHashPattern = new MethodPattern(methodHashVar, null);
		classB1Pattern.addMethodPattern(methodHashPattern);
		//		classB2Pattern.addExcludedMethod(methodHashVar);

		basePattern.addClassPattern(classAPattern);
		basePattern.addClassPattern(classB1Pattern);
		basePattern.addInterfacePattern(interfacePattern);
		//		basePattern.addClassPattern(classB2Pattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new UpdateFieldTypePatternAction(fieldVar, b1ClassVar, b2ClassVar));
		dp2.addActionPattern(new UpdateInvocationPatternAction(methodM2Var, methodM1Var, methodNVar));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		return conflict;
	}

	private ConflictPattern getDependencyBased1Pattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar1 = new FreeVariable(1);
		FreeVariable methodVar2 = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern1 = new MethodPattern(methodVar1, null);
		MethodPattern methodPattern2 = new MethodPattern(methodVar2, null);
		methodPattern1.addMethodInvocationPattern(new MethodInvocationPattern(methodVar2));
		classPattern.addMethodPattern(methodPattern1);
		classPattern.addMethodPattern(methodPattern2);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new UpdatePatternAction(methodVar2));
		dp2.addActionPattern(new InsertMethodPatternAction(insertedMethodVar, classVar, null));
		dp2.addActionPattern(new InsertInvocationPatternAction(methodVar1, insertedMethodVar));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getDependencyBased2Pattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar1 = new FreeVariable(1);
		FreeVariable methodVar2 = new FreeVariable(2);
		FreeVariable insertedClassVar = new FreeVariable(3);
		FreeVariable insertedMethodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern1 = new MethodPattern(methodVar1, null);
		MethodPattern methodPattern2 = new MethodPattern(methodVar2, null);
		methodPattern1.addMethodInvocationPattern(new MethodInvocationPattern(methodVar2));
		classPattern.addMethodPattern(methodPattern1);
		classPattern.addMethodPattern(methodPattern2);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new UpdatePatternAction(methodVar2));
		dp2.addActionPattern(new InsertClassPatternAction(insertedClassVar));
		dp2.addActionPattern(new InsertMethodPatternAction(insertedMethodVar, 
				insertedClassVar, null));
		dp2.addActionPattern(new InsertInvocationPatternAction(methodVar1, insertedMethodVar));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getDependencyBased3Pattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar1 = new FreeVariable(1);
		FreeVariable insertedClassVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern1 = new MethodPattern(methodVar1, null);
		classPattern.addMethodPattern(methodPattern1);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new UpdatePatternAction(methodVar1));
		dp2.addActionPattern(new InsertClassPatternAction(insertedClassVar));
		dp2.addActionPattern(new InsertMethodPatternAction(insertedMethodVar, 
				insertedClassVar, null));
		dp2.addActionPattern(new InsertInvocationPatternAction(methodVar1, insertedMethodVar));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getDependencyBased5Pattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		FreeVariable insertedClassVar = new FreeVariable(3);
		FreeVariable insertedMethodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		classPattern.addFieldPattern(fieldPattern);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		methodPattern.addFieldAccessPattern(new FieldAccessPattern(fieldVar, 
				FieldAccessType.READ));
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new UpdateFieldTypePatternAction(fieldVar));
		dp2.addActionPattern(new InsertClassPatternAction(insertedClassVar));
		dp2.addActionPattern(new InsertMethodPatternAction(insertedMethodVar, 
				insertedClassVar, null));
		dp2.addActionPattern(new InsertInvocationPatternAction(methodVar, insertedMethodVar));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getUnexpectedOverriding1Pattern() {
		FreeVariable classVar1 = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable fieldVar1 = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);
		FreeVariable overideMethodVar = new FreeVariable(4);
		FreeVariable iVar = new FreeVariable(5);

		BasePattern basePattern = new BasePattern();
		InterfacePattern iPattern = new InterfacePattern(iVar);
		ClassPattern classPattern1 = new ClassPattern(classVar1);
		ClassPattern classPattern2 = new ClassPattern(classVar2);
		FieldPattern fieldPattern = new FieldPattern(fieldVar1, null);
		fieldPattern.setType(iVar);
		classPattern1.addFieldPattern(fieldPattern);
		
		classPattern2.addInterface(new InterfaceImplementationPattern(iVar));
		classPattern2.addExcludedMethod(overideMethodVar);
		
		basePattern.addInterfacePattern(iPattern);
		basePattern.addClassPattern(classPattern1);
		basePattern.addClassPattern(classPattern2);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertMethodPatternAction(insertedMethodVar, classVar1, null));
		dp1.addActionPattern(new InsertInvocationPatternAction(overideMethodVar, 
				insertedMethodVar));
		dp2.addActionPattern(new InsertMethodPatternAction(overideMethodVar, classVar2, 
				Visibility.PUBLIC));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);

		return conflict;
	}

	private ConflictPattern getUnexpectedOverriding2Pattern() {
		FreeVariable classVar1 = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable classVar3 = new FreeVariable(2);
		FreeVariable fieldVar1 = new FreeVariable(3);
		FreeVariable methodVar1 = new FreeVariable(4);
		FreeVariable insertedMethod = new FreeVariable(5);
		FreeVariable iVar = new FreeVariable(6);

		BasePattern basePattern = new BasePattern();
		InterfacePattern iPattern = new InterfacePattern(iVar);
		ClassPattern classPattern1 = new ClassPattern(classVar1);
		ClassPattern classPattern2 = new ClassPattern(classVar2);
		ClassPattern classPattern3 = new ClassPattern(classVar3);
		MethodPattern methodPattern = new MethodPattern(methodVar1, null);
		FieldPattern fieldPattern = new FieldPattern(fieldVar1, null);
		fieldPattern.setType(iVar);
		classPattern1.addFieldPattern(fieldPattern);
		classPattern2.addInterface(new InterfaceImplementationPattern(iVar));
		classPattern2.addMethodPattern(methodPattern);
		classPattern3.addExcludedMethod(methodVar1);
		classPattern3.setSuperClass(classPattern2);
		
		basePattern.addInterfacePattern(iPattern);
		basePattern.addClassPattern(classPattern1);
		basePattern.addClassPattern(classPattern3);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertMethodPatternAction(insertedMethod, classVar1, null));
		dp1.addActionPattern(new InsertInvocationPatternAction(methodVar1, insertedMethod));
		dp2.addActionPattern(new InsertMethodPatternAction(methodVar1, classVar3, null));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		conflict.addDifferentVariablesRule(classVar1, classVar3);

		return conflict;
	}
}
