package matcher.entities;

import java.util.ArrayList;
import java.util.List;

public class MethodInstance {

	private String name;
	
	private Visibility visibility;
	
	private Type returnType;
	
	private List<Type> parameters;
	
	private List<MethodInvocationInstance> invocations;
	
	private List<FieldAccessInstance> fieldAccesses;
	
	private ClassInstance classInstance;

	public MethodInstance(String name, Visibility visibility, Type returnType) {
		super();
		this.name = name;
		this.visibility = visibility;
		this.returnType = returnType;
		this.parameters = new ArrayList<>();
		this.invocations = new ArrayList<>();
		this.fieldAccesses = new ArrayList<>();
	}

	public MethodInstance(String name, Visibility visibility, Type returnType, 
			List<Type> parameters) {
		super();
		this.name = name;
		this.visibility = visibility;
		this.returnType = returnType;
		this.parameters = parameters;
		this.invocations = new ArrayList<>();
		this.fieldAccesses = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public Type getReturnType() {
		return returnType;
	}

	public List<Type> getParameters() {
		return parameters;
	}

	public List<MethodInvocationInstance> getInvocations() {
		return invocations;
	}

	public List<FieldAccessInstance> getFieldAccesses() {
		return fieldAccesses;
	}

	public void setClassInstance(ClassInstance classInstance) {
		this.classInstance = classInstance;
	}
	
	public String getQualifiedName() {
		return classInstance.getQualifiedName() + "." + getName() + parametersToString();
	}

	private String parametersToString() {
		return getParameters().toString().replace("[", "(").replace("]", ")");
	}
	
	public boolean isCompatibleWith(MethodInstance m) {
		if(m == null || !getName().equals(m.getName())) {
			return false;
		}
		List<Type> thisParameters = getParameters();
		List<Type> mParameters = m.getParameters();
		if(thisParameters.size() != mParameters.size()) {
			return false;
		}
		for(int i = 0; i < thisParameters.size(); i++) {
			Type t1 = thisParameters.get(i);
			Type t2 = mParameters.get(i);
			if(!t1.isSubtypeOf(t2))
				return false;
		}
		return true;
	}
	
	public void addParameter(Type type) {
		this.parameters.add(type);
	}
	
	public void addFieldAccess(FieldAccessInstance access) {
		this.fieldAccesses.add(access);
	}
	
	public void addMethodInvocation(MethodInvocationInstance invocation) {
		this.invocations.add(invocation);
	}
}
