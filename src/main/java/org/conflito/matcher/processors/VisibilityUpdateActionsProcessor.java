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

public class VisibilityUpdateActionsProcessor extends DeltaProcessor {

  public VisibilityUpdateActionsProcessor(ConflictPattern conflictPattern) {
    super(conflictPattern);
  }

  public void visit(CtElement srcElement, CtElement dstElement) {
    if (getConflictPattern().hasVisibilityActions()) {
      if (srcElement.getParent() instanceof CtConstructorImpl) {
        CtConstructorImpl<?> srcImpl = (CtConstructorImpl<?>) srcElement.getParent();
        CtConstructorImpl<?> dstImpl = (CtConstructorImpl<?>) dstElement.getParent();
        visit(srcImpl, dstImpl);
      } else if (srcElement.getParent() instanceof CtFieldImpl) {
        CtFieldImpl<?> srcImpl = (CtFieldImpl<?>) srcElement.getParent();
        CtFieldImpl<?> dstImpl = (CtFieldImpl<?>) dstElement.getParent();
        visit(srcImpl, dstImpl);
      } else if (srcElement.getParent() instanceof CtMethodImpl) {
        CtMethodImpl<?> srcImpl = (CtMethodImpl<?>) srcElement.getParent();
        CtMethodImpl<?> dstImpl = (CtMethodImpl<?>) dstElement.getParent();
        visit(srcImpl, dstImpl);
      }
    }
  }

  private void visit(CtMethodImpl<?> srcImpl, CtMethodImpl<?> dstImpl) {
    MethodInstance dstMethodInstance = getMethodInstance(dstImpl);
    MethodInstance srcMethodInstance = getMethodInstance(srcImpl);
    Visibility oldVisibility = srcMethodInstance.getVisibility();
    Visibility newVisibility = dstMethodInstance.getVisibility();
    ActionInstance result = new VisibilityAction(Action.UPDATE, dstMethodInstance,
        oldVisibility, newVisibility);
    setResult(result);
  }

  private void visit(CtFieldImpl<?> srcImpl, CtFieldImpl<?> dstImpl) {
    FieldInstance dstFieldInstance = getFieldInstance(dstImpl);
    FieldInstance srcFieldInstance = getFieldInstance(srcImpl);
    Visibility oldVisibility = srcFieldInstance.getVisibility();
    Visibility newVisibility = dstFieldInstance.getVisibility();
    ActionInstance result = new VisibilityAction(Action.UPDATE, dstFieldInstance,
        oldVisibility, newVisibility);
    setResult(result);
  }

  private void visit(CtConstructorImpl<?> srcImpl, CtConstructorImpl<?> dstImpl) {
    if (srcImpl.getDeclaringType() instanceof CtClass ||
        srcImpl.getDeclaringType() instanceof CtClassImpl) {
      ClassInstance classInstance = getClassInstance(dstImpl.getTopLevelType());
      ConstructorInstance dstConstructorInstance = getConstructorInstance(dstImpl, classInstance);
      ConstructorInstance srcConstructorInstance = getConstructorInstance(srcImpl, classInstance);
      Visibility oldVisibility = srcConstructorInstance.getVisibility();
      Visibility newVisibility = dstConstructorInstance.getVisibility();
      ActionInstance result = new VisibilityAction(Action.UPDATE, dstConstructorInstance,
          oldVisibility, newVisibility);
      setResult(result);
    }
  }

}
