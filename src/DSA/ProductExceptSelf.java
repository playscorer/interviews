package DSA;

import java.sql.SQLOutput;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ProductExceptSelf {
    public static int[] productExceptSelf(int[] nums) {
        int[] answer = new int[nums.length];
        int prdTotal=1;
        int prdNoZero=1;
        int oneZero=0;

        for (int num : nums) {
            if (num == 0) {
                oneZero++;
            } else {
                prdNoZero *= num;
            }
            prdTotal *= num;
        }

        for (int i=0; i<nums.length; i++) {
            if (oneZero==1 && nums[i]==0) {
                answer[i] = prdNoZero;
            } else if (nums[i]!=0) {
                answer[i] = prdTotal / nums[i];
            }
        }

        //int prd = IntStream.of(nums).reduce(1, (a, b) -> a * b);
        //IntStream.range(0, nums.length).forEach(i -> answer[i] = nums[i]==0? 0 : prd/nums[i]);

        return answer;
    }

    public static void main(String[] args) {
        int[] answer = ProductExceptSelf.productExceptSelf(new int[]{1,2,3,4});
        IntStream.of(answer).forEach(i -> System.out.print(i + " "));
    }
}
