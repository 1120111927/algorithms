package yadong0305.algorithms.ST;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 一棵二叉查找树（BST）是一棵二叉树，其中每个结点都含有一个Comparable的键（以及相关联的值）且每个结点的键都大于其左子树中的任意结点的键而小于右子树的任意结点的键
 * @param <Key>
 * @param <Value>
 */

public class BST<Key extends Comparable<Key>, Value> extends ST<Key, Value>{

    private Node root;

    /**
     * 嵌套定义一个私有类来表示二叉查找树上的一个结点，每个结点都含有一个键、一个值、一条左链接、一条右链接和一个结点计数器。左链接指向一棵由小于该结点的所有键组成的二叉查找树，右链接指向一棵由大于该结点的所有键组成的二叉查找树。变量N给出了以该结点为根的子树的结点总数
     */
    private class Node {
        private Key key;
        private Value value;
        private Node left;
        private Node right;
        int N;

        public Node(Key key, Value value, int N) {
            this.key = key;
            this.value = value;
            this.N = N;
        }
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.N;
    }

    /**
     * 递归的put()方法
     *   如果树是空的，就返回一个含有该键值对的新结点；
     *   如果被查找的键小于根结点的键，继续在左子树中插入该键，否则在右子树中插入该键
     * @param key
     * @param value
     */

    @Override
    public void put(Key key, Value value) {
        root = put(root, key, value);
    }

    /**
     * 可以将递归调用前的代码想像成沿着树向下走：它会将给定的键和每个结点的键相比较并根据结果向左或者向右移动到下一个结点。然后可以将递归调用后的代码想象成沿着树向上爬。对于get()方法，这对应着一系列的返回指令，但是对于put()方法，这意味着重置搜索路径上每个父结点指向子结点的链接，并增加路径上每个结点中的计数器的值。
     */
    private Node put(Node x, Key key, Value value) {
        if (x == null) return new Node(key, value, 1);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, value);
        else if (cmp > 0) x.right = put(x.right, key, value);
        else x.value = value;
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * 在二叉查找树中查找一个键的递归算法：
     *   如果树是空的，则查找未命中；
     *   如果被查找的键和根结点的键相等，查找命中，否则就递归的在合适的子树中继续查找。
     *   如果被查找的键较小就选择左子树，较大则选择右子树
     * @param key
     * @return
     */
    @Override
    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp<0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.value;
    }

    @Override
    public boolean contains(Key key) {
        return get(key) == null;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * 如果根结点为空，那么一棵二叉查找树中最小的键就是根结点，如果左链接非空，那么树中最小的键就是左子树中的最小键
     * @return
     */
    public Key min() {
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    /**
     * 如果根结点为空，那么一棵二叉查找树中最大的键就是根结点，如果左链接非空，那么树中最大的键就是右子树中的最大键
     * @return
     */
    public Key max() {
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        return max(x.right);
    }

    /**
     * 如果给定的键key小于二叉查找树的根结点的键，那么小于等于key的最大键floor(key)一定在根结点的左子树中；
     * 如果给定的键key大于二叉查找树的根结点的键，那么只有当根结点的右子树中存在小于等于key的结点时，小于等于key的最大键才会出现在右子树中，否则根结点就是小于等于key的最大键
     * @param key
     * @return
     */
    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) return null;
        return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null) return t;
        return x;
    }

    /**
     * 如果给定的键key大于二叉查找树根结点的键，那么大于等于key的最小键ceil(key)一定在根结点的右子树中；
     * 如果给定的键key小于二叉查找树根结点的键，那么只有当根结点的左子树中存在大于等于key的结点时，大于等于key的最小键才会在左子树中出现，否则根结点就是大于等于key的最小键
     */
    public Key ceil(Key key) {
        Node x = ceil(root, key);
        if (x == null) return null;
        return x.key;
    }

    private Node ceil(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return ceil(x.right, key);
        Node t = ceil(x.left, key);
        if (t != null) return t;
        return x;
    }

    /**
     * 二叉查找树的选择操作：
     *   假设要找到排名为k的键（即树中正好有k个小于它的键），如果左子树中的结点树t大于k，那么就继续（递归地）在左子树中查找排名为k的键；
     *   如果t等于k，那么就返回根结点中的键；
     *   如果t小于k，就（递归地）在右子树中查找排名为(k-t-1)的键
     */
    public Key select(int k) {
        if (k >= size(root)) return null;
        return select(root, k).key;
    }

    private Node select(Node x, int k) {
        int t = size(x.left);
        if (t > k) return select(x.left, k);
        else if (t < k) return select(x.right, k-t-1);
        else return x;
    }

    /**
     * 递归方法接受一个指向结点的链接，并返回一个指向结点的链接，这样就能够很方便的改变树的结构，将返回的链接赋给作为参数的链接。
     * 对于deleteMin()，要不断地深入根结点的左子树中直至遇见一个空链接，然后将指向该结点的链接指向该结点的右子树（只需要在递归调用中返回它的右链接即可）。
     */
    public void deleteMin() {
        root = deleteMin(root);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * 对于deleteMax()，要不断地深入根结点的右子树直至遇见一个空链接，然后将指向该结点的链接指向该结点的左子树（只需要在递归调用中返回它的左链接即可）。
     */
    public void deleteMax() {
        root = deleteMax(root);
    }

    private Node deleteMax(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMax(x.right);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * 如果被删除结点没有子结点，返回null；
     * 如果被删除结点只有左子结点，返回左子结点；
     * 如果被删除结点只有右子结点，返回右子结点；
     * 如果被删除结点有两个子结点，在删除结点x后用它的后继结点填补它的位置，因为x有一个右子结点，因此它的后继结点就是其右子树中的最小结点（或者左子树中的最大结点）。这样的替换操作仍能够保证树的有序性。能够用四个简单的步骤将x替换为它的后继结点：
     *   1. 将指向即将被删除的结点的链接保存为t；
     *   2. 将x指向它的后继结点min(t.right);
     *   3. 将x的右链接（原本指向一棵所有结点都大于x.key的二叉查找树）指向deleteMin(t.right)。也就是在删除后所有结点仍然都大于x.key的子二叉查找树；
     *   4. 将x的左链接（本为空）设为t.left（其下所有的键都小于被删除的结点和它的后继结点）
     * 在递归调用后修正被删除的结点的父结点的链接，并将由此结点到根结点的路径上的所有结点的计数器减1
     */

    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp > 0) x.right = delete(x.right, key);
        else if (cmp < 0) x.left = delete(x.left, key);
        else {
            if (x.right == null) return x.left;
            else if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * 要实现能够返回给定范围内键的keys()方法，首先需要一个遍历二叉查找树的基本方法，叫做中序遍历。将所有落在给定范围以内的键加入一个队列Queue并跳过那些不可能含有所查找键的子树
     */

    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new LinkedList<>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {

        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.add(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }
}