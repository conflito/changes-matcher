package matcher.processors;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.FieldInstance;
import matcher.entities.MethodInstance;
import matcher.entities.Visibility;
import matcher.entities.deltas.Action;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.VisibilityAction;
import matcher.patterns.ConflictPattern;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.declaration.CtConstructorImpl;
import spoon.support.reflect.declaration.CtFieldImpl;
import spoon.support.reflect.declaration.CtMethodImpl;

public class VisibilityInsertActionsProcessor extends DeltaProcessor{

	public VisibilityInsertActionsProcessor(ConflictPattern conflictPattern) {
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
		ClassInstance classInstance = getClassInstance(elementImpl.getTopLevelType());
		MethodInstance methodInstance = getMethodInstance(elementImpl, classInstance);
		Visibility newVisibility = methodInstance.getVisibility();
		ActionInstance result = new VisibilityAction(Action.INSERT, methodInstance, 
				null, newVisibility);
		setResult(result);
	}
	
	private void visit(CtFieldImpl<?> elementImpl) {
		ClassInstance classInstance = getClassInstance(elementImpl.getTopLevelType());
		FieldInstance fieldInstance = getFieldInstance(elementImpl, classInstance);
		Visibility newVisibility = fieldInstance.getVisibility();
		ActionInstance result = new VisibilityAction(Action.INSERT, fieldInstance, 
				null, newVisibility);
		setResult(result);
	}
	
	private void visit(CtConstructorImpl<?> elementImpl) {
		ClassInstance classInstance = getClassInstance(elementImpl.getTopLevelType());
		ConstructorInstance constructorInstance = 
				getConstructorInstance(elementImpl, classInstance);
		Visibility newVisibility = constructorInstance.getVisibility();
		ActionInstance result = new VisibilityAction(Action.INSERT, constructorInstance, 
				null, newVisibility);
		setResult(result);
	}

}
