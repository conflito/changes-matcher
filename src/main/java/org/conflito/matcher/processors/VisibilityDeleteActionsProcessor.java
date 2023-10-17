package org.conflito.matcher.processors;

import org.conflito.matcher.entities.ClassInstance;
import org.conflito.matcher.entities.ConstructorInstance;
import org.conflito.matcher.entities.FieldInstance;
import org.conflito.matcher.entities.MethodInstance;
import org.conflito.matcher.entities.Visibility;
import org.conflito.matcher.entities.deltas.Action;
import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.VisibilityAction;
import org.conflito.matcher.patterns.ConflictPattern;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtConstructorImpl;
import spoon.support.reflect.declaration.CtFieldImpl;
import spoon.support.reflect.declaration.CtMethodImpl;

public class VisibilityDeleteActionsProcessor extends DeltaProcessor {

	public VisibilityDeleteActionsProcessor(ConflictPattern conflictPattern) {
		super(conflictPattern);
	}

	public void visit(CtElement element) {
		if(getConflictPattern().hasVisibilityActions()) {
			if(element.getParent() instanceof CtConstructorImpl) {
				CtConstructorImpl<?> elementImpl = (CtConstructorImpl<?>)element.getParent();
				visit(elementImpl);
			}
			else if(element.getParent() instanceof CtFieldImpl) {
				CtFieldImpl<?> elementImpl = (CtFieldImpl<?>)element.getParent();
				visit(elementImpl);
			}
			else if(element.getParent() instanceof CtMethodImpl) {
				CtMethodImpl<?> elementImpl = (CtMethodImpl<?>)element.getParent();
				visit(elementImpl);
			}
		}
	}
	
	private void visit(CtMethodImpl<?> elementImpl) {
		MethodInstance methodInstance = getMethodInstance(elementImpl);
		Visibility visibility = methodInstance.getVisibility();
		ActionInstance result = new VisibilityAction(Action.DELETE, methodInstance,
				visibility, null);
		setResult(result);
	}
	
	private void visit(CtFieldImpl<?> elementImpl) {
		FieldInstance fieldInstance = getFieldInstance(elementImpl);
		Visibility visibility = fieldInstance.getVisibility();
		ActionInstance result = new VisibilityAction(Action.DELETE, fieldInstance, 
				visibility, null);
		setResult(result);
	}
	
	private void visit(CtConstructorImpl<?> elementImpl) {
		if(elementImpl.getDeclaringType() instanceof CtClass ||
				elementImpl.getDeclaringType() instanceof CtClassImpl) {
			ClassInstance classInstance = getClassInstance(elementImpl);
			ConstructorInstance constructorInstance =
					getConstructorInstance(elementImpl, classInstance);
			Visibility visibility = constructorInstance.getVisibility();
			ActionInstance result = new VisibilityAction(Action.DELETE, constructorInstance, 
					visibility, null);
			setResult(result);
		}
		
	}
}
