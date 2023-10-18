package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.DeleteMethodAction;
import org.conflito.matcher.patterns.ClassPattern;
import org.conflito.matcher.patterns.MethodPattern;

public class DeleteMethodPatternAction extends DeletePatternAction {

  private final MethodPattern deletedMethodPattern;

  private final ClassPattern holderClassPattern;

  public DeleteMethodPatternAction(MethodPattern deletedMethodPattern,
      ClassPattern holderClassPattern) {
    super();
    this.deletedMethodPattern = deletedMethodPattern;
    this.holderClassPattern = holderClassPattern;
  }

  public ActionPattern makeCopy() {
    MethodPattern copyMethod = new MethodPattern(deletedMethodPattern);
    ClassPattern copyClass = new ClassPattern(holderClassPattern);
    return new DeleteMethodPatternAction(copyMethod, copyClass);
  }

  public int getDeletedMethodVariableId() {
    return deletedMethodPattern.getVariableId();
  }

  public boolean hasInvocations() {
    return deletedMethodPattern.hasInvocations();
  }

  public boolean hasFieldAccesses() {
    return deletedMethodPattern.hasFieldAccesses();
  }

  @Override
  public boolean matches(ActionInstance action) {
    return action instanceof DeleteMethodAction && filled() && matches((DeleteMethodAction) action);
  }

  private boolean matches(DeleteMethodAction action) {
    return getAction() == action.getAction() &&
        deletedMethodPattern.matches(action.getDeletedMethod()) &&
        holderClassPattern.matches(action.getHolderClass());
  }

  @Override
  public void setVariableValue(int id, String value) {
    deletedMethodPattern.setVariableValue(id, value);
    holderClassPattern.setVariableValue(id, value);
  }

  @Override
  public void clean() {
    deletedMethodPattern.clean();
    holderClassPattern.clean();
  }

  @Override
  public boolean filled() {
    return deletedMethodPattern.filled() && holderClassPattern.filled();
  }

  @Override
  public String toString() {
    String result = "Delete method $" + deletedMethodPattern.getVariableId()
        + " from class $" + holderClassPattern.getVariableId();
    return result;
  }

}
