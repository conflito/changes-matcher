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
	
	public List<ActionInstance> getActionInstances(){
		return actions;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(ActionInstance a: actions) {
			result.append(a.toString() + "\n");
		}
		return result.toString();
	}
	
}
