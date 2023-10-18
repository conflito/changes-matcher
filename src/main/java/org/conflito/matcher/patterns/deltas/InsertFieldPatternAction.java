package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.Visibility;
import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.InsertFieldAction;
import org.conflito.matcher.patterns.ClassPattern;
import org.conflito.matcher.patterns.FieldPattern;

public class InsertFieldPatternAction extends InsertPatternAction {

  private final FieldPattern insertedFieldPattern;

  private final ClassPattern holderClassPattern;

  public InsertFieldPatternAction(FieldPattern insertedFieldPattern,
      ClassPattern holderClassPattern) {
    super();
    this.insertedFieldPattern = insertedFieldPattern;
    this.holderClassPattern = holderClassPattern;
  }

  public ActionPattern makeCopy() {
    FieldPattern fieldCopy = new FieldPattern(insertedFieldPattern);
    ClassPattern classCopy = new ClassPattern(holderClassPattern);
    return new InsertFieldPatternAction(fieldCopy, classCopy);
  }

  public int getInsertedFieldVariableId() {
    return insertedFieldPattern.getVariableId();
  }

  @Override
  public boolean filled() {
    return insertedFieldPattern.filled() && holderClassPattern.filled();
  }

  @Override
  public boolean matches(ActionInstance action) {
    return action instanceof InsertFieldAction && filled()
        && matches((InsertFieldAction) action);
  }

  private boolean matches(InsertFieldAction action) {
    return getAction() == action.getAction() &&
        insertedFieldPattern.matches(action.getInsertedField()) &&
        holderClassPattern.matches(action.getHolderClass());
  }

  @Override
  public void setVariableValue(int id, String value) {
    insertedFieldPattern.setVariableValue(id, value);
    holderClassPattern.setVariableValue(id, value);
  }

  @Override
  public void clean() {
    insertedFieldPattern.clean();
    holderClassPattern.clean();
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    Visibility visibility = insertedFieldPattern.getVisibility();
    result.append("Insert" + (visibility == null ? " " :
        " " + visibility.toString().toLowerCase() + " "));
    result.append("field $" + insertedFieldPattern.getVariableId());
    result.append(" in class $" + holderClassPattern.getVariableId());
    return result.toString();
  }


}
