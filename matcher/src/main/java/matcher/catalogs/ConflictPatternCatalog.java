package matcher.catalogs;

import java.util.HashMap;
import java.util.Map;

import matcher.entities.FieldAccessType;
import matcher.entities.Visibility;
import matcher.entities.deltas.Action;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.FieldAccessPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.InterfaceImplementationPattern;
import matcher.patterns.InterfacePattern;
import matcher.patterns.MethodInvocationPattern;
import matcher.patterns.MethodPattern;
import matcher.patterns.TypePattern;
import matcher.patterns.deltas.DeleteMethodPatternAction;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertClassPatternAction;
import matcher.patterns.deltas.InsertFieldPatternAction;
import matcher.patterns.deltas.InsertInvocationPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.patterns.deltas.UpdateDependencyPatternAction;
import matcher.patterns.deltas.UpdateFieldTypePatternAction;
import matcher.patterns.deltas.UpdateMethodPatternAction;
import matcher.patterns.deltas.VisibilityActionPattern;
import matcher.patterns.goals.TestingGoal;

public class ConflictPatternCatalog {
	
	public static final String PARALLEL_CHANGES = "Parallel Changes";
	
	public static final String CHANGE_METHOD = "Change Method";
	public static final String CHANGE_METHOD_2 = "Change Method 2";
	
	public static final String DEPENDENCY_BASED_CLASS_EXISTS = 
			"Dependency Based (class exists)";
	public static final String DEPENDENCY_BASED_CLASS_NEW = 
			"Dependency Based (new class)";
	public static final String DEPENDENCY_BASED_DEPENDENCY_INSERT =
			"Dependency Base (dependency insertion)";

	public static final String UNEXPECTED_OVERRIDING_NEW_METHOD = 
			"Unexpected Overriding (new method)";
	public static final String UNEXPECTED_OVERRIDING_NEW_CLASS = 
			"Unexpected Overriding (new class)";
	public static final String UNEXPECTED_OVERRIDING_NEW_CALL = 
			"Unexpected Overriding (new call)";
	
	public static final String UNEXPECTED_OVERRIDING_3_NEW_METHOD = 
			"Unexpected Overriding 3 (new method)";
	public static final String UNEXPECTED_OVERRIDING_3_NEW_DEPENDENCY = 
			"Unexpected Overriding 3 (new dependency)";
	public static final String UNEXPECTED_OVERRIDING_3_NEW_CLASS = 
			"Unexpected Overriding 3 (new class)";
	
	public static final String FIELD_HIDING = "Field Hiding";
	
	public static final String ADD_METHOD_OVERRIDING_NEW_METHOD = 
			"Add Method Overriding (new method)";
	public static final String ADD_METHOD_OVERRIDING_NEW_CLASS = 
			"Add Method Overriding (new class)";
	public static final String ADD_METHOD_OVERRIDING_NEW_CALL = 
			"Add Method Overriding (new call)";
	
	public static final String OVERLOAD_BY_ADDITION_CLASS_EXISTS = 
			"Overload by Addition (class exists)";
	public static final String OVERLOAD_BY_ADDITION_CLASS_NEW = 
			"Overload by Addition (new class)";
	public static final String OVERLOAD_BY_ADDITION_CALL_NEW = 
			"Overload by Addition (new call)";
	
	public static final String OVERLOAD_BY_ACCESS_CHANGE_CLASS_EXISTS = 
			"Overload by Access Change (class exists)";
	public static final String OVERLOAD_BY_ACCESS_CHANGE_CLASS_NEW = 
			"Overload by Access Change (new class)";
	public static final String OVERLOAD_BY_ACCESS_CHANGE_CALL_NEW = 
			"Overload by Access Change (new call)";
	
	public static final String OVERLOAD_BY_ACCESS_CHANGE_2_CLASS_EXISTS = 
			"Overload by Access Change 2(class exists)";
	public static final String OVERLOAD_BY_ACCESS_CHANGE_2_CLASS_NEW = 
			"Overload by Access Change 2(new class)";
	public static final String OVERLOAD_BY_ACCESS_CHANGE_2_CALL_NEW = 
			"Overload by Access Change 2(new call)";
	
	public static final String REMOVE_OVERRIDING = "Remove Overriding";
	
	private Map<String, ConflictPattern> patterns;
	
	public ConflictPatternCatalog() {
		patterns = new HashMap<>();
		loadPatterns();
	}
	
	public Iterable<ConflictPattern> getPatterns(){
		return patterns.values();
	}
	
	public static ConflictPattern getConflict(String name) {
		return new ConflictPatternCatalog().patterns.get(name);
	}
	
	private void loadPatterns() {
		patterns.put(PARALLEL_CHANGES, 
				getParallelChangedPattern());
		
		patterns.put(CHANGE_METHOD, 
				getChangeMethodPattern());
		patterns.put(CHANGE_METHOD_2, getChangeMethod2Pattern());
		
		patterns.put(DEPENDENCY_BASED_CLASS_EXISTS, 
				getDependencyBasedClassExistsPattern());
		patterns.put(DEPENDENCY_BASED_CLASS_NEW, 
				getDependencyBasedNewClassPattern());
		patterns.put(DEPENDENCY_BASED_DEPENDENCY_INSERT, 
				getDependencyBasedCallAdditionPattern());
		
		patterns.put(UNEXPECTED_OVERRIDING_NEW_METHOD,
				getUnexpectedOverriding1NewMethodPattern());
		patterns.put(UNEXPECTED_OVERRIDING_NEW_CLASS,
				getUnexpectedOverriding1NewClassPattern());
		patterns.put(UNEXPECTED_OVERRIDING_NEW_CALL,
				getUnexpectedOverriding1NewCallPattern());
		
		patterns.put(UNEXPECTED_OVERRIDING_3_NEW_METHOD,
				getUnexpectedOverriding3NewMethodPattern());
		patterns.put(UNEXPECTED_OVERRIDING_3_NEW_DEPENDENCY,
				getUnexpectedOverriding3NewCallPattern());
		patterns.put(UNEXPECTED_OVERRIDING_3_NEW_CLASS,
				getUnexpectedOverriding3NewClassPattern());
		
		patterns.put(FIELD_HIDING, getFieldHidingPattern());
		
		patterns.put(ADD_METHOD_OVERRIDING_NEW_METHOD,
				getAddMethodOverridingNewMethodPattern());
		patterns.put(ADD_METHOD_OVERRIDING_NEW_CLASS,
				getAddMethodOverridingNewClassPattern());
		patterns.put(ADD_METHOD_OVERRIDING_NEW_CALL,
				getAddMethodOverridingNewCallPattern());
		
		patterns.put(OVERLOAD_BY_ADDITION_CLASS_EXISTS,
				getOverloadByAdditionClassExistsPattern());
		patterns.put(OVERLOAD_BY_ADDITION_CLASS_NEW,
				getOverloadByAdditionNewClassPattern());
		patterns.put(OVERLOAD_BY_ADDITION_CALL_NEW,
				getOverloadByAdditionNewCallPattern());
		
		patterns.put(OVERLOAD_BY_ACCESS_CHANGE_CLASS_EXISTS,
				getOverloadByAccessChangeClassExistsPattern());
		patterns.put(OVERLOAD_BY_ACCESS_CHANGE_CLASS_NEW,
				getOverloadByAccessChangeNewClassPattern());
		patterns.put(OVERLOAD_BY_ACCESS_CHANGE_CALL_NEW,
				getOverloadByAccessChangeNewCallPattern());
		
		patterns.put(OVERLOAD_BY_ACCESS_CHANGE_2_CLASS_EXISTS,
				getOverloadByAccessChange2ClassExistsPattern());
		patterns.put(OVERLOAD_BY_ACCESS_CHANGE_2_CLASS_NEW,
				getOverloadByAccessChange2NewClassPattern());
		patterns.put(OVERLOAD_BY_ACCESS_CHANGE_2_CALL_NEW,
				getOverloadByAccessChange2NewCallPattern());
		
		patterns.put(REMOVE_OVERRIDING, getRemoveOverridingPattern());
	}
	
	private ConflictPattern getParallelChangedPattern() {
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
		dp2.addActionPattern(new UpdateMethodPatternAction(methodPattern));

		ConflictPattern conflict = new ConflictPattern(PARALLEL_CHANGES);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classPattern);
		goal.addMethodToCall(classPattern, methodPattern);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getChangeMethodPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar1 = new FreeVariable(1);
		FreeVariable methodVar2 = new FreeVariable(2);
		FreeVariable methodVar3 = new FreeVariable(3);
		FreeVariable classVar2 = new FreeVariable(4);
		FreeVariable classVar3 = new FreeVariable(5);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		ClassPattern classPattern2 = new ClassPattern(classVar2);
		ClassPattern classPattern3 = new ClassPattern(classVar3);
		MethodPattern methodPattern1 = new MethodPattern(methodVar1, null);
		MethodPattern methodPattern2 = new MethodPattern(methodVar2, null);
		MethodPattern methodPattern3 = new MethodPattern(methodVar3, null);
		methodPattern1.addDependency(methodVar2);
		methodPattern1.addDependency(methodVar3);
		classPattern.addMethodPattern(methodPattern1);
		classPattern2.addMethodPattern(methodPattern2);
		classPattern3.addMethodPattern(methodPattern3);
		basePattern.addClassPattern(classPattern);
		basePattern.addClassPattern(classPattern2);
		basePattern.addClassPattern(classPattern3);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		dp1.addActionPattern(new UpdateMethodPatternAction(methodPattern2));
		dp2.addActionPattern(new UpdateMethodPatternAction(methodPattern3));

		ConflictPattern conflict = new ConflictPattern(CHANGE_METHOD);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addDifferentVariablesRule(methodVar2, methodVar3);
		conflict.addEqualVariableRule(classVar, classVar2);
		conflict.addEqualVariableRule(classVar, classVar3);
		conflict.addEqualVariableRule(classVar2, classVar3);
		
		TestingGoal goal = new TestingGoal(classPattern);
		goal.addMethodToCall(classPattern, methodPattern1);
		goal.addMethodToCall(classPattern2, methodPattern2);
		goal.addMethodToCall(classPattern3, methodPattern3);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getChangeMethod2Pattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable iVar = new FreeVariable(1);
		FreeVariable b1ClassVar = new FreeVariable(2);
		FreeVariable b2ClassVar = new FreeVariable(3);
		FreeVariable methodNVar = new FreeVariable(4);
		FreeVariable methodM1Var = new FreeVariable(5);
		FreeVariable methodM2Var = new FreeVariable(6);
		FreeVariable methodHashVar = new FreeVariable(7);
		FreeVariable fieldVar = new FreeVariable(8);

		BasePattern basePattern = new BasePattern();

		ClassPattern classAPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		TypePattern typePattern = new TypePattern(b1ClassVar);
		fieldPattern.setType(typePattern);
		classAPattern.addFieldPattern(fieldPattern);
		MethodPattern methodNPattern = new MethodPattern(methodNVar, null);
		methodNPattern.addDependency(methodM2Var);
		methodNPattern.addFieldAccessPattern(new FieldAccessPattern(fieldVar, FieldAccessType.READ));
		classAPattern.addMethodPattern(methodNPattern);
		MethodPattern methodM1Pattern = new MethodPattern(methodM1Var, null);
		methodM1Pattern.addDependency(methodHashVar);
		MethodPattern methodM2Pattern = new MethodPattern(methodM2Var, null);
		classAPattern.addMethodPattern(methodM1Pattern);
		classAPattern.addMethodPattern(methodM2Pattern);

		InterfacePattern interfacePattern = new InterfacePattern(iVar);

		ClassPattern classB1Pattern = new ClassPattern(b1ClassVar);
		ClassPattern classB2Pattern = new ClassPattern(b2ClassVar);
		InterfaceImplementationPattern iPattern = 
				new InterfaceImplementationPattern(iVar);
		classB1Pattern.addInterface(iPattern);
		classB2Pattern.addInterface(iPattern);
		MethodPattern methodHashPattern = new MethodPattern(methodHashVar, null);
		classB1Pattern.addMethodPattern(methodHashPattern);
		classB2Pattern.addExcludedMethod(methodHashVar);

		basePattern.addClassPattern(classAPattern);
		basePattern.addClassPattern(classB1Pattern);
		basePattern.addInterfacePattern(interfacePattern);
		basePattern.addClassPattern(classB2Pattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		TypePattern newTypePattern = new TypePattern(b2ClassVar);

		dp1.addActionPattern(
				new UpdateFieldTypePatternAction(fieldPattern, newTypePattern));

		dp2.addActionPattern(
				new UpdateDependencyPatternAction(methodNPattern, methodM2Var, methodM1Var));

		ConflictPattern conflict = new ConflictPattern(CHANGE_METHOD_2);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classAPattern);
		goal.addMethodToCall(classAPattern, methodNPattern);
		goal.addMethodToCall(classAPattern, methodM1Pattern);
		goal.addMethodToCall(classB2Pattern, methodHashPattern);
		conflict.setTestingGoal(goal);
		
		return conflict;
	}

	private ConflictPattern getDependencyBasedClassExistsPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar1 = new FreeVariable(1);
		FreeVariable holderClassVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		ClassPattern holderClassPattern = new ClassPattern(holderClassVar);
		MethodPattern methodPattern1 = new MethodPattern(methodVar1, null);
		classPattern.addMethodPattern(methodPattern1);
		basePattern.addClassPattern(classPattern);
		basePattern.addClassPattern(holderClassPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		MethodPattern insertedMethodPattern = 
				new MethodPattern(insertedMethodVar, null);
		insertedMethodPattern.addDependency(methodVar1);

		dp1.addActionPattern(
				new UpdateMethodPatternAction(methodPattern1));
		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern, 
						holderClassPattern));

		ConflictPattern conflict = new ConflictPattern(DEPENDENCY_BASED_CLASS_EXISTS);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addEqualVariableRule(classVar, holderClassVar);
		
		TestingGoal goal = new TestingGoal(holderClassPattern);
		goal.addMethodToCall(holderClassPattern, insertedMethodPattern);
		goal.addMethodToCall(classPattern, methodPattern1);
		conflict.setTestingGoal(goal);

		return conflict;
	}

	private ConflictPattern getDependencyBasedNewClassPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar1 = new FreeVariable(1);
		FreeVariable insertedClassVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern1 = new MethodPattern(methodVar1, null);
		classPattern.addMethodPattern(methodPattern1);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		ClassPattern insertedClassPattern = new ClassPattern(insertedClassVar);
		MethodPattern insertedMethodPattern = 
				new MethodPattern(insertedMethodVar, null);

		insertedMethodPattern.addDependency(methodVar1);
		insertedClassPattern.addMethodPattern(insertedMethodPattern);

		dp1.addActionPattern(new UpdateMethodPatternAction(methodPattern1));
		dp2.addActionPattern(new InsertClassPatternAction(insertedClassPattern));

		ConflictPattern conflict = new ConflictPattern(DEPENDENCY_BASED_CLASS_NEW);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(insertedClassPattern);
		goal.addMethodToCall(insertedClassPattern, insertedMethodPattern);
		goal.addMethodToCall(classPattern, methodPattern1);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getDependencyBasedCallAdditionPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable classVar3 = new FreeVariable(2);
		FreeVariable methodVar1 = new FreeVariable(3);
		FreeVariable methodVar2 = new FreeVariable(4);
		FreeVariable methodVar3 = new FreeVariable(5);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		ClassPattern classPattern2 = new ClassPattern(classVar2);
		ClassPattern classPattern3 = new ClassPattern(classVar3);
		MethodPattern methodPattern1 = new MethodPattern(methodVar1, null);
		MethodPattern methodPattern2 = new MethodPattern(methodVar2, null);
		methodPattern2.addDependency(methodVar1);
		MethodPattern methodPattern3 = new MethodPattern(methodVar3, null);
		classPattern.addMethodPattern(methodPattern1);
		classPattern2.addMethodPattern(methodPattern2);
		classPattern3.addMethodPattern(methodPattern3);
		basePattern.addClassPattern(classPattern);
		basePattern.addClassPattern(classPattern2);
		basePattern.addClassPattern(classPattern3);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		MethodInvocationPattern insertedInvocationPattern =
				new MethodInvocationPattern(methodVar2);

		dp1.addActionPattern(new UpdateMethodPatternAction(methodPattern1));
		dp2.addActionPattern(
				new InsertInvocationPatternAction(insertedInvocationPattern, methodPattern3));

		ConflictPattern conflict = new ConflictPattern(DEPENDENCY_BASED_DEPENDENCY_INSERT);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addEqualVariableRule(classVar, classVar2);
		conflict.addEqualVariableRule(classVar, classVar3);
		conflict.addEqualVariableRule(classVar2, classVar3);
		
		TestingGoal goal = new TestingGoal(classPattern3);
		goal.addMethodToCall(classPattern3, methodPattern3);
		goal.addMethodToCall(classPattern, methodPattern1);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getUnexpectedOverriding1NewMethodPattern() {
		FreeVariable classVar1 = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable insertedMethodVar = new FreeVariable(2);
		FreeVariable overideMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern1 = new ClassPattern(classVar1);
		ClassPattern classPattern2 = new ClassPattern(classVar2);

		classPattern2.addExcludedMethod(overideMethodVar);
		basePattern.addClassPattern(classPattern1);
		basePattern.addClassPattern(classPattern2);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		MethodPattern insertedMethodPattern1 = 
				new MethodPattern(insertedMethodVar, null);
		MethodPattern insertedMethodPattern2 = 
				new MethodPattern(overideMethodVar, Visibility.PUBLIC);
		insertedMethodPattern1.addDependency(overideMethodVar);

		dp1.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern1, classPattern1));
		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern2, classPattern2));

		ConflictPattern conflict = 
				new ConflictPattern(UNEXPECTED_OVERRIDING_NEW_METHOD);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classPattern1);
		goal.addMethodToCall(classPattern1, insertedMethodPattern1);
		goal.addMethodToCall(classPattern2, insertedMethodPattern2);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getUnexpectedOverriding1NewClassPattern() {
		FreeVariable classVar1 = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable insertedMethodVar = new FreeVariable(2);
		FreeVariable overideMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern2 = new ClassPattern(classVar2);

		classPattern2.addExcludedMethod(overideMethodVar);
		basePattern.addClassPattern(classPattern2);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		
		MethodPattern insertedMethodPattern2 = 
				new MethodPattern(overideMethodVar, Visibility.PUBLIC);
		
		ClassPattern classPattern1 = new ClassPattern(classVar1);
		MethodPattern insertedMethodPattern1 = 
				new MethodPattern(insertedMethodVar, null);
		insertedMethodPattern1.addDependency(overideMethodVar);
		classPattern1.addMethodPattern(insertedMethodPattern1);

		dp1.addActionPattern(
				new InsertClassPatternAction(classPattern1));
		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern2, classPattern2));

		ConflictPattern conflict = 
				new ConflictPattern(UNEXPECTED_OVERRIDING_NEW_CLASS);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classPattern1);
		goal.addMethodToCall(classPattern1, insertedMethodPattern1);
		goal.addMethodToCall(classPattern2, insertedMethodPattern2);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getUnexpectedOverriding1NewCallPattern() {
		FreeVariable classVar1 = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		FreeVariable overideMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern1 = new ClassPattern(classVar1);
		ClassPattern classPattern2 = new ClassPattern(classVar2);
		MethodPattern methodPattern = 
				new MethodPattern(methodVar, null);
		classPattern1.addMethodPattern(methodPattern);
		classPattern2.addExcludedMethod(overideMethodVar);
		basePattern.addClassPattern(classPattern1);
		basePattern.addClassPattern(classPattern2);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		
		ClassPattern holderClass = new ClassPattern(classVar2);
		MethodPattern insertedMethodPattern2 = 
				new MethodPattern(overideMethodVar, Visibility.PUBLIC);
		MethodInvocationPattern mip = new MethodInvocationPattern(overideMethodVar);
		InsertInvocationPatternAction iipa = 
				new InsertInvocationPatternAction(mip, methodPattern);

		dp1.addActionPattern(iipa);
		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern2, holderClass));

		ConflictPattern conflict = 
				new ConflictPattern(UNEXPECTED_OVERRIDING_NEW_CALL);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classPattern1);
		goal.addMethodToCall(classPattern1, methodPattern);
		goal.addMethodToCall(holderClass, insertedMethodPattern2);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getUnexpectedOverriding3NewMethodPattern() {
		FreeVariable classVar1 = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable classVar3 = new FreeVariable(2);
		FreeVariable methodVar = new FreeVariable(3);
		FreeVariable insertedMethod1 = new FreeVariable(4);
		FreeVariable insertedMethod2 = new FreeVariable(5);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern1 = new ClassPattern(classVar1);
		ClassPattern classPattern2 = new ClassPattern(classVar2);
		ClassPattern classPattern3 = new ClassPattern(classVar3);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		classPattern1.addMethodPattern(methodPattern);
		classPattern2.setSuperClass(classPattern1);
		basePattern.addClassPattern(classPattern2);
		basePattern.addClassPattern(classPattern3);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedMethodPattern1 = new MethodPattern(insertedMethod1, null);
		InsertMethodPatternAction impa = 
				new InsertMethodPatternAction(insertedMethodPattern1, classPattern2);
		impa.addCompatible(methodPattern);
		dp1.addActionPattern(impa);

		MethodPattern insertedMethodPattern2 = new MethodPattern(insertedMethod2, null);
		insertedMethodPattern2.addDependency(methodVar);
		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern2, classPattern3));

		ConflictPattern conflict = new ConflictPattern(UNEXPECTED_OVERRIDING_3_NEW_METHOD);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classPattern3);
		goal.addMethodToCall(classPattern3, insertedMethodPattern2);
		goal.addMethodToCall(classPattern2, insertedMethodPattern1);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getUnexpectedOverriding3NewClassPattern() {
		FreeVariable classVar1 = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable classVar3 = new FreeVariable(2);
		FreeVariable methodVar = new FreeVariable(3);
		FreeVariable insertedMethod1 = new FreeVariable(4);
		FreeVariable insertedMethod2 = new FreeVariable(5);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern1 = new ClassPattern(classVar1);
		ClassPattern classPattern2 = new ClassPattern(classVar2);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		classPattern1.addMethodPattern(methodPattern);
		classPattern2.setSuperClass(classPattern1);
		basePattern.addClassPattern(classPattern2);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedMethodPattern1 = new MethodPattern(insertedMethod1, null);
		InsertMethodPatternAction impa = 
				new InsertMethodPatternAction(insertedMethodPattern1, classPattern2);
		impa.addCompatible(methodPattern);
		dp1.addActionPattern(impa);

		ClassPattern classPattern3 = new ClassPattern(classVar3);
		MethodPattern insertedMethodPattern2 = new MethodPattern(insertedMethod2, null);
		insertedMethodPattern2.addDependency(methodVar);
		classPattern3.addMethodPattern(insertedMethodPattern2);
		dp2.addActionPattern(
				new InsertClassPatternAction(classPattern3));

		ConflictPattern conflict = 
				new ConflictPattern(UNEXPECTED_OVERRIDING_3_NEW_CLASS);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classPattern3);
		goal.addMethodToCall(classPattern3, insertedMethodPattern2);
		goal.addMethodToCall(classPattern2, insertedMethodPattern1);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getUnexpectedOverriding3NewCallPattern() {
		FreeVariable classVar1 = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable classVar3 = new FreeVariable(2);
		FreeVariable methodVar = new FreeVariable(3);
		FreeVariable methodVar2 = new FreeVariable(4);
		FreeVariable insertedMethod1 = new FreeVariable(5);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern1 = new ClassPattern(classVar1);
		ClassPattern classPattern2 = new ClassPattern(classVar2);
		ClassPattern classPattern3 = new ClassPattern(classVar3);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		MethodPattern methodPattern2 = new MethodPattern(methodVar2, null);
		classPattern1.addMethodPattern(methodPattern);
		classPattern2.setSuperClass(classPattern1);
		classPattern3.addMethodPattern(methodPattern2);


		basePattern.addClassPattern(classPattern1);
		basePattern.addClassPattern(classPattern2);
		basePattern.addClassPattern(classPattern3);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedMethodPattern1 = new MethodPattern(insertedMethod1, null);
		InsertMethodPatternAction impa = 
				new InsertMethodPatternAction(insertedMethodPattern1, classPattern2);
		impa.addCompatible(methodPattern);
		dp1.addActionPattern(impa);

		MethodInvocationPattern insertedInvocationPattern = 
				new MethodInvocationPattern(methodVar);
		dp2.addActionPattern(
				new InsertInvocationPatternAction(insertedInvocationPattern, methodPattern2));

		ConflictPattern conflict = new ConflictPattern(UNEXPECTED_OVERRIDING_3_NEW_DEPENDENCY);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addEqualVariableRule(classVar2, classVar3);
		
		TestingGoal goal = new TestingGoal(classPattern3);
		goal.addMethodToCall(classPattern3, methodPattern2);
		goal.addMethodToCall(classPattern2, insertedMethodPattern1);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getFieldHidingPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable fieldVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);
		FreeVariable insertedFieldVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		superClassPattern.addFieldPattern(fieldPattern);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedMethodPattern = 
				new MethodPattern(insertedMethodVar, null);
		FieldAccessPattern insertedAccessPattern = 
				new FieldAccessPattern(fieldVar, FieldAccessType.WRITE);
		insertedMethodPattern.addFieldAccessPattern(insertedAccessPattern);

		FieldPattern insertedFieldPattern = new FieldPattern(insertedFieldVar, null);


		dp1.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern, classPattern));

		dp2.addActionPattern(
				new InsertFieldPatternAction(insertedFieldPattern, classPattern));


		ConflictPattern conflict = new ConflictPattern(FIELD_HIDING);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addEqualVariableRule(fieldVar, insertedFieldVar);
		
		TestingGoal goal = new TestingGoal(classPattern);
		goal.addMethodToCall(classPattern, insertedMethodPattern);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getAddMethodOverridingNewMethodPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);
		FreeVariable holderClassVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		ClassPattern holderClassPattern = new ClassPattern(holderClassVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		superClassPattern.addMethodPattern(methodPattern);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);
		basePattern.addClassPattern(holderClassPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedMethodPattern1 = 
				new MethodPattern(insertedMethodVar, Visibility.PUBLIC);
		MethodPattern insertedMethodPattern2 = 
				new MethodPattern(methodVar, Visibility.PUBLIC);
		insertedMethodPattern1.addDependency(methodVar);

		dp1.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern1, holderClassPattern));

		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern2, classPattern));


		ConflictPattern conflict = new ConflictPattern(ADD_METHOD_OVERRIDING_NEW_METHOD);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addEqualVariableRule(classVar, holderClassVar);
		
		TestingGoal goal = new TestingGoal(holderClassPattern);
		goal.addMethodToCall(holderClassPattern, insertedMethodPattern1);
		goal.addMethodToCall(classPattern, insertedMethodPattern2);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getAddMethodOverridingNewCallPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		FreeVariable methodVar2 = new FreeVariable(3);
		FreeVariable holderClassVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		ClassPattern holderClassPattern = new ClassPattern(holderClassVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		MethodPattern methodPattern2 = new MethodPattern(methodVar2, null);
		superClassPattern.addMethodPattern(methodPattern);
		classPattern.setSuperClass(superClassPattern);
		holderClassPattern.addMethodPattern(methodPattern2);
		basePattern.addClassPattern(classPattern);
		basePattern.addClassPattern(holderClassPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodInvocationPattern mip = new MethodInvocationPattern(methodVar);
		InsertInvocationPatternAction iipa = 
				new InsertInvocationPatternAction(mip, methodPattern2);
		
		MethodPattern insertedMethodPattern2 = 
				new MethodPattern(methodVar, Visibility.PUBLIC);


		dp1.addActionPattern(iipa);
		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern2, classPattern));


		ConflictPattern conflict = 
				new ConflictPattern(ADD_METHOD_OVERRIDING_NEW_CALL);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addEqualVariableRule(classVar, holderClassVar);
		
		TestingGoal goal = new TestingGoal(holderClassPattern);
		goal.addMethodToCall(holderClassPattern, methodPattern2);
		goal.addMethodToCall(classPattern, insertedMethodPattern2);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getAddMethodOverridingNewClassPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);
		FreeVariable holderClassVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		superClassPattern.addMethodPattern(methodPattern);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		ClassPattern holderClassPattern = new ClassPattern(holderClassVar);
		MethodPattern insertedMethodPattern1 = 
				new MethodPattern(insertedMethodVar, Visibility.PUBLIC);
		holderClassPattern.addMethodPattern(insertedMethodPattern1);
		MethodPattern insertedMethodPattern2 = 
				new MethodPattern(methodVar, Visibility.PUBLIC);
		insertedMethodPattern1.addDependency(methodVar);

		dp1.addActionPattern(
				new InsertClassPatternAction(holderClassPattern));

		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern2, classPattern));


		ConflictPattern conflict = 
				new ConflictPattern(ADD_METHOD_OVERRIDING_NEW_CLASS);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addEqualVariableRule(classVar, holderClassVar);
		
		TestingGoal goal = new TestingGoal(holderClassPattern);
		goal.addMethodToCall(holderClassPattern, insertedMethodPattern1);
		goal.addMethodToCall(classPattern, insertedMethodPattern2);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getOverloadByAdditionClassExistsPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar = new FreeVariable(1);
		FreeVariable insertedMethodVar1 = new FreeVariable(2);
		FreeVariable insertedMethodVar2 = new FreeVariable(3);
		FreeVariable holderClassVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		ClassPattern holderClassPattern = new ClassPattern(holderClassVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);
		basePattern.addClassPattern(holderClassPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedMethodPattern1 = 
				new MethodPattern(insertedMethodVar1, Visibility.PUBLIC);
		MethodPattern insertedMethodPattern2 =
				new MethodPattern(insertedMethodVar2, Visibility.PUBLIC);
		insertedMethodPattern1.addDependency(methodVar);

		dp1.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern1, holderClassPattern));

		InsertMethodPatternAction impa = 
				new InsertMethodPatternAction(insertedMethodPattern2, classPattern);
		impa.addCompatible(methodPattern);

		dp2.addActionPattern(impa);

		ConflictPattern conflict = new ConflictPattern(OVERLOAD_BY_ADDITION_CLASS_EXISTS);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addEqualVariableRule(classVar, holderClassVar);
		
		TestingGoal goal = new TestingGoal(holderClassPattern);
		goal.addMethodToCall(holderClassPattern, insertedMethodPattern1);
		goal.addMethodToCall(classPattern, insertedMethodPattern2);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getOverloadByAdditionNewCallPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar = new FreeVariable(1);
		FreeVariable methodVar2 = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);
		FreeVariable holderClassVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		ClassPattern holderClassPattern = new ClassPattern(holderClassVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		MethodPattern methodPattern2 = new MethodPattern(methodVar2, null);
		classPattern.addMethodPattern(methodPattern);
		holderClassPattern.addMethodPattern(methodPattern2);
		basePattern.addClassPattern(classPattern);
		basePattern.addClassPattern(holderClassPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodInvocationPattern mip = new MethodInvocationPattern(methodVar);
		InsertInvocationPatternAction iipa = 
				new InsertInvocationPatternAction(mip, methodPattern2);

		MethodPattern insertedMethodPattern =
				new MethodPattern(insertedMethodVar, Visibility.PUBLIC);
		InsertMethodPatternAction impa = 
				new InsertMethodPatternAction(insertedMethodPattern, classPattern);
		impa.addCompatible(methodPattern);
		
		dp1.addActionPattern(iipa);
		dp2.addActionPattern(impa);

		ConflictPattern conflict = new ConflictPattern(OVERLOAD_BY_ADDITION_CALL_NEW);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addEqualVariableRule(classVar, holderClassVar);
		
		TestingGoal goal = new TestingGoal(holderClassPattern);
		goal.addMethodToCall(holderClassPattern, methodPattern2);
		goal.addMethodToCall(classPattern, insertedMethodPattern);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getOverloadByAdditionNewClassPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable methodVar = new FreeVariable(1);
		FreeVariable insertedMethodVar1 = new FreeVariable(2);
		FreeVariable insertedMethodVar2 = new FreeVariable(3);
		FreeVariable holderClassVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedCompatiblePattern =
				new MethodPattern(insertedMethodVar1, null);
		InsertMethodPatternAction impa = 
				new InsertMethodPatternAction(insertedCompatiblePattern, classPattern);
		impa.addCompatible(methodPattern);

		ClassPattern insertedClassPattern = new ClassPattern(holderClassVar);
		MethodPattern insertedMethodPattern = new MethodPattern(insertedMethodVar2, null);
		insertedClassPattern.addMethodPattern(insertedMethodPattern);

		dp1.addActionPattern(impa);
		dp2.addActionPattern(new InsertClassPatternAction(insertedClassPattern));

		ConflictPattern conflict = new ConflictPattern(OVERLOAD_BY_ADDITION_CLASS_NEW);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(insertedClassPattern);
		goal.addMethodToCall(insertedClassPattern, insertedMethodPattern);
		goal.addMethodToCall(classPattern, insertedCompatiblePattern);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getOverloadByAccessChangeClassExistsPattern() {
		FreeVariable holderClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable topMethodVar = new FreeVariable(2);
		FreeVariable subMethodVar = new FreeVariable(3);
		FreeVariable insertedMethodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern holderClassPattern = new ClassPattern(holderClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern topMethodPattern = 
				new MethodPattern(topMethodVar, Visibility.PUBLIC);
		MethodPattern subMethodPattern = 
				new MethodPattern(subMethodVar, Visibility.PRIVATE);
		holderClassPattern.addMethodPattern(topMethodPattern);
		holderClassPattern.addMethodPattern(subMethodPattern);
		holderClassPattern.addCompatible(subMethodVar, topMethodVar);
		basePattern.addClassPattern(holderClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedMethodPattern = 
				new MethodPattern(insertedMethodVar, Visibility.PUBLIC);
		insertedMethodPattern.addDependency(topMethodVar);

		dp1.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern, classPattern));

		dp2.addActionPattern(
				new VisibilityActionPattern(Action.UPDATE, Visibility.PUBLIC, 
						Visibility.PRIVATE, subMethodVar));

		ConflictPattern conflict = new ConflictPattern(OVERLOAD_BY_ACCESS_CHANGE_CLASS_EXISTS);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classPattern);
		goal.addMethodToCall(classPattern, insertedMethodPattern);
		goal.addMethodToCall(holderClassPattern, subMethodPattern);
		conflict.setTestingGoal(goal);

		return conflict;
	}

	private ConflictPattern getOverloadByAccessChangeNewClassPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable topMethodVar = new FreeVariable(2);
		FreeVariable subMethodVar = new FreeVariable(3);
		FreeVariable insertedMethodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		MethodPattern topMethodPattern = 
				new MethodPattern(topMethodVar, Visibility.PUBLIC);
		MethodPattern subMethodPattern = 
				new MethodPattern(subMethodVar, Visibility.PRIVATE);
		superClassPattern.addMethodPattern(topMethodPattern);
		superClassPattern.addMethodPattern(subMethodPattern);
		superClassPattern.addCompatible(subMethodVar, topMethodVar);
		basePattern.addClassPattern(superClassPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		ClassPattern insertedClassPattern = new ClassPattern(classVar);
		MethodPattern insertedMethodPattern = 
				new MethodPattern(insertedMethodVar, Visibility.PUBLIC);
		insertedMethodPattern.addDependency(topMethodVar);
		insertedClassPattern.addMethodPattern(insertedMethodPattern);

		dp1.addActionPattern(
				new InsertClassPatternAction(insertedClassPattern));

		dp2.addActionPattern(
				new VisibilityActionPattern(Action.UPDATE, Visibility.PUBLIC, 
						Visibility.PRIVATE, subMethodVar));

		ConflictPattern conflict = new ConflictPattern(OVERLOAD_BY_ACCESS_CHANGE_CLASS_NEW);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(insertedClassPattern);
		goal.addMethodToCall(insertedClassPattern, insertedMethodPattern);
		goal.addMethodToCall(superClassPattern, subMethodPattern);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getOverloadByAccessChangeNewCallPattern() {
		FreeVariable holderClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable topMethodVar = new FreeVariable(2);
		FreeVariable subMethodVar = new FreeVariable(3);
		FreeVariable methodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern holderClassPattern = new ClassPattern(holderClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern topMethodPattern = 
				new MethodPattern(topMethodVar, Visibility.PUBLIC);
		MethodPattern subMethodPattern = 
				new MethodPattern(subMethodVar, Visibility.PRIVATE);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		holderClassPattern.addMethodPattern(topMethodPattern);
		holderClassPattern.addMethodPattern(subMethodPattern);
		holderClassPattern.addCompatible(subMethodVar, topMethodVar);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(holderClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodInvocationPattern mip = new MethodInvocationPattern(topMethodVar);
		InsertInvocationPatternAction iipa = 
				new InsertInvocationPatternAction(mip, methodPattern);

		dp1.addActionPattern(iipa);

		dp2.addActionPattern(
				new VisibilityActionPattern(Action.UPDATE, Visibility.PUBLIC, 
						Visibility.PRIVATE, subMethodVar));

		ConflictPattern conflict = 
				new ConflictPattern(OVERLOAD_BY_ACCESS_CHANGE_CALL_NEW);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classPattern);
		goal.addMethodToCall(classPattern, methodPattern);
		goal.addMethodToCall(holderClassPattern, subMethodPattern);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getOverloadByAccessChange2ClassExistsPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable topMethodVar = new FreeVariable(2);
		FreeVariable subMethodVar = new FreeVariable(3);
		FreeVariable insertedMethodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern topMethodPattern = 
				new MethodPattern(topMethodVar, Visibility.PUBLIC);
		MethodPattern subMethodPattern = 
				new MethodPattern(subMethodVar, Visibility.PUBLIC);
		superClassPattern.addMethodPattern(topMethodPattern);
		superClassPattern.addMethodPattern(subMethodPattern);
		superClassPattern.addCompatible(subMethodVar, topMethodVar);
		basePattern.addClassPattern(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedMethodPattern = 
				new MethodPattern(insertedMethodVar, Visibility.PUBLIC);
		insertedMethodPattern.addDependency(subMethodVar);

		dp1.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern, classPattern));

		dp2.addActionPattern(
				new VisibilityActionPattern(Action.UPDATE, Visibility.PRIVATE, 
						Visibility.PUBLIC, subMethodVar));

		ConflictPattern conflict = new ConflictPattern(OVERLOAD_BY_ACCESS_CHANGE_2_CLASS_EXISTS);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classPattern);
		goal.addMethodToCall(classPattern, insertedMethodPattern);
		goal.addMethodToCall(superClassPattern, topMethodPattern);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getOverloadByAccessChange2NewClassPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable topMethodVar = new FreeVariable(2);
		FreeVariable subMethodVar = new FreeVariable(3);
		FreeVariable insertedMethodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		MethodPattern topMethodPattern = 
				new MethodPattern(topMethodVar, Visibility.PUBLIC);
		MethodPattern subMethodPattern = 
				new MethodPattern(subMethodVar, Visibility.PUBLIC);
		superClassPattern.addMethodPattern(topMethodPattern);
		superClassPattern.addMethodPattern(subMethodPattern);
		superClassPattern.addCompatible(subMethodVar, topMethodVar);
		basePattern.addClassPattern(superClassPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		ClassPattern insertedClassPattern = new ClassPattern(classVar);
		MethodPattern insertedMethodPattern = 
				new MethodPattern(insertedMethodVar, Visibility.PUBLIC);
		insertedMethodPattern.addDependency(subMethodVar);
		insertedClassPattern.addMethodPattern(insertedMethodPattern);

		dp1.addActionPattern(
				new InsertClassPatternAction(insertedClassPattern));

		dp2.addActionPattern(
				new VisibilityActionPattern(Action.UPDATE, Visibility.PRIVATE, 
						Visibility.PUBLIC, subMethodVar));

		ConflictPattern conflict = new ConflictPattern(OVERLOAD_BY_ACCESS_CHANGE_2_CLASS_NEW);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(insertedClassPattern);
		goal.addMethodToCall(insertedClassPattern, insertedMethodPattern);
		goal.addMethodToCall(superClassPattern, topMethodPattern);
		conflict.setTestingGoal(goal);

		return conflict;
	}
	
	private ConflictPattern getOverloadByAccessChange2NewCallPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable topMethodVar = new FreeVariable(2);
		FreeVariable subMethodVar = new FreeVariable(3);
		FreeVariable methodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern topMethodPattern = 
				new MethodPattern(topMethodVar, Visibility.PUBLIC);
		MethodPattern subMethodPattern = 
				new MethodPattern(subMethodVar, Visibility.PUBLIC);
		MethodPattern methodPattern =
				new MethodPattern(methodVar, null);
		superClassPattern.addMethodPattern(topMethodPattern);
		superClassPattern.addMethodPattern(subMethodPattern);
		superClassPattern.addCompatible(subMethodVar, topMethodVar);
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodInvocationPattern mip = new MethodInvocationPattern(subMethodVar);
		InsertInvocationPatternAction iipa =
				new InsertInvocationPatternAction(mip, methodPattern);

		dp1.addActionPattern(iipa);

		dp2.addActionPattern(
				new VisibilityActionPattern(Action.UPDATE, Visibility.PRIVATE, 
						Visibility.PUBLIC, subMethodVar));

		ConflictPattern conflict = 
				new ConflictPattern(OVERLOAD_BY_ACCESS_CHANGE_2_CALL_NEW);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classPattern);
		goal.addMethodToCall(classPattern, methodPattern);
		goal.addMethodToCall(superClassPattern, topMethodPattern);
		conflict.setTestingGoal(goal);

		return conflict;
	}

	private ConflictPattern getRemoveOverridingPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		superClassPattern.addMethodPattern(methodPattern);
		classPattern.addMethodPattern(methodPattern);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedMethodPattern = 
				new MethodPattern(insertedMethodVar, Visibility.PUBLIC);
		insertedMethodPattern.addDependency(methodVar);

		dp1.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern, classPattern));

		dp2.addActionPattern(
				new DeleteMethodPatternAction(methodPattern, classPattern));

		ConflictPattern conflict = new ConflictPattern(REMOVE_OVERRIDING);
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		
		TestingGoal goal = new TestingGoal(classPattern);
		goal.addMethodToCall(classPattern, insertedMethodPattern);
		goal.addMethodToCall(superClassPattern, methodPattern);
		conflict.setTestingGoal(goal);

		return conflict;
	}
}
