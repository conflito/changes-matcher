package test.matcher;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import matcher.entities.ChangeInstance;
import matcher.entities.Visibility;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.MatchingHandler;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertFieldPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.utils.Pair;

public class TestMatcher {

	private static final String SRC_FOLDER = "src/test/resources/";
	private static final String INS_FIELD_METHOD_FOLDER = "FieldAndMethodInsertInstance/";
	
	@Test
	public void insertPrivateFieldAndPublicMethodTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square.java");
		File firstVar = new File(SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square01.java");
		File secondVar = new File(SRC_FOLDER + INS_FIELD_METHOD_FOLDER + "Square02.java");
		ConflictPattern cp = getInsertPublicFieldAndPublicMethodPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		ChangeInstance ci = cih.getChangeInstance(base, firstVar, secondVar, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for inserting private "
				+ "field and public method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 3, "Not 3 assignments with only 3 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
					assignments.get(0).getSecond().equals("base.Square"), "Class is not base.Square?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("base.Square.t"), "Field is not base.Square.t?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("base.Square.m()"), "Method is not base.Square.m()?");
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
		dp1.addActionPattern(new InsertFieldPatternAction(fieldVar, classVar, Visibility.PRIVATE));
		dp2.addActionPattern(new InsertMethodPatternAction(methodVar,classVar, Visibility.PUBLIC));
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}
}
