package matcher.entities;

public class FieldInstance {

	private String name;
	
	private Visibility visibility;
	
	private Type type;
	
	private ClassInstance classInstance;

	public FieldInstance(String name, Visibility visibility, Type type) {
		super();
		this.name = name;
		this.visibility = visibility;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public Type getType() {
		return type;
	}

	public void setClassInstance(ClassInstance classInstance) {
		this.classInstance = classInstance;
	}
	
	public String getQualifiedName() {
		return getName() + "." + classInstance.getQualifiedName();
	}
	
	
}