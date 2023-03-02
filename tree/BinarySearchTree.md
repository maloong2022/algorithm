### 二叉搜索树 Binary Search Tree

满足如下性质（BST 性质）的二叉树

- 任意节点的关键码 >= 他的左子树中所有节点的关键码
- 任意节点的关键码 <= 他的右子树中所有节点的关键码

根据以上性质，二叉搜索树的中序遍历必然为一个有序序列

#### BST - 建立

为了避免越界，减少边界的特殊情况判断，一般在 BST 中额外插入两个保护节点

- 一个关键码为正无穷大（一个很大的正整数）
- 一个关键码为负无穷大

仅由这两个节点构成的 BST 就是一个初始的空 BST

#### BST - 检索

检索关键码 val 是否存在
从根开始递归查找

- 若当前节点的关键码等于 val，则已经找到
- 若关键码大于 val，递归检索左子树（或不存在）
- 若关键码小于 val，递归检索右子树（或不存在）

#### BST - 插入

插入 val 与检索 val 的过程类似

- 若检索发现存在，则放弃插入（或把 val 对应节点的计数+1，视要求而定）
- 若检索发现不存在（子树为空），直接在对应位置新建关键码为 val 的节点

#### BST - 求前驱/后继

前驱： BST 中小于 val 的最大节点
后继： BST 中大于 val 的最小节点

求前驱和后继也是基于检索的，先检索 val
以后继为例：

- 如果检索到了 val，并且 val 存在右子树，则在右子树一直往左走到底
- 否则说明没有找到 val 或者 val 没有右子树，此时前驱就在检索过程经过的节点中（即当前节点的所有祖先节点，可以拿一个变量顺便求一下）

#### BST - 删除

从 BST 中删除关键码为 val 的节点，可以基于检索+ 求后继实现
首先检索 val

- 如果 val 只有一颗子树，直接删除 val，把子树和父节点相连就好了
- 如果由两颗子树，需要找到后继，先删除后继，再用后继节点代替 val 的位置（因为后继是右子树一直往左走到底，所以它最多只会由一个子树）

#### BST 时间复杂度

查询、出入、求前驱、求后继、删除操作的时间复杂度：
随机数据期望 O(ongN)
在非随机数据上，BST 容易退化为 O(N)，一般都要结合旋转操作来进行平衡（保证它不往一边偏）。
保证性能的关键：左右子树节点数平衡

### 实战

701. Insert into a Binary Search Tree

You are given the root node of a binary search tree (BST) and a value to insert into the tree. Return the root node of the BST after the insertion. It is guaranteed that the new value does not exist in the original BST.

Notice that there may exist multiple valid ways for the insertion, as long as the tree remains a BST after insertion. You can return any of them.

Example 1:
![](https://assets.leetcode.com/uploads/2020/10/05/insertbst.jpg)

```
Input: root = [4,2,7,1,3], val = 5
Output: [4,2,7,1,3,5]
Explanation: Another accepted tree is:
```

![](https://assets.leetcode.com/uploads/2020/10/05/bst.jpg)

Example 2:

```
Input: root = [40,20,60,10,30,50,70], val = 25
Output: [40,20,60,10,30,50,70,null,null,25]
```

Example 3:

```
Input: root = [4,2,7,1,3,null,null,null,null,null,null], val = 5
Output: [4,2,7,1,3,5]
```

Constraints:

- The number of nodes in the tree will be in the range [0, 104].
- -108 <= Node.val <= 108
- All the values Node.val are unique.
- -108 <= val <= 108
- It's guaranteed that val does not exist in the original BST.

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public TreeNode insertIntoBST(TreeNode root, int val) {
        if(root == null) return new TreeNode(val);

        if(val<root.val){
            // 重新赋值，以为root会变，假如为空，则返回新节点
            root.left = insertIntoBST(root.left,val);
        }else{
            root.right = insertIntoBST(root.right,val);
        }

        return root;
    }
}
```

面试题 04.06. Successor LCCI

Write an algorithm to find the "next" node (i.e., in-order successor) of a given node in a binary search tree.

Return null if there's no "next" node for the given node.

Example 1:

```
Input: root = [2,1,3], p = 1

  2
 / \
1   3

Output: 2
```

Example 2:

```
Input: root = [5,3,6,2,4,null,null,1], p = 6

      5
     / \
    3   6
   / \
  2   4
 /
1

Output: null
```

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {

    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        return getNext(root,p.val);
    }

    public TreeNode getNext(TreeNode root, int val){
        TreeNode ans = null;
        TreeNode curr = root;
        // 用while替代递归
        while(curr != null){
            // 找到了
            if(curr.val == val){
                // 右子树不为空，一路向左
                if(curr.right != null){
                    ans = curr.right;
                    while(ans.left !=null) ans = ans.left;
                }
                break;
            }
            // 当前节点大于寻找的节点，向左
            if(val < curr.val){
                // 没有找到，肯定在寻找的路径里面。在大于val值里面取最小的值
                if(ans == null || ans.val > curr.val) ans = curr;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return ans;
    }
}
```

450. Delete Node in a BST

Given a root node reference of a BST and a key, delete the node with the given key in the BST. Return the root node reference (possibly updated) of the BST.

Basically, the deletion can be divided into two stages:

Search for a node to remove.
If the node is found, delete the node.

Example 1:
![](https://assets.leetcode.com/uploads/2020/09/04/del_node_1.jpg)

```
Input: root = [5,3,6,2,4,null,7], key = 3
Output: [5,4,6,2,null,null,7]
Explanation: Given key to delete is 3. So we find the node with value 3 and delete it.
One valid answer is [5,4,6,2,null,null,7], shown in the above BST.
Please notice that another valid answer is [5,2,6,null,4,null,7] and it's also accepted.
```

![](https://assets.leetcode.com/uploads/2020/09/04/del_node_supp.jpg)

Example 2:

```
Input: root = [5,3,6,2,4,null,7], key = 0
Output: [5,3,6,2,4,null,7]
Explanation: The tree does not contain a node with value = 0.
```

Example 3:

```
Input: root = [], key = 0
Output: []
```

Constraints:

- The number of nodes in the tree is in the range [0, 104].
- -105 <= Node.val <= 105
- Each node has a unique value.
- root is a valid binary search tree.
- -105 <= key <= 105

Follow up: Could you solve it with time complexity O(height of tree)?

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public TreeNode deleteNode(TreeNode root, int key) {
        if(root==null) return null;
        //找到了
        if(root.val == key){
            // 如果是单子树直接返回兄弟节点，让上层递归进行链接
            if(root.left==null) return root.right;
            if(root.right==null) return root.left;
            // 如果有左子树和右子树，在后继节点
            // 1. 从右子树找
            TreeNode next = root.right;
            // 2. 一路向左
            while(next.left != null) next = next.left;
            // 删除后继节点
            root.right = deleteNode(root.right,next.val);
            // 用后继节点替换根节点
            root.val = next.val;
            return root;
        }
        // key小于找左子树
        if(key < root.val){
            root.left = deleteNode(root.left,key);
        }else{//可以大于找右子树
            root.right = deleteNode(root.right,key);
        }
        return root;
    }
}
```
