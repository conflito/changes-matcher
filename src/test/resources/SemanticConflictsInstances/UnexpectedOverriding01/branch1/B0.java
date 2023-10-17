public class B0 implements B {
	private int x;
	private int y;
	
	public B0(int n1, int n2) {
		x = n1;
		y = n2;
	}
	
	/* (non-Javadoc)
	 * @see B0#getX()
	 */
	@Override
	public int getX() {
		return x;
	}

	/* (non-Javadoc)
	 * @see B0#getY()
	 */
	@Override
	public int getY() {
		return y;
	}
	
}

