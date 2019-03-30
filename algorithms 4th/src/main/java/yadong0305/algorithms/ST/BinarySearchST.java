package yadong0305.algorithms.ST;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 有序数组中的二分查找：
 *   使用一对平行数组，一个存储键，一个存储值，保证数组中Comparable类型的键有序，然后使用数组的索引来高效地实现get()和其他操作
 */
@SuppressWarnings("unchecked")
public class BinarySearchST<Key extends Comparable<Key>, Value> extends ST<Key, Value> {

    private Key[] keys;
    private Value[] values;
    private int N;

    public BinarySearchST(int capacity) {

        keys = (Key[]) new Comparable[capacity];
        values = (Value[]) new Object[capacity];
    }

    /**
     * resize()方法，动态增加表的大小
     * @param capacity：更改后表的大小
     */
    public void resize(int capacity) {
        Key[] keys = (Key[]) new Comparable[capacity];
        Value[] values = (Value[]) new Object[capacity];
        for (int i=0; i<N; i++) {
            keys[i] = this.keys[i];
            values[i] = this.values[i];
        }
        this.keys = keys;
        this.values = values;
    }

    /**
     * rank()方法，返回表中小于给定键的键的数量
     */
    public int rank(Key key) {
        int lo = 0;
        int hi = N - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo)/2;
            int cmp = key.compareTo(keys[mid]);
            if (cmp < 0) {
                hi = mid - 1;
            } else if (cmp > 0) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        return lo;
    }

    /**
     * put()方法，只要给定的键存在于表中，rank()方法就能够精确地告诉我们到哪里去更新它的值，以及当键不存在时将键存储到表的何处。将所有更大的键向后移动一格来腾出位置（从后往前移动）并将给定的键值对分别插入到各自数组中的合适位置
     */
    public void put(Key key, Value value) {
        int i = rank(key);
        if (i<N && key.compareTo(keys[i]) == 0) {
            values[i] = value;
            return;
        }
        if (N == keys.length) {
            resize(N*2);
        }
        for (int j=N; j>i; j--) {
            keys[j] = keys[j-1];
            values[j] = values[j-1];
        }
        keys[i] = key;
        values[i] = value;
        N++;
    }

    /**
     * get()方法，只要给定的键存在于表中，rank()方法就能够精确的返回键的位置，如果找不到，那么就不存在表中了。
     */
    public Value get(Key key) {
        if (isEmpty()) return null;
        int i = rank(key);
        if (i<N && key.compareTo(keys[i]) == 0) {
            return values[i];
        }
        return null;
    }

    public boolean contains(Key key) {
        int pos = rank(key);
        return key.compareTo(keys[pos]) == 0;
    }

    public void delete(Key key) {
        int pos = rank(key);
        for (int i = pos; i < N - 1; i++) {
            keys[i] = keys[i + 1];
            values[i] = values[i + 1];
        }
        N--;
        if (N < keys.length / 4) {
            resize(keys.length / 2);
        }
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public Key min() {
        return keys[0];
    }

    public Key max() {
        return keys[N-1];
    }

    public Key floor(Key key) {
        int pos = rank(key);
        if (pos<0) {
            return null;
        }
        return keys[pos];
    }

    public Key ceiling(Key key) {
        int pos = rank(key);
        if (pos == N) {
            return null;
        }
        return keys[pos];
    }

    public Key select(int key) {
        return keys[key];
    }

    public void deleteMin() {
        delete(keys[0]);
    }

    public void deleteMax() {
        delete(keys[N-1]);
    }

    public int size(Key lo, Key hi) {
        return rank(hi) - rank(lo) + 1;
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> temp = new LinkedList<>();
        for (int i=rank(lo); i<=rank(hi); i++) {
            temp.add(keys[i]);
        }
        return temp;
    }

    public Iterable<Key> keys() {
        return keys(keys[0], keys[N-1]);
    }

}
