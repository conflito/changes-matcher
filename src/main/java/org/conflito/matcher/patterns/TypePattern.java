package org.conflito.matcher.patterns;

import org.conflito.matcher.entities.Type;

public class TypePattern {

	FreeVariable typeVar;

	public TypePattern(FreeVariable typeVar) {
		super();
		this.typeVar = typeVar;
	}
	
	public TypePattern(TypePattern typePattern) {
		super();
		this.typeVar = new FreeVariable(typePattern.typeVar);
	}

	public int getVariableId() {
		return typeVar.getId();
	}

	public boolean hasVariableId(int id) {
		return typeVar.isId(id);
	}

	public void setVariableValue(int id, String value) {
		if(typeVar.isId(id))
			typeVar.setValue(value);
	}

	public void clean() {
		typeVar.clean();
	}

	public boolean filled() {
		return typeVar.hasValue();
	}
	
	public boolean matches(Type typeInstance) {
		return filled() && typeMatch(typeInstance);
	}

	private boolean typeMatch(Type typeInstance) {
		if(typeInstance.isArray())
			return typeInstance.getArrayType().toString().equals(typeVar.getValue());
		else
			return typeInstance.toString().equals(typeVar.getValue());
	}

}
