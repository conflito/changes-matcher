package matcher.entities.deltas;

public class UpdateAction extends ActionInstance {

	private Holder entity;

	public UpdateAction(Action action, Holder entity) {
		super(action);
		this.entity = entity;
	}

	public Holder getEntity() {
		return entity;
	}
}
