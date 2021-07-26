
public class Client {

    public static boolean testClone(){
        PTTStringsMap<Integer> original = createTree();
        int nextValue = 123;

        original.put("Bob", nextValue++);
        original.put("Alice", nextValue++);
        original.put("Bryan", nextValue++);

        PTTStringsMap<Integer> copy = original.clone();
        return original.equals(copy);
    }

    public static boolean testCloneChangeOriginal(){
        PTTStringsMap<Integer> original = createTree();
        int nextValue = 123;

        original.put("Bob", nextValue++);
        original.put("Alice", nextValue++);
        original.put("Bryan", nextValue++);

        PTTStringsMap<Integer> copy = original.clone();

        original.put("Bryan", nextValue * 10);

        return original.equals(copy);
    }

    public static boolean testCloneChangeCopy(){
        PTTStringsMap<Integer> original = createTree();
        int nextValue = 123;

        original.put("Bob", nextValue++);
        original.put("Alice", nextValue++);
        original.put("Bryan", nextValue++);

        PTTStringsMap<Integer> copy = original.clone();

        copy.put("Bryan", nextValue * 10);

        return original.equals(copy);
    }

    private static PTTStringsMap<Integer> createTree(){
        return new PTTStringsMap<>();
    }
}