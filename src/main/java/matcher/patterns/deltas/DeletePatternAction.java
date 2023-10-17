package matcher.patterns.deltas;

import matcher.entities.deltas.Action;

public abstract class DeletePatternAction extends ActionPattern{

	public DeletePatternAction() {
		super(Action.DELETE);
	}
	
}
