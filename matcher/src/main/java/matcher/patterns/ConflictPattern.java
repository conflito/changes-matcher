package matcher.patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import matcher.entities.ChangeInstance;
import matcher.entities.deltas.DeltaInstance;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.goals.BDDTestingGoal;
import matcher.patterns.goals.TestingGoal;
import matcher.utils.Pair;

public class ConflictPattern {
	
	private BasePattern basePattern;
	
	private DeltaPattern firstDelta;
	private DeltaPattern secondDelta;
	
	private List<Pair<FreeVariable, FreeVariable>> differentVariables;

	private Map<FreeVariable, Set<FreeVariable>> equalVariables;
	
	private BDDTestingGoal bddTestingGoal;
	private TestingGoal testingGoal;
	
	private String conflictName;
	
	public ConflictPattern(String conflictName) {
		this.conflictName = conflictName;
		this.differentVariables = new ArrayList<>();
		this.equalVariables = new HashMap<>();
	}
	
	public ConflictPattern(ConflictPattern cp) {
		super();
		this.conflictName = cp.conflictName;
		this.basePattern = new BasePattern(cp.basePattern);
		this.firstDelta = new DeltaPattern(cp.firstDelta);
		this.secondDelta = new DeltaPattern(cp.secondDelta);
		
		this.differentVariables = new ArrayList<>();
		this.equalVariables = new HashMap<>();
		
		for(Pair<FreeVariable, FreeVariable> p: cp.differentVariables) {
			FreeVariable firstCopy = new FreeVariable(p.getFirst());
			FreeVariable secondCopy = new FreeVariable(p.getSecond());
			Pair<FreeVariable, FreeVariable> copy = new Pair<>(firstCopy, secondCopy);
			this.differentVariables.add(copy);
		}
		
		for(Entry<FreeVariable, Set<FreeVariable>> e: cp.equalVariables.entrySet()) {
			FreeVariable keyCopy = new FreeVariable(e.getKey());
			Set<FreeVariable> valuesCopy = new HashSet<>();
			for(FreeVariable v: e.getValue()) {
				valuesCopy.add(new FreeVariable(v));
			}
			this.equalVariables.put(keyCopy, valuesCopy);
		}
		if(cp.bddTestingGoal != null)
			this.bddTestingGoal = new BDDTestingGoal(cp.bddTestingGoal);
		if(cp.testingGoal != null)
			this.testingGoal = new TestingGoal(cp.testingGoal);
	}
	
	public String getConflictName() {
		return conflictName;
	}

	public void setTestingGoal(BDDTestingGoal goal) {
		this.bddTestingGoal = goal;
	}
	
	public void setTestingGoal(TestingGoal goal) {
		this.testingGoal = goal;
	}
	
	public boolean hasTestingGoal() {
		return this.testingGoal != null;
	}
	
	public String getTestBDD() {
		return bddTestingGoal.getTestBDD();
	}
	
	public String getTestTargetClass() {
		return testingGoal.getTargetClass();
	}
	
	public List<String> getTestTargetMethods() {
		return testingGoal.getMethodsToCall();
	}
	
	public void setBasePattern(BasePattern basePattern) {
		this.basePattern = basePattern;
	}
	
	public void setFirstDeltaPattern(DeltaPattern deltaPattern) {
		this.firstDelta = deltaPattern;
	}
	
	public void setSecondDeltaPattern(DeltaPattern deltaPattern) {
		this.secondDelta = deltaPattern;
	}
	
	public void addDifferentVariablesRule(FreeVariable v1, FreeVariable v2) {
		this.differentVariables.add(new Pair<>(v1, v2));
	}
	
	public void addEqualVariableRule(FreeVariable v1, FreeVariable v2) {
		if(!equalVariables.containsKey(v1))
			equalVariables.put(v1, new HashSet<>());
		if(!equalVariables.containsKey(v2))
			equalVariables.put(v2, new HashSet<>());
		equalVariables.get(v1).add(v2);
		equalVariables.get(v2).add(v1);
	}
	
	public boolean canBeEqual(int i, int j) {
		FreeVariable v1 = new FreeVariable(i);
		FreeVariable v2 = new FreeVariable(j);
		return equalVariables.containsKey(v1) && equalVariables.get(v1).contains(v2);
	}

	public boolean hasInvocations() {
		return basePattern.hasInvocations() || firstDelta.hasInvocations() ||
				secondDelta.hasInvocations();
	}

	public boolean hasFieldAccesses() {
		return basePattern.hasFieldAccesses() || firstDelta.hasFieldAccesses() ||
				secondDelta.hasFieldAccesses();
	}

	public boolean hasSuperClasses() {
		return basePattern.hasSuperClass();
	}
	
	public boolean hasInterfaces() {
		return basePattern.hasInterfaces();
	}

	public boolean hasMethods() {
		return basePattern.hasMethods();
	}

	public boolean hasFields() {
		return basePattern.hasFields();
	}

	public boolean hasConstructors() {
		return basePattern.hasConstructors();
	}

	public boolean hasCompatibleMethods() {
		return basePattern.hasCompatible();
	}
	
	public boolean hasInsertInvocationActions() {
		return firstDelta.hasInsertInvocationActions() || 
				secondDelta.hasInsertInvocationActions();
	}
	
	public boolean hasInsertMethodActions() {
		return firstDelta.hasInsertMethodActions() || 
				secondDelta.hasInsertMethodActions();
	}
	
	public boolean hasInsertConstructorActions() {
		return firstDelta.hasInsertConstructorActions() || 
				secondDelta.hasInsertConstructorActions();
	}
	
	public boolean hasInsertFieldActions() {
		return firstDelta.hasInsertFieldActions() || 
				secondDelta.hasInsertFieldActions();
	}
	
	public boolean hasInsertFieldAccessActions() {
		return firstDelta.hasInsertFieldAccessActions() || 
				secondDelta.hasInsertFieldAccessActions();
	}
	
	public boolean hasInsertClassActions() {
		return firstDelta.hasInsertClassActions() || 
				secondDelta.hasInsertClassActions();
	}
	
	public boolean hasDeleteInvocationActions() {
		return firstDelta.hasDeleteInvocationActions() || 
				secondDelta.hasDeleteInvocationActions();
	}
	
	public boolean hasDeleteFieldActions() {
		return firstDelta.hasDeleteFieldActions() || 
				secondDelta.hasDeleteFieldActions();
	}
	
	public boolean hasDeleteMethodActions() {
		return firstDelta.hasDeleteMethodActions() || 
				secondDelta.hasDeleteMethodActions();
	}
	
	public boolean hasDeleteConstructorsActions() {
		return firstDelta.hasDeleteConstructorsActions() || 
				secondDelta.hasDeleteConstructorsActions();
	}
	
	public boolean hasDeleteFieldAccessActions() {
		return firstDelta.hasDeleteFieldAccessActions() || 
				secondDelta.hasDeleteFieldAccessActions();
	}
	
	public boolean hasUpdateActions() {
		return firstDelta.hasUpdateActions() || 
				secondDelta.hasUpdateActions();
	}
	
	public boolean hasUpdateFieldTypeActions() {
		return firstDelta.hasUpdateFieldTypeActions() || 
				secondDelta.hasUpdateFieldTypeActions();
	}
	
	public boolean hasUpdateInvocationActions() {
		return firstDelta.hasUpdateInvocationActions() || 
				secondDelta.hasUpdateInvocationActions();
	}
	
	public boolean hasVisibilityActions() {
		return firstDelta.hasVisibilityActions() || 
				secondDelta.hasVisibilityActions();
	}
	
	public boolean fitForMatch(ChangeInstance instance) {
		return baseFitForMatch(instance) && deltasFitForMatch(instance);
	}
	
	private boolean baseFitForMatch(ChangeInstance instance) {
		boolean result = true;
		if(hasFields())
			result &= instance.hasFields();
		if(result && basePattern.hasFieldAccesses())
			result &= instance.hasFieldAccesses();
		if(result && hasMethods())
			result &= instance.hasMethods();
		if(result && basePattern.hasInvocations())
			result &= instance.hasInvocations();
		if(result && hasInterfaces())
			result &= instance.hasInterfaces();
		if(result && hasConstructors())
			result &= instance.hasConstructors();
		if(result && hasCompatibleMethods())
			result &= instance.hasCompatible();
		return result;
	}
	
	private boolean deltasFitForMatch(ChangeInstance instance) {
		boolean result = true;
		if(hasInsertInvocationActions())
			result &= instance.hasInsertInvocationActions();
		if(result && hasInsertMethodActions())
			result &= instance.hasInsertMethodActions();
		if(result && hasInsertConstructorActions())
			result &= instance.hasInsertConstructorActions();
		if(result && hasInsertFieldActions())
			result &= instance.hasInsertFieldActions();
		if(result && hasInsertFieldAccessActions())
			result &= instance.hasInsertFieldAccessActions();
		if(result && hasInsertClassActions())
			result &= instance.hasInsertClassActions();
		
		if(result && hasDeleteInvocationActions())
			result &= instance.hasDeleteInvocationActions();
		if(result &&hasDeleteFieldActions())
			result &= instance.hasDeleteFieldActions();
		if(result && instance.hasDeleteMethodActions())
			result &= instance.hasDeleteMethodActions();
		if(result && instance.hasDeleteConstructorsActions())
			result &= instance.hasDeleteConstructorsActions();
		if(result && hasDeleteFieldAccessActions())
			result &= instance.hasDeleteFieldAccessActions();
		
		if(result && hasUpdateActions())
			result &= instance.hasUpdateActions();
		if(result && hasUpdateFieldTypeActions())
			result &= instance.hasUpdateFieldTypeActions();
		if(result && hasUpdateInvocationActions())
			result &= instance.hasUpdateInvocationActions();
		
		if(result && hasVisibilityActions())
			result &= instance.hasVisibilityActions();
		return result;
	}
	
	
	public boolean matches(ChangeInstance instance) {
		return basePattern.filled() && deltasFilled() &&
			   basePattern.matches(instance.getBaseInstance()) &&
			   deltasMatch(instance) &&
			   differentVariableMatch();
	}
	
	private boolean differentVariableMatch() {
		return differentVariables.stream().allMatch(p ->
				!p.getFirst().getValue().equals(p.getSecond().getValue()));
	}

	private boolean deltasFilled() {
		return firstDelta.filled() && secondDelta.filled();
	}

	private boolean deltasMatch(ChangeInstance instance) {
		List<DeltaInstance> instances = instance.getDeltaInstances();
		for(int i = 0; i < instances.size(); i++) {
			for(int j = 0; j < instances.size(); j++) {
				DeltaInstance firstInstance = instances.get(i);
				DeltaInstance secondInstance = instances.get(j);
				if(i != j) {
					boolean match = (firstDelta.matches(firstInstance) &&
									secondDelta.matches(secondInstance)) ||
									(secondDelta.matches(firstInstance) &&
									firstDelta.matches(secondInstance));
					if(match)
						return true;
				}
			}
		}
		return false;
	}
	
	public void setVariableValue(int id, String value) {
		basePattern.setVariableValue(id, value);
		firstDelta.setVariableValue(id, value);
		secondDelta.setVariableValue(id, value);
		for(Pair<FreeVariable, FreeVariable> p: differentVariables) {
			if(p.getFirst().isId(id))
				p.getFirst().setValue(value);
			if(p.getSecond().isId(id))
				p.getSecond().setValue(value);
		}
		for(Entry<FreeVariable, Set<FreeVariable>> e: equalVariables.entrySet()) {
			FreeVariable v1 = e.getKey();
			if(v1.isId(id))
				v1.setValue(value);
			for(FreeVariable v: e.getValue()) {
				if(v.isId(id))
					v.setValue(value);
			}
		}
	}
	
	public void clean() {
		basePattern.clean();
		firstDelta.clean();
		secondDelta.clean();
	}
	
	public List<Integer> getFieldsVariableIds(){
		return basePattern.getFieldsVariableIds();
	}
	
	public List<Integer> getFieldTypesVariableIds(){
		return basePattern.getFieldTypesVariableIds();
	}

	public List<Integer> getMethodVariableIds() {
		return basePattern.getMethodsVariableIds();
	}
	
	public List<Integer> getInterfaceVariableIds(){
		return basePattern.getInterfacesVariableIds();
	}
	
	public List<Integer> getInvocationsVariableIds() {
		return basePattern.getInvocationsVariableIds();
	}

	public List<Integer> getConstructorsVariableIds() {
		return basePattern.getConstructorsVariableIds();
	}

	public List<Integer> getClassVariableIds() {
		return basePattern.getClassVariableIds();
	}
	

	public List<Integer> getFieldAccessVariableIds() {
		return basePattern.getFieldAccessesVariableIds();
	}

	public List<Integer> getDeltaFieldsVariableIds() {
		List<Integer> result = new ArrayList<>();
		result.addAll(firstDelta.getFieldsVariableIds());
		result.addAll(secondDelta.getFieldsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getDeltaFieldTypesVariableIds(){
		List<Integer> result = new ArrayList<>();
		result.addAll(firstDelta.getFieldTypesVariableIds());
		result.addAll(secondDelta.getFieldTypesVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getDeltaClassesVariableIds() {
		List<Integer> result = new ArrayList<>();
		result.addAll(firstDelta.getClassesVariableIds());
		result.addAll(secondDelta.getClassesVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getDeltaMethodsVariableIds() {
		List<Integer> result = new ArrayList<>();
		result.addAll(firstDelta.getMethodsVariableIds());
		result.addAll(secondDelta.getMethodsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getDeltaConstructorsVariableIds() {
		List<Integer> result = new ArrayList<>();
		result.addAll(firstDelta.getConstructorsVariableIds());
		result.addAll(secondDelta.getConstructorsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getDeltaInvocationsVariableIds() {
		List<Integer> result = new ArrayList<>();
		result.addAll(firstDelta.getInvocationsVariableIds());
		result.addAll(secondDelta.getInvocationsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getDeltaFieldAccessVariableIds() {
		List<Integer> result = new ArrayList<>();
		result.addAll(firstDelta.getFieldAccessesVariableIds());
		result.addAll(secondDelta.getFieldAccessesVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getUpdatedVariableIds() {
		List<Integer> result = new ArrayList<>();
		result.addAll(firstDelta.getUpdatesVariableIds());
		result.addAll(secondDelta.getUpdatesVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getUpdatedFieldsVariableIds(){
		List<Integer> result = new ArrayList<>();
		result.addAll(firstDelta.getUpdatedFieldsVariableIds());
		result.addAll(secondDelta.getUpdatedFieldsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getUpdatedInvocationsVariableIds(){
		List<Integer> result = new ArrayList<>();
		result.addAll(firstDelta.getUpdatedInvocationsVariableIds());
		result.addAll(secondDelta.getUpdatedInvocationsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getVisibilityActionsVariableIds() {
		List<Integer> result = new ArrayList<>();
		result.addAll(firstDelta.getVisibilityActionsVariableIds());
		result.addAll(secondDelta.getVisibilityActionsVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

}
