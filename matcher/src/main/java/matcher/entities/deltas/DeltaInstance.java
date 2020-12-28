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
		List<String> result = getInsertFieldsQualifiedNames();
		result.addAll(getDeleteFieldsQualifiedNames());
		return result;
	}

	private List<String> getInsertFieldsQualifiedNames() {
		return actions.stream()
					  .filter(a -> insertFieldAction(a))
					  .map(a -> ((InsertFieldAction) a).getInsertedEntity().getQualifiedName())
					  .collect(Collectors.toList());
	}
	
	private List<String> getDeleteFieldsQualifiedNames(){
		return actions.stream()
				  .filter(a -> deleteFieldAction(a))
				  .map(a -> ((DeleteFieldAction) a).getDeletedEntity().getQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private boolean deleteFieldAction(ActionInstance a) {
		return a instanceof DeleteFieldAction;
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
		List<String> result = getInsertConstructorsQualifiedNames();
		result.addAll(getDeleteConstructorsQualifiedNames());
		return result;
	}
	
	private List<String> getInsertConstructorsQualifiedNames() {
		return actions.stream()
				  .filter(a -> insertConstructorAction(a))
				  .map(a -> ((InsertConstructorAction) a).getInsertedEntity().getQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private List<String> getDeleteConstructorsQualifiedNames() {
		return actions.stream()
				  .filter(a -> deleteConstructorAction(a))
				  .map(a -> ((DeleteConstructorAction) a).getDeletedEntity().getQualifiedName())
				  .collect(Collectors.toList());
	}

	private boolean insertConstructorAction(ActionInstance a) {
		return a instanceof InsertConstructorAction;
	}
	
	private boolean deleteConstructorAction(ActionInstance a) {
		return a instanceof DeleteConstructorAction;
	}

	public List<String> getInvocationsQualifiedNames() {
		return getInsertInvocationsQualifiedNames();
	}

	private List<String> getInsertInvocationsQualifiedNames() {
		return actions.stream()
				  .filter(a -> insertInvocationAction(a))
				  .map(a -> ((InsertAction) a).getInsertedEntity().getQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private boolean insertInvocationAction(ActionInstance a) {
		return !insertFieldAction(a) && !insertConstructorAction(a) &&
				!insertMethodAction(a) && !insertFieldAccessAction(a) && a instanceof InsertAction;
	}
	
	public List<String> getFieldAccessesQualifiedNames() {
		List<String> result = getInsertFieldAccessesQualifiedNames();
		result.addAll(getDeleteFieldAccessesQualifiedNames());
		return result;
	}

	private List<String> getInsertFieldAccessesQualifiedNames() {
		return actions.stream()
				  .filter(a -> insertFieldAccessAction(a))
				  .map(a -> ((InsertFieldAccessAction) a).getInsertedEntity().getQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private List<String> getDeleteFieldAccessesQualifiedNames() {
		return actions.stream()
				  .filter(a -> deleteFieldAccessAction(a))
				  .map(a -> ((DeleteFieldAccessAction) a).getDeletedEntity().getQualifiedName())
				  .collect(Collectors.toList());
	}

	private boolean insertFieldAccessAction(ActionInstance a) {
		return a instanceof InsertFieldAccessAction;
	}
	
	private boolean deleteFieldAccessAction(ActionInstance a) {
		return a instanceof DeleteFieldAccessAction;
	}
}
