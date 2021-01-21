package matcher.entities;

public class InterfaceInstance {

	private String name;

	public InterfaceInstance(String name) {
		super();
		this.name = name;
	}

	public String getQualifiedName() {
		return name;
	}
	
	public String toStringDebug() {
		return "Interface " + name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof InterfaceInstance 
				&& equalsInterface((InterfaceInstance) obj));
	}
	
	private boolean equalsInterface(InterfaceInstance i) {
		return name.equals(i.getQualifiedName());
	}
	
	
}
