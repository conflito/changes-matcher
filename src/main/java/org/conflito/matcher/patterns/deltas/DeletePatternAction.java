package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.Action;

public abstract class DeletePatternAction extends ActionPattern {

  public DeletePatternAction() {
    super(Action.DELETE);
  }

}
