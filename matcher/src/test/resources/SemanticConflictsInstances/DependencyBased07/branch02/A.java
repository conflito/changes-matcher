public class A{

	public int m() {
		return 1;
	}
	
	public int n() {
		return m() + 2;
	}
	
	public int m1() {
		return 10 + n();
	}
}