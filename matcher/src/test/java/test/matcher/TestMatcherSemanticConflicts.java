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
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodInvocationPattern;
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeleteMethodPatternAction;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertClassPatternAction;
import matcher.patterns.deltas.InsertFieldAccessPatternAction;
import matcher.patterns.deltas.InsertFieldPatternAction;
import matcher.patterns.deltas.InsertInvocationPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.patterns.deltas.UpdatePatternAction;
import matcher.patterns.deltas.VisibilityActionPattern;
import matcher.utils.Pair;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TestMatcherSemanticConflicts {

	private static final String SRC_FOLDER = "src/test/resources/SemanticConflictsInstances/";
	private static final String CONFIG_FILE_NAME = "config.properties";
	
	private static final String OVERLOAD_ADDITION_FOLDER = "AddOverloadingMByAdditionAddCall2M/";
	private static final String FIELD_HIDING_FOLDER = "AddFieldHidingAddMethodThatUseDefFinChild/";
	private static final String METHOD_OVERIDING_FOLDER = "AddOveridingMAddCall2MInParent/";
	private static final String OVERLOAD_ACCESS_CHANGE_FOLDER = 
			"AddOverloadingMByChangeAccessibility1AddCall2M/";
	private static final String OVERLOAD_ACCESS_CHANGE2_FOLDER = 
			"AddOverloadingMByChangeAccessibility2AddCall2M/";
	private static final String OVERRIDING_FOLDER = "AddOverridingMAddCall2MinChild/";
	private static final String REMOVE_OVERRIDER_FOLDER ="RemoveOverridingMAddCall2M/";
	private static final String CHANGE_METHOD1_FOLDER ="ChangeMethod01/";
	private static final String DEPENDENCY_BASED1_FOLDER = "DependencyBased01/";
	private static final String DEPENDENCY_BASED2_FOLDER = "DependencyBased02/";
	private static final String DEPENDENCY_BASED3_FOLDER = "DependencyBased03/";
	private static final String DEPENDENCY_BASED4_FOLDER = "DependencyBased04/";
	private static final String UNEXPECTED_OVERIDE_FOLDER = "UnexpectedOverriding01/";

	@Test
	public void overloadByAdditionTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ OVERLOAD_ADDITION_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "A.java";
		String firstVarPath = SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "A01.java";
		String secondVarPath = SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "A02.java";
		
		ConflictPattern cp = getOverloadByAdditionPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(basePath, firstVarPath, secondVarPath, cp);
		
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
		
		ConflictPattern cp = getFieldHidingPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(basePath, firstVarPath, secondVarPath, cp);
		
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
		
		ConflictPattern cp = getMethodOveridingPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(base1Path, var1Path, base2Path, var2Path, cp);
		
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
		
		ConflictPattern cp = getOverloadAccessChangePattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(base1Path, var1Path, base2Path, var2Path, cp);
		
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
		
		ConflictPattern cp = getOverloadAccessChange2Pattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(base1Path, var1Path, base2Path, var2Path, cp);
		
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

		ConflictPattern cp = getOverridingPattern();
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(basePath, var1Path, var2Path, cp);
		
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
		
		ConflictPattern cp = getRemoveOverridingPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(basePath, var1Path, var2Path, cp);
		
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
		
		ConflictPattern cp = getChangeMethodPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(basePath, var1Path, var2Path, cp);
				
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
	
	@Test
	public void dependencyBased1Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED1_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEPENDENCY_BASED1_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED1_FOLDER + "A01.java";
		String var2Path = SRC_FOLDER + DEPENDENCY_BASED1_FOLDER + "A02.java";
		
		ConflictPattern cp = getDependencyBased1Pattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(basePath, var1Path, var2Path, cp);
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

	@Test
	public void dependencyBased2Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED2_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEPENDENCY_BASED2_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED2_FOLDER + "A01.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED2_FOLDER + "B.java";
		
		ConflictPattern cp = getDependencyBased2Pattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(basePath, var1Path, null, newClassPath, cp);
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
	public void dependencyBased3Test() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEPENDENCY_BASED3_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEPENDENCY_BASED3_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + DEPENDENCY_BASED3_FOLDER + "A01.java";
		String newClassPath = SRC_FOLDER + DEPENDENCY_BASED3_FOLDER + "B.java";
		
		ConflictPattern cp = getDependencyBased3Pattern();
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(basePath, var1Path, null, newClassPath, cp);
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
		
		ConflictPattern cp = getDependencyBased2Pattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(basePath, var1Path, null, newClassPath, cp);
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
	public void unexpectedOverridingTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UNEXPECTED_OVERIDE_FOLDER + CONFIG_FILE_NAME);
		
		String base1Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + "A.java";
		String var1Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + "A01.java";
		String base2Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + "B0.java";
		String var2Path = SRC_FOLDER + UNEXPECTED_OVERIDE_FOLDER + "B0_1.java";

		ConflictPattern cp = getUnexpectedOverriding1Pattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(base1Path, var1Path, base2Path, var2Path, cp);
		assertTrue(result.size() == 1, "More than one result for unexpected overriding 1?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
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
		
		return new ConflictPattern(basePattern, dp1, dp2);
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
		return new ConflictPattern(basePattern, dp1, dp2);
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
		
		return new ConflictPattern(basePattern, dp1, dp2);
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
		return new ConflictPattern(basePattern, dp1, dp2);
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
		return new ConflictPattern(basePattern, dp1, dp2);
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
		
		return new ConflictPattern(basePattern, dp1, dp2);
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
		
		return new ConflictPattern(basePattern, dp1, dp2);
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

		return new ConflictPattern(basePattern, dp1, dp2);
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
		
		return new ConflictPattern(basePattern, dp1, dp2);
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
		
		return new ConflictPattern(basePattern, dp1, dp2);
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
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}
	
	private ConflictPattern getUnexpectedOverriding1Pattern() {
		FreeVariable classVar1 = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable fieldVar1 = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);
		FreeVariable overideMethodVar = new FreeVariable(4);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern1 = new ClassPattern(classVar1);
		ClassPattern classPattern2 = new ClassPattern(classVar2);
		FieldPattern fieldPattern = new FieldPattern(fieldVar1, null);
		classPattern1.addFieldPattern(fieldPattern);
		basePattern.addClassPattern(classPattern1);
		basePattern.addClassPattern(classPattern2);
		
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertMethodPatternAction(insertedMethodVar, classVar1, null));
		dp1.addActionPattern(new InsertInvocationPatternAction(overideMethodVar, 
				insertedMethodVar));
		dp2.addActionPattern(new InsertMethodPatternAction(overideMethodVar, classVar2, 
				Visibility.PUBLIC));
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}
}
