package test.matcher;

import org.junit.jupiter.api.Test;

import gumtree.spoon.diff.Diff;
import matcher.entities.ChangeInstance;
import matcher.entities.Visibility;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.MatchingHandler;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.patterns.deltas.InsertPatternAction;
import matcher.utils.Pair;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class TestMatcherSemanticConflicts {

	private static final String SRC_FOLDER = "src/test/resources/SemanticConflictsInstances/";
	private static final String OVERLOAD_ADDITION_FOLDER = "AddOverloadingMByAdditionAddCall2M/";

	@Test
	public void overloadByAdditionTest() throws ApplicationException {
		File base = new File(SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "TestClass.java");
		File firstVar = new File(SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "TestClass01.java");
		File secondVar = new File(SRC_FOLDER + OVERLOAD_ADDITION_FOLDER + "TestClass02.java");
		ConflictPattern cp = getOverloadByAdditionPattern();
		ChangeInstanceHandler cih = new ChangeInstanceHandler();
		Diff d1 = cih.getDiff(base, firstVar);
		Diff d2 = cih.getDiff(base, secondVar);
		ChangeInstance ci = cih.getChangeInstance(base, d1, d2, cp);
		MatchingHandler mh = new MatchingHandler();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		assertTrue(result.size() == 1, "More than one result for overloading method?");
		List<Pair<Integer,String>> assignments = result.get(0);
		assertTrue(assignments.size() == 4, "Not 4 assignments with only 4 variables?");
		assertTrue(assignments.get(0).getFirst() == 0 && 
				assignments.get(0).getSecond().equals("base.TestClass"), "Class is not "
						+ "base.TestClass?");
		assertTrue(assignments.get(1).getFirst() == 1 && 
				assignments.get(1).getSecond().equals("base.TestClass.move(java.lang.Number, "
						+ "java.lang.Number)"), 
				"Method in class is not base.TestClass.move(java.lang.Number, java.lang.Number)?");
		assertTrue(assignments.get(2).getFirst() == 2 && 
				assignments.get(2).getSecond().equals("base.TestClass.reset()"), 
				"Inserted method with invocation is not base.TestClass.reset()?");
		assertTrue(assignments.get(3).getFirst() == 3 && 
				assignments.get(3).getSecond().equals("base.TestClass.move(int, int)"), 
				"Inserted compatible method is not base.TestClass.move(int, int)?");
	}

	private ConflictPattern getOverloadByAdditionPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar = new FreeVariable(1);
		FreeVariable insertedMethodVar1 = new FreeVariable(2);
		FreeVariable insertedMethodVar2 = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);
		
		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		dp1.addActionPattern(new InsertMethodPatternAction(insertedMethodVar1, classVar, 
				Visibility.PUBLIC));
		dp1.addActionPattern(new InsertPatternAction(methodVar, insertedMethodVar1));
		InsertMethodPatternAction impa = new InsertMethodPatternAction(insertedMethodVar2, classVar, 
				Visibility.PUBLIC);
		impa.addCompatible(methodVar);
		dp2.addActionPattern(impa);
		
		return new ConflictPattern(basePattern, dp1, dp2);
	}
}