package matcher.entities;

import matcher.entities.deltas.Deletable;
import matcher.entities.deltas.Holder;
import matcher.entities.deltas.Insertable;
import matcher.entities.deltas.Visible;

public class FieldInstance implements Insertable, Deletable, Visible, Holder{

	private String name;
	
	private Visibility visibility;
	
	private Type type;

	public FieldInstance(String name, Visibility visibility, Type type) {
		super();
		this.name = name;
		this.visibility = visibility;
		this.type = type;
	}

	public String getQualifiedName() {
		return name;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public Type getType() {
		return type;
	}
	
	public boolean equals(Object o) {
		return (this == o) || (o instanceof FieldInstance && equalsFieldInstance((FieldInstance)o));
	}

	private boolean equalsFieldInstance(FieldInstance o) {
		return getQualifiedName().equals(o.getQualifiedName());
	}
	
	public int hashCode() {
		return getQualifiedName().hashCode();
	}
	
}
