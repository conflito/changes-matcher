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

	public List<String> getMethodsQualifiedNames() {
		return getInsertMethodsQualifiedNames();
	}

	private List<String> getInsertMethodsQualifiedNames() {
		return actions.stream()
				  .filter(a -> insertMethodAction(a))
				  .map(a -> ((InsertMethodAction) a).getInsertedEntity().getQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private boolean insertMethodAction(ActionInstance a) {
		return a instanceof InsertMethodAction;
	}
	
	public List<String> getConstructorsQualifiedNames() {
		return getInsertConstructorsQualifiedNames();
	}
	
	private List<String> getInsertConstructorsQualifiedNames() {
		return actions.stream()
				  .filter(a -> insertConstructorAction(a))
				  .map(a -> ((InsertConstructorAction) a).getInsertedEntity().getQualifiedName())
				  .collect(Collectors.toList());
	}

	private boolean insertConstructorAction(ActionInstance a) {
		return a instanceof InsertConstructorAction;
	}
}
