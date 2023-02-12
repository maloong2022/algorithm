### Cache

缓存的两个要素：大小、替换策略

常见替换算法：

- LRU - least recently used，最近最少使用（淘汰最旧数据）
- LFU - least frequently used， 最不经常使用（淘汰频次最少数据）

146. LRU Cache

Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.

Implement the LRUCache class:

- LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
- int get(int key) Return the value of the key if the key exists, otherwise return -1.
- void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.

The functions get and put must each run in O(1) average time complexity.

Example 1:

```
Input
["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
[[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
Output
[null, null, null, 1, null, -1, null, -1, 3, 4]

Explanation
LRUCache lRUCache = new LRUCache(2);
lRUCache.put(1, 1); // cache is {1=1}
lRUCache.put(2, 2); // cache is {1=1, 2=2}
lRUCache.get(1);    // return 1
lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
lRUCache.get(2);    // returns -1 (not found)
lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
lRUCache.get(1);    // return -1 (not found)
lRUCache.get(3);    // return 3
lRUCache.get(4);    // return 4
```

Constraints:

- 1 <= capacity <= 3000
- 0 <= key <= 104
- 0 <= value <= 105
- At most 2 \* 105 calls will be made to get and put.

思路：

哈希表+双向链表

实现要点

- 一维线性存储结构；
- 支持头部插入（可借助保护节点）；
- 支持尾部删除（链表删除尾节点，同时删除哈希表）；
- 支持中间删除（链表删除节点，可借助 tail 保护节点，后续在头部插入）；
- 支持判断一个元素在不在线性表（直接查哈希表）；
- 哈希表和链表的混合结构，链表用于按时间顺序保存数据，哈希表存储 key 到链表的节点索引。

o(1) 访问： 直接查询哈希表
o(1) 更新：通过哈希表定位到链表结点，删除该结点（若存在），在表头重新插入
o(1) 删除：总是淘汰链表末尾结点，同时在哈希表中删除

NOTE：get 由于访问了，如果存在值的时候需要更新位置。

```java

import com.google.common.annotations.VisibleForTesting;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {

    Map<K, CacheNode<K, V>> posMap;
    final int capacity;
    transient int size;
    CacheNode<K, V> header;
    CacheNode<K, V> rear;

    static class CacheNode<K, V> {
        K key;
        V value;
        CacheNode<K, V> pre;
        CacheNode<K, V> next;

        public CacheNode() {
        }

        public CacheNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        posMap = new HashMap<>();
        header = new CacheNode<>();
        rear = new CacheNode<>();
        header.next = rear;
        rear.pre = header;
    }

    public V get(K key) {
        if (!posMap.containsKey(key)) {
            return null;
        }
        // update the position
        CacheNode<K, V> node = posMap.get(key);
        removeNode(node);
        // add header
        addHeader(key, node.value);
        return (V) node.value;
    }

    public void put(K key, V value) {
        if (posMap.containsKey(key)) {
            // remove the node
            removeNode(posMap.get(key));
            // add header
            addHeader(key, value);
        } else {
            if (isFull()) {
                // remove rear
                removeTail(key);
                // add header
                addHeader(key, value);
            } else {
                // add header
                addHeader(key, value);
                ++size;
            }
        }
    }

    private void removeNode(CacheNode<K, V> oldNode) {
        oldNode.next.pre = oldNode.pre;
        oldNode.pre.next = oldNode.next;
    }

    private void removeTail(K key) {
        CacheNode<K, V> tail = rear.pre;
        tail.next.pre = tail.pre;
        tail.pre.next = tail.next;
        K tailKey = tail.key;
        posMap.remove(tailKey);
    }

    private void addHeader(K key, V value) {
        CacheNode<K, V> newNode = new CacheNode<>(key, value);
        header.next.pre = newNode;
        newNode.next = header.next;
        newNode.pre = header;
        header.next = newNode;
        posMap.put(key, newNode);
    }

    private boolean isFull() {
        return size == capacity;
    }

    @VisibleForTesting
    public String cacheList() {
        StringBuilder cacheList = new StringBuilder();
        cacheList.append("[");
        CacheNode<K, V> cur = header.next;
        while (cur != rear) {
            cacheList.append("[key=").append(cur.key).append(",value=").append(cur.value).append("]=>");
            cur = cur.next;
        }
        cacheList.append("null]");
        return cacheList.toString();
    }

    @VisibleForTesting
    public String cachePosMap() {
        StringBuilder cacheMap = new StringBuilder();
        cacheMap.append("[");
        CacheNode<K, V> cur = header.next;
        while (cur != rear) {
            cacheMap.append("(key=").append(cur.key).append(",value=").append(cur.value).append("),");
            cur = cur.next;
        }
        cacheMap.append("]");
        return cacheMap.toString();
    }
}

```
