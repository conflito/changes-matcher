package test.matcher;

import org.junit.jupiter.api.Test;

import matcher.Matcher;
import matcher.entities.Visibility;
import matcher.entities.deltas.Action;
import matcher.exceptions.ApplicationException;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.VisibilityActionPattern;
import matcher.utils.Pair;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class TestMatcherVisibilities {
	
	private static final String SRC_FOLDER = "src" + File.separator + "test" + 
			File.separator + "resources" + File.separator + 
			"OperationsInstances" + File.separator;
	private static final String CONFIG_FILE_NAME = "config.properties";
	private static final String BASE_BRANCH_FOLDER = "base" + File.separator;
	private static final String VAR1_BRANCH_FOLDER = "branch01" + File.separator;
	private static final String VAR2_BRANCH_FOLDER = "branch02" + File.separator;
	
	private static final String INS_VIS_FIELD_FOLDER = 
			"VisibilityFieldInsertInstance"+ File.separator;
	private static final String DEL_VIS_FIELD_FOLDER = 
			"VisibilityFieldDeleteInstance"+ File.separator;
	private static final String UPD_VIS_FIELD_FOLDER = 
			"VisibilityFieldUpdateInstance"+ File.separator;
	
	@Test
	public void insertVisibilityInFieldTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ INS_VIS_FIELD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + INS_VIS_FIELD_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + INS_VIS_FIELD_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + INS_VIS_FIELD_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getInsertFieldVisibilityPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "More than one result for updating method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Updated field is not t?");
	}
	
	@Test
	public void deleteVisibilityInFieldTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ DEL_VIS_FIELD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + DEL_VIS_FIELD_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + DEL_VIS_FIELD_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + DEL_VIS_FIELD_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getDeleteFieldVisibilityPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
		
		assertEquals(1, result.size(), "More than one result for updating method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Updated field is not t?");
	}
	
	@Test
	public void updateVisibilityInFieldTest() throws ApplicationException {
		Matcher matcher = new Matcher(SRC_FOLDER 
				+ UPD_VIS_FIELD_FOLDER + CONFIG_FILE_NAME);
		
		String basePath = SRC_FOLDER + UPD_VIS_FIELD_FOLDER + 
				BASE_BRANCH_FOLDER + "Square.java";
		String firstVarPath = SRC_FOLDER + UPD_VIS_FIELD_FOLDER + 
				VAR1_BRANCH_FOLDER + "Square.java";
		String secondVarPath = SRC_FOLDER + UPD_VIS_FIELD_FOLDER + 
				VAR2_BRANCH_FOLDER + "Square.java";
		
		String[] bases = {basePath};
		String[] variants1 = {firstVarPath};
		String[] variants2 = {secondVarPath};
		
		ConflictPattern cp = getUpdateFieldVisibilityPattern();
		
		List<List<Pair<Integer, String>>> result = 
				matcher.matchingAssignments(bases, variants1, variants2, cp);
				
		assertEquals(1, result.size(), "More than one result for updating method?");
		
		List<Pair<Integer,String>> assignments = result.get(0);
		assertEquals(2, assignments.size(), "Not 2 assignments with only 2 variables?");
		
		assertEquals(0, assignments.get(0).getFirst(), "Variable id is not 0?"); 
		assertEquals("base.Square", assignments.get(0).getSecond(), 
				"Class is not base.Square?");
		
		assertEquals(1, assignments.get(1).getFirst(), "Variable id is not 1?"); 
		assertEquals("t", assignments.get(1).getSecond(), "Updated field is not t?");
	}

	private ConflictPattern getInsertFieldVisibilityPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldvar = new FreeVariable(1);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldvar, Visibility.PACKAGE);
		classPattern.addFieldPattern(fieldPattern);
		basePattern.addClassPattern(classPattern);
		
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new VisibilityActionPattern(Action.INSERT, Visibility.PRIVATE, 
				null, fieldvar));
		
		ConflictPattern conflict = new ConflictPattern("Dummy Visibility Pattern");
		conflict.setBasePattern(basePattern);
//		conflict.addDeltaPattern(dp1);
//		conflict.addDeltaPattern(dp2);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		return conflict;
	}
	
	private ConflictPattern getDeleteFieldVisibilityPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldvar = new FreeVariable(1);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldvar, Visibility.PRIVATE);
		classPattern.addFieldPattern(fieldPattern);
		basePattern.addClassPattern(classPattern);
		
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new VisibilityActionPattern(Action.DELETE, null, 
				Visibility.PRIVATE, fieldvar));
		
		ConflictPattern conflict = new ConflictPattern("Dummy Visibility Pattern 2");
		conflict.setBasePattern(basePattern);
//		conflict.addDeltaPattern(dp1);
//		conflict.addDeltaPattern(dp2);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		return conflict;
	}
	
	private ConflictPattern getUpdateFieldVisibilityPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldvar = new FreeVariable(1);
		
		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldvar, Visibility.PRIVATE);
		classPattern.addFieldPattern(fieldPattern);
		basePattern.addClassPattern(classPattern);
		
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new VisibilityActionPattern(Action.UPDATE, Visibility.PROTECTED, 
				Visibility.PRIVATE, fieldvar));
		
		ConflictPattern conflict = new ConflictPattern("Dummy Visibility Pattern 2");
		conflict.setBasePattern(basePattern);
//		conflict.addDeltaPattern(dp1);
//		conflict.addDeltaPattern(dp2);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		return conflict;
	}

}
