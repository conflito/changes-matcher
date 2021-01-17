package test.matcher;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import matcher.Matcher;
import matcher.entities.Visibility;
import matcher.exceptions.ApplicationException;
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
import matcher.patterns.deltas.InsertInvocationPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.utils.Pair;

public class TestMatcherInserts {

	private static final String SRC_FOLDER = "src/test/resources/OperationsInstances/";
	private static final String CONFIG_FILE_NAME = "config.properties";
	
	private static final String INS_FIELD_METHOD_FOLDER = "FieldAndMethodInsertInstance/";
	private static final String INS_FIELD_CONSTR_FOLDER = "FieldAndConstructorInsertInstance/";
	private static final String INS_CONSTR_COMPAT_METHOD_FOLDER = 
			"ConstructorAndCompatibleMethodInsertInstance/";
	private static final String INS_METHOD_WITH_INV_FOLDER = "MethodWithInvocationInsertInstance/";
	private static final String INS_METHOD_WITH_ACCESS_FOLDER = "MethodWithFieldAccessInsertInstance/";
	
	@Test
	public void insertPrivateFieldAndPublicMethodTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_FIELD_METHOD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square01.java";
		String secondVarPath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square02.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertPrivateFieldAndPublicMethodPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertTrue(result.size() == 1, "More than one result for inserting private "
				+ "field and public method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 3, "Not 3 assignments with only 3 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
					assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("t"), "Field is not t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m()"), "Method is not m()?");
	}
	
	@Test
	public void insertPrivateFieldAndPublicMethodNoMatchTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_FIELD_METHOD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square01.java";
		String secondVarPath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square02.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertPublicFieldAndPublicMethodPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertTrue(result.size() == 0, "Finds match when there's no private field insertion?");
	}
	
	@Test
	public void insertPrivateFieldAndAnyVisibilityConstructorTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_FIELD_CONSTR_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_FIELD_CONSTR_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_FIELD_CONSTR_FOLDER + "Square01.java";
		String secondVarPath = SRC_FOLDER + INS_FIELD_CONSTR_FOLDER + "Square02.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertPrivateFieldAndAnyVisibilityConstructorPattern();
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertTrue(result.size() == 1, "More than one result for inserting private "
				+ "field and public constructor?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 3, "Not 3 assignments with only 3 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("t"), "Field is not t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("base.Square.Square()"), "Constructor is not "
						+ "base.Square.Square()?");
	}
	
	@Test
	public void insertConstructorAndCompatibleMethodTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_FIELD_CONSTR_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_CONSTR_COMPAT_METHOD_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_CONSTR_COMPAT_METHOD_FOLDER + "Square01.java";
		String secondVarPath = SRC_FOLDER + INS_CONSTR_COMPAT_METHOD_FOLDER + "Square02.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertConstructorAndCompatibleMethodPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
				
		assertTrue(result.size() == 1, "More than one result for inserting private "
				+ "field and public constructor?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("move(java.lang.Number)"), 
				"Top method is not move(java.lang.Number)?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("move(int)"), 
				"Inserted and compatible method is not move(int)?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("base.Square.Square()"), "Inserted constructor is not "
						+ "base.Square.Square()?");
	}
	
	@Test
	public void insertMethodWithInvocationTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_METHOD_WITH_INV_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_METHOD_WITH_INV_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_METHOD_WITH_INV_FOLDER + "Square01.java";
		String secondVarPath = SRC_FOLDER + INS_METHOD_WITH_INV_FOLDER + "Square02.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertMethodWithInvocationPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertTrue(result.size() == 1, "More than one result for inserting method with invocation?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 3, "Not 3 assignments with only 3 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Method in class (and then invoked) is not m()?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m2()"), 
				"Inserted method with invocation is not m2()?");
	}
	
	@Test
	public void insertMethodWithFieldAccessTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_METHOD_WITH_ACCESS_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_METHOD_WITH_ACCESS_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_METHOD_WITH_ACCESS_FOLDER + "Square01.java";
		String secondVarPath = SRC_FOLDER + INS_METHOD_WITH_ACCESS_FOLDER + "Square02.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertMethodWithFieldAccess();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 3, "Not 3 assignments with only 3 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("t"), "Field is not t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("m()"), 
				"Inserted method with field access is not m()?");
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
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		
		return conflict;
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
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		
		return conflict;
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
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		
		return conflict;
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
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		
		return conflict;
	}

	private ConflictPattern getInsertMethodWithInvocationPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar = new FreeVariable(1);
		FreeVariable insMethodVar2 = new FreeVariable(2);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp2.addActionPattern(new InsertMethodPatternAction(insMethodVar2, classVar, null));
		dp2.addActionPattern(new InsertInvocationPatternAction(methodVar, insMethodVar2));
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		
		return conflict;
	}
	
	private ConflictPattern getInsertMethodWithFieldAccess() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable insMethodVar2 = new FreeVariable(2);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		classPattern.addFieldPattern(fieldPattern);
		basePattern.addClassPattern(classPattern);
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp2.addActionPattern(new InsertMethodPatternAction(insMethodVar2, classVar, null));
		dp2.addActionPattern(new InsertFieldAccessPatternAction(fieldVar, insMethodVar2, null));
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.addDeltaPattern(dp1);
		conflict.addDeltaPattern(dp2);
		
		return conflict;
	}
}
