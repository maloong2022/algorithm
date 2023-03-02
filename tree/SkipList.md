### 跳表

跳表（Skip List）是对元素有序的链表的优化，对标的是平衡树和二分查找。

- 二分查找：可以在数组上 O(logN)查询，不可修改序列（不能用于链表）
- 平衡树：支持高效的查询、插入、删除，但比较复杂，不容易实现

跳表是一种查询、插入、删除都是 O(logN)的数据结构，其特点是原理简单、容易实现、方便扩展、效率优秀，在 Redis、LevelDB 等热门项目中用于代替平衡树。

链表插入、删除都是 O(1)，但查询很慢--O(N)
跳表的核心思想：如何提高有序链表的查询效率？

#### 查询

1. 从最高级索引、头元素起步
2. 沿着索引查找，直至找到一个大于或者等于目标的元素，或者到达索引末尾
3. 如果该元素等于目标，则表明目标已经被找到，算法结束
4. 如果该元素大于目标或者已到达末尾，则回到当前索引的上一个元素，转入下一级索引，重复 2

#### 时间复杂度

在一次查询中，每一层至多遍历 3 个节点

- 最高级索引只有 2 个节点
- 每一级索引遍历的第 3 个节点必然大于目标--不然的话在上一级索引中应该走得更远才对
  时间复杂度<= 3\*层数 = O(logN)

#### 空间复杂度

索引的层数： O(logN)
每层索引的节点数：N/2 + N/4 + N/8 + ... <N
整个跳表的节点总数大约为 2N（N 个原始数据+ N 个索引节点）
空间复杂度为： O(N)

也可以每 3 个节点建立一个索引
这样可以节省一点空间，但相应的造成查询效率稍微下降（每层至多遍历 3 个节点变成 4 个）
复杂度不变，常数变化（时间和空间的平衡）

#### 插入

先查询，再插入？
问题：插入很多次数后，一个索引节点代表的节点数量会增多，如果不断更新索引，时间复杂度会退化

解决方案：
重建？ -- 效率太低！
在每个节点上记录它代表的下一级节点个数？ -- 需要维护额外信息，实现复杂

跳表选择的方案是：利用随机+概率

#### 随机建立索引

现实中的跳表不限制“每 2 个节点建立一个索引”，而是：

- 在原始数据中随机 n/2 个元素建立一级索引
- 在一级索引中随机 n/4 个元素建立二级索引
- 在二级索引中随机 n/8 个元素建立三级索引
- 。。。。
  当元素足够多时，可以期望随机出来的索引分布比较均匀，查询的时间复杂度依旧是 O(logN)

#### 概率更新索引

当插入一个元素时，如何决定是否更新索引呢？
跳表实现了一个特别的“骰子”，可能返回 1 ～ MAX_LEVEL 之间的整数：

- 1/2 概率返回 1，表示不需要更新索引，直接在原始链表中插入即可
- 1/4 概率返回 2，表示需要为这个新元素建立一级索引
- 1/8 概率返回 3，表示需要为这个新元素建立一级和二级索引
- 1/16 概率返回 4，表示需要为这个新元素建立一级、二级和三级索引
- 。。。。

通过这个“骰子”可以保证，无论插入多少个元素，各级索引的节点数量期望依然是 N/2，N/4。。。
时间复杂度 O(logN)

#### 删除

删除元素很简单，还是基于查询
在此过程中把原始链表和各级索引中对应的节点（如果有的话）都删掉就行了
时间复杂度 O(logN)

### 实战

1206. Design Skiplist

Design a Skiplist without using any built-in libraries.

A skiplist is a data structure that takes O(log(n)) time to add, erase and search. Comparing with treap and red-black tree which has the same function and performance, the code length of Skiplist can be comparatively short and the idea behind Skiplists is just simple linked lists.

For example, we have a Skiplist containing [30,40,50,60,70,90] and we want to add 80 and 45 into it. The Skiplist works this way:

![](https://assets.leetcode.com/uploads/2019/09/27/1506_skiplist.gif)

Artyom Kalinin [CC BY-SA 3.0], via Wikimedia Commons

You can see there are many layers in the Skiplist. Each layer is a sorted linked list. With the help of the top layers, add, erase and search can be faster than O(n). It can be proven that the average time complexity for each operation is O(log(n)) and space complexity is O(n).

See more about Skiplist: [](https://en.wikipedia.org/wiki/Skip_list)

Implement the Skiplist class:

- Skiplist() Initializes the object of the skiplist.
- bool search(int target) Returns true if the integer target exists in the Skiplist or false otherwise.
- void add(int num) Inserts the value num into the SkipList.
- bool erase(int num) Removes the value num from the Skiplist and returns true. If num does not exist in the Skiplist, do nothing and return false. If there exist multiple num values, removing any one of them is fine.
  Note that duplicates may exist in the Skiplist, your code needs to handle this situation.

Example 1:

```
Input
["Skiplist", "add", "add", "add", "search", "add", "search", "erase", "erase", "search"]
[[], [1], [2], [3], [0], [4], [1], [0], [1], [1]]
Output
[null, null, null, null, false, null, true, false, true, false]

Explanation
Skiplist skiplist = new Skiplist();
skiplist.add(1);
skiplist.add(2);
skiplist.add(3);
skiplist.search(0); // return False
skiplist.add(4);
skiplist.search(1); // return True
skiplist.erase(0);  // return False, 0 is not in skiplist.
skiplist.erase(1);  // return True
skiplist.search(1); // return False, 1 has already been erased.
```

Constraints:

- 0 <= num, target <= 2 \* 104
- At most 5 \* 104 calls will be made to search, add, and erase.
