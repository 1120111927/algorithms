package yadong0305.algorithms.ST;

import java.util.ArrayList;

/**
 * 顺序查找：在查找中一个一个地顺序遍历符号表中的所有键并使用equals()方法来寻找与被查找地键匹配的键
 */

public class SequentialSearchST<Key, Value> extends ST<Key, Value>
{
    private Node first;
    private int n;

    private class Node
    {
        Key key;
        Value value;
        Node next;

        public Node(Key key, Value value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public SequentialSearchST() {
        n = 0;
    }

    /**
     * 遍历链表，用equals()方法比较需被查找的键和每个结点中的键，如果匹配成功就返回相应的值，否则返回null

     */
    public Value get(Key key) {
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                return x.value;
            }
        }
        return null;
    }

    /**
     * 遍历链表，用equals()方法比较需被查找的键和每个结点中的键，如果匹配成功就用第二个参数指定的值更新和该键相关联的值，否则用给定的键值对创建一个新的结点并将其插入到链表的开头
     */
    public void put(Key key, Value value) {
        if (value == null) {
            delete(key);
            return;
        }
        for (Node x = first; x != null; x=x.next) {
            if (key.equals(x.key)) {
                x.value = value;
                return;
            }
        }
        first = new Node(key, value, first);
        n++;
    }

    public int size() {
        return n;
    }

    public Iterable<Key> keys() {
        ArrayList<Key> keys = new ArrayList<>();
        for (Node x = first; x != null; x = x.next) {
            keys.add(x.key);
        }
        return keys;
    }

    public void delete(Key key) {
        if (first == null) {
            return;
        }
        if (key.equals(first.key)) {
            first = first.next;
            return;
        }
        for (Node x = first; x.next != null; x = x.next) {
            if (key.equals(x.next.key)) {
                x.next = x.next.next;
                return;
            }
        }
    }

    @Override
    public boolean contains(Key key) {
        return get(key) == null;
    }

    @Override
    public boolean isEmpty() {
        return n == 0;
    }

}
