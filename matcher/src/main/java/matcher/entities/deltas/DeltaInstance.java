package matcher.entities.deltas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	public List<String> getFieldsQualifiedNames() {
		return getInsertFieldsQualifiedNames();
	}

	private List<String> getInsertFieldsQualifiedNames() {
		return actions.stream()
					  .filter(a -> insertFieldAction(a))
					  .map(a -> ((InsertFieldAction) a).getInsertedEntity().getQualifiedName())
					  .collect(Collectors.toList());
	}
	
	private boolean insertFieldAction(ActionInstance a) {
		return a instanceof InsertFieldAction;
	}
}
