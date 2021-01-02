package test.matcher;

import matcher.entities.ChangeInstance;
import matcher.entities.Visibility;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.MatchingHandler;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FieldAccessPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodInvocationPattern;
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeleteConstructorPatternAction;
import matcher.patterns.deltas.DeleteFieldAccessPatternAction;
import matcher.patterns.deltas.DeleteFieldPatternAction;
import matcher.patterns.deltas.DeleteMethodPatternAction;
import matcher.patterns.deltas.DeletePatternAction;
import matcher.patterns.deltas.DeltaPattern;
import matcher.utils.Pair;

import org.junit.jupiter.api.Test;

import gumtree.spoon.diff.Diff;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class TestMatcherDeletes {

	private static final String SRC_FOLDER = "src/test/resources/OperationsInstances/";
	private static final String DEL_FIELD_METHOD_FOLDER = "FieldAndMethodDeleteInstance/";
	private static final String DEL_FIELD_CONSTR_FOLDER = "FieldAndConstructorDeleteInstance/";
	private static final String DEL_FIELD_COMPA_METHOD_FOLDER = "FieldAndCompatibleMethodDeleteInstance/";
	private static final String DEL_FIELD_AND_INVO_FOLDER = "MethodInvocationAndFieldDeleteInstance/";
	private static final String DEL_FIELD_ACCESS_FOLDER = "FieldAccessAndMethodDeleteInstance/";

	@Test
	public void deletePrivateFieldAndPublicMethodTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + DEL_FIELD_METHOD_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + DEL_FIELD_METHOD_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + DEL_FIELD_METHOD_FOLDER + "Square02.java");
		ConflictPattern cp = getDeletePrivateFieldAndPublicMethodPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for deleting private "
				+ "field and public method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 3, "Not 3 assignments with only 3 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("t"), "Field is not t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m()"), "Method is not base.Square.m()?");
	}
	
	@Test
	public void deletePrivateFieldAndPublicConstructorTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + DEL_FIELD_CONSTR_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + DEL_FIELD_CONSTR_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + DEL_FIELD_CONSTR_FOLDER + "Square02.java");
		ConflictPattern cp = getDeletePrivateFieldAndPublicConstructorPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for deleting private "
				+ "field and constructor method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 3, "Not 3 assignments with only 3 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("t"), "Field is not t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("base.Square.Square()"), "Constructor is "
						+ "not base.Square.m()?");
	}
	
	@Test
	public void deleteFieldAndCompatibleMethodTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + DEL_FIELD_COMPA_METHOD_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + DEL_FIELD_COMPA_METHOD_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + DEL_FIELD_COMPA_METHOD_FOLDER + "Square02.java");
		ConflictPattern cp = getDeleteFieldAndCompatibleMethodPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for deleting field and compatible method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("t"), 
				"Deleted field is not t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m(java.lang.Number)"), 
				"Top method is not m(java.lang.Number)?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("m(int)"), 
				"Deleted and compatible method is not m(int)?");
	}
	
	@Test
	public void deleteFieldAndMethodInvocationTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + DEL_FIELD_AND_INVO_FOLDER + "Shape.java");
		File firstVar = new File(SRC_FOLDER + DEL_FIELD_AND_INVO_FOLDER + "Shape01.java");
		File secondVar = new File(SRC_FOLDER + DEL_FIELD_AND_INVO_FOLDER + "Shape02.java");
		ConflictPattern cp = getDeleteFieldAndMethodInvocationPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for deleting field and method invocation?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 3, "Not 3 assignments with only 3 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Shape"), "Class is not base.Shape?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("t"), 
				"Deleted field is not t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m()"), 
				"Deleted method invoation is not m()?");
	}
	
	@Test
	public void deleteFieldAccessAndMethodTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + DEL_FIELD_ACCESS_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + DEL_FIELD_ACCESS_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + DEL_FIELD_ACCESS_FOLDER + "Square02.java");
		ConflictPattern cp = getDeleteFieldAccessAndMethodPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for deleting field and compatible method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("t"), 
				"Accessed field is not t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m()"), 
				"Method with deleted access is not m()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("m2()"), 
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
		dp1.addActionPattern(new DeleteFieldPatternAction(fieldVar, classVar, Visibility.PRIVATE));
		dp2.addActionPattern(new DeleteMethodPatternAction(methodVar, classVar, Visibility.PUBLIC));

		return new ConflictPattern(basePattern, dp1, dp2);
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
		dp1.addActionPattern(new DeleteFieldPatternAction(fieldVar, classVar, Visibility.PRIVATE));
		dp2.addActionPattern(new DeleteConstructorPatternAction(constVar, classVar, Visibility.PUBLIC));

		return new ConflictPattern(basePattern, dp1, dp2);
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
		dp1.addActionPattern(new DeleteFieldPatternAction(fieldVar, classVar, null));
		dp2.addActionPattern(new DeleteMethodPatternAction(subMethodVar, classVar, null));
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}
	
	private ConflictPattern getDeleteFieldAndMethodInvocationPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		MethodInvocationPattern invoPattern = new MethodInvocationPattern(methodVar);
		methodPattern.addMethodInvocationPattern(invoPattern);
		classPattern.addFieldPattern(fieldPattern);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new DeleteFieldPatternAction(fieldVar, classVar, null));
		dp2.addActionPattern(new DeletePatternAction(methodVar, methodVar));
		
		return new ConflictPattern(basePattern, dp1, dp2);
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
		dp1.addActionPattern(new DeleteFieldAccessPatternAction(fieldVar, methodVar1, null));
		dp2.addActionPattern(new DeleteMethodPatternAction(methodVar2, classVar, null));
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}
}
