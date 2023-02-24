### 树的遍历

先序、中序、后序一般用递归来求，树的先序遍历又称树的深度优先遍历。

层次序一般借助队列来求妈，树的层序遍历又称树的广度优先遍历。

### 实战

#### 深度优先遍历

94. Binary Tree Inorder Traversal

Given the root of a binary tree, return the inorder traversal of its nodes' values.

Example 1:

![](https://assets.leetcode.com/uploads/2020/09/15/inorder_1.jpg)

```bash
Input: root = [1,null,2,3]
Output: [1,3,2]
```

Example 2:

```
Input: root = []
Output: []
```

Example 3:

```
Input: root = [1]
Output: [1]
```

Constraints:

- The number of nodes in the tree is in the range [0, 100].
- -100 <= Node.val <= 100

Follow up: Recursive solution is trivial, could you do it iteratively?

```java
class Solution {
    private List<Integer> seq;
    public List<Integer> inorderTraversal(TreeNode root){
        seq = new ArrayList<>();
        dfs(root);
        return seq;
    }

    private void dfs(TreeNode root){
        if(root == null) return;
        dfs(root.left);
        seq.add(root.val);
        dfs(root.right);
    }
}
```

589. N-ary Tree Preorder Traversal

Given the root of an n-ary tree, return the preorder traversal of its nodes' values.

Nary-Tree input serialization is represented in their level order traversal. Each group of children is separated by the null value (See examples)

Example 1:

![](https://assets.leetcode.com/uploads/2018/10/12/narytreeexample.pn)

```
Input: root = [1,null,3,2,4,null,5,6]
Output: [1,3,5,6,2,4]
```

Example 2:

![](https://assets.leetcode.com/uploads/2019/11/08/sample_4_964.png)

```
Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
Output: [1,2,3,6,7,11,14,4,8,12,5,9,13,10]
```

Constraints:

- The number of nodes in the tree is in the range [0, 104].
- 0 <= Node.val <= 104
- The height of the n-ary tree is less than or equal to 1000.

Follow up: Recursive solution is trivial, could you do it iteratively?

1. Recursive

```java
class Solution{
  private List<Integer> seq;
  public List<Integer> preorder(Node root){
      seq = new ArrayList<>();
      dfs(root);
      return seq;
  }

  private void dfs(Node root){
      if (root == null) return;
      seq.add(root.val);
      for(Node child: root.children){
        dfs(child);
      }
  }
}
```

2. Iterative

```java
class Solution{
  private List<Integer> seq;
  public List<Integer> preorder(Node root){
    seq = new ArrayList<>();
    // 特例
    if(root == null) return seq;
    Stack<Node> s = new Stack<>();
    // 先遍历根
    s.push(root);
    while(!s.isEmpty()){
        Node node = s.pop();
        // 遍历根
        seq.add(node.val)
        // 由于栈是后进先出，所以孩子反着放
        for (int i = node.children.size()-1;i>=0;i--){
            s.push(node.children.get(i));
        }
    }
    return seq;
  }

}

```

#### 层序遍历

429. N-ary Tree Level Order Traversal

Given an n-ary tree, return the level order traversal of its nodes' values.

Nary-Tree input serialization is represented in their level order traversal, each group of children is separated by the null value (See examples).

Example 1:

![](https://assets.leetcode.com/uploads/2018/10/12/narytreeexample.png)

```
Input: root = [1,null,3,2,4,null,5,6]
Output: [[1],[3,2,4],[5,6]]
```

Example 2:

![](https://assets.leetcode.com/uploads/2019/11/08/sample_4_964.png)

```
Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
Output: [[1],[2,3,4,5],[6,7,8,9,10],[11,12,13],[14]]
```

Constraints:

- The height of the n-ary tree is less than or equal to 1000
- The total number of nodes is between [0, 104]

```java

class Solution {
    public List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        // 返回特例
        if(root == null) return res;
        // Pair存本结点以及此结点的层数
        Queue<Pair<Node,Integer>> seq = new LinkedList<Pair<Node,Integer>>();
        // 从根开始，且根为0层
        seq.add(new Pair<Node,Integer>(root,0));
        while(!seq.isEmpty()){
            // 取出结点
            Node node = seq.peek().getKey();
            // 取出层数，此时结点可以出列
            Integer  depth = seq.poll().getValue();
            // 层数大于数组长度，说明要新开数组存结点
            if (depth >= res.size()) res.add(new ArrayList());
            // 存储刚刚取出结点的值
            res.get(depth).add(node.val);
            // 处理取出结点的孩子
            for(Node child: node.children){
                // 存入处理队列，深度加1，因为孩子是下一层了
                seq.add(new Pair<Node,Integer>(child,depth+1));
            }
        }
        return res;
    }
}
```

#### 树的序列化

105. Construct Binary Tree from Preorder and Inorder Traversal

Given two integer arrays preorder and inorder where preorder is the preorder traversal of a binary tree and inorder is the inorder traversal of the same tree, construct and return the binary tree.

Example 1:

![](https://assets.leetcode.com/uploads/2021/02/19/tree.jpg)

```
Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
Output: [3,9,20,null,null,15,7]
```

Example 2:

```
Input: preorder = [-1], inorder = [-1]
Output: [-1]
```

Constraints:

- 1 <= preorder.length <= 3000
- inorder.length == preorder.length
- -3000 <= preorder[i], inorder[i] <= 3000
- preorder and inorder consist of unique values.
- Each value of inorder also appears in preorder.
- preorder is guaranteed to be the preorder traversal of the tree.
- inorder is guaranteed to be the inorder traversal of the tree.

```java
class Solution {
    int[] preorder;
    int[] inorder;
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        this.preorder = preorder;
        this.inorder = inorder;
        return build(0, preorder.length -1, 0, inorder.length - 1);
    }
    // 用preorder[l1...r1] 和inorder[l2...r2]还原二叉树
    private TreeNode build(int l1,int r1,int l2,int r2){
        if (l1 > r1) return null; // 递归边界
        TreeNode root = new TreeNode(preorder[l1]);
        int mid = l2; // mid是root在中序中的位置
        while(inorder[mid]!= root.val) mid++;
        // 中序中l2 ~ (mid-1)就是左子树
        // 中序中 (mid + 1) ~ r2就是右子树
        // 左子树先序起点，除去第一位根，l1+1，终点为：起点+ 个数 - 1（因为下标是从0开始的）
        // 个数为： 终点 - 起点 + 1
        // (l1 + 1) + [(mid - 1) - (l2) + 1 ]  - 1
        // 两点之间的个数等于末尾数-开始数 + 1
        root.left = build(l1 + 1, l1 + (mid - l2),l2, mid-1);
        // 右子树比较简单了
        root.right = build(l1 + (mid -l2) +1, r1, mid + 1, r2);
        return root;
    }

}
```

**终点 = 起点 + 个数 - 1；**
**个数 = 终点 - 起点 + 1；**

106. Construct Binary Tree from Inorder and Postorder Traversal

Given two integer arrays inorder and postorder where inorder is the inorder traversal of a binary tree and postorder is the postorder traversal of the same tree, construct and return the binary tree.

Example 1:

![](https://assets.leetcode.com/uploads/2021/02/19/tree.jpg)

```
Input: inorder = [9,3,15,20,7], postorder = [9,15,7,20,3]
Output: [3,9,20,null,null,15,7]
```

Example 2:

```
Input: inorder = [-1], postorder = [-1]
Output: [-1]
```

Constraints:

- 1 <= inorder.length <= 3000
- postorder.length == inorder.length
- -3000 <= inorder[i], postorder[i] <= 3000
- inorder and postorder consist of unique values.
- Each value of postorder also appears in inorder.
- inorder is guaranteed to be the inorder traversal of the tree.
- postorder is guaranteed to be the postorder traversal of the tree.

```java
class Solution {
    private int[] inorder;
    private int[] postorder;
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        this.inorder = inorder;
        this.postorder = postorder;
        return build(0, inorder.length-1,0,postorder.length-1);
    }

    TreeNode build(int l1,int r1,int l2, int r2){
        if(l1 > r1) return null;
        TreeNode root = new TreeNode(postorder[r2]);
        int mid = l1;
         while(inorder[mid]!= root.val)mid++;
        // 下面这种方式不行，因为没有记录找的过程，没有值的话mid不更新
        // for(int i=0;i<r1;i++){
        //     if(inorder[i] == root.val){
        //         mid = i;
        //         break;
        //     }
        // }
        root.left = build(l1, mid-1,l2,l2+(mid-1-l1+1)-1);
        root.right = build(mid+1,r1,l2+(mid-1-l1+1)-1+1,r2-1);
        return root;
    }
}

```

297. Serialize and Deserialize Binary Tree

Serialization is the process of converting a data structure or object into a sequence of bits so that it can be stored in a file or memory buffer, or transmitted across a network connection link to be reconstructed later in the same or another computer environment.

Design an algorithm to serialize and deserialize a binary tree. There is no restriction on how your serialization/deserialization algorithm should work. You just need to ensure that a binary tree can be serialized to a string and this string can be deserialized to the original tree structure.

Clarification: The input/output format is the same as how LeetCode serializes a binary tree. You do not necessarily need to follow this format, so please be creative and come up with different approaches yourself.

Example 1:

![](https://assets.leetcode.com/uploads/2020/09/15/serdeser.jpg)

```
Input: root = [1,2,3,null,null,4,5]
Output: [1,2,3,null,null,4,5]
```

Example 2:

```
Input: root = []
Output: []
```

Constraints:

- The number of nodes in the tree is in the range [0, 104].
- -1000 <= Node.val <= 1000

```java
public class Codec {

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        List<String> seq = new ArrayList<String>();
        dfs(seq,root);
        return String.join(",",seq);
    }
    //由于可能有重复值，不能使用先序加中序的方式实现
    //先序单独之所以不能完成，是由于我们不知道什么时候换枝
    // 所以 构造 1,2,null,null,3,4,null,null,5,null,null
    // 使用null来标记结束
    void dfs(List<String> seq, TreeNode root){
        if(root == null){
            // 正常的dfs直接返回，此处因为要构造上面提到的结构，需要先加null，再返回
            seq.add("null");
            return;
        }
        // 先根
        seq.add(Integer.toString(root.val));
        // 再左子树
        dfs(seq,root.left);
        // 最后右子树
        dfs(seq,root.right);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        seq = data.split(",");
        return store();
    }

    TreeNode store(){
        // 此处curr是seq位置一直递增的，不需要还原现场
        if(seq[curr].equals("null")){
            // 移动位置,处理下一位
            curr++;
            return null;
        }
        // 非空说明是需要还原的根结点
        TreeNode root = new TreeNode(Integer.parseInt(seq[curr]));
        // 移动位置,看下一位是否需要还原
        curr++;
        // 处理根结点左右子树
        root.left = store();
        root.right = store();
        // 最后返回根结点
        return root;
    }

   String[] seq;
   // 当前位置
   int curr;
}
```

#### 树的直径

两次深度优先遍历

1. 第一次从任意出发点出发，找到离他最远的点 P。（P 点必为直径的一端）
2. 再从 P 点出发，找到离他最远的点 Q。
3. 连接 P，Q 即为树的直径。

```java
import javafx.util.Pair;

import java.util.*;

public class Solution {

    // 出边数组
    private List<List<Integer>> to = new ArrayList<>();
    // 广度优先辅助队列
    private Queue<Integer> q = new LinkedList<>();

    private int n = 0;


    public int treeDiameter(List<List<Integer>> edges) {

        for (List<Integer> edge : edges) {
            int x = edge.get(0);
            int y = edge.get(1);
            n = Math.max(n, Math.max(x, y));
        }
        // 从0开始，所以数组个数为n+1
        n++;
        // 初始化出边数组,一个结点，与他可以到达的所有点
        for (int i = 0; i < n; i++) to.add(new ArrayList<>());
        for (List<Integer> edge : edges) {
            int x = edge.get(0);
            int y = edge.get(1);
            // x可以到达y
            to.get(x).add(y);
            // y也可以到达x
            to.get(y).add(x);
        }

        // 从0出发找到最远的点P
        int p = findFarthest(0).getKey();
        // 从P出发找到最远的点，返回距离，
        // 因为要求的是距离
        return findFarthest(p).getValue();


    }

    // <点，距离>
    Pair<Integer, Integer> findFarthest(int start) {
        // init the depth list
        List<Integer> depth = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            depth.add(-1);
        }
        // just the level search aglor template
        q.add(start);
        depth.set(start, 0);
        while (!q.isEmpty()) {
            // poll the queue
            int x = q.poll();
            // deal with the children
            for (int y : to.get(x)) {
                // if the depth is not equarl -1, it had walked
                if (depth.get(y) != -1) continue;
                // update the depth of y, it's equarl the parent plus 1
                depth.set(y, depth.get(x) + 1);
                // add the child y
                q.add(y);
            }
        }
        // find the max depth
        int ans = start;
        for (int i = 0; i < n; i++) {
            if (depth.get(i) > depth.get(ans)) ans = i;
        }
        return new Pair<>(ans, depth.get(ans));


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

The Fix way of Thinking:

At first, get the parent node, and then use the up forward flag method,
Why is it call up forward flag method, I think it because the recursive
let the first return is the node self and then up forward to flag the
branch has the node

- p up to root flag to red
- q up until see the red color at first, then the node is the LCA.

1. Depth aglor

```c++
/**
 * Definition for a binary tree node.
 * struct TreeNode {
 *     int val;
 *     TreeNode *left;
 *     TreeNode *right;
 *     TreeNode(int x) : val(x), left(NULL), right(NULL) {}
 * };
 */
class Solution {
public:
    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
        this->p = p;
        this->q = q;
        dfs(root);
        return ans;
    }

private:
    TreeNode* p;
    TreeNode* q;
    TreeNode* ans;
    // 深度遍历树，找到最深的一颗子树，既包含p也包含q的结点就是最近公共祖先
    // pair<包含p，包含q>
    pair<bool,bool> dfs(TreeNode* root){
        if(root == nullptr) return {false,false};
        pair<bool,bool> leftResult = dfs(root->left);
        pair<bool,bool> rightResult = dfs(root->right);
        pair<bool,bool> result;
        // 子树中要么左边有p，要么右边有p，或者是跟结点就是p
        result.first = (leftResult.first || rightResult.first || root->val == p->val);
        // 子树中要么左边有q，要么右边有q，或者是跟结点就是q
        result.second = (leftResult.second || rightResult.second || root->val == q->val);
        // 子树中同时包含p和q，并且此时ans为空，表示第一次相遇
        if (result.first && result.second && ans == nullptr) ans = root;
        return result;
    }
};
```

2. split sub question

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

### 基环树

向一棵树添加一条边，就形成了一个环，此时整个结构被称为基环树（preudotree/unicyclic graph）
