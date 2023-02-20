import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p>Modifief sample implementation of
 * <a href="https://en.wikipedia.org/wiki/AVL_tree" title="AVL Tree">AVL Tree</a>.
 * </p>
 * @see <a href="https://en.wikipedia.org/wiki/AVL_tree" title="AVL Tree">AVL Tree</a>
 * @see <a href="https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/avltree/AVLTree.java" title="AVL Tree Implementation">Baeldung AVL Tree Sample Implementation</a>
 */
public class AVLTree<Key extends Comparable<Key>, Value>  {

    public class Node<Key, Value> {
        Key key;
        Value value;
        int height;
        Node<Key, Value> left;
        Node<Key, Value> right;

        Node(Key key, Value value) {
            this.key = key;
            this.value = value;
            this.height = 0;
            this.left = null;
            this.right = null;
        }
    }

    private Node<Key, Value> root;
    private final Comparator<Key> comparator;

    public AVLTree() {
        root = null;
        comparator = new Comparator<Key>() {
            @Override
            public int compare(Key o1, Key o2) {
                return o1.compareTo(o2);
            }
        };
    }

    public AVLTree(Comparator<Key> keyComparator) {
        root = null;
        comparator = keyComparator;
    }

    // public interface for tree size
    // @param  void
    // @return size of tree
    public int size() {
        return size(this.root);
    }

    // public interface for tree size
    // @param  Node node
    // @return size of subtree rooted at node
    private int size(Node<Key, Value> node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    // --------------------------- end boilerplate code ---------------------------

    /**
     * <p>Non-recursive implementation of search for an AVL Tree</p>
     */
    public Value search(Key key) throws FindException {
        Node<Key, Value> node = root;
        while (node != null) {
            int compareResult = comparator.compare(key, node.key);
            if (compareResult == 0) {
                break;
            }
            node = compareResult < 0 ? node.left : node.right;
        }
        if(node != null)
            return node.value;
        else
            throw new FindException("Key not found.");
    }

    public void insert(Key key, Value value) {
        root = insert(root, key, value);
    }

    public void delete(Key key) {
        root = delete(root, key);
    }

    public Node<Key, Value> getRoot() {
        return root;
    }

    public int height() {
        return root == null ? -1 : root.height;
    }

    private Node<Key, Value> insert(Node<Key, Value> node, Key key, Value value) {
        if (node == null) {
            return new Node<Key, Value>(key, value);
        } else {
            int compareResult = comparator.compare(key, node.key);
            if (compareResult < 0) {
                node.left = insert(node.left, key, value);
            } else if (compareResult > 0) {
                node.right = insert(node.right, key, value);
            } else {
                // duplicate key
                // may throw runtime exception or quietly overwrite value
                node.value = value;
            }
        }
        return rebalance(node);
    }

    private Node<Key, Value> delete(Node<Key, Value> node, Key key) {
        if (node == null) {
            return node;
        } else {
            int compareResult = comparator.compare(key, node.key);
            if (compareResult < 0) {
                node.left = delete(node.left, key);
            } else if (compareResult > 0) {
                node.right = delete(node.right, key);
            } else {
                if (node.left == null || node.right == null) {
                    node = (node.left == null) ? node.right : node.left;
                } else {
                    Node<Key, Value> leftMostChild = leftMostChild(node.right);
                    node.key = leftMostChild.key;
                    node.right = delete(node.right, node.key);
                }
            }
        }
        if (node != null) {
            node = rebalance(node);
        }
        return node;
    }

    private Node<Key, Value> leftMostChild(Node<Key, Value> node) {
        Node<Key, Value> current = node;
        /* loop down to find the leftmost leaf */
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private Node<Key, Value> rebalance(Node<Key, Value> z) {
        updateHeight(z);
        int balance = getBalance(z);
        if (balance > 1) {
            if (height(z.right.right) > height(z.right.left)) {
                z = rotateLeft(z);
            } else {
                z.right = rotateRight(z.right);
                z = rotateLeft(z);
            }
        } else if (balance < -1) {
            if (height(z.left.left) > height(z.left.right)) {
                z = rotateRight(z);
            } else {
                z.left = rotateLeft(z.left);
                z = rotateRight(z);
            }
        }
        return z;
    }

    private Node<Key, Value> rotateRight(Node<Key, Value> y) {
        Node<Key, Value> x = y.left;
        Node<Key, Value> z = x.right;
        x.right = y;
        y.left = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node<Key, Value> rotateLeft(Node<Key, Value> y) {
        Node<Key, Value> x = y.right;
        Node<Key, Value> z = x.left;
        x.left = y;
        y.right = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private void updateHeight(Node<Key, Value> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int height(Node<Key, Value> node) {
        return node == null ? -1 : node.height;
    }

    public int getBalance(Node<Key, Value> node) {
        return (node == null) ? 0 : height(node.right) - height(node.left);
    }
    private List<Node<Key,Value>> inOrder() {
        List<Node<Key,Value>> nodes = new ArrayList<>();
        inOrder(root, nodes);
        return nodes;
    }

    private void inOrder(Node<Key,Value> node, List<Node<Key,Value>> nodes) {
        if(node != null) {
            inOrder(node.left, nodes);
            nodes.add(node);
            inOrder(node.right, nodes);
        }
    }

    // Java String is immutable!
    @Override
    public String toString() {
        return "AVL Tree of size "+ this.size()+" and height "+ this.height();
    }

}