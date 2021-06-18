package matcher;

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

import java.io.File;
import java.util.List;

public class TestMatcherDeletes {

	private static final String SRC_FOLDER = "src" + File.separator + "test" + 
								File.separator + "resources" + File.separator + 
								"OperationsInstances" + File.separator;
	private static final String CONFIG_FILE_NAME = "config.properties";
	private static final String BASE_BRANCH_FOLDER = "base" + File.separator;
	private static final String VAR1_BRANCH_FOLDER = "branch01" + File.separator;
	private static final String VAR2_BRANCH_FOLDER = "branch02" + File.separator;
	
	private static final String DEL_FIELD_METHOD_FOLDER = 
			"FieldAndMethodDeleteInstance" + File.separator;
	private static final String DEL_FIELD_CONSTR_FOLDER = 
			"FieldAndConstructorDeleteInstance" + File.separator;
	private static final String DEL_FIELD_COMPA_METHOD_FOLDER = 
			"FieldAndCompatibleMethodDeleteInstance" + File.separator;
	private static final String DEL_FIELD_AND_INVO_FOLDER = 
			"MethodInvocationAndFieldDeleteInstance" + File.separator;
	private static final String DEL_FIELD_ACCESS_FOLDER = 
			"FieldAccessAndMethodDeleteInstance" + File.separator;

	@Test
	public void deletePrivateFieldAndPublicMethodTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEL_FIELD_METHOD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEL_FIELD_METHOD_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + DEL_FIELD_METHOD_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + DEL_FIELD_METHOD_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getDeletePrivateFieldAndPublicMethodPattern();
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for deleting private "
				+ "field and public method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(3, assignments.size(), "Not 3 assignments with only 3 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Field is not t?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m()", assignments.get(2).getSecond(), 
				"Method is not base.Square.m()?");
	}
	
	@Test
	public void deletePrivateFieldAndPublicConstructorTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEL_FIELD_CONSTR_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEL_FIELD_CONSTR_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + DEL_FIELD_CONSTR_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + DEL_FIELD_CONSTR_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getDeletePrivateFieldAndPublicConstructorPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "Not one result for deleting private "
				+ "field and constructor method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(3, assignments.size(), "Not 3 assignments with only 3 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Field is not t?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("base.Square.<init>()", assignments.get(2).getSecond(), 
				"Constructor is not base.Square.<init>()?");
	}
	
	@Test
	public void deleteFieldAndCompatibleMethodTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEL_FIELD_COMPA_METHOD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEL_FIELD_COMPA_METHOD_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + DEL_FIELD_COMPA_METHOD_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + DEL_FIELD_COMPA_METHOD_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getDeleteFieldAndCompatibleMethodPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), 
				"Not one result for deleting field and compatible method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), "Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Deleted field is not t?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m(java.lang.Number)", assignments.get(2).getSecond(), 
				"Top method is not m(java.lang.Number)?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m(int)", assignments.get(3).getSecond(), 
				"Deleted and compatible method is not m(int)?");
	}
	
	@Test
	public void deleteFieldAndMethodInvocationTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEL_FIELD_AND_INVO_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEL_FIELD_AND_INVO_FOLDER + 
				BASE_BRANCH_FOLDER + "Shape.java";
		String firstVarPath = SRC_FOLDER + DEL_FIELD_AND_INVO_FOLDER + 
				VAR1_BRANCH_FOLDER + "Shape.java";
		String secondVarPath = SRC_FOLDER + DEL_FIELD_AND_INVO_FOLDER + 
				VAR2_BRANCH_FOLDER + "Shape.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getDeleteFieldAndMethodInvocationPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		assertEquals(1, result.size(), 
				"Not one result for deleting field and method invocation?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(3, assignments.size(), "Not 3 assignments with only 3 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Shape", assignments.get(0).getSecond(), 
				"Class is not base.Shape?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), 
				"Deleted field is not t?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m()", assignments.get(2).getSecond(), 
				"Deleted method invoation is not m()?");
	}
	
	@Test
	public void deleteFieldAccessAndMethodTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEL_FIELD_ACCESS_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEL_FIELD_ACCESS_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + DEL_FIELD_ACCESS_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + DEL_FIELD_ACCESS_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getDeleteFieldAccessAndMethodPattern();
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);
				
		assertEquals(1, result.size(), 
				"Not one result for deleting field and compatible method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(4, assignments.size(), 
				"Not 4 assignments with only 4 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), 
				"Accessed field is not t?");
		
		assertEquals(2, assignments.get(2).getFirst(), "Variable id is not 2?"); 
		assertEquals("m()", assignments.get(2).getSecond(), 
				"Method with deleted access is not m()?");
		
		assertEquals(3, assignments.get(3).getFirst(), "Variable id is not 3?"); 
		assertEquals("m2()", assignments.get(3).getSecond(), 
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
		
		dp1.addActionPattern(new DeleteFieldPatternAction(fieldPattern, classPattern));
		dp2.addActionPattern(new DeleteMethodPatternAction(methodPattern, classPattern));
		
		ConflictPattern conflict = new ConflictPattern("Dummy Delete Pattern");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
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
		
		dp1.addActionPattern(new DeleteFieldPatternAction(fieldPattern, classPattern));
		dp2.addActionPattern(new DeleteConstructorPatternAction(cPattern, classPattern));

		ConflictPattern conflict = new ConflictPattern("Dummy Delete Pattern 2");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
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
		
		dp1.addActionPattern(new DeleteFieldPatternAction(fieldPattern, classPattern));
		dp2.addActionPattern(new DeleteMethodPatternAction(subMethodPattern, classPattern));
		
		ConflictPattern conflict = new ConflictPattern("Dummy Delete Pattern 3");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
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
		methodPattern.addDependency(methodVar);
		classPattern.addFieldPattern(fieldPattern);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);
		
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		
		MethodInvocationPattern deletedInvocation = new MethodInvocationPattern(methodVar);
		
		dp1.addActionPattern(new DeleteFieldPatternAction(fieldPattern, classPattern));
		dp2.addActionPattern(new DeleteInvocationPatternAction(deletedInvocation, methodPattern));
		
		ConflictPattern conflict = new ConflictPattern("Dummy Delete Pattern 4");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
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
		
		dp1.addActionPattern(new DeleteFieldAccessPatternAction(accessPattern, methodPattern1));
		dp2.addActionPattern(new DeleteMethodPatternAction(methodPattern2, classPattern));
		
		ConflictPattern conflict = new ConflictPattern("Dummy Delete Pattern 4");
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		return conflict;
	}
}
