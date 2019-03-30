package yadong0305.algorithms.ST;

public abstract class ST<Key, Value> {

    public abstract void put(Key key, Value value);
    public abstract Value get(Key key);
    public abstract void delete(Key key);
    public abstract boolean contains(Key key);
    public abstract boolean isEmpty();
    public abstract Iterable<Key> keys();
}
