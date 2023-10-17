
public class A{

	private B b;

	public  A(B b ) {
		this.b = b;
	}
	
	public boolean n(A other ) {
		return b.equals(other);
	}

}