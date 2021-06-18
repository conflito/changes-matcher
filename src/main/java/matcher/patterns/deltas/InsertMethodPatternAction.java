package matcher.patterns.deltas;

import java.util.ArrayList;
import java.util.List;

import matcher.entities.MethodInstance;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertMethodAction;
import matcher.patterns.ClassPattern;
import matcher.patterns.MethodPattern;

public class InsertMethodPatternAction extends InsertPatternAction {

	private MethodPattern insertedMethodPattern;
	
	private ClassPattern holderClassPattern;
	
	private List<MethodPattern> compatibles;
	
	public InsertMethodPatternAction(MethodPattern insertedMethodPattern, ClassPattern holderClassPattern) {
		super();
		this.insertedMethodPattern = insertedMethodPattern;
		this.holderClassPattern = holderClassPattern;
		this.compatibles = new ArrayList<>();
	}

	public ActionPattern makeCopy() {
		MethodPattern methodCopy = new MethodPattern(insertedMethodPattern);
		ClassPattern classCopy = new ClassPattern(holderClassPattern);
		InsertMethodPatternAction result = new InsertMethodPatternAction(methodCopy, classCopy);
		for(MethodPattern m: compatibles) {
			result.addCompatible(new MethodPattern(m));
		}
		return result;
	}

	public void addCompatible(MethodPattern methodPattern) {
		compatibles.add(methodPattern);
	}
	
	public int getInsertedMethodVariableId() {
		return insertedMethodPattern.getVariableId();
	}
	
	public boolean hasInvocations() {
		return insertedMethodPattern.hasInvocations();
	}
	
	public boolean hasFieldAccesses() {
		return insertedMethodPattern.hasFieldAccesses();
	}
	
	@Override
	public void setVariableValue(int id, String value) {
		insertedMethodPattern.setVariableValue(id, value);
		holderClassPattern.setVariableValue(id, value);
		compatibles.forEach(m -> m.setVariableValue(id, value));
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertMethodAction && filled() && 
				matches((InsertMethodAction)action);
	}
	
	private boolean matches(InsertMethodAction action) {
		return getAction() == action.getAction() &&
			   insertedMethodPattern.matches(action.getInsertedMethod()) &&
			   holderClassPattern.matches(action.getHolderClass()) &&
			   compatiblesMatch(action);
	}
	
	private boolean compatiblesMatch(InsertMethodAction action) {
		return compatibles.stream().allMatch(v -> matchesOne(v, action.getCompatibles()));
	}

	private boolean matchesOne(MethodPattern pattern, List<MethodInstance> compatibles) {
		return compatibles.stream().anyMatch(m -> pattern.matches(m));
	}
	
	@Override
	public void clean() {
		insertedMethodPattern.clean();
		holderClassPattern.clean();
		compatibles.forEach(m -> m.clean());
	}

	@Override
	public boolean filled() {
		return insertedMethodPattern.filled() && holderClassPattern.filled() &&
				compatibles.stream().allMatch(m -> m.filled());
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Insert method $" + insertedMethodPattern.getVariableId());
		result.append(" in class $" + holderClassPattern.getVariableId() + "\n");
		result.append(insertedMethodPattern.toString());
		for(MethodPattern mp: compatibles) {
			result.append("Method $" + insertedMethodPattern.getVariableId());
			result.append(" compatible with method $" + mp.getVariableId() + "\n");
		}
		return result.toString();
	}
	
}
