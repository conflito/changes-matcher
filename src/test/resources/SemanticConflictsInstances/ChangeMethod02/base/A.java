
public class A{

	private B1[] v = new B1[2];

	public A() {
		v[0] = new B1(1,1);
		v[1] = new B1(2,2);
	}

	public boolean n() {
		return m2(v);
	}

	public static boolean m1(B[]  v){
		return v[0].hashCode() > v[1].hashCode();
	}

	public static boolean m2(B[] v) {
		return v[0].getX()+v[0].getY() > v[1].getX()+v[1].getY();
	}
}