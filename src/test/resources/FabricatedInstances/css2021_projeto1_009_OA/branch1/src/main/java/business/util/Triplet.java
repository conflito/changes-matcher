package business.util;

public class Triplet<K, V, T>{

    private K first;
    private V second;
    private T third;

    public Triplet(K first, V second, T third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public K getValue0(){
        return first;
    }

    public V getValue1(){
        return second;
    }

    public T getValue2(){
        return third;
    }

}