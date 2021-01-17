package matcher.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.ChangeInstance;
import matcher.entities.deltas.DeltaInstance;
import matcher.patterns.deltas.DeltaPattern;
import matcher.utils.Pair;

public class ConflictPattern {
	
	private BasePattern basePattern;
	
	private List<DeltaPattern> deltaPatterns;
	
	private List<Pair<FreeVariable, FreeVariable>> differentVariables;

	
	public ConflictPattern() {
		this.deltaPatterns = new ArrayList<>();
		this.differentVariables = new ArrayList<>();
	}
	
	public void setBasePattern(BasePattern basePattern) {
		this.basePattern = basePattern;
	}
	
	public void addDeltaPattern(DeltaPattern deltaPattern) {
		this.deltaPatterns.add(deltaPattern);
	}
	
	public void addDifferentVariablesRule(FreeVariable v1, FreeVariable v2) {
		this.differentVariables.add(new Pair<>(v1, v2));
	}

	public boolean hasInvocations() {
		return basePattern.hasInvocations();
	}

	public boolean hasFieldAccesses() {
		return basePattern.hasFieldAccesses();
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
		return deltaPatterns.stream().anyMatch(d -> d.hasInsertInvocationActions());
	}
	
	public boolean hasInsertMethodActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasInsertMethodActions());
	}
	
	public boolean hasInsertConstructorActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasInsertConstructorActions());
	}
	
	public boolean hasInsertFieldActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasInsertFieldActions());
	}
	
	public boolean hasInsertFieldAccessActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasInsertFieldAccessActions());
	}
	
	public boolean hasInsertClassActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasInsertClassActions());
	}
	
	public boolean hasDeleteInvocationActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasDeleteInvocationActions());
	}
	
	public boolean hasDeleteFieldActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasDeleteFieldActions());
	}
	
	public boolean hasDeleteMethodActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasDeleteMethodActions());
	}
	
	public boolean hasDeleteConstructorsActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasDeleteConstructorsActions());
	}
	
	public boolean hasDeleteFieldAccessActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasDeleteFieldAccessActions());
	}
	
	public boolean hasUpdateActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasUpdateActions());
	}
	
	public boolean hasUpdateFieldTypeActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasUpdateFieldTypeActions());
	}
	
	public boolean hasUpdateInvocationActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasUpdateInvocationActions());
	}
	
	public boolean hasVisibilityActions() {
		return deltaPatterns.stream().anyMatch(d -> d.hasVisibilityActions());
	}
	
	public boolean matches(ChangeInstance instance) {
		return basePattern.filled() && deltasFilled() &&
			   basePattern.matches(instance.getBaseInstance()) &&
			   deltasMatch(instance) &&
			   differentVariableMatch();
	}
	
	private boolean differentVariableMatch() {
		return differentVariables.stream().allMatch(p ->
				p.getFirst().getValue() != p.getSecond().getValue());
	}

	private boolean deltasFilled() {
		return deltaPatterns.stream().allMatch(d -> d.filled());
	}

	private boolean deltasMatch(ChangeInstance instance) {
		return deltaPatterns.stream().allMatch(dp -> 
				deltaPatternMatchesOne(dp, instance.getDeltaInstances()));
	}
	
	private boolean deltaPatternMatchesOne(DeltaPattern pattern, List<DeltaInstance> instances) {
		return instances.stream().anyMatch(d -> pattern.matches(d));
	}
	
	public void setVariableValue(int id, String value) {
		basePattern.setVariableValue(id, value);
		deltaPatterns.forEach(d -> d.setVariableValue(id, value));
		for(Pair<FreeVariable, FreeVariable> p: differentVariables) {
			if(p.getFirst().isId(id))
				p.getFirst().setValue(value);
			if(p.getSecond().isId(id))
				p.getSecond().setValue(value);
		}
	}
	
	public void clean() {
		basePattern.clean();
		deltaPatterns.forEach(d -> d.clean());
	}
	
	public String toStringDebug() {
		StringBuilder result = new StringBuilder();
		result.append(basePattern.toStringDebug() + "\n----------------\n");
		deltaPatterns.forEach(d -> result.append(d.toStringDebug() + "\n----------------\n"));
		return result.toString();
	}
	
	public String toStringFilled() {
		StringBuilder result = new StringBuilder();
		result.append(basePattern.toStringFilled() + "\n----------------\n");
		deltaPatterns.forEach(d -> result.append(d.toStringFilled() + "\n----------------\n"));
		return result.toString();
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
		deltaPatterns.forEach(d -> result.addAll(d.getFieldsVariableIds()));
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getDeltaFieldTypesVariableIds(){
		List<Integer> result = new ArrayList<>();
		deltaPatterns.forEach(d -> result.addAll(d.getFieldTypesVariableIds()));
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getDeltaClassesVariableIds() {
		List<Integer> result = new ArrayList<>();
		deltaPatterns.forEach(d -> result.addAll(d.getClassesVariableIds()));
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getDeltaMethodsVariableIds() {
		List<Integer> result = new ArrayList<>();
		deltaPatterns.forEach(d -> result.addAll(d.getMethodsVariableIds()));
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getDeltaConstructorsVariableIds() {
		List<Integer> result = new ArrayList<>();
		deltaPatterns.forEach(d -> result.addAll(d.getConstructorsVariableIds()));
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getDeltaInvocationsVariableIds() {
		List<Integer> result = new ArrayList<>();
		deltaPatterns.forEach(d -> result.addAll(d.getInvocationsVariableIds()));
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getDeltaFieldAccessVariableIds() {
		List<Integer> result = new ArrayList<>();
		deltaPatterns.forEach(d -> d.getFieldAccessesVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getUpdatedVariableIds() {
		List<Integer> result = new ArrayList<>();
		deltaPatterns.forEach(d -> d.getUpdatesVariableIds());
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getUpdatedFieldsVariableIds(){
		List<Integer> result = new ArrayList<>();
		deltaPatterns.forEach(d -> result.addAll(d.getUpdatedFieldsVariableIds()));
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public List<Integer> getUpdatedInvocationsVariableIds(){
		List<Integer> result = new ArrayList<>();
		deltaPatterns.forEach(d -> result.addAll(d.getUpdatedInvocationsVariableIds()));
		return result.stream().distinct().collect(Collectors.toList());
	}

	public List<Integer> getVisibilityActionsVariableIds() {
		List<Integer> result = new ArrayList<>();
		deltaPatterns.forEach(d -> result.addAll(d.getVisibilityActionsVariableIds()));
		return result.stream().distinct().collect(Collectors.toList());
	}

}
