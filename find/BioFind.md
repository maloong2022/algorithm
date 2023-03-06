### 二分查找的前提

- 目标函数具有单调性（单调递增或者递减）
- 存在上下界（bounded）
- 能够通过索引访问（index accessible）

### 模版与实战

1. 普通二分

```java
int left = 0, right = n-1;
while(left<=right){
        int mid = (left + right) / 2;
        if(array[mid] == target){
                // find the target
                break or return mid;
        }
        if(array[mid] < target){
                left = mid + 1;
        }else{
                right = mid - 1;
        }
}
```

704. Binary Search

Given an array of integers nums which is sorted in ascending order, and an integer target, write a function to search target in nums. If target exists, then return its index. Otherwise, return -1.

You must write an algorithm with O(log n) runtime complexity.

Example 1:

```
Input: nums = [-1,0,3,5,9,12], target = 9
Output: 4
Explanation: 9 exists in nums and its index is 4
```

Example 2:

```
Input: nums = [-1,0,3,5,9,12], target = 2
Output: -1
Explanation: 2 does not exist in nums so return -1
```

Constraints:

- 1 <= nums.length <= 104
- -104 < nums[i], target < 104
- All the integers in nums are unique.
- nums is sorted in ascending order.

```java
class Solution {
    public int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while(left <= right){
            int mid = (left+right) / 2;
            // 怕爆int可以使用long，也可以写成 mid = left + (right-left)/2;
            if(nums[mid] == target){
                return mid;
            }
            if(nums[mid] < target){
                left = mid + 1;
            }else{
                right = mid - 1;
            }
        }
        return -1;
    }
}
```

2. lower_bound / upper_bound

lower_bound 和 upper_bound 的问题是：给定的 target 不一定在数组中存在，array[mid]即使不等于 target，也可能就是最后的答案，不能随便排除在外。

解决方案：
掌握三种中的一种，推荐第一种：

1. （1.1）+（1.2）：最严谨的划分，一侧包含，一侧不包含，终止于 left==right
2. （2）：双侧都包含，用 ans 维护答案，终止于 left>right (思想是：专门出来 mid 可能会是答案)
3. （3）：双侧都不包含，终止于 left+1 == right，最后再检查答案 （思想是：答案要么是 left，要么是 right，每次都带着 mid，要么不存在）

1.1 的模版-- 后继型

查找 lower_bound（第一个>=target 的数），不存在返回 n

```java
int left = 0, right = n;
while(left < right){
        int mid = (left+right) >> 1;// 肯定访问不到n,向下取整
        if(array[mid]>=target) // condition satisfied,should be included
            right = mid;
        else
            left = mid + 1;
}
return right;
```

改为

```java
array[mid] > taget;
```

就是 upper_bound。

1.2 的模版 -- 前驱型
查最后一个<=target 的数，不存在返回-1

```java
int left = -1, right = n-1;
while(left<right){
        int mid = (left+right+1) >> 1;//向上取整
        if(array[mid] <= target)// condition satisfied, should be included
            left = mid;
        else
            right = mid - 1;
}
return right;
```

2 的模版，求小于 target 的最大值

```java
int left = 0,right = n - 1;
int ans = -1;
while(left <= right){
    int mid = (left + right)/2;
    if(array[mid] <= target){
            //update ans using mid
            ans = Math.max(ans,mid);
        left = mid + 1;
    } else {
            right = mid - 1;
    }
}
```

3 的模版

```java
int left = 0, right = n - 1;
// [L,R] 这种情况会死循环，所以采用这种情况退出循环
while(left+1 < right){
    int mid = (left+right)/2;
    if(array[mid]<=target){
            left = mid;
    }else{
            right = mid;
    }
}
```

153. Find Minimum in Rotated Sorted Array

Suppose an array of length n sorted in ascending order is rotated between 1 and n times. For example, the array nums = [0,1,2,4,5,6,7] might become:

- [4,5,6,7,0,1,2] if it was rotated 4 times.
- [0,1,2,4,5,6,7] if it was rotated 7 times.

Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].

Given the sorted rotated array nums of unique elements, return the minimum element of this array.

You must write an algorithm that runs in O(log n) time.

Example 1:

```
Input: nums = [3,4,5,1,2]
Output: 1
Explanation: The original array was [1,2,3,4,5] rotated 3 times.
```

Example 2:

```
Input: nums = [4,5,6,7,0,1,2]
Output: 0
Explanation: The original array was [0,1,2,4,5,6,7] and it was rotated 4 times.
```

Example 3:

```
Input: nums = [11,13,15,17]
Output: 11
Explanation: The original array was [11,13,15,17] and it was rotated 4 times.
```

Constraints:

- n == nums.length
- 1 <= n <= 5000
- -5000 <= nums[i] <= 5000
- All the integers of nums are unique.
- nums is sorted and rotated between 1 and n times.

```java
class Solution {
    public int findMin(int[] nums) {
        /*
        3 4 5 1 2 让每个数尝试跟结尾比较nums[i] <= nums[n-1]
        0 0 0 1 1 这样就相当于一个有序序列，可以用二分了
        */
        int left = 0, right = nums.length - 1; //由题意肯定有答案，此处就不必要取left=-1或者right = n了
        while(left < right){
            int mid = (left+right) >> 1;
            if(nums[mid] <= nums[nums.length-1]){
                // 表明可能是答案了，对应例子的1的位置
                right = mid;
            }else{
                left = mid + 1;
            }
        }
        return nums[right];//结束的时候right=left，随便返回一个就好了
    }
}
```

154. Find Minimum in Rotated Sorted Array II

Suppose an array of length n sorted in ascending order is rotated between 1 and n times. For example, the array nums = [0,1,4,4,5,6,7] might become:

- [4,5,6,7,0,1,4] if it was rotated 4 times.
- [0,1,4,4,5,6,7] if it was rotated 7 times.

Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].

Given the sorted rotated array nums that may contain duplicates, return the minimum element of this array.

You must decrease the overall operation steps as much as possible.

Example 1:

```
Input: nums = [1,3,5]
Output: 1
```

Example 2:

```
Input: nums = [2,2,2,0,1]
Output: 0
```

Constraints:

- n == nums.length
- 1 <= n <= 5000
- -5000 <= nums[i] <= 5000
- nums is sorted and rotated between 1 and n times.

Follow up: This problem is similar to Find Minimum in Rotated Sorted Array, but nums may contain duplicates. Would this affect the runtime complexity? How and why?

```java

```

#### 二分总结

**推荐使用 1.1+1.2**

只要“条件”单调，二分就适用

- 旋转排序数组中的最小值，序列本身并不单调
- 但“<=结尾“这个条件，把序列分成两半，一半不满足（>结尾），一半满足（<=结尾）
  可以把“条件满足”看作 1，不满足看作 0，这就是一个 0/1 分段函数，二分查找分界点。

写出正确的二分代码“三步走”：

1. 写出二分的条件（一般是不等式，例如 upper_bound：> val 的数中最小的）
2. 把条件放到 if(...)里，并确定满足条件时要小的（right=mid）还是要大的（left=mid）
3. 另一半放到 else 里(left=mid+1 或者 right=mid-1),如果是后者，求 mid 时补+1
4. 如果题目有无解的情况，上界加 1 或者下界减 1，用于表示无解。

为什么要向上取整也就是加 1，是为了处理特殊情况:数组只有两种情况的话[L,R]，

```java
  if(array[mid] <= target)// condition satisfied, should be included
    left = mid;
  else
    right = mid - 1;

```

如果向下取整，此时 mid=L，进入第一个分枝，left=L，right=R，这样就死循环了，永远不可能 right=left。
所以向上取整，此时 mid=R,进入第一个分支 left=R，left=right，如果进入第二个分支 right=L，也等于 left。

34. Find First and Last Position of Element in Sorted Array

Given an array of integers nums sorted in non-decreasing order, find the starting and ending position of a given target value.

If target is not found in the array, return [-1, -1].

You must write an algorithm with O(log n) runtime complexity.

Example 1:

```
Input: nums = [5,7,7,8,8,10], target = 8
Output: [3,4]
```

Example 2:

```
Input: nums = [5,7,7,8,8,10], target = 6
Output: [-1,-1]
```

Example 3:

```
Input: nums = [], target = 0
Output: [-1,-1]
```

Constraints:

- 0 <= nums.length <= 105
- -109 <= nums[i] <= 109
- nums is a non-decreasing array.
- -109 <= target <= 109

```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        // 开始位置：第一个>=target的数
        // 结束位置：最后一个<=target的树
        int[] ans = new int[2];

        int left = 0, right = nums.length;//有无解;
        while(left < right){
            int mid = (left+right) >> 1;
            if(nums[mid] >= target){
                right = mid;
            }else{
                left = mid + 1;
            }
        }

        ans[0] = right;
        // 有无解
        left = -1;right = nums.length -1;
        while(left<right){
            int mid = (left+right+1)>>1;
            if(nums[mid]<=target){
                left = mid;
            }else{
                right = mid - 1;
            }
        }

        ans[1] =right;
        // 避免出现无解的情况
        if(ans[0]>ans[1]){
            ans[0] = -1;
            ans[1] = -1;
        }
        return ans;
    }
}
```

69. Sqrt(x)

Given a non-negative integer x, return the square root of x rounded down to the nearest integer. The returned integer should be non-negative as well.

You must not use any built-in exponent function or operator.

- For example, do not use pow(x, 0.5) in c++ or x \*\* 0.5 in python.

Example 1:

```
Input: x = 4
Output: 2
Explanation: The square root of 4 is 2, so we return 2.
```

Example 2:

```
Input: x = 8
Output: 2
Explanation: The square root of 8 is 2.82842..., and since we round it down to the nearest integer, 2 is returned.
```

Constraints:

- 0 <= x <= 231 - 1

```java
class Solution {
    public int mySqrt(int x) {
        // 找最大的ans： 其中满足ans*ans<= x
        // 例如 x = 8, 0 满足， 1满足，2满足，3不满足
        int left = 0,right = x;
        while(left<right){
            // 由于时right=mid-1，所以向上取整
            int mid = (left+right+1) >> 1;
            // mid*mid可能会爆int，因为题目给出x取值范围为int最大值
            // 也可以强制转换为long
            if(mid <= x/mid){
                left = mid;
            }else{
                right = mid - 1;
            }
        }
        return right;
    }
}

```

### 实数二分

求实数的平方根，答案精确 5 位

```java
double myRealSqrt(){
        double left=0,right=x;
        // 相差比答案多差两位
        while(right-left > 1e-7){
                double  mid = (left+right)/2;//由于实数是一个点，所以不需要向上或者向下取整了
                if(mid*mid <= x){
                        left = mid;
                }else{
                        // 同样不需要加1 了
                        right = mid;
                }
        }
}
```

### 三分查找

二分用于在单调函数上找特定值
三分用于在单峰函数上找极大值（或者单谷函数的极小值）
三分法也用于求函数的局部极大/极小值
要求：函数是分段严格单调递增/递减的（不能出现一段平的情况）

以求单峰函数 f 的极大值为例，可在定义域[l,r]上取任意点 lmid，rmid

- 若 f(lmid)<=f(rmid),则函数必然在 lmid 处单调递增，极值在[lmid,r]上
- 若 f(lmid)>f(rmid),则函数必然在 rmid 处单调递减，极值在[l,rmid]上

* lmid，rmid 可取三等分点
* 也可取 lmid 为二等分点，rmid 为 lmid 稍加一点偏移量
* 取黄金分割点最快

### 实战

162. Find Peak Element

A peak element is an element that is strictly greater than its neighbors.

Given a 0-indexed integer array nums, find a peak element, and return its index. If the array contains multiple peaks, return the index to any of the peaks.

You may imagine that nums[-1] = nums[n] = -∞. In other words, an element is always considered to be strictly greater than a neighbor that is outside the array.

You must write an algorithm that runs in O(log n) time.

Example 1:

```
Input: nums = [1,2,3,1]
Output: 2
Explanation: 3 is a peak element and your function should return the index number 2.
```

Example 2:

```
Input: nums = [1,2,1,3,5,6,4]
Output: 5
Explanation: Your function can return either index number 1 where the peak element is 2, or index number 5 where the peak element is 6.
```

Constraints:

- 1 <= nums.length <= 1000
- -231 <= nums[i] <= 231 - 1
- nums[i] != nums[i + 1] for all valid i.

```java
class Solution {
    public int findPeakElement(int[] nums) {
        int left=0, right = nums.length-1;
        while(left<right){
            // 三分，向下取整
            int lmid = (left+right)/2;
            // rmid只比lmid多一点点，由于是整数，故加1就好了
            int rmid = lmid + 1;
            // 单调递增，左边肯定不是结果，lmid肯定不是
            if(nums[lmid]<=nums[rmid]) left = lmid+1;
            // 单调递减，右边肯定不是结果
            else right = rmid -1;
        }
        return right;
    }
}
```

### 二分答案

把最优性问题转化为判定问题的基本技巧

对于一个最优化问题
求解：求一个最优解（最大值/最小值）
判定：给一个解，判断它是否合法（是否能够实现）

“判定”通常要比“求解”简单很多
如果我们有了一个判定算法，那把解空间枚举+判定一遍，就得到解了

当解空间具有单调性时，就可以用二分代替枚举，利用二分+判定的方法快速求出最优解，这种方法称为二分答案法。

例如：求解--猜数；判定--大了还是小了；
低效算法：1 到 n 挨个猜一遍；高效算法：二分。

374. Guess Number Higher or Lower

We are playing the Guess Game. The game is as follows:

I pick a number from 1 to n. You have to guess which number I picked.

Every time you guess wrong, I will tell you whether the number I picked is higher or lower than your guess.

You call a pre-defined API int guess(int num), which returns three possible results:

- -1: Your guess is higher than the number I picked (i.e. num > pick).
- 1: Your guess is lower than the number I picked (i.e. num < pick).
- 0: your guess is equal to the number I picked (i.e. num == pick).
- Return the number that I picked.

Example 1:

```
Input: n = 10, pick = 6
Output: 6
```

Example 2:

```
Input: n = 1, pick = 1
Output: 1
```

Example 3:

```
Input: n = 2, pick = 1
Output: 1
```

Constraints:

- 1 <= n <= 231 - 1
- 1 <= pick <= n

```java

```

410. Split Array Largest Sum

Given an integer array nums and an integer k, split nums into k non-empty subarrays such that the largest sum of any subarray is minimized.

Return the minimized largest sum of the split.

A subarray is a contiguous part of the array.

Example 1:

```
Input: nums = [7,2,5,10,8], k = 2
Output: 18
Explanation: There are four ways to split nums into two subarrays.
The best way is to split it into [7,2,5] and [10,8], where the largest sum among the two subarrays is only 18.
```

Example 2:

```
Input: nums = [1,2,3,4,5], k = 2
Output: 9
Explanation: There are four ways to split nums into two subarrays.
The best way is to split it into [1,2,3] and [4,5], where the largest sum among the two subarrays is only 9.
```

Constraints:

- 1 <= nums.length <= 1000
- 0 <= nums[i] <= 106
- 1 <= k <= min(50, nums.length)

```java
class Solution {
    public int splitArray(int[] nums, int k) {
        int left = 0, right = 0;
        for(int num:nums){
            // 左边最小数不能小于元素本身
            left = Math.max(left,num);
            // 右边值肯定小于所有数字之和
            right += num;
        }
        // 二分查找
        while(left<right){
            // 由于left = mid + 1;向下取整
            int mid = (right+left)/2;
            if(validate(nums,k,mid)){
                // 需要最小的，所以需要左边的值，移动right，并且mid可能是答案
                right = mid;
            }else{
                left = mid + 1;
            }
        }
        return right;

    }

    // m：要求分割数；size：指定大小
    boolean validate(int[] nums,int m,int size){
        int box = 0;
        int count = 1;
        for(int num: nums){
            // 盒子加上num还小于等于指定值，可以继续加
            if(box+num <= size){
                box += num;
            }else{
                // 新开一个盒子
                count++;
                box = num;
            }
        }
        // 返回盒子数是否满足要求
        return count<=m;
    }
}

```

求解：最小化“m 个子数组各自和的最大值”
判定：给一个数值 T，“m 个子树组各自和的最大值<=T"是否合法
换一种说法：“能否将 nums 分成 m 个连续子数组，每组的和<=T“
这个解空间具有特殊的单调性--单调分段 0/1 函数

```
true  ^                    一一一一一一一一一一一一
false |-------------------|-------------------------------->
                         18
```

直接求出 18 比较困难，但可以通过猜测一个值 T，判断 T 是否合法（true or false），从而得知答案是在 T 左侧还是右侧。
最高效的猜测方法当然就是二分。

二分答案通常用于最优化问题的求解

- 尤其是在出现“最大值最小” “最小值最大”这类字眼的题目上
- “最大值最小”中的“最小”是一个最优化目标，“最大”一般是一个限制条件（例如：限制划分出的子数组的和）

对应的判定问题的条件通常是一个不等式

- 不等式就反映了上述限制条件

关于这个条件的合法情况具有特殊单调性
此时就可以用二分答案把求解转化为判定的技巧
二分答案的本质是建立一个单调分段 0/1 函数，定义域为解空间（答案），值域为 0 或 1，在这个函数上二分查找分界点。

1482. Minimum Number of Days to Make m Bouquets

You are given an integer array bloomDay, an integer m and an integer k.

You want to make m bouquets. To make a bouquet, you need to use k adjacent flowers from the garden.

The garden consists of n flowers, the ith flower will bloom in the bloomDay[i] and then can be used in exactly one bouquet.

Return the minimum number of days you need to wait to be able to make m bouquets from the garden. If it is impossible to make m bouquets return -1.

Example 1:

```
Input: bloomDay = [1,10,3,10,2], m = 3, k = 1
Output: 3
Explanation: Let us see what happened in the first three days. x means flower bloomed and _ means flower did not bloom in the garden.
We need 3 bouquets each should contain 1 flower.
After day 1: [x, _, _, _, _]   // we can only make one bouquet.
After day 2: [x, _, _, _, x]   // we can only make two bouquets.
After day 3: [x, _, x, _, x]   // we can make 3 bouquets. The answer is 3.
```

Example 2:

```
Input: bloomDay = [1,10,3,10,2], m = 3, k = 2
Output: -1
Explanation: We need 3 bouquets each has 2 flowers, that means we need 6 flowers. We only have 5 flowers so it is impossible to get the needed bouquets and we return -1.
```

Example 3:

```
Input: bloomDay = [7,7,7,7,12,7,7], m = 2, k = 3
Output: 12
Explanation: We need 2 bouquets each should have 3 flowers.
Here is the garden after the 7 and 12 days:
After day 7: [x, x, x, x, _, x, x]
We can make one bouquet of the first three flowers that bloomed. We cannot make another bouquet from the last three flowers that bloomed because they are not adjacent.
After day 12: [x, x, x, x, x, x, x]
It is obvious that we can make two bouquets in different ways.
```

Constraints:

- bloomDay.length == n
- 1 <= n <= 105
- 1 <= bloomDay[i] <= 109
- 1 <= m <= 106
- 1 <= k <= n

```java
class Solution {
    public int minDays(int[] bloomDay, int m, int k) {
        // 最晚开花的时间
        int latestBloom = 0;
        for(int bloom: bloomDay){
            latestBloom = Math.max(latestBloom,bloom);
        }
        // 有无解，后继型找到尾巴还不行就是无解，一直走得是else那个分支。
        int left = 0, right = latestBloom + 1;
        while(left<right){
            int mid = (left+right)/2;
            if(vaildateOnDay(bloomDay,m,k,mid)){
                right = mid;//找大的里面最小的
            }else {
                left = mid + 1;
            }
        }
        // 有无解，后继型找到尾巴还不行就是无解
        if(right == latestBloom+1) return -1;
        return right;

    }

    boolean vaildateOnDay(int[] bloomDay,int m,int k,int now){
        // 做的花束数
        int bouquet = 0;
        // 连续的花数
        int consecutive = 0;
        for(int bloom: bloomDay){
            // 已经盛开
            if(bloom <= now){
                consecutive++;
                // 刚好等于需要的花朵数
                if(consecutive == k){
                    // 做一束花
                    bouquet++;
                    // 连续的花清零
                    consecutive = 0;
                }
            }else{ //花没有盛开
                // 连续的花清零
                consecutive = 0;
            }
        }
        // 返回是否大于需要制作的花束数
        return bouquet >= m;
    }
}
```
