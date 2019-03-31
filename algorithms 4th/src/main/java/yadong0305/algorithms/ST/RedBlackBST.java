package yadong0305.algorithms.ST;

import com.sun.org.apache.regexp.internal.RE;

/**
 * 一棵2-3查找树或为一棵空树，或由以下结点组成：
 *   2-结点：含有一个键（及其对应值）和两条链接，左链接指向的2-3树中的键都小于该结点，右链接指向的2-3树的键都大于该结点
 *   3-结点：含有两个键（及其对应的值）和三条链接，左链接指向的2-3树中的键都小于该结点，中链接指向的2-3树中的键都位于该结点的两个键之间，右链接指向的2-3树中的键都大于该结点。
 *
 *   一棵完美的2-3查找树中的所有空链接到根结点的距离都应该是相同的。
 *
 *   使用红黑二叉树实现2-3查找树。红黑二叉树背后的基本思想是用标准的二叉查找树（完全由2-结点构成）和一些额外的信息（替换3-结点）来表示2-3树。将树中的链接分为两种类型：红链接将两个2-结点连接起来构成一个3-结点，黑链接则是2-3树中的普通链接。
 *
 *   红黑树的另一种定义是含有红黑链接并满足下列条件的二叉查找树：
 *     红链接均为左链接；
 *     没有任何一个结点同时和两条红链接相连；
 *     该树是完美黑色平衡的，即任意空链接到根结点的路径上的黑链接数量相同。
 *   满足这样定义的红黑树和相应的2-3树是一一对应的。
 *
 *   将链接的颜色保存在表示结点的Node数据类型的布尔变量color中。如果指向它的链接是红色的，那么该变量为true，黑色则为false，约定空链接为黑色。
 * @param <Key>
 * @param <Value>
 */
public class RedBlackBST<Key extends Comparable<Key>, Value> extends ST<Key, Value> {

    private Node root;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        Key key;    // 键
        Value value;    // 相关联的值
        Node left;    // 左子树
        Node right;    // 右子树
        int N;    // 这棵子树中的结点总数
        boolean color;    // 由其父结点指向它的链接的颜色

        Node(Key key, Value value, int N, boolean color) {
            this.key = key;
            this.value = value;
            this.N = N;
            this.color = color;
        }
    }

    /**
     * 测试一个结点和它的父结点之间的链接的颜色
     * @param x
     * @return
     */
    public boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.N;
    }

    /**
     * 要在2-3查找树中插入一个新结点，先进行一次未命中的查找，
     *   如果未命中的查找结束于一个2-结点，只要把这个2-结点替换为一个3-结点，将要插入的键保存在其中即可；
     *   如果为命中的查找结束于一个3-结点，分以下情况：
     *     + 向一棵只含有一个3-结点的树中插入新键：先临时将新键存入该结点中，使之成为一个4-结点，然后将它转换为一棵由3个2-结点组成的2-3树，其中一个结点（根）含有中键，一个结点含有3个键中的最小者（和根结点的左链接相连），一个结点含有3个键中的最大者（和根结点的右链接相连）
     *     + 向一个父结点为2-结点的3-结点中插入新键：构造一个临时的4-结点并将其分解，此时不会为中键创建一个新结点，而是将其移动至原来的父结点中，即将指向原3-结点的一条链接替换为新父结点中的原中键左右两边的两条链接，并分别指向两个新的2-结点
     *     + 向一个父结点为3-结点的3-结点中插入新键：构造一个临时的4-结点并分解它，然后将它的中键插入它的父结点中。但是父结点也是一个3-结点，再用这个中键构造一个新的临时4-结点，然后在这个结点上进行相同的变换，及分解这个父结点并将它的中键插入到它的父结点中去。一直向上不断分解临时的4-结点并将中键插入到更高层的父结点，直至遇到一个2-结点并将它替换为一个不需要继续分解的3-结点，或是到达3-结点的根。
     *     + 分解根结点：如果从插入结点到根结点的路径上全都是3-结点，根结点最终变成一个临时的4-结点。可以按照向一棵只有一个3-结点的树中插入新键的方法处理这个问题，将临时的4-结点分解为3个2-结点，使得树高增加1。
     *
     *  局部变换：将一个4-结点分解为一棵2-3树可能有6中情况，这个4-结点可能是根结点，可能是一个2-结点的左子结点或者右子结点，也可能是一个3-结点的左子结点、中子结点或者右子结点。2-3树插入算法的根本在于这些变换都是局部的：除了相关的结点和链接之外不必修改或者检查树的其他部分。不光是在树的底部，树中的任何地方只要符合相应的模式，变换都可以进行。每个变换都会将4-结点中的一个键送入它的父结点中，并重构相应的链接而不必涉及树的其他部分。
     *
     *  全局性质：这些局部变换不会影响树的全局有序性和平衡性，任意空链接到根结点的路径长度都是相等的。所有局部变换都不会影响整棵树的有序性和平衡性。
     *
     * 在插入新的键时可以使用旋转操作保证2-3树和红黑树之间的一一对应关系，因为旋转操作可以保持红黑树的两个重要性质：有序性和完美平衡性。
     * 使用旋转操作保持红黑树的另外两个重要性质（不存在两天连续的红链接和不存在红色的右链接）。
     * 向单个2-结点中插入新键：
     *   一棵只含有一个键的红黑树只含有一个2-结点。插入另一个键之后，需要将它们旋转。如果新键小于老键，只需要新增一个红色的结点即可，新的红黑树和单个3-结点完全等价。如果新键大于老键，那么新增的红色结点将会产生一条红色的右链接，需要使用root = rotateLeft(root)来将其旋转为左链接并修正根结点的链接，插入操作才算完成。
     * 向树底的2-结点插入新键：
     *   用和二叉查找树相同的方式向一棵红黑树中插入一个新键会在树的底部新增一个结点，但总是用红链接将新结点和它的父结点相连。如果指向新结点的是父结点的左链接，那么父结点就直接成为了一个3-结点；如果指向新结点的是父结点的右链接，这就是一个错误的3-结点，一次左旋转就能够修正它
     * 向一棵双键树中插入新键：
     *    新键大于原树中的两个键，因此它被连接到3-结点的右链接。此时树是平衡的，根结点为中间大小的键，它有两条红链接分别和较小和较大的结点相连，如果将两条链接的颜色都由红变黑，那么就得到了一棵由三个结点组成的、高为2的平衡树。
     *    如果新键小于原树中的两个键，它会被连接到最左边的空链接，这样就产生了两条连续的红链接，此时只需要将上层的红链接右旋转即可得到第一种情况
     *    如果新键介于原树中的两个键之间，会产生两条连续的红链接，一条红色左链接接一条红色右链接，只需要将下层的红链接左旋转即可得到第二种情况。
     *
     * 只要谨慎的使用左旋转、右旋转和颜色转换，就能够保证插入操作后红黑树和2-3树的一一对应关系。在沿着插入点到根结点的路径向上移动时在所经过的每个结点中顺序完成以下操作，就能完成插入操作：
     *   如果右子结点是红色的而左子结点是黑色的，进行左旋转；
     *   如果左子结点是红色的且它的左子结点也是红色的，进行右旋转；
     *   如果左右子结点均为红色，进行颜色转换。
     * @param key
     * @param value
     */
    @Override
    public void put(Key key, Value value) {
        root = put(root, key, value);
        root.color = BLACK;
    }

    private Node put(Node h, Key key, Value value) {
        if (h == null) return new Node(key, value, 1, RED);
        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = put(h.left, key, value);
        else if (cmp > 0) h.right = put(h.right, key, value);
        else h.value = value;
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.N = size(h.left) + size(h.right) + 1;
        return h;
    }

    /**
     * 左旋转：将一条红色的右链接转化为左链接。
     * rotateLeft()接收一条指向红黑树中的某个结点的链接作为参数，假设被指向的结点的右链接是红色的，这个方法会对树进行必要的调整（将用两个键中的较小者作为根结点变为较大者作为根结点）并返回一个指向包含同一组键的子树且其左链接为红色的根结点的链接。
     */
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.N = h.N;
        h.N = size(h.left) + size(h.right) + 1;
        return x;
    }

    /**
     * 右旋转
     * @param
     * @return
     */
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.N = h.N;
        h.N = size(h.left) + size(h.right) + 1;
        return x;
    }

    /**
     * flipColors()用来转换一个结点的两个红色子结点的颜色。将子结点的颜色由红变黑，将父结点的颜色由黑变红
     */
    private void flipColors(Node h) {
        h.color = RED;
        h.left.color = BLACK;
        h.right.color =BLACK;
    }

    /**
     * 将二叉查找树的查找算法一般化就能够直接得到2-3树的查找算法。要判断一个键是否在树中，先将它和根结点中的键比较，如果它和其中任意一个相等，查找命中；否则就根据比较的结果找到指向相应区间的链接，并在其指向的子树中递归地继续查找。如果这是个空链接，查找未命中。
     * 红黑树的get()方法不会检查结点的颜色，因此平衡性相关的操作不会产生任何负担；因为树是平衡的，所以查找比二叉查找树更快。
     * @param key
     * @return
     */
    @Override
    public Value get(Key key) {
        return null;
    }

    @Override
    public void delete(Key key) {

    }

    @Override
    public boolean contains(Key key) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterable<Key> keys() {
        return null;
    }
}
