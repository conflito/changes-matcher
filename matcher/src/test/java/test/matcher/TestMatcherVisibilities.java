package test.matcher;

import org.junit.jupiter.api.Test;

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
	
	private static final String SRC_FOLDER = "src/test/resources/";
	private static final String INS_VIS_FIELD_FOLDER = "VisibilityFieldInsertInstance/";
	
	@Test
	public void insertVisibilityInFieldTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + INS_VIS_FIELD_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + INS_VIS_FIELD_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + INS_VIS_FIELD_FOLDER + "Square02.java");
		ConflictPattern cp = getInsertFieldVisibilityPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		ChangeInstance ci = cih.getChangeInstance(base, firstVar, secondVar, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for updating method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 2, "Not 2 assignments with only 2 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("base.Square.t"), 
				"Updated field is not base.Square.t?");
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

}
