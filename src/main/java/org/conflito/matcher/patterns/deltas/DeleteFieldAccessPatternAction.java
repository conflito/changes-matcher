package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.DeleteFieldAccessAction;
import org.conflito.matcher.patterns.FieldAccessPattern;
import org.conflito.matcher.patterns.MethodPattern;

public class DeleteFieldAccessPatternAction extends DeletePatternAction {

  private final FieldAccessPattern deletedFieldAccessPattern;

  private final MethodPattern holderMethodPattern;

  public DeleteFieldAccessPatternAction(FieldAccessPattern deletedFieldAccessPattern,
      MethodPattern holderMethodPattern) {
    super();
    this.deletedFieldAccessPattern = deletedFieldAccessPattern;
    this.holderMethodPattern = holderMethodPattern;
  }

  public ActionPattern makeCopy() {
    FieldAccessPattern fieldAccessCopy = new FieldAccessPattern(deletedFieldAccessPattern);
    MethodPattern methodCopy = new MethodPattern(holderMethodPattern);
    return new DeleteFieldAccessPatternAction(fieldAccessCopy, methodCopy);
  }

  public int getDeletedFieldAccessVariableId() {
    return deletedFieldAccessPattern.getVariableId();
  }


  @Override
  public boolean matches(ActionInstance action) {
    return action instanceof DeleteFieldAccessAction && filled()
        && matches((DeleteFieldAccessAction) action);
  }

  private boolean matches(DeleteFieldAccessAction action) {
    return getAction() == action.getAction() &&
        deletedFieldAccessPattern.matches(action.getDeletedFieldAccess()) &&
        holderMethodPattern.matches(action.getHolderMethod());
  }

  @Override
  public void setVariableValue(int id, String value) {
    deletedFieldAccessPattern.setVariableValue(id, value);
    holderMethodPattern.setVariableValue(id, value);
  }

  @Override
  public boolean filled() {
    return deletedFieldAccessPattern.filled() && holderMethodPattern.filled();
  }

  @Override
  public void clean() {
    deletedFieldAccessPattern.clean();
    holderMethodPattern.clean();
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("Delete field ");
    if (deletedFieldAccessPattern.isFieldRead()) {
      result.append("read ");
    } else if (deletedFieldAccessPattern.isFieldWrite()) {
      result.append("write ");
    } else {
      result.append("use ");
    }
    result.append("of field $" + deletedFieldAccessPattern.getVariableId());
    result.append(" in method " + holderMethodPattern.getVariableId());
    return result.toString();
  }
}
