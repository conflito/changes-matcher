package matcher.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.deltas.Deletable;
import matcher.entities.deltas.Holder;
import matcher.entities.deltas.Insertable;
import matcher.entities.deltas.Updatable;
import matcher.entities.deltas.Visible;

public class ConstructorInstance implements Insertable, Deletable, Visible, Holder, Updatable{

	private Visibility visibility;

	private List<Type> parameters;

	private ClassInstance classInstance;

	private List<MethodInstance> directDependencies;

	public ConstructorInstance(Visibility visibility) {
		super();
		this.visibility = visibility;
		this.parameters = new ArrayList<>();
		this.directDependencies = new ArrayList<>();
	}

	public ConstructorInstance(Visibility visibility, List<Type> parameters) {
		super();
		this.visibility = visibility;
		this.parameters = parameters;
		this.directDependencies = new ArrayList<>();
	}

	public List<MethodInstance> getDirectDependencies(){
		return directDependencies;
	}

	public void addDirectDependency(MethodInstance m) {
		directDependencies.add(m);
	}
	
	public boolean hasDependencies() {
		return !directDependencies.isEmpty();
	}

	public boolean dependsOn(String methodName) {
		boolean result = 
				directDependencies.stream()
							      .anyMatch(m -> m.getQualifiedName().equals(methodName));
		if(!result) {
			for(MethodInstance m: directDependencies) {
				if(m.dependsOn(methodName))
					result = true;
			}
		}
		return result;
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

	public String getQualifiedName() {
		return classInstance.getQualifiedName() + "." 
				+ classInstance.getName() 
				+ parametersToString();
	}

	public List<String> getInvocationsQualifiedNames() {
		return directDependencies.stream()
								 .map(MethodInstance::getQualifiedName)
								 .collect(Collectors.toList());
	}

	public String getSimpleName() {
		return classInstance.getName() + parametersToString();
	}

	private String parametersToString() {
		return getParameters().toString().replace("[", "(").replace("]", ")");
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
