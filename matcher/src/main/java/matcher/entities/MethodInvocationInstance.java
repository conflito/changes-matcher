package matcher.entities;

/**
 * A class representing a method invocation in the system's domain
 * 
 * @author Nuno Castanho
 *
 */
public class MethodInvocationInstance {
	
	private MethodInstance invokedMethod;

	/**
	 * Creates an instance of MethodInvocationInstance
	 * @param invokedMethod
	 * 			the invoked method
	 */
	public MethodInvocationInstance(MethodInstance invokedMethod) {
		super();
		this.invokedMethod = invokedMethod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((invokedMethod == null) ? 0 : invokedMethod.hashCode());
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
		if (invokedMethod == null) {
			if (other.invokedMethod != null)
				return false;
		} else if (!invokedMethod.equals(other.invokedMethod))
			return false;
		return true;
	}

	/**
	 * Get the invoked method in this invocation
	 * @return the invoked method in this invocation
	 */
	public MethodInstance getInvokedMethod() {
		return invokedMethod;
	}

	/**
	 * Get the qualified name of the invoked method in this invocation
	 * @return the qualified name of the invoked method
	 */
	public String getQualifiedName() {
		return invokedMethod.getQualifiedName();
	}

	
	
}
