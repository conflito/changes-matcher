package org.conflito.matcher.patterns.deltas;

import org.conflito.matcher.entities.deltas.Action;

public abstract class InsertPatternAction extends ActionPattern {

  public InsertPatternAction() {
    super(Action.INSERT);
  }

}
