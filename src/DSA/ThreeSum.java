package DSA;

import java.util.*;

public class ThreeSum {
    public static List<List<Integer>> hashmapSolution(int[] nums) {
        Map<Integer, Integer> orig = new HashMap<>();
        for (int i=0; i<nums.length; i++) {
            orig.put(nums[i],i);
        }

        Set<List<Integer>> list = new HashSet<>();
        for (int i=0; i<nums.length; i++) {
            for (int j=i+1; j<nums.length; j++) {
                int compl = -nums[i] - nums[j];
                if (orig.containsKey(compl) && orig.get(compl) != i && orig.get(compl) != j) {
                    List<Integer> triple = new ArrayList<>(Arrays.asList(
                            new Integer[] {nums[i], nums[orig.get(compl)], nums[j]}));
                    Collections.sort(triple);
                    list.add(triple);
                }
            }
        }

        return new ArrayList<>(list);
    }

    public static List<List<Integer>> twoSumIISolution(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> list = new ArrayList<>();

        for (int i=0; i<nums.length; i++) {
            while (i>0 && i<nums.length && nums[i] == nums[i-1]) {
                i++;
            }
            int j=i+1;
            int k=nums.length-1;
            System.out.println(i + " " + j + " " + k);
            while (j<k) {
                if (nums[i] + nums[j] + nums[k] == 0) {
                    list.add(new ArrayList<>(Arrays.asList(new Integer[]{nums[i], nums[j], nums[k]})));
                    j++;
                    while (j<k && nums[j] == nums[j-1]) {
                        j++;
                    }
                } else {
                    if (nums[i] + nums[j] + nums[k] > 0) {
                        k--;
                    } else if (nums[i] + nums[j] + nums[k] < 0) {
                        j++;
                    }
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        List<List<Integer>> list = ThreeSum.twoSumIISolution(new int[] {-1,0,1,2,-1,-4});
        System.out.println("res:= " + list);
    }
}
