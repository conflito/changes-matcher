package test.matcher;

import matcher.Matcher;
import matcher.entities.Visibility;
import matcher.exceptions.ApplicationException;
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
import matcher.patterns.deltas.DeleteInvocationPatternAction;
import matcher.patterns.deltas.DeleteMethodPatternAction;
import matcher.patterns.deltas.DeltaPattern;
import matcher.utils.Pair;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TestMatcherDeletes {

	private static final String SRC_FOLDER = "src/test/resources/OperationsInstances/";
	private static final String CONFIG_FILE_NAME = "config.properties";
	
	private static final String DEL_FIELD_METHOD_FOLDER = "FieldAndMethodDeleteInstance/";
	private static final String DEL_FIELD_CONSTR_FOLDER = "FieldAndConstructorDeleteInstance/";
	private static final String DEL_FIELD_COMPA_METHOD_FOLDER = "FieldAndCompatibleMethodDeleteInstance/";
	private static final String DEL_FIELD_AND_INVO_FOLDER = "MethodInvocationAndFieldDeleteInstance/";
	private static final String DEL_FIELD_ACCESS_FOLDER = "FieldAccessAndMethodDeleteInstance/";

	@Test
	public void deletePrivateFieldAndPublicMethodTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEL_FIELD_METHOD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEL_FIELD_METHOD_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + DEL_FIELD_METHOD_FOLDER + "Square01.java";
		String secondVarPath = SRC_FOLDER + DEL_FIELD_METHOD_FOLDER + "Square02.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getDeletePrivateFieldAndPublicMethodPattern();
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
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
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEL_FIELD_CONSTR_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEL_FIELD_CONSTR_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + DEL_FIELD_CONSTR_FOLDER + "Square01.java";
		String secondVarPath = SRC_FOLDER + DEL_FIELD_CONSTR_FOLDER + "Square02.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getDeletePrivateFieldAndPublicConstructorPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
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
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEL_FIELD_COMPA_METHOD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEL_FIELD_COMPA_METHOD_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + DEL_FIELD_COMPA_METHOD_FOLDER + "Square01.java";
		String secondVarPath = SRC_FOLDER + DEL_FIELD_COMPA_METHOD_FOLDER + "Square02.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getDeleteFieldAndCompatibleMethodPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
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
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEL_FIELD_AND_INVO_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEL_FIELD_AND_INVO_FOLDER + "Shape.java";
		String firstVarPath = SRC_FOLDER + DEL_FIELD_AND_INVO_FOLDER + "Shape01.java";
		String secondVarPath = SRC_FOLDER + DEL_FIELD_AND_INVO_FOLDER + "Shape02.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getDeleteFieldAndMethodInvocationPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
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
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEL_FIELD_ACCESS_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEL_FIELD_ACCESS_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + DEL_FIELD_ACCESS_FOLDER + "Square01.java";
		String secondVarPath = SRC_FOLDER + DEL_FIELD_ACCESS_FOLDER + "Square02.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getDeleteFieldAccessAndMethodPattern();
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);
				
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
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		
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
		dp1.addActionPattern(new DeleteFieldPatternAction(fieldVar, classVar, Visibility.PRIVATE));
		dp2.addActionPattern(new DeleteConstructorPatternAction(constVar, classVar, Visibility.PUBLIC));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		
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
		dp1.addActionPattern(new DeleteFieldPatternAction(fieldVar, classVar, null));
		dp2.addActionPattern(new DeleteMethodPatternAction(subMethodVar, classVar, null));
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		
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
		MethodInvocationPattern invoPattern = new MethodInvocationPattern(methodVar);
		methodPattern.addMethodInvocationPattern(invoPattern);
		classPattern.addFieldPattern(fieldPattern);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new DeleteFieldPatternAction(fieldVar, classVar, null));
		dp2.addActionPattern(new DeleteInvocationPatternAction(methodVar, methodVar));
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		
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
		dp1.addActionPattern(new DeleteFieldAccessPatternAction(fieldVar, methodVar1, null));
		dp2.addActionPattern(new DeleteMethodPatternAction(methodVar2, classVar, null));
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		
		return conflict;
	}
}
