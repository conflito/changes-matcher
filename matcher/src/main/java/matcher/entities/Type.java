package matcher.entities;

import spoon.reflect.reference.CtTypeReference;

public class Type {

	private CtTypeReference<?> type;

	public Type(CtTypeReference<?> type) {
		super();
		this.type = type;
	}

	public String getTypeName() {
		return type.toString();
	}
	
	public boolean isSubtypeOf(Type t) {
		return type.isSubtypeOf(t.type);
	}
	
	
}
