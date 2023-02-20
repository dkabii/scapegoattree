import java.lang.module.FindException;

public class Main {
    public static void main(String[] args) throws FindException {
        AVLTree<Integer, String> T = new AVLTree<>();
        for(int i = 0; i<20000; i++) {
            T.insert(i, String.valueOf(i));
        }
        System.out.println(T);
        System.out.println(T.search(200001));
    }
}