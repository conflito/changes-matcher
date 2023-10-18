package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.Action;

public abstract class UpdatePatternAction extends ActionPattern {

  public UpdatePatternAction() {
    super(Action.UPDATE);
  }

}
