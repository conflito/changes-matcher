public class A{

    public int m(){
		return m1() + 20;
    }

	public int m1() {
		return 1;
	}
	
	public int k() {
		return 2 * m();
	}

}