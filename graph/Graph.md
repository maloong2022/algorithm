### 图

链表、树、图的关系

链表是特殊化的树

树是特殊化的图

- N 个点 N-1 条边的连通无向图-- 树
- N 个点 N 条边的连通无向图 -- 基环树

图的存储

- 邻接矩阵 o(n^2) 适合稠密图
- 出边数组 o(n+m)
- 邻接表 o(n+m)

图的遍历

深度优先遍历

- 划分连通块

广度优先遍历

- 拓扑排序

### 实战

#### 深度搜索

684. Redundant Connection

In this problem, a tree is an undirected graph that is connected and has no cycles.

You are given a graph that started as a tree with n nodes labeled from 1 to n, with one additional edge added. The added edge has two different vertices chosen from 1 to n, and was not an edge that already existed. The graph is represented as an array edges of length n where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the graph.

Return an edge that can be removed so that the resulting graph is a tree of n nodes. If there are multiple answers, return the answer that occurs last in the input.

Example 1:

![](https://assets.leetcode.com/uploads/2021/05/02/reduntant1-1-graph.jpg)

```
Input: edges = [[1,2],[1,3],[2,3]]
Output: [2,3]
```

Example 2:

![](https://assets.leetcode.com/uploads/2021/05/02/reduntant1-2-graph.jpg)

```
Input: edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
Output: [1,4]
```

Constraints:

- n == edges.length
- 3 <= n <= 1000
- edges[i].length == 2
- 1 <= ai < bi <= edges.length
- ai != bi
- There are no repeated edges.
- The given graph is connected.

```java
class Solution {
    // 确定出边数组的容量
    private int n;
    // 出边数组
    private List<List<Integer>> to;
    // 记录访问情况
    private boolean[] vistied;
    // 记录是否有环
    private boolean hasCycle;

    public int[] findRedundantConnection(int[][] edges) {

        for(int[] edge: edges){
            int x = edge[0];
            int y = edge[1];
            n = Math.max(n,Math.max(x,y));
        }
        // 初始化出边数组,从下标1开始,此题从1开始的节点
        to = new ArrayList<>(n+1);
        // 此处注意i不能小于to的size，因为初始化的只是容量，不是size的值
        for(int i = 0; i<= n;i++){
            to.add(new ArrayList<>());
        }

        // 初始化访问数组
        vistied = new boolean[n+1];
        // 构建出边数组
        for(int [] edge: edges){
            int x = edge[0];
            int y = edge[1];
            to.get(x).add(y);
            to.get(y).add(x);
            // 每加一条边都去监控有无环的变化
            hasCycle = false;
            vistied = new boolean[n+1];
            // 从加的边开始遍历，避免加进来了孤立的点
            dfs(x,0);
            if(hasCycle) return edge;
        }
        return null;
    }

    private void dfs(int x, int fa){
        vistied[x] = true;
        // 出边数组访问x能到的周围点的方法
        for(int y: to.get(x)){
            // 由于无向图，我们采用了互相指的方式，所以要区分这种情况
            if (y == fa) continue;
            // 没有访问过继续访问
            if (!vistied[y]) dfs(y,x);
            // 如果访问过且不是我们人为的互指情况
            else hasCycle = true;
        }
    }
}

```

#### 广度搜索

207. Course Schedule

There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.

For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
Return true if you can finish all courses. Otherwise, return false.

Example 1:

```
Input: numCourses = 2, prerequisites = [[1,0]]
Output: true
Explanation: There are a total of 2 courses to take.
To take course 1 you should have finished course 0. So it is possible.
```

Example 2:

```
Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
Output: false
Explanation: There are a total of 2 courses to take.
To take course 1 you should have finished course 0, and to take course 0 you should also have finished course 1. So it is impossible.
```

Constraints:

- 1 <= numCourses <= 2000
- 0 <= prerequisites.length <= 5000
- prerequisites[i].length == 2
- 0 <= ai, bi < numCourses
- All the pairs prerequisites[i] are unique.

```java
class Solution {
    // 出边数组
    private List<List<Integer>> to;
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 初始化出边数组
        to = new ArrayList<>(numCourses);
        // 此处注意i不能小于to的size，因为初始化的只是容量，不是size的值
        for(int i=0;i< numCourses;i++) to.add(new ArrayList<>());
        // 入度数组,此题从0开始，可以不加1
        int[] inDeg = new int[numCourses];

        for(int[] pre: prerequisites){
            int ai = pre[0];
            int bi = pre[1];
            // bi可以到达ai
            to.get(bi).add(ai);
            // ai的入度加1
            inDeg[ai]++;
        }
        // 广度优先算法模版
        Queue<Integer> q = new LinkedList<>();
        // 拓扑排序第一步： 从零入度出发
        for(int i=0;i<numCourses;i++){
            if(inDeg[i]==0)q.add(i);
        }
        List<Integer> lessons = new ArrayList();
        while(!q.isEmpty()){
            // 出对头处理逻辑
            int x = q.poll();
            lessons.add(x);
            // 拓扑排序第二步：扩展一个点，周围的点入度减1（因为算过了）
            // 此处由于有入度限制所以不需要标记访问过了，如果是有环，入度不可能为0
            for(int y: to.get(x)){
                inDeg[y]--;
                // 拓扑排序第三步：入度减为0，表示可以修了，入列
                if(inDeg[y] == 0) q.add(y);
            }
        }
        return lessons.size() == numCourses;
    }
}

```

210. Course Schedule II

There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.

For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
Return the ordering of courses you should take to finish all courses. If there are many valid answers, return any of them. If it is impossible to finish all courses, return an empty array.

Example 1:

```
Input: numCourses = 2, prerequisites = [[1,0]]
Output: [0,1]
Explanation: There are a total of 2 courses to take. To take course 1 you should have finished course 0. So the correct course order is [0,1].
```

Example 2:

```
Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
Output: [0,2,1,3]
Explanation: There are a total of 4 courses to take. To take course 3 you should have finished both courses 1 and 2. Both courses 1 and 2 should be taken after you finished course 0.
So one correct course order is [0,1,2,3]. Another correct ordering is [0,2,1,3].
```

Example 3:

```
Input: numCourses = 1, prerequisites = []
Output: [0]
```

Constraints:

- 1 <= numCourses <= 2000
- 0 <= prerequisites.length <= numCourses \* (numCourses - 1)
- prerequisites[i].length == 2
- 0 <= ai, bi < numCourses
- ai != bi
- All the pairs [ai, bi] are distinct.

```java
class Solution {
    // 出边数组
    private List<List<Integer>> to;

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // 初始化出边数组
        to = new ArrayList<>(numCourses);
        for(int i=0; i< numCourses;i++) to.add(new ArrayList<>());
        // 构建入度数组
        int[] inDeg = new int[numCourses];
        for(int[] pre: prerequisites){
            int ai = pre[0];
            int bi = pre[1];
            // bi 可以到达ai
            to.get(bi).add(ai);
            // ai 入度加1
            inDeg[ai]++;
        }

        // 广度优先算法模版
        Queue<Integer> q = new LinkedList<>();
        // 初始化学习课程列表
        List<Integer> lessons = new ArrayList();
        // 入度为0的都可以先修
        for(int i=0; i< numCourses;i++){
            if(inDeg[i] == 0) q.add(i);
        }
        while(!q.isEmpty()){
            int x = q.poll();
            lessons.add(x);
            // 处理x可访问的周边的点
            for(int y: to.get(x)){
                inDeg[y]--;
                //  如果y的入度变为0了，那么可以修了
                if(inDeg[y]==0)q.add(y);
            }
        }
        if(lessons.size()==numCourses){
            int[] result = new int[numCourses];
            for(int i=0; i< numCourses;i++) result[i] = lessons.get(i);
            // 下面方式都不行
            // Integer[] result = lessons.toArray(new Integer[numCourses]);
            // return lessons.stream().toArray(int[]::new);
            return result;
        }else{
            return new int[0];
        }

    }
}

```
