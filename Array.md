### 性质

基本特点：支持随机访问
关键：索引与寻址
内存形式：一段连续的存储空间
查询快插入删除慢，需要移动元素(除非在末尾删除和插入)。

### 时间复杂度

| 操作                | 时间复杂度 | 备注         |
| ------------------- | ---------- | ------------ |
| Lookup              | O(1)       |              |
| Insert              | O(n)       | 元素需要移动 |
| Delete              | O(n)       | 元素需要移动 |
| Append(push back)   | O(1)       |              |
| Prepend(push front) | O(n)       |              |

### 实战

26. Remove Duplicates from Sorted Array

Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place such that each unique element appears only once. The relative order of the elements should be kept the same.

Since it is impossible to change the length of the array in some languages, you must instead have the result be placed in the first part of the array nums. More formally, if there are k elements after removing the duplicates, then the first k elements of nums should hold the final result. It does not matter what you leave beyond the first k elements.

Return k after placing the final result in the first k slots of nums.

Do not allocate extra space for another array. You must do this by modifying the input array in-place with O(1) extra memory.

Custom Judge:

The judge will test your solution with the following code:

```
int[] nums = [...]; // Input array
int[] expectedNums = [...]; // The expected answer with correct length

int k = removeDuplicates(nums); // Calls your implementation

assert k == expectedNums.length;
for (int i = 0; i < k; i++) {
    assert nums[i] == expectedNums[i];
}
```

If all assertions pass, then your solution will be accepted.

```java
class Solution {
    public int removeDuplicates(int[] nums) {
      int n = 0;
      for (int i=0; i<nums.length; i++) {
        if (i==0 ||  (nums[i] != nums[i-1])) {
          nums[n] = nums[i];
          n++;
        }
      }
      return n;
    }
}
```

283. Move Zeroes

Given an integer array nums, move all 0's to the end of it while maintaining the relative order of the non-zero elements.

Note that you must do this in-place without making a copy of the array.

Example 1:

```
Input: nums = [0,1,0,3,12]
Output: [1,3,12,0,0]
```

Example 2:

```
Input: nums = [0]
Output: [0]
```

Constraints:

1 <= nums.length <= 104

-231 <= nums[i] <= 231 - 1

Follow up: Could you minimize the total number of operations done?

```java
class Solution {
  public void moveZeroes(int[] nums) {
    int n = 0;
    for(int i=0;i<nums.length;i++){
      if(nums[i] != 0){
        nums[n] = nums[i];
        n++;
      }
    }
    while(n<nums.length){
      nums[n]=0;
      n++;
    }
  }
}
```

88. Merge Sorted array

You are given two integer arrays nums1 and nums2, sorted in non-decreasing order, and two integers m and n, representing the number of elements in nums1 and nums2 respectively.

Merge nums1 and nums2 into a single array sorted in non-decreasing order.

The final sorted array should not be returned by the function, but instead be stored inside the array nums1. To accommodate this, nums1 has a length of m + n, where the first m elements denote the elements that should be merged, and the last n elements are set to 0 and should be ignored. nums2 has a length of n.

Example 1:

```
Input: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
Output: [1,2,2,3,5,6]
Explanation: The arrays we are merging are [1,2,3] and [2,5,6].
The result of the merge is [1,2,2,3,5,6] with the underlined elements coming from nums1.
```

Example 2:

```
Input: nums1 = [1], m = 1, nums2 = [], n = 0
Output: [1]
Explanation: The arrays we are merging are [1] and [].
The result of the merge is [1].
```

Example 3:

```
Input: nums1 = [0], m = 0, nums2 = [1], n = 1
Output: [1]
Explanation: The arrays we are merging are [] and [1].
The result of the merge is [1].
Note that because m = 0, there are no elements in nums1. The 0 is only there to ensure the merge result can fit in nums1.
```

Constraints:

nums1.length == m + n

nums2.length == n

0 <= m, n <= 200

1 <= m + n <= 200

-109 <= nums1[i], nums2[j] <= 109

Follow up: Can you come up with an algorithm that runs in O(m + n) time?

```java
class Solution {
  public void merge(int[] nums1, int m, int[] nums2, int n) {
    int i = m-1,j = n-1;
    for (int k = (m+n)-1;k>=0;k--){
      if(j<0 || i>=0 && nums1[i] > nums2[j]){
        nums1[k] = nums1[i];
        i--;
       }else {
        nums1[k] = nums2[j];
        j--;
      }
    }
  }
}
```

### Fliter modle

apply for: The relative order of the elements should be kept the same.

```java
int n;
for(int i=0; i < arr.size(); i++){
    if (filter) {
        arr[n] = arr[i];
        n++;
    }
}
return n;
```

### Merge modle

```java
for (int k = (m+n)-1;k>=0;k--){
  // base on one array
  if(j<0 || i>=0 && nums1[i] > nums2[j]){
    nums1[k] = nums1[i];
    i--;
  }else{
    nums1[k] = nums2[j];
    j--;
  }
}
```

### resizable array

如何实现一个变长数组

- 支持索引和随机访问(连续内存空间)
- 分配多长的连续空间？
- 空间不够用了怎么办？
- 空间剩余很多如何回收？

变长数组的关键点

- 基础数组的特性：支持索引与随机访问，连续存储空间
- 空间的弹性管理

实现要点

- 维护实际长度 size 和容量 capacity 来管理
- 插入元素的时候，如果当前空间不够，重新申请 2 倍连续空间，拷贝到新空间
- 删除元素的时候，如果当前 size/capacity 比例不到 25%，释放空间
