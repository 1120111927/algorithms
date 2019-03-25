# algorithms

## 查找

范型符号表API

> `public class ST<Key, Value>`

| 方法 | 描述 |
|---|---|
|`ST()`| 创建一张符号表 |
| `void put(Key key, Value val)` | 将键值对存入表中（若值为空则将键key从表中删除） |
| `Value get(Key key)` | 获取键key对应的值（获取键key对应的值（若键key不存在则返回null）） |
| `void delete(Kye key)` | 从表中删去键key（及其对应的值） |
| `boolean contains(Kye key)` | 键key在表中是否有对应的值 |
| `boolean isEmpty(Key key)` | 表是否为空 |
| `int size()` | 表中的键值对数量 |
| `Iterable<key> keys()` | 表中的所有键的集合 |

有序的范型符号表API

> `public class ST<Key extends Comparable<key>, Value>`

| 方法 | 描述 |
| --- | --- |
| `ST()` | 创建一张有序符号表 |
| `void put(Key key, Value val)` | 将键值对存入表中（若值为空，则将键从表中删除） |
| `Value get(Key key)` | 获取键key对应的值（若键key不存在则返回空） |
| `void delete(Key key)` | 从表中删除键key（及其对应的值） |
| `boolean contains(Key key)` | 键key是否存在于表中 |
| `boolean isEmpty()` | 表是否为空 |
| `int size()` | 表中的键值对数量 |
| `Key min()` | 最小的键 |
| `Key max()` | 最大的键 |
| `Key floor(Key key)` | 小于等于key的最大键 |
| `Key ceiling(Key key)` | 大于等于key的最小键 |
| `int rank(Key key)` | 小于key的键的数量 |
| `Key select(int key)` | 排名为k的键 |
| `void deleteMin()` | 删除最小的键 |
| `void deleteMax()` | 删除最大的键 |
| `int size(Key lo, Key hi)` | [lo..hi]之间键的数量 |
| `Iterable<Key> keys(Key lo, Key hi)` | [lo..hi]之间的所有键，已排序 |
| `Iterable<Key> keys()` | 表中的所有键的集合，已排序 |