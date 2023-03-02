### 堆 Heap

堆是一种高效的维护集合中最大或最小元素的数据结构

大根堆：根节点最大的堆，用于维护和查询 max
小根堆：根节点最小的堆，用于维护和查询 min

堆是一颗树，并且满足堆性质

- 大根堆任意节点的关键码>= 它所有子结点的关键码（父>= 子）
- 小根堆任意节点的关键码<= 它所有子结点的关键码（父<= 子）

二叉堆 Binary Heap

二叉堆是一种简易实现

- 本质上一颗满足堆性质的完全二叉树

常规操作

- 建堆（build）： O（N）
- 查询最值（get max/min）：O（1）
- 插入（insert）：O（log N）
- 取出最值（delete max/min）：O（logN）

斐波那契堆、配对堆等可以做到插入 O（1），左偏树、斜堆等可以支持合并

#### 二叉堆的实现

二叉堆一般使用一个一维数组来存储，利用完全二叉树的节点编号特性

假设第一个元素存储在索引（下标）为 1 的位置的话

- 索引为 p 的节点的左孩子的索引为 p\*2
- 索引为 p 的节点的右孩子的索引为 p\*2 + 1
- 索引为 p 的节点的父亲的索引为 p/2(下取整)

假设第一个元素存储在索引（下标）为 0 的位置的话

- 索引为 p 的节点的左孩子的索引为 p\*2 + 1
- 索引为 p 的节点的右孩子的索引为 p\*2 + 2
- 索引为 p 的节点的父亲的索引为 (p-1)/2(下取整)

#### 插入（insert）

新元素一律插入到数组 heap 的尾部

- 设插入到了索引 p 的位置

然后向上进行一次调整（Heapify Up）

- 若已到达根，停止
- 若满足堆性质(heap[p]<=heap[p/2]),停止
- 否则交换 heap[p]和 heap[p/2],令 p = p/2，继续调整

O（log N）

#### 取出堆顶（extract/delete max）

把堆顶（heap[1]）与堆尾（heap[n]）交换，删除堆尾（数组最后一个元素）

然后从根向下进行一次调整（Heapify Down）

- 每次与左右子节点中较大的一个比较，检查堆性质，不满足则交换
- 注意判断子结点是否存在

O（log N）

#### 优先队列（Priority Queue）

二叉堆是优先队列一种简单、常用的实现，但不是最优的实现

理论上二叉堆可以支持 O（log N）删除任意元素，只需要

- 定位该元素在堆中的节点 P（可以通过在数值与索引之间建立映射得到）
- 与堆尾交换，删除堆尾
- 从 p 向上、向下各进行一次调整

不过优先队列并没有提供这个方法，在各语言内置的库中，需要支持删除任意元素时，一般使用有序集合等基于平衡二叉搜索树的实现

### 实战

23. Merge k Sorted Lists

You are given an array of k linked-lists lists, each linked-list is sorted in ascending order.

Merge all the linked-lists into one sorted linked-list and return it.

Example 1:

```
Input: lists = [[1,4,5],[1,3,4],[2,6]]
Output: [1,1,2,3,4,4,5,6]
Explanation: The linked-lists are:
[
  1->4->5,
  1->3->4,
  2->6
]
merging them into one sorted list:
1->1->2->3->4->4->5->6
```

Example 2:

```
Input: lists = []
Output: []
```

Example 3:

```
Input: lists = [[]]
Output: []
```

Constraints:

- k == lists.length
- 0 <= k <= 104
- 0 <= lists[i].length <= 500
- -104 <= lists[i][j] <= 104
- lists[i] is sorted in ascending order.
- The sum of lists[i].length will not exceed 104.

1. Use the jdk priority queue

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if(lists == null || lists.length == 0){
            return null;
        }
        PriorityQueue<ListNode> queue = new PriorityQueue<>((v1,v2)-> v1.val - v2.val);
        for(ListNode node: lists){
            if(node!=null){
                queue.add(node);
            }
        }
        ListNode protect = new ListNode(0);
        ListNode tail = protect;
        while(!queue.isEmpty()){
            ListNode curr = queue.poll();
            tail.next = curr;
            tail = tail.next;
            if(curr.next != null){
                queue.add(curr.next);
            }
        }
        return protect.next;
    }
}
```

2. Use My own binary heap

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if(lists == null || lists.length == 0){
            return null;
        }
       BinaryHeap queue = new BinaryHeap();
        for(ListNode node: lists){
            if(node!=null){
                queue.push(new Node(node.val, node));
            }
        }
        ListNode protect = new ListNode();
        ListNode tail = protect;
        while(!queue.isEmpty()){
            Node curr = queue.pop();
            tail.next = curr.listNode;
            tail = tail.next;
            ListNode p = curr.listNode.next;
            if(p != null){
                queue.push(new Node(p.val,p));
            }
        }
        return protect.next;
    }

    class BinaryHeap{
        private List<Node> heap;

        public BinaryHeap() {
            this.heap = new ArrayList<>();
        }

        public boolean isEmpty() {
            return this.heap.isEmpty();
        }

        public void push(Node node) {
            //先放到末尾
            this.heap.add(node);
            //往上调整
            heapifyUp(heap.size() - 1);

        }

        public Node pop() {
            Node ans = this.heap.get(0);
            // 末尾交换到头部，然后删除末尾
            Collections.swap(this.heap, 0, heap.size() - 1);
            this.heap.remove(this.heap.size() - 1);
            // 向下调整
            heapifyDown(0);
            return ans;
        }


        private void heapifyDown(int p) {
            int child = p * 2 + 1;
            // child未出界，说明p有合法的child，还不是叶子
            while (child < heap.size()) {
                int otherChild = p * 2 + 2;
                // 先比较两个孩子，谁小就继续跟p比较
                // child存较小的孩子
                if (otherChild < heap.size() && (this.heap.get(otherChild).key < this.heap.get(child).key))
                    child = otherChild;
                // 让child跟p比较
                if (this.heap.get(child).key < this.heap.get(p).key) {
                    Collections.swap(this.heap, child, p);
                    p = child;
                    child = p * 2 + 1;
                } else break;
            }

        }

        private void heapifyUp(int p) {
            while (p > 0) {
                int fa = (p - 1) / 2;
                if (this.heap.get(p).key < this.heap.get(fa).key) {
                    Collections.swap(this.heap, p, fa);
                    p = fa;
                } else break;
            }
        }
    }

    class Node {
        int key;
        ListNode listNode;
        Node(int key,ListNode listNode){
            this.key = key;
            this.listNode = listNode;
        }
    }
}
```

239. Sliding Window Maximum

You are given an array of integers nums, there is a sliding window of size k which is moving from the very left of the array to the very right. You can only see the k numbers in the window. Each time the sliding window moves right by one position.

Return the max sliding window.

Example 1:

```
Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
Output: [3,3,5,5,6,7]
Explanation:
Window position                Max
---------------               -----
[1  3  -1] -3  5  3  6  7       3
 1 [3  -1  -3] 5  3  6  7       3
 1  3 [-1  -3  5] 3  6  7       5
 1  3  -1 [-3  5  3] 6  7       5
 1  3  -1  -3 [5  3  6] 7       6
 1  3  -1  -3  5 [3  6  7]      7
```

Example 2:

```
Input: nums = [1], k = 1
Output: [1]
```

Constraints:

- 1 <= nums.length <= 105
- -104 <= nums[i] <= 104
- 1 <= k <= nums.length

1. 用单调队列处理

思路： 尽快的从集合中删除不符合的数

```java
import java.util.*;

public class Solution {
    Deque<Integer> q;//存下标
    List<Integer> ans;
    public int[] maxSlidingWindow(int[] nums, int k) {
        //下标（时间）递增，值递减的队列
        q = new ArrayDeque<>();
        ans = new ArrayList<>();
        for (int i=0;i<nums.length;i++){
            // 删除出界的选项
            while(!q.isEmpty()&&q.getFirst() <= i - k)q.removeFirst();
            // 插入新选项i，维护单调性（值递减）
            while(!q.isEmpty()&&nums[q.getLast()] <= nums[i]) q.removeLast();
            q.addLast(i);
            //  取队头更新答案
            if(i >= k-1){
                ans.add(nums[q.getFirst()]);
            }
        }
        return ans.stream().mapToInt(i->i).toArray();
    }
}
```

思路套路：

- 单调队列维护的是一个候选集合，前面的比较旧，后面的比较新（时间有单调性）
- 候选项的某个属性也具有单调性
- 确定递增递减的方法-- 考虑任意两个候选项 j1 < j2, 写出 j1 比 j2 优的条件，此题为 nums[j1] > nums[j2]

排除冗余的关键：若 j1 比 j2 差，此题为：nums[j1]<=nums[j2],j1 的生命周期还比 j2 短，那么 j1 就没有什么用了。

单调队列题目代码模版：

- for 每个元素
- （1） while（队头过期）队头出列
- （2）取队头为最佳选项，计算答案
- （3）while（队尾与新元素不满足单调性）队尾出列
- （4）新元素入队

（2）（3）的顺序取决于 i 是不是候选项。队列永远存的是下标不是值（保证时间单调性）

2. 用堆

```java
class Solution {
    // <关键码，下标> 并且修改比较器，（由于里面存的是Pair）为大根堆 v2-v1
    // 小根堆的话就是v1-v2
      private PriorityQueue<Pair<Integer, Integer>> q = new PriorityQueue<>((v1,v2)-> v2.getKey()-v1.getKey());
    public int[] maxSlidingWindow(int[] nums, int k) {
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            q.add(new Pair<>(nums[i],i));
            if(i >= k-1) {
                //[i-k+1,i] 这一段时间的max
                // 懒惰删除影响此段时间的堆顶元素
                while (q.peek().getValue() <= i-k) q.poll();
                ans.add(q.peek().getKey());
            }
        }
        int[] result = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            result[i] = ans.get(i);
        }
        return result;
    }
}
```

对比单调队列和堆的解法

- 用堆的复杂度为 O(nlogN)，单调队列是 O(N),但是堆是更通用的选择，一个集合维护一个最值，然后要取最值。
- 懒惰删除法

1. 指的是需要从堆中删除一个元素（不一定是最大值）时，不直接删除，而是打上删除标记（soft delete）
2. 等未来它作为最大值被取出时，再判断它是否已经被标记，若是则抛弃它，取下一个最大值。

“懒惰”的含义--只需要删除的元素还不是最大值，在堆里面多呆一会也无妨，未来等它会影响最大值正确性的时候再说。
