public class B2 extends B1{
	private int z;
	
	public B2(int n1, int n2, int n3) {
		super(n1,n2);
		z = n3;
	}
	
	@Override
	public int m() {
		return super.m() + z;
	}
	
}

