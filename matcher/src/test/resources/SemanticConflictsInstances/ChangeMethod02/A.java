
public class A{

	private B1 b;

	public A() {
		b = new B1(1,1);
	}

	public boolean n(B v) {
		return m2(v);
	}

	public boolean m1(B v){
		return b.hashCode() > v.hashCode();
	}

	public boolean m2(B v) {
		return b.getX()+b.getY() > v.getX()+v.getY();
	}
}