package test.matcher;

import org.junit.jupiter.api.Test;

import gumtree.spoon.diff.Diff;
import matcher.entities.ChangeInstance;
import matcher.entities.FieldAccessType;
import matcher.entities.Visibility;
import matcher.entities.deltas.Action;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.MatchingHandler;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertFieldAccessPatternAction;
import matcher.patterns.deltas.InsertFieldPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.patterns.deltas.InsertPatternAction;
import matcher.patterns.deltas.VisibilityActionPattern;
import matcher.utils.Pair;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class TestMatcherSemanticConflicts {

	private static final String SRC_FOLDER = "src/test/resources/SemanticConflictsInstances/";
	private static final String OVERLOAD_ADDITION_FOLDER = "AddOverloadingMByAdditionAddCall2M/";
	private static final String FIELD_HIDING_FOLDER = "AddFieldHidingAddMethodThatUseDefFinChild/";
	private static final String METHOD_OVERIDING_FOLDER = "AddOveridingMAddCall2MInParent/";
	private static final String OVERLOAD_ACCESS_CHANGE_FOLDER = 
			"AddOverloadingMByChangeAccessibility1AddCall2M/";
	
//	@Test
	public void overloadByAdditionTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "TestClass.java");
		File firstVar = new File(SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "TestClass01.java");
		File secondVar = new File(SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "TestClass02.java");
		ConflictPattern cp = getOverloadByAdditionPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.TestClass"), "Class is not "
						+ "base.TestClass?");
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
	
//	@Test
	public void addFieldHidingTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + FIELD_HIDING_FOLDER + "B.java");
		File firstVar = new File(SRC_FOLDER + FIELD_HIDING_FOLDER + "B01.java");
		File secondVar = new File(SRC_FOLDER + FIELD_HIDING_FOLDER + "B02.java");
		ConflictPattern cp = getFieldHidingPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
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
	
//	@Test
	public void methodOveridingTest() throws ApplicationException {
		File base1 = new File(SRC_FOLDER + METHOD_OVERIDING_FOLDER + "C.java");
		File var1 = new File(SRC_FOLDER + METHOD_OVERIDING_FOLDER + "C01.java");
		File base2 = new File(SRC_FOLDER + METHOD_OVERIDING_FOLDER + "D.java");
		File var2 = new File(SRC_FOLDER + METHOD_OVERIDING_FOLDER + "D01.java");
		ConflictPattern cp = getMethodOveridingPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base1, var1);
		Diff d2 = cih.getDiff(base2, var2);
		ChangeInstance ci = cih.getChangeInstance(base1, base2, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 5, "Not 5 assignments with only 5 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("C"), "Superclass is not C");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("D"), "Class is not D?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("C.C()"), 
				"Constructor is not C.C()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("resize()"), 
				"Inserted method that writes to field is not resize()?");
		assertTrue(assignments.get(4).getFirst() == 4 && 
				assignments.get(4).getSecond().equals("h"), 
				"Field is not h?");
	}
	
	@Test
	public void overloadingAccessChangeTest() throws ApplicationException {
		/**
		 * Test is incomplete because gumtree spoon diff assumes move(0,0)
		 * as move(int,int) even if move(int,int) is private
		 * Awating reply on the issue in the github repo
		 */
		File base1 = new File(SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + "E.java");
		File var1 = new File(SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + "E01.java");
		File base2 = new File(SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + "F.java");
		File var2 = new File(SRC_FOLDER + OVERLOAD_ACCESS_CHANGE_FOLDER + "F01.java");
		ConflictPattern cp = getOverloadAccessChangePattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base1, var1);
		Diff d2 = cih.getDiff(base2, var2);
		ChangeInstance ci = cih.getChangeInstance(base1, base2, d1, d2, cp);
		System.out.println(ci);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		System.out.println(result);
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
		dp1.addActionPattern(new InsertPatternAction(methodVar, insertedMethodVar1));
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
		dp1.addActionPattern(new InsertPatternAction(methodVar, cVar));
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
		dp1.addActionPattern(new InsertPatternAction(topMethodVar, classVar));
		dp2.addActionPattern(new VisibilityActionPattern(Action.UPDATE, Visibility.PUBLIC, 
				Visibility.PRIVATE, subMethodVar));
		return new ConflictPattern(basePattern, dp1, dp2);
	}
}
