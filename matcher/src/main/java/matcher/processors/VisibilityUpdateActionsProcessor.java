package matcher.processors;

import java.util.Optional;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.FieldInstance;
import matcher.entities.MethodInstance;
import matcher.entities.Visibility;
import matcher.entities.deltas.Action;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.VisibilityAction;
import matcher.patterns.ConflictPattern;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtTypeMember;
import spoon.support.reflect.declaration.CtConstructorImpl;
import spoon.support.reflect.declaration.CtFieldImpl;
import spoon.support.reflect.declaration.CtMethodImpl;

public class VisibilityUpdateActionsProcessor {

	private ConflictPattern conflictPattern;
	
	private ClassProcessor classProcessor;
	private MethodProcessor methodProcessor;
	private ConstructorProcessor constructorProcessor;
	private FieldProcessor fieldProcessor;
	
	private ActionInstance result;
	
	public VisibilityUpdateActionsProcessor(ConflictPattern conflictPattern) {
		super();
		this.conflictPattern = conflictPattern;
		classProcessor = new ClassProcessor(conflictPattern);
		methodProcessor = new MethodProcessor(conflictPattern);
		constructorProcessor = new ConstructorProcessor(conflictPattern);
		fieldProcessor = new FieldProcessor();
	}
	
	public void visit(CtElement srcElement, CtElement dstElement) {
		if(conflictPattern.hasVisibilityActions()) {
			if(srcElement.getParent() instanceof CtConstructorImpl) {
				CtConstructorImpl<?> srcImpl = (CtConstructorImpl<?>)srcElement.getParent();
				CtConstructorImpl<?> dstImpl = (CtConstructorImpl<?>)dstElement.getParent();
				visit(srcImpl, dstImpl);
			}
			else if(srcElement.getParent() instanceof CtFieldImpl) {
				CtFieldImpl<?> srcImpl = (CtFieldImpl<?>)srcElement.getParent();
				CtFieldImpl<?> dstImpl = (CtFieldImpl<?>)dstElement.getParent();
				visit(srcImpl, dstImpl);
			}
			else if(srcElement.getParent() instanceof CtMethodImpl) {
				CtMethodImpl<?> srcImpl = (CtMethodImpl<?>)srcElement.getParent();
				CtMethodImpl<?> dstImpl = (CtMethodImpl<?>)dstElement.getParent();
				visit(srcImpl, dstImpl);
			}
		}
	}
	
	public Optional<ActionInstance> getResult() {
		return Optional.ofNullable(result);
	}
	
	private void visit(CtMethodImpl<?> srcImpl, CtMethodImpl<?> dstImpl) {
		ClassInstance classInstance = getClassInstance(dstImpl.getTopLevelType());
		MethodInstance dstMethodInstance = getMethodInstance(dstImpl, classInstance);
		MethodInstance srcMethodInstance = getMethodInstance(srcImpl, classInstance);
		Visibility oldVisibility = srcMethodInstance.getVisibility();
		Visibility newVisibility = dstMethodInstance.getVisibility();
		result = new VisibilityAction(Action.UPDATE, dstMethodInstance, oldVisibility, newVisibility);
	}
	
	private void visit(CtFieldImpl<?> srcImpl, CtFieldImpl<?> dstImpl) {
		ClassInstance classInstance = getClassInstance(dstImpl.getTopLevelType());
		FieldInstance dstFieldInstance = getFieldInstance(dstImpl, classInstance);
		FieldInstance srcFieldInstance = getFieldInstance(srcImpl, classInstance);
		Visibility oldVisibility = srcFieldInstance.getVisibility();
		Visibility newVisibility = dstFieldInstance.getVisibility();
		result = new VisibilityAction(Action.UPDATE, dstFieldInstance, oldVisibility, newVisibility);
	}
	
	private void visit(CtConstructorImpl<?> srcImpl, CtConstructorImpl<?> dstImpl) {
		ClassInstance classInstance = getClassInstance(dstImpl.getTopLevelType());
		ConstructorInstance dstConstructorInstance = getConstructorInstance(dstImpl, classInstance);
		ConstructorInstance srcConstructorInstance = getConstructorInstance(srcImpl, classInstance);
		Visibility oldVisibility = srcConstructorInstance.getVisibility();
		Visibility newVisibility = dstConstructorInstance.getVisibility();
		result = new VisibilityAction(Action.UPDATE, dstConstructorInstance, 
				oldVisibility, newVisibility);
	}	
	
	private ClassInstance getClassInstance(CtTypeMember member) {
		CtClass<?> holder = (CtClass<?>) member.getTopLevelType();
		classProcessor.process(holder);
		return classProcessor.getClassInstance();
	}
	
	private FieldInstance getFieldInstance(CtField<?> field, ClassInstance classInstance) {
		fieldProcessor.process(field);
		FieldInstance fieldInstance = fieldProcessor.getFieldInstance();
		fieldInstance.setClassInstance(classInstance);
		return fieldInstance;
	}
	
	private ConstructorInstance getConstructorInstance(CtConstructor<?> constructor, 
			ClassInstance classInstance) {
		constructorProcessor.process(constructor);
		ConstructorInstance constructorInstance = constructorProcessor.getConstructorInstance();
		constructorInstance.setClassInstance(classInstance);
		return constructorInstance;
	}
	
	private MethodInstance getMethodInstance(CtMethod<?> method, ClassInstance classInstance) {
		methodProcessor.process(method);
		MethodInstance methodInstance = methodProcessor.getMethodInstance();
		methodInstance.setClassInstance(classInstance);
		return methodInstance;
	}


}
