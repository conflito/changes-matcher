package matcher.patterns.deltas;

import java.util.ArrayList;
import java.util.List;

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
	
	public boolean hasDeleteActions() {
		return actions.stream().anyMatch(a -> a instanceof DeletePatternAction); 
	}
	
	public boolean hasUpdateActions() {
		return actions.stream().anyMatch(a -> a instanceof UpdatePatternAction);
	}
	
	public boolean hasVisibilityActions() {
		return actions.stream().anyMatch(a -> a instanceof VisibilityActionPattern);
	}
	
}
