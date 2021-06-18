package matcher.patterns.deltas;

import matcher.entities.deltas.Action;

public abstract class InsertPatternAction extends ActionPattern {

	public InsertPatternAction() {
		super(Action.INSERT);
	}

}
