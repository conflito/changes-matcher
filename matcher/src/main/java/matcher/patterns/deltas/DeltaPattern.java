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
	
	public boolean hasInsertActions() {
		return actions.stream().anyMatch(a -> a instanceof InsertPatternAction);
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
		List<Integer> result = getInsertFieldsVariableIds();
		result.addAll(getDeleteFieldsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getFieldAccessesVariableIds() {
		List<Integer> result = getInsertFieldAccessesVariableIds();
		result.addAll(getDeleteFieldAccessesVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getMethodsVariableIds() {
		List<Integer> result = getInsertMethodsVariableIds();
		result.addAll(getDeleteMethodsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getInvocationsVariableIds() {
		List<Integer> result = getInsertedMethodInvocationsVariableIds();
		result.addAll(getDeletedMethodInvocationsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	private List<Integer> getInsertedMethodInvocationsVariableIds() {
		return actions.stream()
					  .filter(a -> insertInvocationAction(a))
					  .map(a -> ((InsertPatternAction)a).getInsertedEntity().getId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeletedMethodInvocationsVariableIds() {
		return actions.stream()
					  .filter(a -> deleteInvocationAction(a))
					  .map(a -> ((DeletePatternAction)a).getDeletedEntity().getId())
					  .collect(Collectors.toList());
	}

	public List<Integer> getConstructorsVariableIds() {
		List<Integer> result = getInsertConstructorsVariableIds();
		result.addAll(getDeleteConstructorsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	private List<Integer> getInsertConstructorsVariableIds() {
		return actions.stream()
				  .filter(a -> insertConstructorAction(a))
				  .map(a -> ((InsertConstructorPatternAction)a).getInsertedEntity().getId())
				  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeleteConstructorsVariableIds(){
		return actions.stream()
				  .filter(a -> deleteConstructorAction(a))
				  .map(a -> ((DeleteConstructorPatternAction)a).getDeletedEntity().getId())
				  .collect(Collectors.toList());
	}

	private List<Integer> getInsertMethodsVariableIds(){
		return actions.stream()
					  .filter(a -> insertMethodAction(a))
					  .map(a -> ((InsertMethodPatternAction)a).getInsertedEntity().getId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeleteMethodsVariableIds(){
		return actions.stream()
					  .filter(a -> deleteMethodAction(a))
					  .map(a -> ((DeleteMethodPatternAction)a).getDeletedEntity().getId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getInsertFieldsVariableIds(){
		return actions.stream()
					  .filter(a -> insertFieldAction(a))
					  .map(a -> ((InsertFieldPatternAction)a).getInsertedEntity().getId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeleteFieldsVariableIds() {
		return actions.stream()
				  .filter(a -> deleteFieldAction(a))
				  .map(a -> ((DeleteFieldPatternAction)a).getDeletedEntity().getId())
				  .collect(Collectors.toList());
	}

	private List<Integer> getInsertFieldAccessesVariableIds() {
		return actions.stream()
				  .filter(a -> insertFieldAccessAction(a))
				  .map(a -> ((InsertFieldAccessPatternAction)a).getInsertedEntity().getId())
				  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeleteFieldAccessesVariableIds(){
		return actions.stream()
				  .filter(a -> deleteFieldAccessAction(a))
				  .map(a -> ((DeleteFieldAccessPatternAction)a).getDeletedEntity().getId())
				  .collect(Collectors.toList());
	}

	private boolean deleteFieldAccessAction(ActionPattern a) {
		return a instanceof DeleteFieldAccessPatternAction;
	}

	private boolean insertFieldAccessAction(ActionPattern a) {
		return a instanceof InsertFieldAccessPatternAction;
	}

	private boolean insertFieldAction(ActionPattern a) {
		return a instanceof InsertFieldPatternAction;
	}
	
	private boolean deleteFieldAction(ActionPattern a) {
		return a instanceof DeleteFieldPatternAction;
	}
	
	private boolean insertMethodAction(ActionPattern a) {
		return a instanceof InsertMethodPatternAction;
	}
	
	private boolean deleteMethodAction(ActionPattern a) {
		return a instanceof DeleteMethodPatternAction;
	}
	
	private boolean insertConstructorAction(ActionPattern a) {
		return a instanceof InsertConstructorPatternAction;
	}
	
	private boolean deleteConstructorAction(ActionPattern a) {
		return a instanceof DeleteConstructorPatternAction;
	}
	
	private boolean insertInvocationAction(ActionPattern a) {
		return !insertFieldAction(a) && !insertMethodAction(a) &&
				!insertConstructorAction(a) && !insertFieldAccessAction(a) &&
				a instanceof InsertPatternAction;
	}
	
	private boolean deleteInvocationAction(ActionPattern a) {
		return !deleteFieldAction(a) && !deleteMethodAction(a) &&
				!deleteConstructorAction(a) && !deleteFieldAccessAction(a) &&
				a instanceof DeletePatternAction;
	}

}
