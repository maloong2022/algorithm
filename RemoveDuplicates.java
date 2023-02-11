import java.util.Arrays;

/**
 * 26. Remove Duplicates from Sorted Array
 * Given an integer array nums sorted in non-decreasing order, 
 * remove the duplicates in-place such that each unique element appears only once. 
 * The relative order of the elements should be kept the same.
 * Since it is impossible to change the length of the array in some languages, 
 * you must instead have the result be placed in the first part of the array nums.
 * More formally, if there are k elements after removing the duplicates, 
 * then the first k elements of nums should hold the final result. 
 * It does not matter what you leave beyond the first k elements.
 * Return k after placing the final result in the first k slots of nums.
 * Do not allocate extra space for another array. You must do this by modifying 
 * the input array in-place with O(1) extra memory.
*/
 
public class RemoveDuplicates {
  public static int removeDuplicates(int[] nums){
    int n = 0;
    for (int i=0; i<nums.length; i++) {
      if (i==0 ||  (nums[i] != nums[i-1])) {
        nums[n] = nums[i];
        n++;
      }
    }
    return n;
  }

  /**
   * @param arr: The array waiting to convert
   * @param len: The length of the deal
   * @return a int array to a string which contains all the element of the array
   **/
  public static String arraysToString(int[] arr,int len) {
    if (arr == null){
      return null;
    }
    int Max = arr.length - 1;
    if (Max == -1) {
      return "[]";
    }
    if (len > 1 && len-1 < Max) {
      Max = len-1;
    }
    StringBuilder str = new StringBuilder();
    str.append("[");
    for (int i=0;;i++){
      str.append(arr[i]);
      if (i == Max) {
        return str.append("]").toString();
      }
      str.append(",");
    }
  }

  public static void main(String[] args){
    int[] nums = {0,0,1,1,1,2,2,3,3,4};
    int len = removeDuplicates(nums);
    System.out.println(len);
    System.out.println(arraysToString(nums,len));
  }
}
