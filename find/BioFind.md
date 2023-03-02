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
