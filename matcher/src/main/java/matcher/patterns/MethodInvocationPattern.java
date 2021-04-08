package matcher.patterns;

import matcher.entities.MethodInvocationInstance;

public class MethodInvocationPattern {

	private FreeVariable freeVariable;

	public MethodInvocationPattern(FreeVariable freeVariable) {
		super();
		this.freeVariable = freeVariable;
	}
	
	public MethodInvocationPattern(MethodInvocationPattern methodInvocationPattern) {
		super();
		this.freeVariable = new FreeVariable(methodInvocationPattern.freeVariable);
	}
	
	public FreeVariable getFreeVariable() {
		return freeVariable;
	}

	public int getVariableId() {
		return freeVariable.getId();
	}
	
	public boolean isVariableId(int id) {
		return freeVariable.isId(id);
	}
	
	public void setVariableValue(int id, String value) {
		if(isVariableId(id))
			freeVariable.setValue(value);
	}
	
	public void clean() {
		freeVariable.clean();
	}
	
	public boolean filled() {
		return freeVariable.hasValue();
	}
	
	public boolean matches(MethodInvocationInstance instance) {
		return filled() && (sameName(instance) || 
				instance.getInvokedMethod().dependsOn(freeVariable.getValue()));
	}

	private boolean sameName(MethodInvocationInstance instance) {
		return instance.getQualifiedName().equals(freeVariable.getValue());
	}

}
