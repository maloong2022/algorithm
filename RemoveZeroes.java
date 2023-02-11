import java.util.Arrays;

/**
 * 283. Move Zeroes
 * Given an integer array nums, move all 0's to the end of it while 
 * maintaining the relative order of the non-zero elements.
 * Note that you must do this in-place without making a copy of the array.
*/
public class RemoveZeroes {
  public static void removeZeroes(int[] nums) {
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

  public static void main(String[] args){
    int[] nums =new int[] {0,1,0,3,12};
    removeZeroes(nums);
    System.out.println(Arrays.toString(nums));
  }
}
