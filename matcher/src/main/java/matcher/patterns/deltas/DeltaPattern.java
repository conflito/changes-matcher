package matcher.patterns.deltas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.deltas.DeltaInstance;

public class DeltaPattern {

	private List<ActionPattern> actions;

	public DeltaPattern() {
		super();
		actions = new ArrayList<>();
	}
	
	public void addActionPattern(ActionPattern a) {
		actions.add(a);
	}
	
	public boolean filled() {
		return actions.stream().allMatch(ActionPattern::filled);
	}
	
	public boolean matches(DeltaInstance instance) {
		return filled() &&
			   actions.stream().allMatch(a -> a.matchesOne(instance.getActionInstances()));
	}
	
	public void setVariableValue(int id, String value) {
		for(ActionPattern a: actions) {
			a.setVariableValue(id, value);
		}
	}
	
	public boolean hasInsertInvocationActions() {
		return actions.stream().anyMatch(a -> a instanceof InsertInvocationPatternAction);
	}
	
	public boolean hasInsertMethodActions() {
		return actions.stream().anyMatch(a -> a instanceof InsertMethodPatternAction);
	}
	
	public boolean hasInsertConstructorActions() {
		return actions.stream().anyMatch(a -> a instanceof InsertConstructorPatternAction);
	}
	
	public boolean hasInsertFieldActions() {
		return actions.stream().anyMatch(a -> a instanceof InsertFieldPatternAction);
	}
	
	public boolean hasInsertFieldAccessActions() {
		return actions.stream().anyMatch(a -> a instanceof InsertFieldAccessPatternAction);
	}
	
	public boolean hasInsertClassActions() {
		return actions.stream().anyMatch(a -> a instanceof InsertClassPatternAction);
	}
	
	public boolean hasDeleteActions() {
		return actions.stream().anyMatch(a -> a instanceof DeletePatternAction); 
	}
	
	public boolean hasDeleteFieldActions() {
		return actions.stream().anyMatch(a -> a instanceof DeleteFieldPatternAction); 
	}

	public boolean hasDeleteMethodActions() {
		return actions.stream().anyMatch(a -> a instanceof DeleteMethodPatternAction); 
	}
	
	public boolean hasDeleteConstructorsActions() {
		return actions.stream().anyMatch(a -> a instanceof DeleteConstructorPatternAction); 
	}
	
	public boolean hasDeleteFieldAccessActions() {
		return actions.stream().anyMatch(a -> a instanceof DeleteFieldAccessPatternAction); 
	}
	
	public boolean hasUpdateActions() {
		return actions.stream().anyMatch(a -> a instanceof UpdatePatternAction);
	}
	
	public boolean hasVisibilityActions() {
		return actions.stream().anyMatch(a -> a instanceof VisibilityActionPattern);
	}
	
	public String toStringDebug() {
		StringBuilder result = new StringBuilder();
		
		for(ActionPattern a: actions)
			result.append(a.toStringDebug() + "\n");
		
		return result.toString();
	}

	public List<Integer> getFieldsVariableIds() {
		List<Integer> result = getInsertedFieldsVariableIds();
		result.addAll(getDeletedFieldsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getFieldAccessesVariableIds() {
		List<Integer> result = getInsertedFieldAccessesVariableIds();
		result.addAll(getDeletedFieldAccessesVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getMethodsVariableIds() {
		List<Integer> result = getInsertedMethodsVariableIds();
		result.addAll(getDeletedMethodsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getInvocationsVariableIds() {
		List<Integer> result = getInsertedMethodInvocationsVariableIds();
		result.addAll(getDeletedMethodInvocationsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	private List<Integer> getInsertedMethodInvocationsVariableIds() {
		return actions.stream()
					  .filter(a -> isInsertInvocationAction(a))
					  .map(a -> ((InsertPatternAction)a).getInsertedEntityId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeletedMethodInvocationsVariableIds() {
		return actions.stream()
					  .filter(a -> isDeleteInvocationAction(a))
					  .map(a -> ((DeletePatternAction)a).getDeletedEntityId())
					  .collect(Collectors.toList());
	}

	public List<Integer> getConstructorsVariableIds() {
		List<Integer> result = getInsertedConstructorsVariableIds();
		result.addAll(getDeletedConstructorsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	private List<Integer> getInsertedConstructorsVariableIds() {
		return actions.stream()
				  .filter(a -> isInsertConstructorAction(a))
				  .map(a -> ((InsertConstructorPatternAction)a).getInsertedEntityId())
				  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeletedConstructorsVariableIds(){
		return actions.stream()
				  .filter(a -> isDeleteConstructorAction(a))
				  .map(a -> ((DeleteConstructorPatternAction)a).getDeletedEntityId())
				  .collect(Collectors.toList());
	}

	private List<Integer> getInsertedMethodsVariableIds(){
		return actions.stream()
					  .filter(a -> isInsertMethodAction(a))
					  .map(a -> ((InsertMethodPatternAction)a).getInsertedEntityId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeletedMethodsVariableIds(){
		return actions.stream()
					  .filter(a -> isDeleteMethodAction(a))
					  .map(a -> ((DeleteMethodPatternAction)a).getDeletedEntityId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getInsertedFieldsVariableIds(){
		return actions.stream()
					  .filter(a -> isInsertFieldAction(a))
					  .map(a -> ((InsertFieldPatternAction)a).getInsertedEntityId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeletedFieldsVariableIds() {
		return actions.stream()
				  .filter(a -> isDeleteFieldAction(a))
				  .map(a -> ((DeleteFieldPatternAction)a).getDeletedEntityId())
				  .collect(Collectors.toList());
	}

	private List<Integer> getInsertedFieldAccessesVariableIds() {
		return actions.stream()
				  .filter(a -> isInsertFieldAccessAction(a))
				  .map(a -> ((InsertFieldAccessPatternAction)a).getInsertedEntityId())
				  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeletedFieldAccessesVariableIds(){
		return actions.stream()
				  .filter(a -> isDeleteFieldAccessAction(a))
				  .map(a -> ((DeleteFieldAccessPatternAction)a).getDeletedEntityId())
				  .collect(Collectors.toList());
	}

	private boolean isDeleteFieldAccessAction(ActionPattern a) {
		return a instanceof DeleteFieldAccessPatternAction;
	}

	private boolean isInsertFieldAccessAction(ActionPattern a) {
		return a instanceof InsertFieldAccessPatternAction;
	}

	private boolean isInsertFieldAction(ActionPattern a) {
		return a instanceof InsertFieldPatternAction;
	}
	
	private boolean isDeleteFieldAction(ActionPattern a) {
		return a instanceof DeleteFieldPatternAction;
	}
	
	private boolean isInsertMethodAction(ActionPattern a) {
		return a instanceof InsertMethodPatternAction;
	}
	
	private boolean isDeleteMethodAction(ActionPattern a) {
		return a instanceof DeleteMethodPatternAction;
	}
	
	private boolean isInsertConstructorAction(ActionPattern a) {
		return a instanceof InsertConstructorPatternAction;
	}
	
	private boolean isDeleteConstructorAction(ActionPattern a) {
		return a instanceof DeleteConstructorPatternAction;
	}
	
	private boolean isInsertInvocationAction(ActionPattern a) {
		return !isInsertFieldAction(a) && !isInsertMethodAction(a) &&
				!isInsertConstructorAction(a) && !isInsertFieldAccessAction(a) &&
				a instanceof InsertPatternAction;
	}
	
	private boolean isDeleteInvocationAction(ActionPattern a) {
		return !isDeleteFieldAction(a) && !isDeleteMethodAction(a) &&
				!isDeleteConstructorAction(a) && !isDeleteFieldAccessAction(a) &&
				a instanceof DeletePatternAction;
	}

	public List<Integer> getUpdatesVariableIds() {
		return actions.stream()
				  .filter(a -> isUpdateAction(a))
				  .map(a -> ((UpdatePatternAction)a).getEntityId())
				  .collect(Collectors.toList());
	}
	
	private boolean isUpdateAction(ActionPattern a) {
		return a instanceof UpdatePatternAction;
	}

	public List<Integer> getVisibilityActionsVariableIds() {
		return actions.stream()
				  .filter(a -> isVisibilityAction(a))
				  .map(a -> ((VisibilityActionPattern)a).getEntityId())
				  .collect(Collectors.toList());
	}
	
	private boolean isVisibilityAction(ActionPattern a) {
		return a instanceof VisibilityActionPattern;
	}
	
	public void clean() {
		for(ActionPattern a: actions) {
			a.clean();
		}
	}

	public String toStringFilled() {
		StringBuilder result = new StringBuilder();
		
		for(ActionPattern a: actions)
			result.append(a.toStringFilled() + "\n");
		
		return result.toString();
	}
}
