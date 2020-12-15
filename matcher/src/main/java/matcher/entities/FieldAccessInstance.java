package matcher.entities;

import matcher.entities.deltas.Deletable;
import matcher.entities.deltas.Insertable;

public class FieldAccessInstance implements Insertable, Deletable{

	private String qualifiedName;
	
	private FieldAccessType accessType;

	public FieldAccessInstance(String qualifiedName, FieldAccessType accessType) {
		super();
		this.qualifiedName = qualifiedName;
		this.accessType = accessType;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public FieldAccessType getAccessType() {
		return accessType;
	}
	
	public boolean isFieldRead() {
		return getAccessType() == FieldAccessType.READ;
	}
	
	public boolean isFieldWrite() {
		return getAccessType() == FieldAccessType.WRITE;
	}
}
