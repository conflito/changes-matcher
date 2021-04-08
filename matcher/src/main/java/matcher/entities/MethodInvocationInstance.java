package matcher.entities;

public class MethodInvocationInstance {
	
	private MethodInstance invokedMethod;

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

	public MethodInstance getInvokedMethod() {
		return invokedMethod;
	}

	public String getQualifiedName() {
		return invokedMethod.getQualifiedName();
	}

	
	
}
