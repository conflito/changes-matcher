public class C {

    private A a;

    public C(){
        a = new B();
    }

    public int k(){
    	a.move(10, 10);
        return 2;
    }
}
