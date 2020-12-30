package test.matcher;

import org.junit.jupiter.api.Test;

import matcher.entities.ChangeInstance;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.MatchingHandler;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.UpdatePatternAction;
import matcher.utils.Pair;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class TestMatcherUpdates {
	
	private static final String SRC_FOLDER = "src/test/resources/";
	private static final String UPD_INS_METHOD = "MethodUpdateWithInsertOperationInstance/";
	private static final String UPD_DEL_METHOD = "MethodUpdateWithDeleteOperationInstance/";
	
	@Test
	public void methodUpdateWithInsertOperationTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + UPD_INS_METHOD + "Square.java");
		File firstVar = new File(SRC_FOLDER + UPD_INS_METHOD + "Square01.java");
		File secondVar = new File(SRC_FOLDER + UPD_INS_METHOD + "Square02.java");
		ConflictPattern cp = getUpdateMethodPattern();
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
				assignments.get(1).getSecond().equals("base.Square.m()"), 
				"Updated method is not base.Square.m()?");
	}
	
	@Test
	public void methodUpdateWithDeleteOperationTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + UPD_DEL_METHOD + "Shape.java");
		File firstVar = new File(SRC_FOLDER + UPD_DEL_METHOD + "Shape01.java");
		File secondVar = new File(SRC_FOLDER + UPD_DEL_METHOD + "Shape02.java");
		ConflictPattern cp = getUpdateMethodPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		ChangeInstance ci = cih.getChangeInstance(base, firstVar, secondVar, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for updating method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 2, "Not 2 assignments with only 2 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.Shape"), "Class is not base.Shape?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("base.Shape.m()"), 
				"Updated method is not base.Shape.m()?");
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
		dp1.addActionPattern(new UpdatePatternAction(methodVar));
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}
}
