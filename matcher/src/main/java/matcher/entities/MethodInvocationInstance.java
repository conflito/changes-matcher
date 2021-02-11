package matcher.entities;

import matcher.entities.deltas.Deletable;
import matcher.entities.deltas.Insertable;
import matcher.entities.deltas.Updatable;

public class MethodInvocationInstance implements Insertable, Deletable, Updatable{

	private String qualifiedName;

	public MethodInvocationInstance(String qualifiedName) {
		super();
		this.qualifiedName = qualifiedName;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((qualifiedName == null) ? 0 : qualifiedName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MethodInvocationInstance))
			return false;
		MethodInvocationInstance other = (MethodInvocationInstance) obj;
		if (qualifiedName == null) {
			if (other.qualifiedName != null)
				return false;
		} else if (!qualifiedName.equals(other.qualifiedName))
			return false;
		return true;
	}
	
}
