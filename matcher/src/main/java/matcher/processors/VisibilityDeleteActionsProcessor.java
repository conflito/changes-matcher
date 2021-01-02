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
		ClassInstance classInstance = getClassInstance(elementImpl.getTopLevelType());
		ConstructorInstance constructorInstance = 
				getConstructorInstance(elementImpl, classInstance);
		Visibility visibility = constructorInstance.getVisibility();
		ActionInstance result = new VisibilityAction(Action.DELETE, constructorInstance, 
				visibility, null);
		setResult(result);
	}
}
