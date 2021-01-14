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
	
	public List<String> getClassesQualifiedNames() {
		return actions.stream()
					  .filter(a -> isInsertClassAction(a))
					  .map(a ->((InsertClassAction) a).getInsertedEntityQualifiedName())
					  .collect(Collectors.toList());
	}
	
	private boolean isInsertClassAction(ActionInstance a) {
		return a instanceof InsertClassAction;
	}

	public List<String> getFieldsQualifiedNames() {
		List<String> result = getInsertedFieldsQualifiedNames();
		result.addAll(getDeletedFieldsQualifiedNames());
		return result;
	}

	private List<String> getInsertedFieldsQualifiedNames() {
		return actions.stream()
					  .filter(a -> isInsertFieldAction(a))
					  .map(a -> ((InsertFieldAction) a).getInsertedEntityQualifiedName())
					  .collect(Collectors.toList());
	}
	
	private List<String> getDeletedFieldsQualifiedNames(){
		return actions.stream()
				  .filter(a -> isDeleteFieldAction(a))
				  .map(a -> ((DeleteFieldAction) a).getDeletedEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private boolean isDeleteFieldAction(ActionInstance a) {
		return a instanceof DeleteFieldAction;
	}

	private boolean isInsertFieldAction(ActionInstance a) {
		return a instanceof InsertFieldAction;
	}

	public List<String> getMethodsQualifiedNames() {
		List<String> result = getInsertedMethodsQualifiedNames();
		result.addAll(getDeletedMethodsQualifiedNames());
		return result;
	}

	private List<String> getInsertedMethodsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isInsertMethodAction(a))
				  .map(a -> ((InsertMethodAction) a).getInsertedEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private List<String> getDeletedMethodsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isDeleteMethodAction(a))
				  .map(a -> ((DeleteMethodAction) a).getDeletedEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private boolean isInsertMethodAction(ActionInstance a) {
		return a instanceof InsertMethodAction;
	}
	
	private boolean isDeleteMethodAction(ActionInstance a) {
		return a instanceof DeleteMethodAction;
	}
	
	public List<String> getConstructorsQualifiedNames() {
		List<String> result = getInsertedConstructorsQualifiedNames();
		result.addAll(getDeletedConstructorsQualifiedNames());
		return result;
	}
	
	private List<String> getInsertedConstructorsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isInsertConstructorAction(a))
				  .map(a -> ((InsertConstructorAction) a).getInsertedEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private List<String> getDeletedConstructorsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isDeleteConstructorAction(a))
				  .map(a -> ((DeleteConstructorAction) a).getDeletedEntityQualifiedName())
				  .collect(Collectors.toList());
	}

	private boolean isInsertConstructorAction(ActionInstance a) {
		return a instanceof InsertConstructorAction;
	}
	
	private boolean isDeleteConstructorAction(ActionInstance a) {
		return a instanceof DeleteConstructorAction;
	}

	public List<String> getInvocationsQualifiedNames() {
		List<String> result = getInsertedInvocationsQualifiedNames();
		result.addAll(getDeletedInvocationsQualifiedNames());
		return result;
	}

	private List<String> getInsertedInvocationsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isInsertInvocationAction(a))
				  .map(a -> ((InsertAction) a).getInsertedEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private List<String> getDeletedInvocationsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isDeleteInvocationAction(a))
				  .map(a -> ((DeleteAction) a).getDeletedEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private boolean isInsertInvocationAction(ActionInstance a) {
		return !isInsertFieldAction(a) && !isInsertConstructorAction(a) &&
				!isInsertMethodAction(a) && !isInsertFieldAccessAction(a) && a instanceof InsertAction;
	}
	
	private boolean isDeleteInvocationAction(ActionInstance a) {
		return !isDeleteFieldAction(a) && !isDeleteConstructorAction(a) &&
				!isDeleteMethodAction(a) && !isDeleteFieldAccessAction(a) && a instanceof DeleteAction;
	}
	
	public List<String> getFieldAccessesQualifiedNames() {
		List<String> result = getInsertedFieldAccessesQualifiedNames();
		result.addAll(getDeletedFieldAccessesQualifiedNames());
		return result;
	}

	private List<String> getInsertedFieldAccessesQualifiedNames() {
		return actions.stream()
				  .filter(a -> isInsertFieldAccessAction(a))
				  .map(a -> ((InsertFieldAccessAction) a).getInsertedEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private List<String> getDeletedFieldAccessesQualifiedNames() {
		return actions.stream()
				  .filter(a -> isDeleteFieldAccessAction(a))
				  .map(a -> ((DeleteFieldAccessAction) a).getDeletedEntityQualifiedName())
				  .collect(Collectors.toList());
	}

	private boolean isInsertFieldAccessAction(ActionInstance a) {
		return a instanceof InsertFieldAccessAction;
	}
	
	private boolean isDeleteFieldAccessAction(ActionInstance a) {
		return a instanceof DeleteFieldAccessAction;
	}

	public List<String> getUpdatesQualifiedNames() {
		List<String> result = getUpdatedEntitiesQualifiedNames();
		result.addAll(getUpdatedFieldsQualifiedNames());
		return result;
	}
	
	private List<String> getUpdatedEntitiesQualifiedNames() {
		return actions.stream()
				  .filter(a -> isUpdateAction(a))
				  .map(a -> ((UpdateAction) a).getEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private List<String> getUpdatedFieldsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isUpdateFieldTypeAction(a))
				  .map(a -> ((UpdateFieldTypeAction) a).getEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private boolean isUpdateAction(ActionInstance a) {
		return a instanceof UpdateAction && !isUpdateFieldTypeAction(a);
	}
	
	private boolean isUpdateFieldTypeAction(ActionInstance a) {
		return a instanceof UpdateFieldTypeAction;
	}

	public List<String> getVisibilityActionsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isVisibilityAction(a))
				  .map(a -> ((VisibilityAction) a).getEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private boolean isVisibilityAction(ActionInstance a) {
		return a instanceof VisibilityAction;
	}
}
