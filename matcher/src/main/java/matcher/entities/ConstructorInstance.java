package matcher.entities;

import java.util.ArrayList;
import java.util.List;

public class ConstructorInstance {

	private Visibility visibility;
	
	private List<Type> parameters;
	
	private ClassInstance classInstance;
	
	private List<MethodInvocationInstance> invocations;
	
	public ConstructorInstance(Visibility visibility) {
		super();
		this.visibility = visibility;
		this.parameters = new ArrayList<>();
		this.invocations = new ArrayList<>();
	}
	
	public ConstructorInstance(Visibility visibility, List<Type> parameters) {
		super();
		this.visibility = visibility;
		this.parameters = parameters;
		this.invocations = new ArrayList<>();
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public List<Type> getParameters() {
		return parameters;
	}

	public void setClassInstance(ClassInstance classInstance) {
		this.classInstance = classInstance;
	}
	
	public List<MethodInvocationInstance> getInvocations(){
		return invocations;
	}
	
	public String getQualifiedName() {
		return classInstance.getQualifiedName() + "." 
					  							+ classInstance.getName() 
					  							+ parametersToString();
	}
	
	private String parametersToString() {
		return getParameters().toString().replace("[", "(").replace("]", ")");
	}
	
	public void addMethodInvocation(MethodInvocationInstance invocation) {
		this.invocations.add(invocation);
	}
	
	public boolean equals(Object o) {
		return (this == o) || (o instanceof ConstructorInstance 
				&& equalsConstructorInstance((ConstructorInstance)o));
	}

	private boolean equalsConstructorInstance(ConstructorInstance o) {
		return getQualifiedName().equals(o.getQualifiedName());
	}
	
	public int hashCode() {
		return getQualifiedName().hashCode();
	}

}
