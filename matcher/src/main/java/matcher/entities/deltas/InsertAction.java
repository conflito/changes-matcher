package matcher.entities.deltas;

public abstract class InsertAction extends ActionInstance {

	public InsertAction() {
		super(Action.INSERT);
	}
		
	public abstract String toString();
}
