package branch01;

public class Square extends Shape{

	private int x;
	
	public Square() {
		move(0, 0);
		int n = x;
	}
	
	public void move(int dx, int dy) {
		int n = x;
		x = 10;
	}
	
	public void reset() {
		move(0, 0);
	}
}
