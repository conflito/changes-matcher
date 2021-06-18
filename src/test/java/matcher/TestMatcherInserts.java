package matcher;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

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
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertConstructorPatternAction;
import matcher.patterns.deltas.InsertFieldPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.utils.Pair;

public class TestMatcherInserts {

	private static final String SRC_FOLDER = "src" + File.separator + "test" + 
			File.separator + "resources" + File.separator + 
			"OperationsInstances" + File.separator;
	private static final String CONFIG_FILE_NAME = "config.properties";
	private static final String BASE_BRANCH_FOLDER = "base" + File.separator;
	private static final String VAR1_BRANCH_FOLDER = "branch01" + File.separator;
	private static final String VAR2_BRANCH_FOLDER = "branch02" + File.separator;
	
	private static final String INS_FIELD_METHOD_FOLDER = 
			"FieldAndMethodInsertInstance" + File.separator;
	private static final String INS_FIELD_CONSTR_FOLDER = 
			"FieldAndConstructorInsertInstance" + File.separator;
	private static final String INS_CONSTR_COMPAT_METHOD_FOLDER = 
			"ConstructorAndCompatibleMethodInsertInstance" + File.separator;
	private static final String INS_METHOD_WITH_INV_FOLDER = 
			"MethodWithInvocationInsertInstance" + File.separator;
	private static final String INS_METHOD_WITH_ACCESS_FOLDER = 
			"MethodWithFieldAccessInsertInstance" + File.separator;
	
	@Test
	public void insertPrivateFieldAndPublicMethodTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_FIELD_METHOD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertPrivateFieldAndPublicMethodPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for inserting private "
				+ "field and public method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(3, assignments.size(), "Not 3 assignments with only 3 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Field is not t?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m()", assignments.get(2).getSecond(), "Method is not m()?");
	}
	
	@Test
	public void insertPrivateFieldAndPublicMethodNoMatchTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_FIELD_METHOD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + INS_FIELD_METHOD_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertPublicFieldAndPublicMethodPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(0, result.size(), 
				"Finds match when there's no private field insertion?");
	}
	
	@Test
	public void insertPrivateFieldAndAnyVisibilityConstructorTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_FIELD_CONSTR_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_FIELD_CONSTR_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_FIELD_CONSTR_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + INS_FIELD_CONSTR_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertPrivateFieldAndAnyVisibilityConstructorPattern();
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for inserting private "
				+ "field and public constructor?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(3, assignments.size(), "Not 3 assignments with only 3 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Field is not t?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2"); 
		assertEquals("base.Square.<init>()", assignments.get(2).getSecond(), 
				"Constructor is not base.Square.<init>()?");
	}
	
	@Test
	public void insertConstructorAndCompatibleMethodTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_CONSTR_COMPAT_METHOD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_CONSTR_COMPAT_METHOD_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_CONSTR_COMPAT_METHOD_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + INS_CONSTR_COMPAT_METHOD_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertConstructorAndCompatibleMethodPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for inserting private "
				+ "field and public constructor?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("move(java.lang.Number)", assignments.get(1).getSecond(), 
				"Top method is not move(java.lang.Number)?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("move(int)", assignments.get(2).getSecond(), 
				"Inserted and compatible method is not move(int)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("base.Square.<init>()", assignments.get(3).getSecond(), 
				"Inserted constructor is not base.Square.<init>()?");
	}
	
	@Test
	public void insertMethodWithInvocationTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_METHOD_WITH_INV_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_METHOD_WITH_INV_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_METHOD_WITH_INV_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + INS_METHOD_WITH_INV_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertMethodWithInvocationPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), 
				"Not one result for inserting method with invocation?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(3, assignments.size(), "Not 3 assignments with only 3 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("m()", assignments.get(1).getSecond(), 
				"Method in class (and then invoked) is not m()?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m2()", assignments.get(2).getSecond(), 
				"Inserted method with invocation is not m2()?");
	}
	
	@Test
	public void insertMethodWithFieldAccessTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_METHOD_WITH_ACCESS_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_METHOD_WITH_ACCESS_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_METHOD_WITH_ACCESS_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + INS_METHOD_WITH_ACCESS_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertMethodWithFieldAccess();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);

		assertEquals(1, result.size(), "Not one result for insert method "
				+ "with field access?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(3, assignments.size(), "Not 3 assignments with only 3 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Field is not t?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m()", assignments.get(2).getSecond(), 
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
		
		FieldPattern insertedFieldPattern = new FieldPattern(fieldVar, Visibility.PRIVATE);
		MethodPattern insertedMethodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		dp1.addActionPattern(new InsertFieldPatternAction(insertedFieldPattern, classPattern));
		dp2.addActionPattern(new InsertMethodPatternAction(insertedMethodPattern, classPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Insert Pattern");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
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
		
		FieldPattern insertedFieldPattern = new FieldPattern(fieldVar, Visibility.PUBLIC);
		MethodPattern insertedMethodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		dp1.addActionPattern(new InsertFieldPatternAction(insertedFieldPattern, classPattern));
		dp2.addActionPattern(new InsertMethodPatternAction(insertedMethodPattern, classPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Insert Pattern 2");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
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
		
		FieldPattern insertedFieldPattern = new FieldPattern(fieldVar, Visibility.PRIVATE);
		ConstructorPattern insertedConstructorPattern = new ConstructorPattern(constVar, null);
		dp1.addActionPattern(new InsertFieldPatternAction(insertedFieldPattern, classPattern));
		dp2.addActionPattern(
				new InsertConstructorPatternAction(insertedConstructorPattern, classPattern));
		
		ConflictPattern conflict = new ConflictPattern("Dummy Insert Pattern 3");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
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
		
		ConstructorPattern insertedConstructorPattern = new ConstructorPattern(constVar, null);
		dp1.addActionPattern(
				new InsertConstructorPatternAction(insertedConstructorPattern, classPattern));
		
		MethodPattern insertedMethodPattern = new MethodPattern(insertMethodVar, null);
		InsertMethodPatternAction mInsert = 
				new InsertMethodPatternAction(insertedMethodPattern, classPattern);
		mInsert.addCompatible(methodPattern);
		dp2.addActionPattern(mInsert);
		
		ConflictPattern conflict = new ConflictPattern("Dummy Insert Pattern 4");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
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
		
		MethodPattern insertedMethodPattern = new MethodPattern(insMethodVar2, null);
		insertedMethodPattern.addDependency(methodVar);
		
		dp2.addActionPattern(new InsertMethodPatternAction(insertedMethodPattern, classPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Insert Pattern 5");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
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
		
		MethodPattern insertedMethodPattern = new MethodPattern(insMethodVar2, null);
		FieldAccessPattern fieldAccessPattern = new FieldAccessPattern(fieldVar, null);
		insertedMethodPattern.addFieldAccessPattern(fieldAccessPattern);
		
		dp2.addActionPattern(new InsertMethodPatternAction(insertedMethodPattern, classPattern));
//		dp2.addActionPattern(
//				new InsertFieldAccessPatternAction(fieldAccessPattern, insertedMethodPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Insert Pattern 6");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		return conflict;
	}
}
