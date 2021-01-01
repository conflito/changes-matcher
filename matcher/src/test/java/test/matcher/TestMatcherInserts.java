package test.matcher;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import gumtree.spoon.diff.Diff;
import matcher.entities.ChangeInstance;
import matcher.entities.Visibility;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.MatchingHandler;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertConstructorPatternAction;
import matcher.patterns.deltas.InsertFieldAccessPatternAction;
import matcher.patterns.deltas.InsertFieldPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.patterns.deltas.InsertPatternAction;
import matcher.utils.Pair;

public class TestMatcherInserts {

	private static final String SRC_FOLDER = "src/test/resources/OperationsInstances/";
	private static final String INS_FIELD_METHOD_FOLDER = "FieldAndMethodInsertInstance/";
	private static final String INS_FIELD_CONSTR_FOLDER = "FieldAndConstructorInsertInstance/";
	private static final String INS_CONSTR_COMPAT_METHOD_FOLDER = 
			"ConstructorAndCompatibleMethodInsertInstance/";
	private static final String INS_METHOD_WITH_INV_FOLDER = "MethodWithInvocationInsertInstance/";
	private static final String INS_METHOD_WITH_ACCESS_FOLDER = "MethodWithFieldAccessInsertInstance/";
	
	@Test
	public void insertPrivateFieldAndPublicMethodTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square02.java");
		ConflictPattern cp = getInsertPrivateFieldAndPublicMethodPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for inserting private "
				+ "field and public method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 3, "Not 3 assignments with only 3 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
					assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("base.Square.t"), "Field is not base.Square.t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("base.Square.m()"), "Method is not base.Square.m()?");
	}
	
	@Test
	public void insertPrivateFieldAndPublicMethodNoMatchTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square02.java");
		ConflictPattern cp = getInsertPublicFieldAndPublicMethodPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 0, "Finds match when there's no private field insertion?");
	}
	
	@Test
	public void insertPrivateFieldAndAnyVisibilityConstructorTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + INS_FIELD_CONSTR_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + INS_FIELD_CONSTR_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + INS_FIELD_CONSTR_FOLDER + "Square02.java");
		ConflictPattern cp = getInsertPrivateFieldAndAnyVisibilityConstructorPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for inserting private "
				+ "field and public constructor?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 3, "Not 3 assignments with only 3 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("base.Square.t"), "Field is not base.Square.t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("base.Square.Square()"), "Constructor is not "
						+ "base.Square.Square()?");
	}
	
	@Test
	public void insertConstructorAndCompatibleMethodTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + INS_CONSTR_COMPAT_METHOD_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + INS_CONSTR_COMPAT_METHOD_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + INS_CONSTR_COMPAT_METHOD_FOLDER + "Square02.java");
		ConflictPattern cp = getInsertConstructorAndCompatibleMethodPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for inserting private "
				+ "field and public constructor?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("base.Square.move(java.lang.Number)"), 
				"Top method is not base.Square.move(java.lang.Number)?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("base.Square.move(int)"), 
				"Inserted and compatible method is not base.Square.move(int)?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("base.Square.Square()"), "Inserted constructor is not "
						+ "base.Square.Square()?");
	}
	
	@Test
	public void insertMethodWithInvocationTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + INS_METHOD_WITH_INV_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + INS_METHOD_WITH_INV_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + INS_METHOD_WITH_INV_FOLDER + "Square02.java");
		ConflictPattern cp = getInsertMethodWithInvocationPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for inserting method with invocation?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("base.Square.m()"), 
				"Method in class (and then invoked) is not base.Square.m()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("base.Square.m1()"), 
				"One of the inserted methods is not base.Square.m1()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("base.Square.m2()"), "Inserted method with invocation "
						+ "is not base.Square.m2()?");
	}
	
	@Test
	public void insertMethodWithFieldAccessTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + INS_METHOD_WITH_ACCESS_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + INS_METHOD_WITH_ACCESS_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + INS_METHOD_WITH_ACCESS_FOLDER + "Square02.java");
		ConflictPattern cp = getInsertMethodWithFieldAccess();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("base.Square.t"), "Field is not base.Square.t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("base.Square.m2()"), 
				"One of the inserted methods is not base.Square.m2()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("base.Square.m()"), "Inserted method with field access "
						+ "is not base.Square.2()?");
	}

	private ConflictPattern getInsertPrivateFieldAndPublicMethodPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		basePattern.addClassPattern(classPattern);
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertFieldPatternAction(fieldVar, classVar, Visibility.PRIVATE));
		dp2.addActionPattern(new InsertMethodPatternAction(methodVar,classVar, Visibility.PUBLIC));
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}
	
	private ConflictPattern getInsertPublicFieldAndPublicMethodPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		basePattern.addClassPattern(classPattern);
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertFieldPatternAction(fieldVar, classVar, Visibility.PUBLIC));
		dp2.addActionPattern(new InsertMethodPatternAction(methodVar,classVar, Visibility.PUBLIC));
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}

	private ConflictPattern getInsertPrivateFieldAndAnyVisibilityConstructorPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable constVar = new FreeVariable(2);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		basePattern.addClassPattern(classPattern);
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertFieldPatternAction(fieldVar, classVar, Visibility.PRIVATE));
		dp2.addActionPattern(new InsertConstructorPatternAction(constVar, classVar, null));
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}
	
	private ConflictPattern getInsertConstructorAndCompatibleMethodPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar = new FreeVariable(1);
		
		FreeVariable insertMethodVar = new FreeVariable(2);
		FreeVariable constVar = new FreeVariable(3);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);
		
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertConstructorPatternAction(constVar, classVar, null));
		InsertMethodPatternAction mInsert = new InsertMethodPatternAction(insertMethodVar, classVar, null);
		mInsert.addCompatible(methodVar);
		dp2.addActionPattern(mInsert);
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}

	private ConflictPattern getInsertMethodWithInvocationPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar = new FreeVariable(1);
		FreeVariable insMethodVar1 = new FreeVariable(2);
		FreeVariable insMethodVar2 = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertMethodPatternAction(insMethodVar1, classVar, null));
		dp2.addActionPattern(new InsertMethodPatternAction(insMethodVar2, classVar, null));
		dp2.addActionPattern(new InsertPatternAction(methodVar, insMethodVar2));
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}
	
	private ConflictPattern getInsertMethodWithFieldAccess() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable insMethodVar1 = new FreeVariable(2);
		FreeVariable insMethodVar2 = new FreeVariable(3);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		classPattern.addFieldPattern(fieldPattern);
		basePattern.addClassPattern(classPattern);
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertMethodPatternAction(insMethodVar1, classVar, null));
		dp2.addActionPattern(new InsertMethodPatternAction(insMethodVar2, classVar, null));
		dp2.addActionPattern(new InsertFieldAccessPatternAction(fieldVar, insMethodVar2, null));
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}
}
