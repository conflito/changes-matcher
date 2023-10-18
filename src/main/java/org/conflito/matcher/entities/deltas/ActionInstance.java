package org.conflito.matcher.entities.deltas;

public abstract class ActionInstance {

  private final Action action;

  public ActionInstance(Action action) {
    this.action = action;
  }

  public Action getAction() {
    return action;
  }

}
