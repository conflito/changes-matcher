package test.matcher;

import org.junit.jupiter.api.Test;

import gumtree.spoon.diff.Diff;
import matcher.entities.ChangeInstance;
import matcher.entities.Visibility;
import matcher.entities.deltas.Action;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.MatchingHandler;
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
	
	private static final String SRC_FOLDER = "src/test/resources/OperationsInstances/";
	private static final String INS_VIS_FIELD_FOLDER = "VisibilityFieldInsertInstance/";
	private static final String DEL_VIS_FIELD_FOLDER = "VisibilityFieldDeleteInstance/";
	private static final String UPD_VIS_FIELD_FOLDER = "VisibilityFieldUpdateInstance/";
	
	@Test
	public void insertVisibilityInFieldTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + INS_VIS_FIELD_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + INS_VIS_FIELD_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + INS_VIS_FIELD_FOLDER + "Square02.java");
		ConflictPattern cp = getInsertFieldVisibilityPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for updating method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 2, "Not 2 assignments with only 2 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("t"), 
				"Updated field is not t?");
	}
	
	@Test
	public void deleteVisibilityInFieldTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + DEL_VIS_FIELD_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + DEL_VIS_FIELD_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + DEL_VIS_FIELD_FOLDER + "Square02.java");
		ConflictPattern cp = getDeleteFieldVisibilityPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for updating method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 2, "Not 2 assignments with only 2 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("t"), 
				"Updated field is not t?");
	}
	
	@Test
	public void updateVisibilityInFieldTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + UPD_VIS_FIELD_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + UPD_VIS_FIELD_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + UPD_VIS_FIELD_FOLDER + "Square02.java");
		ConflictPattern cp = getUpdateFieldVisibilityPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for updating method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 2, "Not 2 assignments with only 2 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("t"), 
				"Updated field is not t?");
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
		
		return new ConflictPattern(basePattern, dp1, dp2);
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
		
		return new ConflictPattern(basePattern, dp1, dp2);
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
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}

}
