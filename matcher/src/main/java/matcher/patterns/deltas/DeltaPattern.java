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
	
	public DeltaPattern(DeltaPattern deltaPattern) {
		super();
		this.actions = new ArrayList<>();
		for(ActionPattern a: deltaPattern.actions) {
			this.actions.add(a.makeCopy());
		}
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
	
	public boolean hasInvocations() {
		return hasInsertInvocationActions() || hasDeleteInvocationActions() ||
			   hasUpdateInvocationActions() || hasMethodInsertsWithInvocations() ||
			   hasConstructorInsertedWithInvocations() || hasClassInsertsWithInvocations();
	}

	public boolean hasFieldAccesses() {
		return hasInsertFieldAccessActions() || hasDeleteFieldAccessActions() ||
			   hasMethodInsertsWithFieldAccesses() || hasClassInsertsWithFieldAccesses();
	}
	
	private boolean hasClassInsertsWithFieldAccesses() {
		for(ActionPattern a: actions) {
			if(isInsertClassAction(a)) {
				InsertClassPatternAction ima = (InsertClassPatternAction) a;
				if(ima.hasFieldAccesses()) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasMethodInsertsWithFieldAccesses() {
		for(ActionPattern a: actions) {
			if(isInsertMethodAction(a)) {
				InsertMethodPatternAction ima = (InsertMethodPatternAction) a;
				if(ima.hasFieldAccesses()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean hasClassInsertsWithInvocations() {
		for(ActionPattern a: actions) {
			if(isInsertClassAction(a)) {
				InsertClassPatternAction ima = (InsertClassPatternAction) a;
				if(ima.hasInvocations()) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasMethodInsertsWithInvocations() {
		for(ActionPattern a: actions) {
			if(isInsertMethodAction(a)) {
				InsertMethodPatternAction ima = (InsertMethodPatternAction) a;
				if(ima.hasInvocations()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean hasConstructorInsertedWithInvocations() {
		for(ActionPattern a: actions) {
			if(isInsertConstructorAction(a)) {
				InsertConstructorPatternAction ima = (InsertConstructorPatternAction) a;
				if(ima.hasInvocations()) {
					return true;
				}
			}
		}
		return false;
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
	
	public boolean hasDeleteInvocationActions() {
		return actions.stream().anyMatch(a -> a instanceof DeleteInvocationPatternAction); 
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
		return actions.stream().anyMatch(a -> a instanceof UpdatePatternAction &&
				!(a instanceof UpdateFieldTypePatternAction) && 
				!(a instanceof UpdateDependencyPatternAction));
	}
	
	public boolean hasUpdateFieldTypeActions() {
		return actions.stream().anyMatch(a -> a instanceof UpdateFieldTypePatternAction);
	}
	
	public boolean hasUpdateInvocationActions() {
		return actions.stream().anyMatch(a -> a instanceof UpdateDependencyPatternAction);
	}
	
	public boolean hasVisibilityActions() {
		return actions.stream().anyMatch(a -> a instanceof VisibilityActionPattern);
	}

	public List<Integer> getFieldsVariableIds() {
		List<Integer> result = getInsertedFieldsVariableIds();
		result.addAll(getDeletedFieldsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getFieldTypesVariableIds(){
		return actions.stream()
					  .filter(a -> isUpdateFieldTypeAction(a))
					  .map(a -> (UpdateFieldTypePatternAction)a)
					  .filter(UpdateFieldTypePatternAction::hasNewType)
					  .map(UpdateFieldTypePatternAction::getNewTypeVariableId)
					  .collect(Collectors.toList());
	}
	
	
	public List<Integer> getFieldAccessesVariableIds() {
		List<Integer> result = getInsertedFieldAccessesVariableIds();
		result.addAll(getDeletedFieldAccessesVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getMethodsVariableIds() {
		List<Integer> result = getInsertedMethodsVariableIds();
		result.addAll(getDeletedMethodsVariableIds());
		result.addAll(getMethodsInInsertedClassesVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	private List<Integer> getMethodsInInsertedClassesVariableIds(){
		List<Integer> result = new ArrayList<>();
		for(ActionPattern a: actions) {
			if(isInsertClassAction(a)) {
				InsertClassPatternAction icpa = (InsertClassPatternAction) a;
				result.addAll(icpa.getMethodsVariableIds());
			}
		}
		return result;
	}
	
	public List<Integer> getInvocationsVariableIds() {
		List<Integer> result = getInsertedMethodInvocationsVariableIds();
		result.addAll(getDeletedMethodInvocationsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getClassesVariableIds() {
		return actions.stream()
					  .filter(a -> isInsertClassAction(a))
					  .map(a -> ((InsertClassPatternAction)a).getInsertedClassVariableId())
					  .collect(Collectors.toList());
	}
	
	private boolean isInsertClassAction(ActionPattern a) {
		return a instanceof InsertClassPatternAction;
	}

	private List<Integer> getInsertedMethodInvocationsVariableIds() {
		return actions.stream()
					  .filter(a -> isInsertInvocationAction(a))
					  .map(a -> ((InsertInvocationPatternAction)a).getInsertedInvocationVariableId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeletedMethodInvocationsVariableIds() {
		return actions.stream()
					  .filter(a -> isDeleteInvocationAction(a))
					  .map(a -> ((DeleteInvocationPatternAction)a).getDeletedInvocationVariableId())
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
				  .map(a -> ((InsertConstructorPatternAction)a).getInsertedConstructorVariableId())
				  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeletedConstructorsVariableIds(){
		return actions.stream()
				  .filter(a -> isDeleteConstructorAction(a))
				  .map(a -> ((DeleteConstructorPatternAction)a).getDeletedConstructorVariableId())
				  .collect(Collectors.toList());
	}

	private List<Integer> getInsertedMethodsVariableIds(){
		return actions.stream()
					  .filter(a -> isInsertMethodAction(a))
					  .map(a -> ((InsertMethodPatternAction)a).getInsertedMethodVariableId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeletedMethodsVariableIds(){
		return actions.stream()
					  .filter(a -> isDeleteMethodAction(a))
					  .map(a -> ((DeleteMethodPatternAction)a).getDeletedMethodVariableId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getInsertedFieldsVariableIds(){
		return actions.stream()
					  .filter(a -> isInsertFieldAction(a))
					  .map(a -> ((InsertFieldPatternAction)a).getInsertedFieldVariableId())
					  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeletedFieldsVariableIds() {
		return actions.stream()
				  .filter(a -> isDeleteFieldAction(a))
				  .map(a -> ((DeleteFieldPatternAction)a).getDeletedFieldVariableId())
				  .collect(Collectors.toList());
	}

	private List<Integer> getInsertedFieldAccessesVariableIds() {
		return actions.stream()
				  .filter(a -> isInsertFieldAccessAction(a))
				  .map(a -> ((InsertFieldAccessPatternAction)a).getInsertedFieldAccessVariableId())
				  .collect(Collectors.toList());
	}
	
	private List<Integer> getDeletedFieldAccessesVariableIds(){
		return actions.stream()
				  .filter(a -> isDeleteFieldAccessAction(a))
				  .map(a -> ((DeleteFieldAccessPatternAction)a).getDeletedFieldAccessVariableId())
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
		return a instanceof InsertInvocationPatternAction;
	}
	
	private boolean isDeleteInvocationAction(ActionPattern a) {
		return a instanceof DeleteInvocationPatternAction;
	}

	public List<Integer> getUpdatesVariableIds() {
		List<Integer> result = new ArrayList<>();
		result.addAll(getUpdatedMethodsVariableIds());
		result.addAll(getUpdatedConstructosVariableIds());
		return result;
	}
	
	private List<Integer> getUpdatedMethodsVariableIds(){
		return actions.stream()
					.filter(a -> isUpdateMethodAction(a))
					.map(a -> ((UpdateMethodPatternAction) a).getUpdatedMethodVariableId())
					.collect(Collectors.toList());
	}
	
	private List<Integer> getUpdatedConstructosVariableIds(){
		return actions.stream()
				.filter(a -> isUpdateConstructorAction(a))
				.map(a -> ((UpdateConstructorPatternAction) a).getUpdatedConstructorVariableId())
				.collect(Collectors.toList());
	}
	
	public List<Integer> getUpdatedFieldsVariableIds() {
		return actions.stream()
				  .filter(a -> isUpdateFieldTypeAction(a))
				  .map(a -> ((UpdateFieldTypePatternAction)a).getUpdatedFieldVariableId())
				  .collect(Collectors.toList());
	}
	
	public List<Integer> getUpdatedInvocationsVariableIds() {
		return actions.stream()
				  .filter(a -> isUpdateInvocationAction(a))
				  .map(a -> ((UpdateDependencyPatternAction)a).getNewInvocationVariableId())
				  .collect(Collectors.toList());
	}
	
	private boolean isUpdateConstructorAction(ActionPattern a) {
		return a instanceof UpdateConstructorPatternAction;
	}
	
	private boolean isUpdateMethodAction(ActionPattern a) {
		return a instanceof UpdateMethodPatternAction;
	}
	
	private boolean isUpdateFieldTypeAction(ActionPattern a) {
		return a instanceof UpdateFieldTypePatternAction;
	}
	
	private boolean isUpdateInvocationAction(ActionPattern a) {
		return a instanceof UpdateDependencyPatternAction;
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
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		for(ActionPattern a: actions)
			result.append(a.toString() + "\n");
		
		return result.toString();
	}
}
