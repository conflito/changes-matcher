package matcher.patterns.deltas;

import matcher.entities.deltas.Action;

public abstract class UpdatePatternAction extends ActionPattern {
	
	public UpdatePatternAction() {
		super(Action.UPDATE);
	}

}
