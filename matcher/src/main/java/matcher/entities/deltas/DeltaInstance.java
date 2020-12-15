package matcher.entities.deltas;

import java.util.ArrayList;
import java.util.List;

public class DeltaInstance {

	private List<ActionInstance> actions;

	public DeltaInstance() {
		super();
		actions = new ArrayList<>();
	}
	
	public void addActionInstance(ActionInstance action) {
		this.actions.add(action);
	}
	
	public Iterable<ActionInstance> getActionInstances(){
		return actions;
	}
	
}
