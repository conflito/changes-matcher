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
					  .map(a ->((InsertClassAction) a).getInsertedClassQualifiedName())
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
	
	public List<String> getFieldTypesQualifiedNames(){
		return actions.stream()
					  .filter(a -> isUpdateFieldTypeAction(a))
					  .map(a ->((UpdateFieldTypeAction)a).getNewTypeName())
					  .collect(Collectors.toList());
	}

	private List<String> getInsertedFieldsQualifiedNames() {
		return actions.stream()
					  .filter(a -> isInsertFieldAction(a))
					  .map(a -> ((InsertFieldAction) a).getInsertedFieldQualifiedName())
					  .collect(Collectors.toList());
	}
	
	private List<String> getDeletedFieldsQualifiedNames(){
		return actions.stream()
				  .filter(a -> isDeleteFieldAction(a))
				  .map(a -> ((DeleteFieldAction) a).getDeletedFieldQualifiedName())
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
		result.addAll(getInsertedMethodsInInsertedClassesQualifiedNames());
		return result;
	}
	
	private List<String> getInsertedMethodsInInsertedClassesQualifiedNames(){
		List<String> result = new ArrayList<>();
		for(ActionInstance a: actions) {
			if(isInsertClassAction(a)) {
				InsertClassAction ica = (InsertClassAction) a;
				result.addAll(ica.getInsertedMethodsQualifiedName());
			}
		}
		return result;
	}

	private List<String> getInsertedMethodsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isInsertMethodAction(a))
				  .map(a -> ((InsertMethodAction) a).getInsertedMethodQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private List<String> getDeletedMethodsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isDeleteMethodAction(a))
				  .map(a -> ((DeleteMethodAction) a).getDeletedMethodQualifiedName())
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
				  .map(a -> ((InsertConstructorAction) a).getInsertedConstructorQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private List<String> getDeletedConstructorsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isDeleteConstructorAction(a))
				  .map(a -> ((DeleteConstructorAction) a).getDeletedConstructorQualifiedName())
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
				  .map(a -> ((InsertInvocationAction) a).getInsertedInvocationQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private List<String> getDeletedInvocationsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isDeleteInvocationAction(a))
				  .map(a -> ((DeleteInvocationAction) a).getDeletedInvocationQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private boolean isInsertInvocationAction(ActionInstance a) {
		return a instanceof InsertInvocationAction;
	}
	
	private boolean isDeleteInvocationAction(ActionInstance a) {
		return a instanceof DeleteInvocationAction;
	}
	
	public List<String> getFieldAccessesQualifiedNames() {
		List<String> result = getInsertedFieldAccessesQualifiedNames();
		result.addAll(getDeletedFieldAccessesQualifiedNames());
		return result;
	}

	private List<String> getInsertedFieldAccessesQualifiedNames() {
		return actions.stream()
				  .filter(a -> isInsertFieldAccessAction(a))
				  .map(a -> ((InsertFieldAccessAction) a).getInsertedFieldAccessQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private List<String> getDeletedFieldAccessesQualifiedNames() {
		return actions.stream()
				  .filter(a -> isDeleteFieldAccessAction(a))
				  .map(a -> ((DeleteFieldAccessAction) a).getDeletedFieldAccessQualifiedName())
				  .collect(Collectors.toList());
	}

	private boolean isInsertFieldAccessAction(ActionInstance a) {
		return a instanceof InsertFieldAccessAction;
	}
	
	private boolean isDeleteFieldAccessAction(ActionInstance a) {
		return a instanceof DeleteFieldAccessAction;
	}

	public List<String> getUpdatesQualifiedNames() {
		return actions.stream()
				  .filter(a -> isUpdateAction(a))
				  .map(a -> ((UpdateAction) a).getUpdatedEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	public List<String> getUpdatedFieldsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isUpdateFieldTypeAction(a))
				  .map(a -> ((UpdateFieldTypeAction) a).getUpdatedEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	public List<String> getUpdatedInvocationsQualifiedNames() {
		return actions.stream()
				  .filter(a -> isUpdateInvocationAction(a))
				  .map(a -> ((UpdateDependencyAction) a).getUpdatedEntityQualifiedName())
				  .collect(Collectors.toList());
	}
	
	private boolean isUpdateAction(ActionInstance a) {
		return a instanceof UpdateAction && !isUpdateFieldTypeAction(a) &&
				!isUpdateInvocationAction(a);
	}
	
	private boolean isUpdateFieldTypeAction(ActionInstance a) {
		return a instanceof UpdateFieldTypeAction;
	}
	
	private boolean isUpdateInvocationAction(ActionInstance a) {
		return a instanceof UpdateDependencyAction;
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
	
	public boolean hasInsertInvocationActions() {
		return actions.stream().anyMatch(a -> isInsertInvocationAction(a));
	}
	
	public boolean hasInsertMethodActions() {
		return actions.stream().anyMatch(a -> isInsertMethodAction(a));
	}
	
	public boolean hasInsertConstructorActions() {
		return actions.stream().anyMatch(a -> isInsertConstructorAction(a));
	}
	
	public boolean hasInsertFieldActions() {
		return actions.stream().anyMatch(a -> isInsertFieldAction(a));
	}
	
	public boolean hasInsertFieldAccessActions() {
		return actions.stream().anyMatch(a -> isInsertFieldAccessAction(a));
	}
	
	public boolean hasInsertClassActions() {
		return actions.stream().anyMatch(a -> isInsertClassAction(a));
	}
	
	public boolean hasDeleteInvocationActions() {
		return actions.stream().anyMatch(a -> isDeleteInvocationAction(a));
	}
	
	public boolean hasDeleteFieldActions() {
		return actions.stream().anyMatch(a -> isDeleteFieldAction(a));
	}
	
	public boolean hasDeleteMethodActions() {
		return actions.stream().anyMatch(a -> isDeleteMethodAction(a));
	}
	
	public boolean hasDeleteConstructorsActions() {
		return actions.stream().anyMatch(a -> isDeleteConstructorAction(a));
	}
	
	public boolean hasDeleteFieldAccessActions() {
		return actions.stream().anyMatch(a -> isDeleteFieldAccessAction(a));
	}
	
	public boolean hasUpdateActions() {
		return actions.stream().anyMatch(a -> isUpdateAction(a));
	}
	
	public boolean hasUpdateFieldTypeActions() {
		return actions.stream().anyMatch(a -> isUpdateFieldTypeAction(a));
	}
	
	public boolean hasUpdateInvocationActions() {
		return actions.stream().anyMatch(a -> isUpdateInvocationAction(a));
	}
	
	public boolean hasVisibilityActions() {
		return actions.stream().anyMatch(a -> isVisibilityAction(a));
	}
}
