package matcher.catalogs;

import java.util.ArrayList;
import java.util.List;

import matcher.entities.FieldAccessType;
import matcher.entities.Visibility;
import matcher.entities.deltas.Action;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FieldAccessPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.InterfaceImplementationPattern;
import matcher.patterns.InterfacePattern;
import matcher.patterns.MethodInvocationPattern;
import matcher.patterns.MethodPattern;
import matcher.patterns.TypePattern;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertClassPatternAction;
import matcher.patterns.deltas.InsertFieldPatternAction;
import matcher.patterns.deltas.InsertInvocationPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.patterns.deltas.UpdateFieldTypePatternAction;
import matcher.patterns.deltas.UpdateMethodPatternAction;
import matcher.patterns.deltas.VisibilityActionPattern;

public class ConflictPatternCatalog {

	private List<ConflictPattern> patterns;
	
	public ConflictPatternCatalog() {
		patterns = new ArrayList<>();
		loadPatterns();
	}
	
	public Iterable<ConflictPattern> getPatterns(){
		return patterns;
	}
	
	private void loadPatterns() {
		patterns.add(getParallelChangedPattern());
		patterns.add(getChangeMethodPattern());
		patterns.add(getDependencyBasedClassExistsPattern());
		patterns.add(getDependencyBasedNewClassPattern());
		patterns.add(getDependencyBasedCallAdditionPattern());
		patterns.add(getDependencyBasedFieldTypeUpdateClassExistsPattern());
		patterns.add(getDependencyBasedFieldTypeUpdateNewClassPattern());
		patterns.add(getUnexpectedOverriding1Pattern());
		patterns.add(getUnexpectedOverriding2Pattern());
		patterns.add(getUnexpectedOverriding3NewMethodPattern());
		patterns.add(getUnexpectedOverriding3NewCallPattern());
		patterns.add(getFieldHidingPattern());
		patterns.add(getAddMethodOveridingPattern());
		patterns.add(getAddMethodOverriding2Pattern());
		patterns.add(getOverloadByAdditionClassExistsPattern());
		patterns.add(getOverloadByAdditionNewClassPattern());
		patterns.add(getOverloadByAccessChangeClassExistsPattern());
		patterns.add(getOverloadByAccessChangeNewClassPattern());
		patterns.add(getOverloadByAccessChange2ClassExistsPattern());
		patterns.add(getOverloadByAccessChange2NewClassPattern());		
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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addDifferentVariablesRule(methodVar2, methodVar3);

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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}
	
	private ConflictPattern getDependencyBasedFieldTypeUpdateClassExistsPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		FreeVariable holderClassVar = new FreeVariable(3);
		FreeVariable insertedMethodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		ClassPattern holderClassPattern = new ClassPattern(holderClassVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		classPattern.addFieldPattern(fieldPattern);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		methodPattern.addFieldAccessPattern(
				new FieldAccessPattern(fieldVar, FieldAccessType.READ));
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);
		basePattern.addClassPattern(holderClassPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedMethodPattern = 
				new MethodPattern(insertedMethodVar, null);
		insertedMethodPattern.addDependency(methodVar);

		dp1.addActionPattern(new UpdateFieldTypePatternAction(fieldPattern));
		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern, 
						holderClassPattern));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}
	
	private ConflictPattern getDependencyBasedFieldTypeUpdateNewClassPattern() {
		FreeVariable classVar = new FreeVariable(0);
		FreeVariable fieldVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		FreeVariable insertedClassVar = new FreeVariable(3);
		FreeVariable insertedMethodVar = new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(classVar);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, null);
		classPattern.addFieldPattern(fieldPattern);
		MethodPattern methodPattern = new MethodPattern(methodVar, null);
		methodPattern.addFieldAccessPattern(
				new FieldAccessPattern(fieldVar, FieldAccessType.READ));
		classPattern.addMethodPattern(methodPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		ClassPattern insertedClassPattern = new ClassPattern(insertedClassVar);
		MethodPattern insertedMethodPattern = 
				new MethodPattern(insertedMethodVar, null);
		insertedMethodPattern.addDependency(methodVar);
		insertedClassPattern.addMethodPattern(insertedMethodPattern);

		dp1.addActionPattern(new UpdateFieldTypePatternAction(fieldPattern));
		dp2.addActionPattern(new InsertClassPatternAction(insertedClassPattern));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}
	
	private ConflictPattern getUnexpectedOverriding1Pattern() {
		FreeVariable classVar1 = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable fieldVar1 = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);
		FreeVariable overideMethodVar = new FreeVariable(4);
		FreeVariable iVar = new FreeVariable(5);

		BasePattern basePattern = new BasePattern();
		InterfacePattern iPattern = new InterfacePattern(iVar);
		ClassPattern classPattern1 = new ClassPattern(classVar1);
		ClassPattern classPattern2 = new ClassPattern(classVar2);
		FieldPattern fieldPattern = new FieldPattern(fieldVar1, null);
		TypePattern typePattern = new TypePattern(iVar);
		fieldPattern.setType(typePattern);
		classPattern1.addFieldPattern(fieldPattern);
		classPattern2.addInterface(new InterfaceImplementationPattern(iVar));
		classPattern2.addExcludedMethod(overideMethodVar);
		basePattern.addInterfacePattern(iPattern);
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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}
	
	private ConflictPattern getUnexpectedOverriding2Pattern() {
		FreeVariable classVar1 = new FreeVariable(0);
		FreeVariable classVar2 = new FreeVariable(1);
		FreeVariable classVar3 = new FreeVariable(2);
		FreeVariable fieldVar1 = new FreeVariable(3);
		FreeVariable methodVar1 = new FreeVariable(4);
		FreeVariable insertedMethod = new FreeVariable(5);
		FreeVariable iVar = new FreeVariable(6);

		BasePattern basePattern = new BasePattern();
		InterfacePattern iPattern = new InterfacePattern(iVar);
		ClassPattern classPattern1 = new ClassPattern(classVar1);
		ClassPattern classPattern2 = new ClassPattern(classVar2);
		ClassPattern classPattern3 = new ClassPattern(classVar3);
		MethodPattern methodPattern = new MethodPattern(methodVar1, null);
		FieldPattern fieldPattern = new FieldPattern(fieldVar1, null);
		TypePattern typePattern = new TypePattern(iVar);
		fieldPattern.setType(typePattern);
		classPattern1.addFieldPattern(fieldPattern);
		classPattern2.addInterface(new InterfaceImplementationPattern(iVar));
		classPattern2.addMethodPattern(methodPattern);
		classPattern3.addExcludedMethod(methodVar1);
		classPattern3.setSuperClass(classPattern2);
		basePattern.addInterfacePattern(iPattern);
		basePattern.addClassPattern(classPattern1);
		basePattern.addClassPattern(classPattern3);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();
		MethodPattern insertedMethodPattern1 =
				new MethodPattern(insertedMethod, null);
		insertedMethodPattern1.addDependency(methodVar1);

		ClassPattern holder = new ClassPattern(classVar3);
		holder.setSuperClass(classPattern2);
		MethodPattern insertedMethodPattern2 =
				new MethodPattern(methodVar1, null);

		dp1.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern1, classPattern1));
		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern2, holder));

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);
		conflict.addDifferentVariablesRule(classVar1, classVar3);

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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}
	
	private ConflictPattern getFieldHidingPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable fieldVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);

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

		FieldPattern insertedFieldPattern = new FieldPattern(fieldVar, null);


		dp1.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern, classPattern));

		dp2.addActionPattern(
				new InsertFieldPatternAction(insertedFieldPattern, classPattern));


		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}
	
	private ConflictPattern getAddMethodOveridingPattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable cVar = new FreeVariable(2);
		FreeVariable methodVar = new FreeVariable(3);
		FreeVariable fieldVar =  new FreeVariable(4);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PACKAGE);
		ConstructorPattern cPattern = new ConstructorPattern(cVar, Visibility.PACKAGE);
		FieldPattern fieldPattern = new FieldPattern(fieldVar, Visibility.PACKAGE);
		superClassPattern.addConstructorPattern(cPattern);
		superClassPattern.addMethodPattern(methodPattern);
		classPattern.addFieldPattern(fieldPattern);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodInvocationPattern insertedInvocationPattern = 
				new MethodInvocationPattern(methodVar);
		MethodPattern insertedMethodPattern = 
				new MethodPattern(methodVar, Visibility.PACKAGE);
		FieldAccessPattern insertedAccessPattern = 
				new FieldAccessPattern(fieldVar, FieldAccessType.WRITE);
		insertedMethodPattern.addFieldAccessPattern(insertedAccessPattern);

		dp1.addActionPattern(
				new InsertInvocationPatternAction(insertedInvocationPattern, cPattern));
		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern, classPattern));


		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}
	
	private ConflictPattern getAddMethodOverriding2Pattern() {
		FreeVariable superClassVar = new FreeVariable(0);
		FreeVariable classVar = new FreeVariable(1);
		FreeVariable methodVar = new FreeVariable(2);
		FreeVariable insertedMethodVar = new FreeVariable(3);

		BasePattern basePattern = new BasePattern();
		ClassPattern superClassPattern = new ClassPattern(superClassVar);
		ClassPattern classPattern = new ClassPattern(classVar);
		MethodPattern methodPattern = new MethodPattern(methodVar, Visibility.PUBLIC);
		superClassPattern.addMethodPattern(methodPattern);
		classPattern.setSuperClass(superClassPattern);
		basePattern.addClassPattern(classPattern);

		DeltaPattern dp1 = new DeltaPattern();
		DeltaPattern dp2 = new DeltaPattern();

		MethodPattern insertedMethodPattern1 = 
				new MethodPattern(insertedMethodVar, Visibility.PUBLIC);
		MethodPattern insertedMethodPattern2 = 
				new MethodPattern(methodVar, Visibility.PUBLIC);
		insertedMethodPattern1.addDependency(methodVar);

		dp1.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern1, classPattern));

		dp2.addActionPattern(
				new InsertMethodPatternAction(insertedMethodPattern2, classPattern));


		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}
	
	private ConflictPattern getOverloadByAccessChangeClassExistsPattern() {
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
				new MethodPattern(subMethodVar, Visibility.PRIVATE);
		superClassPattern.addMethodPattern(topMethodPattern);
		superClassPattern.addMethodPattern(subMethodPattern);
		superClassPattern.addCompatible(subMethodVar, topMethodVar);
		classPattern.setSuperClass(superClassPattern);
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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

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
		classPattern.setSuperClass(superClassPattern);
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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

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

		ConflictPattern conflict = new ConflictPattern();
		conflict.setBasePattern(basePattern);
		conflict.setFirstDeltaPattern(dp1);
		conflict.setSecondDeltaPattern(dp2);

		return conflict;
	}
}
