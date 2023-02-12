### 递归

![](https://photos.google.com/photo/AF1QipOubQwE4tppCGp0N4wrQsILuA9WEwMQaHnvYAkn)

递推：从小的 i-1 通过 for 循环和递推公式，推导出 i 的方式，递推公式往往比较难找和归纳。

递归（recursion）的本质，往下去探寻：

函数自己调用自己；

通过自相似的函数体，进行重复。

递归的三个关键：

- 定义需要递归的问题（重叠子问题）相似重叠的子问题，需要递归什么--数学归纳法思维
- 确定递归边界，即终止条件。
- 保护与还原现场（针对非局部共享状态变量，非必须）

代码模版

```java
void recursion(int level, int param) {
	// 边界
     if (level > MAX_LEVEL) {
           // 终止
           return
     }

     // 处理当前递归函数栈的逻辑
     process(level, param)

     // 递归调用
     recursion(level + 1, new_param)

     // 还原现场（非必须，回溯的核心）
}
```

#### 实战

**子集**（选或不选，任意个）

78. Subsets

Given an integer array nums of unique elements, return all possible subsets (the power set).

The solution set must not contain duplicate subsets. Return the solution in any order.

Example 1:

```
Input: nums = [1,2,3]
Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
```

Example 2:

```
Input: nums = [0]
Output: [[],[0]]
```

Constraints:

- 1 <= nums.length <= 10
- -10 <= nums[i] <= 10
- All the numbers of nums are unique.

```java
class Solution {
    Stack<Integer> chosen;
    List<List<Integer>> ans;
    int[] numsArr;
    int n = 0;
    public  List<List<Integer>> subsets(int[] nums) {
        chosen = new Stack<>();
        ans = new ArrayList<>();
        n = nums.length;
        numsArr = nums;
        recur(0);
        return ans;
    }

    private void recur(int i){
        // the bond of recursion
        if(i == numsArr.length){
            // new arraylist deal with the java reference transform
            ans.add(new ArrayList<>(chosen));
            return;
        }
        // the process of the current recursion

        // 1.not chosen the nums
        recur(i+1);
        // 2.chosen the index i of the nums
        chosen.push(numsArr[i]);
        recur(i+1);
        // pop to come back the condition of recursion
        chosen.pop();
    }
}
```

**组合**（选或不选，选 K 个）

77. Combinations

Given two integers n and k, return all possible combinations of k numbers chosen from the range [1, n].

You may return the answer in any order.

Example 1:

```
Input: n = 4, k = 2
Output: [[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
Explanation: There are 4 choose 2 = 6 total combinations.
Note that combinations are unordered, i.e., [1,2] and [2,1] are considered to be the same combination.
```

Example 2:

```
Input: n = 1, k = 1
Output: [[1]]
Explanation: There is 1 choose 1 = 1 total combination.
```

Constraints:

- 1 <= n <= 20
- 1 <= k <= n

```java
class Solution {
    int n, k;
    List<List<Integer>> ans;
    Stack<Integer> chosen;
    public List<List<Integer>> combine(int n, int k) {
        chosen = new Stack<>();
        ans = new ArrayList<>();
        this.n = n;
        this.k = k;
        recursion(1);
        return ans;
    }

    public void recursion(int i){
        // cut the brench: if the size of chosen great then k or the total size is less then k
        if (chosen.size() > k || (chosen.size() + (n - i + 1)) < k) return;
        // the bond of recursion
        if(i == n+1){
            ans.add(new ArrayList(chosen));
            return;
        }

        // process of the current recursion
        // 1. not chose i
        recursion(i+1);
        // 2. chose i
        chosen.push(i);
        recursion(i+1);

        // clear current recursion's effection
        chosen.pop();
    }
}
```

组合型就是一个子集型加上剪枝！

**排列**（拿一个，只拿一次）

46. Permutations

Given an array nums of distinct integers, return all the possible permutations. You can return the answer in any order.

Example 1:

```
Input: nums = [1,2,3]
Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
```

Example 2:

```
Input: nums = [0,1]
Output: [[0,1],[1,0]]
```

Example 3:

```
Input: nums = [1]
Output: [[1]]
```

Constraints:

- 1 <= nums.length <= 6
- -10 <= nums[i] <= 10
- All the integers of nums are unique.

```java
class Solution {
    List<List<Integer>> ans;
    boolean[] used;
    int n;
    Stack<Integer> chosen;
    int[] numsArr;
    public List<List<Integer>> permute(int[] nums) {
        this.n = nums.length;
        this.numsArr = nums;
        chosen = new Stack<>();
        ans = new ArrayList<>();
        used = new boolean[n];
        recursion(0);
        return ans;
    }

    private void recursion(int pos){
        if(pos == n){
            ans.add(new ArrayList(chosen));
            return;
        }
        // permute the number
        for(int i =0;i < n;i++){
            // use the not chosen number
            if(!used[i]){
                // chose it and change the use flag
                chosen.push(numsArr[i]);
                used[i] = true;
                // recursion
                recursion(pos + 1);

                // clear the effection to next recursion
                chosen.pop();
                used[i] = false;
            }
        }
    }
}
```

47. Permutations II

Given a collection of numbers, nums, that might contain duplicates, return all possible unique permutations in any order.

Example 1:

```
Input: nums = [1,1,2]
Output:
[[1,1,2],
 [1,2,1],
 [2,1,1]]
```

Example 2:

```
Input: nums = [1,2,3]
Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
```

Constraints:

- 1 <= nums.length <= 8
- -10 <= nums[i] <= 10

```java
class Solution {

    List<List<Integer>> ans;
    Stack<Integer> chosen;
    int n;
    int[] numsArr;
    boolean[] used;

    public List<List<Integer>> permuteUnique(int[] nums) {
        this.n = nums.length;
        this.numsArr = nums;
        chosen = new Stack<>();
        ans = new ArrayList<>();
        used = new boolean[n];
         // 全排列2的专属逻辑，对nums进行排序
        Arrays.sort(nums);
        recursion(0);
        return ans;

    }

    public void recursion(int pos){
        if (pos == n) {
            ans.add(new ArrayList<>(chosen));
            return;
        }
        for(int i=0;i<n;i++){
            // 如果i-1这个位置没有被使用过，并且当前元素和i-1一样，说明存在重复
            // 从左往右第一个未被填过的数字
            // 即整个数组的状态其实是保证了 [未填入，未填入，未填入]到 [填入，未填入，未填入]
            if((i>0 && numsArr[i] == numsArr[i-1] && !used[i-1])){
                continue;
            }
            if(!used[i]){
                chosen.push(numsArr[i]);
                used[i] = true;
                recursion(pos+1);
                used[i] = false;
                chosen.pop();
            }
        }
    }
}
```

236. Lowest Common Ancestor of a Binary Tree

Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.

According to the definition of LCA on Wikipedia: “The lowest common ancestor is defined between two nodes p and q as the lowest node in T that has both p and q as descendants (where we allow a node to be a descendant of itself).”

Example 1:

![](https://assets.leetcode.com/uploads/2018/12/14/binarytree.png)

```
Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
Output: 3
Explanation: The LCA of nodes 5 and 1 is 3.
```

Example 2:

![](https://assets.leetcode.com/uploads/2018/12/14/binarytree.png)

```
Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
Output: 5
Explanation: The LCA of nodes 5 and 4 is 5, since a node can be a descendant of itself according to the LCA definition.
```

Example 3:

```
Input: root = [1,2], p = 1, q = 2
Output: 1
```

Constraints:

- The number of nodes in the tree is in the range [2, 105].
- -109 <= Node.val <= 109
- All Node.val are unique.
- p != q
- p and q will exist in the tree.

```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 如果root为空，或者为p，或者为q，直接返回root
        if(root == null || root == p || root == q) return root;
        // 分拆子问题，分别看左子树和右子树，是否包含p或者q
        // 使用DFS思想，一直探查到最底层，但凡下一层满足以下四种情况的任意一种都可以直接返回
        TreeNode left = lowestCommonAncestor(root.left,p,q);
        TreeNode right = lowestCommonAncestor(root.right,p,q);
        // 第一种情况，左子树不包含p，q，右子树也不包含，直接返回空
        if(left == null && right == null) return null;
        // 第二种情况，左子树不包含，右子树包含，返回右子树
        if(left == null ) return right;
        // 第三种情况，右子树不包含，左子树包含，返回左子树
        if(right == null) return left;
        // 第四种情况，左右子树都包含p或者q
        return root;
    }
}
```

#### 递归的基本形式总结

以上三个问题都是递归实现的“暴力搜索”（或者叫枚举、回溯等）

| 递归形式 | 时间复杂度规模 | 问题举例               |
| -------- | -------------- | ---------------------- |
| 指数型   | k^n            | 子集、大体积背包       |
| 排列型   | n!             | 全排列、旅行商、N 皇后 |
| 组合型   | n!/(m!(n-m)!)  | 组合选数               |

### 树

![](https://photos.google.com/photo/AF1QipMP81Ex67oiDFVP8UPTOBJWi182D81HP2Pmuu7w)

#### 实战

226. Invert Binary Tree

Given the root of a binary tree, invert the tree, and return its root.

Example 1:

![](https://assets.leetcode.com/uploads/2021/03/14/invert1-tree.jpg)

```
Input: root = [4,2,7,1,3,6,9]
Output: [4,7,2,9,6,3,1]
```

Example 2:

![](https://assets.leetcode.com/uploads/2021/03/14/invert2-tree.jpg)

```
Input: root = [2,1,3]
Output: [2,3,1]
```

Example 3:

```
Input: root = []
Output: []
```

Constraints:

- The number of nodes in the tree is in the range [0, 100].
- -100 <= Node.val <= 100

```java
class Solution{
  public TreeNode invertTree(TreeNode root) {
    if(root == null) return null;

    // invert the current level (current logic)
    TreeNode temp = root.left;
    root.left = root.right;
    root.right = temp;

    // invert the current level's children (the sub level logic)
    invertTree(root.left);
    invertTree(root.right);
    // after invert the root not change
    return root;
  }
}
```

98. Validate Binary Search Tree

Given the root of a binary tree, determine if it is a valid binary search tree (BST).

A valid BST is defined as follows:

- The left subtree of a node contains only nodes with keys less than the node's key.
- The right subtree of a node contains only nodes with keys greater than the node's key.
- Both the left and right subtrees must also be binary search trees.

Example 1:

![](https://assets.leetcode.com/uploads/2020/12/01/tree1.jpg)

```
Input: root = [2,1,3]
Output: true
```

Example 2:

![](https://assets.leetcode.com/uploads/2020/12/01/tree2.jpg)

```
Input: root = [5,1,4,null,null,3,6]
Output: false
```

Explanation: The root node's value is 5 but its right child's value is 4.

Constraints:

- The number of nodes in the tree is in the range [1, 104].
- -2^31 <= Node.val <= 2^31 - 1

```java
class Solution {

  public boolean isValidBST(TreeNode root){
    return recursion(root, -(1l <<  31), (1l >> 31) - 1);

  }

  private boolean recursion(TreeNode root, long leftRange, long rightRange){
    if (root == null) return true;
    // deal the current logic
    if (root.val < leftRange || root.val > rightRange) return false;

    // deal with the sub logic
    return recursion(root.left, leftRange, (long)root.val - 1) && recursion(root.right, (long)root.val + 1, rightRange);

  }
}

```

104. Maximum Depth of Binary Tree

Given the root of a binary tree, return its maximum depth.

A binary tree's maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.

Example 1:

![](https://assets.leetcode.com/uploads/2020/11/26/tmp-tree.jpg)

```
Input: root = [3,9,20,null,null,15,7]
Output: 3
```

Example 2:

```
Input: root = [1,null,2]
Output: 2
```

Constraints:

- The number of nodes in the tree is in the range [0, 104].
- -100 <= Node.val <= 100

```java
class Solution {

  public int maxDepth(TreeNode root){
    if (root == null) return 0;

    // plus 1 is the current logic, maxDepth left and right is the sub logic
    return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
  }
}
```

思路：

- 思路一（自底向上统计信息，分治思想）

`最大深度 = max（左子树最大深度，右子树最大深度）+1`

- 思路二（自顶向下维护信息）

`把“深度”作为一个全局变量-- 一个跟随结点移动而动态变化的信息
递归一层，变量加 1，在叶子处更新答案，注意保护和还原现场`

```java
class Solution {

    int depth = 0;
    int ans = 0;
    public int maxDepth(TreeNode root) {
        calc(root);
        return ans;
    }

    void calc(TreeNode root){
        if (root == null) return;
        // deal with the current recursion logic
        depth++;
        ans = Math.max(ans,depth);

        // deal with the sub recursion logic
        calc(root.left);
        calc(root.right);
        // clear the effection to next recursion
        depth--;
    }
}

```

111. Minimum Depth of Binary Tree

Given a binary tree, find its minimum depth.

The minimum depth is the number of nodes along the shortest path from the root node down to the nearest leaf node.

Note: A leaf is a node with no children.

Example 1:

![](https://assets.leetcode.com/uploads/2020/10/12/ex_depth.jpg)

```
Input: root = [3,9,20,null,null,15,7]
Output: 2
```

Example 2:

```
Input: root = [2,null,3,null,4,null,5,null,6]
Output: 5
```

Constraints:

- The number of nodes in the tree is in the range [0, 105].
- -1000 <= Node.val <= 1000

很多人写出的代码都不符合 1,2 这个测试用例，是因为没搞清楚题意,
题目中说明:叶子节点是指没有子节点的节点，这句话的意思是 1 不是
叶子节点。题目问的是到叶子节点的最短距离，所以所有返回结果为 1
当然不是这个结果。

而最远的那个题目由于是用最大值，所以不受影响。

另外这道题的关键是搞清楚递归结束条件

- 叶子节点的定义是左孩子和右孩子都为 null 时叫做叶子节点
- 当 root 节点左右孩子都为空时，返回 1
- 当 root 节点左右孩子有一个为空时，返回不为空的孩子节点的深度
- 当 root 节点左右孩子都不为空时，返回左右孩子较小深度的节点值

方法一： 从下往上

```java
class Solution {
  public int minDepth(TreeNode root) {
    if (root == null) return 0;
    if (root.left == null && root.right == null) return 1;
    else if (root.left == null) return minDepth(root.right) + 1;
    else if (root.right == null) return minDepth(root.left) + 1;
    else Math.min(minDepth(root.left), minDepth(root.right)) + 1;
  }
}
```

方法二，从上往下

```java
class Solution {
    int depth;
    int ans;
    public int minDepth(TreeNode root) {
        depth = 0;
        // cmp mini, so you can not figure out it to 0
        ans = 1 << 30;
        recursion(root);
        return ans;
    }

    private void recursion(TreeNode root){
        if(root == null){
            ans = Math.min(ans,depth);
            return;
        }
        // deal with the leaf node
        if(root.left == null && root.right == null){
            // leaf node will be plus 1
            ans = Math.min(ans, depth + 1);
            return;
        }
        depth++;
        if(root.left != null) recursion(root.left);
        if(root.right != null) recursion(root.right);
        depth--;
    }
}
```

### 分治

分而治之，把原问题分成若干子问题，分别解决后（往往有结果集），把结果集合并起来。

关键点：

- 定义出递归状态树；
-
- 原问题和子问题类似（往往是同一类问题）；
-
- 向下递归完毕后，并不是回溯暴力尝试，而是向上合并结果集。

分治算法一般用递归实现

![](https://photos.google.com/photo/AF1QipOFGSxNwAVXwcGdNKoXnK-9VkEnu2M7-Eaz5Oiy)

#### 实战

50. Pow(x, n)

Implement pow(x, n), which calculates x raised to the power n (i.e., xn).

Example 1:

```
Input: x = 2.00000, n = 10
Output: 1024.00000
```

Example 2:

```
Input: x = 2.10000, n = 3
Output: 9.26100
```

Example 3:

```
Input: x = 2.00000, n = -2
Output: 0.25000
```

Explanation: 2-2 = 1/22 = 1/4 = 0.25

Constraints:

- -100.0 < x < 100.0
- -231 <= n <= 231-1
- n is an integer.
- -104 <= xn <= 104

```java
class Solution {
    public double myPow(double x, int n) {
        if (n == 0) return 1;
        // deal with if n is a negative number
        if (n < 0) return 1.0 / myPow(x, -n);
        // deal with that if n is -2^31 change to positive 2^31 is over range
        if( n == -(1 << 31)) return 1.0 / (myPow(x, -(n + 1)) * x);
        double temp = myPow(x, n/2);
        double ans = temp * temp;
        if (n%2 == 1) ans *= x;
        return ans;
    }
}
```

22. Generate Parentheses

Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.

Example 1:

```
Input: n = 3
Output: ["((()))","(()())","(())()","()(())","()()()"]
```

Example 2:

```
Input: n = 1
Output: ["()"]
```

Constraints:

- 1 <= n <= 8

```java
class Solution {
    Map<Integer,List<String>> store = new HashMap<>(); // 记忆算过的n

    public List<String> generateParenthesis(int n) {
        if(n == 0) {
            List<String> an = new ArrayList<>();
            an.add("");
            return an;
        }
        if(store.containsKey(n)) return store.get(n);

        List<String> ans = new ArrayList<>();
        for(int k=1;k<=n;k++){ // 有几种方案，加法原理
            List<String> A = generateParenthesis(k-1);
            List<String> B = generateParenthesis(n-k);
            // 几种组合，乘法原理
            for(String a: A){
                for(String b: B){
                    ans.add("("+ a + ")" + b);
                }
            }
        }
        store.put(n,ans);
        return ans;
    }
}
```

![](https://photos.google.com/photo/AF1QipPStuoyIJ_o6laBy0ReurH5_8oWu-bc7bYnQXse)

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
        // 特殊情况，直接处理，提高性能
        if(lists == null || lists.length == 0){
            return null;
        }

        // 优先队列（默认是最小堆，最小值在顶部，每次出列都是最小值）
        PriorityQueue<ListNode> queue = new PriorityQueue<>((v1,v2)-> v1.val - v2.val);

        for(ListNode node: lists){
            // 注意过滤空结点
            if(node!=null){
                queue.add(node);
            }
        }

        // 组装链表
        ListNode protect = new ListNode(0);
        ListNode tail = protect;
        while(!queue.isEmpty()){
            ListNode curNode = queue.poll();
            tail.next = curNode;
            tail = tail.next;
            //  如果链表结点的下一个结点不为空，需要继续放入优先队列，借助最小堆能力
                if(curNode.next != null){
                    queue.add(curNode.next);
                }
        }
        return protect.next;
    }
}

```

169. Majority Element

Given an array nums of size n, return the majority element.

The majority element is the element that appears more than ⌊n / 2⌋ times. You may assume that the majority element always exists in the array.

Example 1:

```
Input: nums = [3,2,3]
Output: 3
```

Example 2:

```
Input: nums = [2,2,1,1,1,2,2]
Output: 2
```

Constraints:

- n == nums.length
- 1 <= n <= 5 \* 104
- -109 <= nums[i] <= 109

Follow-up: Could you solve the problem in linear time and in O(1) space?

解题思路

模板虽然可以通用分治的模板，但是还是要能够利用数学思维，对题目进行抽象

证明:

如果数 a 是数组 nums 的众数，如果我们将 nums 分成两部分，那么 a 必定是至少一部分的众数

反证法：

假设 a 既不是左半部分的众数，也不是右半部分的众数，那么 a 出现的次数少于 l / 2 + r / 2 次，其中 l 和 r 分别是左半部分和右半部分的长度。由于 l / 2 + r / 2 <= (l + r) / 2，说明 a 也不是数组 nums 的众数，因此出现了矛盾。

抽象成分治法的思路：

讲数组的状态空间一分为二，将求当前数组的众数这个问题，分拆成求左右两个子数组的众数，最后将这两个子问题的答案，归并起来，决策出当前问题（数组）的众数

```java
class Solution {
    public int majorityElement(int[] nums) {
        return majorityElementRec(nums,0,nums.length-1);
    }

    int countInRange(int[] nums, int num, int lo, int hi){
        int count = 0;
        for(int i = lo;i<=hi;i++){
            if(nums[i] == num) count++;
        }
        return count;
    }

    int majorityElementRec(int[] nums, int lo, int hi){
        // 递归终止条件，如果当前的下界和上界一样，直接返回即可当前的数字的众数
        if(lo==hi){
            return nums[lo];
        }
        // 当前层的处理逻辑，计算下边界和上边界中位数，防止越界，不使用(hi+lo)/2
        int mid = (hi-lo)/2 + lo;

        // 分别获取左子问题和右子问题的众数left和right
        int left = majorityElementRec(nums,lo,mid);
        int right = majorityElementRec(nums,mid+1,hi);

        // 合并逻辑，如果左边的众数和右边的达成了一致，直接返回任意一个即可
        if(left==right) return left;

        // 否则看谁是真正的众数
        int leftCount = countInRange(nums,left,lo,hi);
        int rightCount = countInRange(nums,right,lo,hi);

        return leftCount > rightCount? left: right;
    }
}
```
