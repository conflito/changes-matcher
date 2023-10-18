package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.DeleteConstructorAction;
import org.conflito.matcher.patterns.ClassPattern;
import org.conflito.matcher.patterns.ConstructorPattern;

public class DeleteConstructorPatternAction extends DeletePatternAction {

  private final ConstructorPattern deletedConstructorPattern;

  private final ClassPattern holderClassPattern;

  public DeleteConstructorPatternAction(ConstructorPattern insertedConstructorPattern,
      ClassPattern holderClassPattern) {
    super();
    this.deletedConstructorPattern = insertedConstructorPattern;
    this.holderClassPattern = holderClassPattern;
  }

  public ActionPattern makeCopy() {
    ConstructorPattern constructorCopy = new ConstructorPattern(deletedConstructorPattern);
    ClassPattern classCopy = new ClassPattern(holderClassPattern);
    return new DeleteConstructorPatternAction(constructorCopy, classCopy);
  }

  public int getDeletedConstructorVariableId() {
    return deletedConstructorPattern.getVariableId();
  }

  public boolean hasInvocations() {
    return deletedConstructorPattern.hasInvocations();
  }

  @Override
  public boolean matches(ActionInstance action) {
    return action instanceof DeleteConstructorAction && filled()
        && matches((DeleteConstructorAction) action);
  }

  private boolean matches(DeleteConstructorAction action) {
    return getAction() == action.getAction() &&
        deletedConstructorPattern.matches(action.getDeletedConstructor()) &&
        holderClassPattern.matches(action.getHolderClass());
  }

  @Override
  public void setVariableValue(int id, String value) {
    deletedConstructorPattern.setVariableValue(id, value);
    holderClassPattern.setVariableValue(id, value);
  }

  @Override
  public boolean filled() {
    return deletedConstructorPattern.filled() && holderClassPattern.filled();
  }

  @Override
  public void clean() {
    deletedConstructorPattern.clean();
    holderClassPattern.clean();
  }

  @Override
  public String toString() {
    String result = "Delete constructor $" + deletedConstructorPattern.getVariableId()
        + " from class $" + holderClassPattern.getVariableId();
    return result;
  }
}
