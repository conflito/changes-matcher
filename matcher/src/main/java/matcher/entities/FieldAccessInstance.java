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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessType == null) ? 0 : accessType.hashCode());
		result = prime * result + ((qualifiedName == null) ? 0 : qualifiedName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FieldAccessInstance))
			return false;
		FieldAccessInstance other = (FieldAccessInstance) obj;
		if (accessType != other.accessType)
			return false;
		if (qualifiedName == null) {
			if (other.qualifiedName != null)
				return false;
		} else if (!qualifiedName.equals(other.qualifiedName))
			return false;
		return true;
	}
	
	
}
