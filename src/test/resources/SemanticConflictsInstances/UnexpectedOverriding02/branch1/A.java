
public class A{

	private B b;

	public  A(B b) {
		this.b = b;
	}

	public int n() {
		return b.m();
	}
}