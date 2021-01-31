package test.matcher;

import org.junit.jupiter.api.Test;

import matcher.Matcher;
import matcher.exceptions.ApplicationException;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.UpdateConstructorPatternAction;
import matcher.patterns.deltas.UpdateMethodPatternAction;
import matcher.utils.Pair;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class TestMatcherUpdates {
	
	private static final String SRC_FOLDER = "src" + File.separator + "test" + 
			File.separator + "resources" + File.separator + 
			"OperationsInstances" + File.separator;
	private static final String CONFIG_FILE_NAME = "config.properties";
	private static final String BASE_BRANCH_FOLDER = "base" + File.separator;
	private static final String VAR1_BRANCH_FOLDER = "branch01" + File.separator;
	private static final String VAR2_BRANCH_FOLDER = "branch02" + File.separator;
	
	private static final String UPD_INS_METHOD_FOLDER = 
			"MethodUpdateWithInsertOperationInstance" + File.separator;
	private static final String UPD_DEL_METHOD_FOLDER = 
			"MethodUpdateWithDeleteOperationInstance" + File.separator;
	private static final String UPD_INS_CONST_FOLDER = 
			"ConstructorUpdateWithInsertOperationInstance" + File.separator;
	private static final String UPD_DEL_CONST_FOLDER = 
			"ConstructorUpdateWithDeleteOperationInstance" + File.separator;

	@Test
	public void methodUpdateWithInsertOperationTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UPD_INS_METHOD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + UPD_INS_METHOD_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + UPD_INS_METHOD_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + UPD_INS_METHOD_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getUpdateMethodPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertTrue(result.size() == 1, "More than one result for updating method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 2, "Not 2 assignments with only 2 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Updated method is not m()?");
	}
	
	@Test
	public void methodUpdateWithDeleteOperationTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UPD_DEL_METHOD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + UPD_DEL_METHOD_FOLDER + 
				BASE_BRANCH_FOLDER + "Shape.java";
		String firstVarPath = SRC_FOLDER + UPD_DEL_METHOD_FOLDER + 
				VAR1_BRANCH_FOLDER + "Shape.java";
		String secondVarPath = SRC_FOLDER + UPD_DEL_METHOD_FOLDER + 
				VAR2_BRANCH_FOLDER + "Shape.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getUpdateMethodPattern();
		
		List<List<Pair<Integer, String>>> result = 
			matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertTrue(result.size() == 1, "More than one result for updating method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 2, "Not 2 assignments with only 2 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Shape"), "Class is not base.Shape?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("m()"), 
				"Updated method is not m()?");
	}
	
	@Test
	public void constructorUpdateWithInsertOperationTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UPD_INS_CONST_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + UPD_INS_CONST_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + UPD_INS_CONST_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + UPD_INS_CONST_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getUpdateConstructorPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertTrue(result.size() == 1, "More than one result for updating method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 2, "Not 2 assignments with only 2 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("base.Square.Square()"), 
				"Updated constructor is not base.Square.Square()?");
	}
	
	@Test
	public void constructorUpdateWithDeleteOperationTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UPD_DEL_CONST_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + UPD_DEL_CONST_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + UPD_DEL_CONST_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + UPD_DEL_CONST_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getUpdateConstructorPattern();
		
		List<List<Pair<Integer, String>>> result =
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertTrue(result.size() == 1, "More than one result for updating method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 2, "Not 2 assignments with only 2 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("base.Square.Square()"), 
				"Updated constructor is not base.Square.Square()?");
	}
	
	private ConflictPattern getUpdateMethodPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar = new FreeVariable(1);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);
		
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new UpdateMethodPatternAction(methodPattern));
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		return conflict;
	}
	
	private ConflictPattern getUpdateConstructorPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable cVar = new FreeVariable(1);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		ConstructorPattern cPattern = new ConstructorPattern(cVar, null);
		classPattern.addConstructorPattern(cPattern);
		basePattern.addClassPattern(classPattern);
		
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new UpdateConstructorPatternAction(cPattern));
		
		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		return conflict;
	}
}
