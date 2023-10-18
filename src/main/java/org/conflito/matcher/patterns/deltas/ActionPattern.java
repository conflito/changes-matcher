package org.conflito.matcher.patterns.deltas;

import java.util.List;
import org.conflito.matcher.entities.deltas.Action;
import org.conflito.matcher.entities.deltas.ActionInstance;

public abstract class ActionPattern {

  private final Action action;

  public ActionPattern(Action action) {
    super();
    this.action = action;
  }

  public Action getAction() {
    return action;
  }

  public abstract void setVariableValue(int id, String value);

  public abstract boolean filled();

  public abstract boolean matches(ActionInstance action);

  public abstract void clean();

  public boolean matchesOne(List<ActionInstance> actions) {
    return actions.stream().anyMatch(a -> matches(a));
  }

  public abstract ActionPattern makeCopy();
}
