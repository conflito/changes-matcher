package org.conflito.matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.conflito.matcher.entities.Visibility;
import org.conflito.matcher.patterns.BasePattern;
import org.conflito.matcher.patterns.ClassPattern;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.patterns.ConstructorPattern;
import org.conflito.matcher.patterns.FieldAccessPattern;
import org.conflito.matcher.patterns.FieldPattern;
import org.conflito.matcher.patterns.FreeVariable;
import org.conflito.matcher.patterns.MethodPattern;
import org.conflito.matcher.patterns.deltas.DeltaPattern;
import org.conflito.matcher.patterns.deltas.InsertConstructorPatternAction;
import org.conflito.matcher.patterns.deltas.InsertFieldPatternAction;
import org.conflito.matcher.patterns.deltas.InsertMethodPatternAction;
import org.conflito.matcher.utils.Pair;
import org.junit.jupiter.api.Test;

public class TestMatcherInserts extends AbstractTestMatcher {

  private static final String OPERATIONS_INSTANCES_DIR_PATH = buildPath(
      RELATIVE_TEST_RESOURCES_DIR_PATH, "OperationsInstances");

  @Test
  public void insertPrivateFieldAndPublicMethodTest() throws Exception {
    Matcher matcher = new Matcher(
        buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndMethodInsertInstance", CONFIG_FILE_NAME));

    String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndMethodInsertInstance",
        BASE_BRANCH_NAME, "Square.java");
    String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndMethodInsertInstance",
        BRANCH_1_NAME, "Square.java");
    String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndMethodInsertInstance",
        BRANCH_2_NAME, "Square.java");

    String[] bases = {basePath};
    assertTrue(checkIfFilesExist(bases));
    String[] variants1 = {firstVarPath};
    assertTrue(checkIfFilesExist(variants1));
    String[] variants2 = {secondVarPath};
    assertTrue(checkIfFilesExist(variants2));

    ConflictPattern cp = getInsertPrivateFieldAndPublicMethodPattern();

    List<List<Pair<Integer, String>>> result =
        matcher.matchingAssignments(bases, variants1, variants2, cp);

    assertEquals(1, result.size(), "Not one result for inserting private "
        + "field and public method?");

    List<Pair<Integer, String>> assignments = result.get(0);
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
  public void insertPrivateFieldAndPublicMethodNoMatchTest() throws Exception {
    Matcher matcher = new Matcher(
        buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndConstructorInsertInstance",
            CONFIG_FILE_NAME));

    String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndConstructorInsertInstance",
        BASE_BRANCH_NAME, "Square.java");
    String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "FieldAndConstructorInsertInstance", BRANCH_1_NAME, "Square.java");
    String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "FieldAndConstructorInsertInstance", BRANCH_2_NAME, "Square.java");

    String[] bases = {basePath};
    assertTrue(checkIfFilesExist(bases));
    String[] variants1 = {firstVarPath};
    assertTrue(checkIfFilesExist(variants1));
    String[] variants2 = {secondVarPath};
    assertTrue(checkIfFilesExist(variants2));

    ConflictPattern cp = getInsertPublicFieldAndPublicMethodPattern();

    List<List<Pair<Integer, String>>> result =
        matcher.matchingAssignments(bases, variants1, variants2, cp);

    assertEquals(0, result.size(),
        "Finds match when there's no private field insertion?");
  }

  @Test
  public void insertPrivateFieldAndAnyVisibilityConstructorTest() throws Exception {
    Matcher matcher = new Matcher(
        buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndConstructorInsertInstance",
            CONFIG_FILE_NAME));

    String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "FieldAndConstructorInsertInstance",
        BASE_BRANCH_NAME, "Square.java");
    String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "FieldAndConstructorInsertInstance", BRANCH_1_NAME, "Square.java");
    String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "FieldAndConstructorInsertInstance", BRANCH_2_NAME, "Square.java");

    String[] bases = {basePath};
    assertTrue(checkIfFilesExist(bases));
    String[] variants1 = {firstVarPath};
    assertTrue(checkIfFilesExist(variants1));
    String[] variants2 = {secondVarPath};
    assertTrue(checkIfFilesExist(variants2));

    ConflictPattern cp = getInsertPrivateFieldAndAnyVisibilityConstructorPattern();

    List<List<Pair<Integer, String>>> result =
        matcher.matchingAssignments(bases, variants1, variants2, cp);

    assertEquals(1, result.size(), "Not one result for inserting private "
        + "field and public constructor?");

    List<Pair<Integer, String>> assignments = result.get(0);
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
  public void insertConstructorAndCompatibleMethodTest() throws Exception {
    Matcher matcher = new Matcher(
        buildPath(OPERATIONS_INSTANCES_DIR_PATH, "ConstructorAndCompatibleMethodInsertInstance",
            CONFIG_FILE_NAME));

    String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "ConstructorAndCompatibleMethodInsertInstance", BASE_BRANCH_NAME, "Square.java");
    String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "ConstructorAndCompatibleMethodInsertInstance", BRANCH_1_NAME, "Square.java");
    String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "ConstructorAndCompatibleMethodInsertInstance", BRANCH_2_NAME, "Square.java");

    String[] bases = {basePath};
    assertTrue(checkIfFilesExist(bases));
    String[] variants1 = {firstVarPath};
    assertTrue(checkIfFilesExist(variants1));
    String[] variants2 = {secondVarPath};
    assertTrue(checkIfFilesExist(variants2));

    ConflictPattern cp = getInsertConstructorAndCompatibleMethodPattern();

    List<List<Pair<Integer, String>>> result =
        matcher.matchingAssignments(bases, variants1, variants2, cp);

    assertEquals(1, result.size(), "Not one result for inserting private "
        + "field and public constructor?");

    List<Pair<Integer, String>> assignments = result.get(0);
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
  public void insertMethodWithInvocationTest() throws Exception {
    Matcher matcher = new Matcher(
        buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodWithInvocationInsertInstance",
            CONFIG_FILE_NAME));

    String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodWithInvocationInsertInstance",
        BASE_BRANCH_NAME, "Square.java");
    String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "MethodWithInvocationInsertInstance", BRANCH_1_NAME, "Square.java");
    String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "MethodWithInvocationInsertInstance", BRANCH_2_NAME, "Square.java");

    String[] bases = {basePath};
    assertTrue(checkIfFilesExist(bases));
    String[] variants1 = {firstVarPath};
    assertTrue(checkIfFilesExist(variants1));
    String[] variants2 = {secondVarPath};
    assertTrue(checkIfFilesExist(variants2));

    ConflictPattern cp = getInsertMethodWithInvocationPattern();

    List<List<Pair<Integer, String>>> result =
        matcher.matchingAssignments(bases, variants1, variants2, cp);

    assertEquals(1, result.size(),
        "Not one result for inserting method with invocation?");

    List<Pair<Integer, String>> assignments = result.get(0);
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
  public void insertMethodWithFieldAccessTest() throws Exception {
    Matcher matcher = new Matcher(
        buildPath(OPERATIONS_INSTANCES_DIR_PATH, "MethodWithFieldAccessInsertInstance",
            CONFIG_FILE_NAME));

    String basePath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "MethodWithFieldAccessInsertInstance", BASE_BRANCH_NAME, "Square.java");
    String firstVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "MethodWithFieldAccessInsertInstance", BRANCH_1_NAME, "Square.java");
    String secondVarPath = buildPath(OPERATIONS_INSTANCES_DIR_PATH,
        "MethodWithFieldAccessInsertInstance", BRANCH_2_NAME, "Square.java");

    String[] bases = {basePath};
    assertTrue(checkIfFilesExist(bases));
    String[] variants1 = {firstVarPath};
    assertTrue(checkIfFilesExist(variants1));
    String[] variants2 = {secondVarPath};
    assertTrue(checkIfFilesExist(variants2));

    ConflictPattern cp = getInsertMethodWithFieldAccess();

    List<List<Pair<Integer, String>>> result =
        matcher.matchingAssignments(bases, variants1, variants2, cp);

    assertEquals(1, result.size(), "Not one result for insert method "
        + "with field access?");

    List<Pair<Integer, String>> assignments = result.get(0);
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
