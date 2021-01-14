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
	
}
