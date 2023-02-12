### 单调性

84. Largest Rectangle in Histogram

Given an array of integers heights representing the histogram's bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.

Example 1:

![](https://assets.leetcode.com/uploads/2021/01/04/histogram.jpg)

```
Input: heights = [2,1,5,6,2,3]
Output: 10
Explanation: The above is a histogram where width of each bar is 1.
The largest rectangle is shown in the red area, which has an area = 10 units.
```

Example 2:

![](https://assets.leetcode.com/uploads/2021/01/04/histogram-1.jpg)

```
Input: heights = [2,4]
Output: 4
```

Constraints:

- 1 <= heights.length <= 105
- 0 <= heights[i] <= 104

```java
class Solution {
    class Rect {
        int width;
        int height;
        public Rect(int width,int height){
            this.width = width;
            this.height = height;
        }
    }
    private Stack<Rect> s = new Stack();
    public int largestRectangleArea(int[] heights) {
        int ans = 0;
        // 保证最后栈被弹空，C++ 可以采用这种方式
        //heights[heights.length] = 0;
        // 利用单调递增
        for(int height: heights){
            // 累加宽度
            int accumulateWidth = 0;
            // 当前高度破环了单调性，确定了栈顶高度，直到单调性满足
            while(!s.isEmpty()&&s.peek().height>height){
                // 累加宽度
                accumulateWidth += s.peek().width;
                // 更新答案
                ans = Math.max(ans,s.peek().height*accumulateWidth);
                // 移除栈顶
                s.pop();
            }
            // 添加栈顶
            s.push(new Rect(accumulateWidth+1,height));
        }
        // 保证最后栈被弹空
        int accumulateWidth = 0;
        while(!s.isEmpty()){
            accumulateWidth += s.peek().width;
            ans = Math.max(ans,s.peek().height*accumulateWidth);
            s.pop();
        }
        return ans;
    }
}
```

单调栈题目思维套路：

- 确定递增递减--关键在于考虑“前面不能影响到后面”的条件
- 本题中若 h[i-1] > h[i], 则 h[i-1]这个高度就无法影响到更后面，自然可以单独计算了

代码模版

- for 每个元素
- while（栈顶与新元素不满足单调性）{弹栈，更新答案，累加“宽度”}
- 入栈

42. Trapping Rain Water

Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.

Example 1:

![](https://assets.leetcode.com/uploads/2018/10/22/rainwatertrap.png)

```
Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
Output: 6
Explanation: The above elevation map (black section) is represented by array [0,1,0,2,1,0,1,3,2,1,2,1]. In this case, 6 units of rain water (blue section) are being trapped.
```

Example 2:

```
Input: height = [4,2,0,3,2,5]
Output: 9
```

Constraints:

- n == height.length
- 1 <= n <= 2 \* 104
- 0 <= height[i] <= 105

方法一：按照横条计算，利用单调递减栈

```java
class Solution {
    class Rect {
        int width;
        int height;
        public Rect(){}
        public Rect(int width,int height){
            this.width = width;
            this.height = height;
        }
    }
    Stack<Rect> s = new Stack();
    // 利用单调递减,一次一横条水考虑
    public int trap(int[] height) {
        int ans = 0;
        for(int h: height){
            int accumlatedWidth = 0;
            while(!s.isEmpty() && s.peek().height <= h){
                int bottom = s.peek().height;
                accumlatedWidth  += s.peek().width;
                s.pop();

                // 左边是空的话，水会流走，所以为0
                if(s.isEmpty()) continue;

                // 以bottom为底的横块水，最高可以到up（左右两侧高度的min）
                int up = Math.min(h,s.peek().height);
                ans += accumlatedWidth * (up - bottom);
            }
            s.push(new Rect(accumlatedWidth+1,h));
        }
        // 由于是单调递减的，如果栈中还有剩余也是留不住水的，所以不需要清空栈
        return ans;
    }
}
```

方法二：按照竖条计算，维护前缀和后缀最高值

```java
class Solution {
    // 前面最高
    int[] preMax;
    // 后面最高
    int[] sufMax;
    // 按照竖条来算
    public int trap(int[] height) {
        int n = height.length;
        preMax = new int[n];
        sufMax = new int[n];
        // 左边第一个
        preMax[0] = height[0];
        for (int i=1;i<n;i++) preMax[i] = Math.max(preMax[i-1],height[i]);
        // 右边最后一个
        sufMax[n-1] = height[n-1];
        for (int i=n-2;i>=0;i--) sufMax[i] = Math.max(sufMax[i+1],height[i]);

        int ans = 0;
        // 第一个和最后一个可以不需要考虑，流走
        for(int i=1;i<n-1;i++){
            // 高度是元素前面最大值和后面最大值中较小的
           int up = Math.min(preMax[i-1],sufMax[i+1]);
           int bottom = height[i];
           if(up>bottom) ans += (up - bottom);
        }

        return ans;
    }
}
```

#### 单调队列

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

85. Maximal Rectangle

Given a rows x cols binary matrix filled with 0's and 1's, find the largest rectangle containing only 1's and return its area.

Example 1:

![](https://assets.leetcode.com/uploads/2020/09/14/maximal.jpg)

```
Input: matrix = [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]
Output: 6
Explanation: The maximal rectangle is shown in the above picture.
```

Example 2:

```
Input: matrix = [["0"]]
Output: 0
```

Example 3:

```
Input: matrix = [["1"]]
Output: 1
```

Constraints:

- rows == matrix.length
- cols == matrix[i].length
- 1 <= row, cols <= 200
- matrix[i][j] is '0' or '1'.
