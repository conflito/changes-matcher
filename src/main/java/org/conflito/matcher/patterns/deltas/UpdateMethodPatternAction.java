package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.UpdateMethodAction;
import org.conflito.matcher.patterns.ClassPattern;
import org.conflito.matcher.patterns.MethodPattern;

public class UpdateMethodPatternAction extends UpdatePatternAction {

  private final MethodPattern updatedMethodPattern;

  private final ClassPattern classPattern;

  public UpdateMethodPatternAction(MethodPattern updatedMethodPattern,
      ClassPattern classPattern) {
    super();
    this.updatedMethodPattern = updatedMethodPattern;
    this.classPattern = classPattern;
  }

  public ActionPattern makeCopy() {
    return new UpdateMethodPatternAction(
        new MethodPattern(updatedMethodPattern),
        new ClassPattern(classPattern));
  }

  public int getUpdatedMethodVariableId() {
    return updatedMethodPattern.getVariableId();
  }

  @Override
  public void setVariableValue(int id, String value) {
    updatedMethodPattern.setVariableValue(id, value);
    classPattern.setVariableValue(id, value);
  }

  @Override
  public boolean filled() {
    return updatedMethodPattern.filled() && classPattern.filled();
  }

  @Override
  public boolean matches(ActionInstance action) {
    return action instanceof UpdateMethodAction && filled() &&
        matches((UpdateMethodAction) action);
  }

  private boolean matches(UpdateMethodAction action) {
    return getAction() == action.getAction() &&
        updatedMethodPattern.matches(action.getUpdatedMethodInstance()) &&
        classPattern.matches(action.getClassInstance());
  }

  @Override
  public void clean() {
    updatedMethodPattern.clean();
    classPattern.clean();
  }

  @Override
  public String toString() {
    return "Update method $" + updatedMethodPattern.getVariableId() +
        " of class " + classPattern.getVariableId();
  }

}
