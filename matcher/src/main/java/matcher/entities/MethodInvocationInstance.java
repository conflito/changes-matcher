package matcher.entities;

import matcher.entities.deltas.Deletable;
import matcher.entities.deltas.Insertable;

public class MethodInvocationInstance implements Insertable, Deletable{

	private String qualifiedName;

	public MethodInvocationInstance(String qualifiedName) {
		super();
		this.qualifiedName = qualifiedName;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}
	
}
