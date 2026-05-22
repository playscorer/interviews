package gs.nyc2026;

import java.util.Map;

/**
 *  Jeffersonton, VA - Round 2 of Super Day - May 19th, 2026
 */
public class Round1 {
    /**
     * Design an algorithm for an Elevator system, with n number of elevators, and calls the closest Elevator.
     * The idea is to find the :
     *      min(dist(Elevator.currentFloor-Person.currentFloor | having Elevator.direction == Person.direction),
     *          dist(Elevator.destFloor-Elevator.currentFloor + Elevator.destFloor-Person.currentFloor
     *                                                         | having Elevator.direction != Person.direction))
     *
     * Think of Person being on 10th floor and willing to go up, and 9 elevators are on the 100th,
     * and 1 on the 11th going down to 1st.
     */
    private static class Elevator {
        int direction;
        int currentFloor;
        int destFloor;
    }
    private static class Person {
        int direction;
        int currentFloor;
    }
    private static class Building {
        Map<Integer, Elevator> buildingMap;
    }

    /**
     * LeetCode 239 - Sliding Window Maximum
     *
     * You are given an array of integers nums, there is a sliding window of size k
     * which is moving from the very left of the array to the very right.
     * You can only see the k numbers in the window. Each time the sliding window moves right by one position.
     *
     * The interviewer : prices is an array of end day prices and we want to return each max in a window of 3 prices.
     * The window maintains its size and slides by one.
     *
     * Ex : [103 101 102 105 104 106] -> [103, 105, 105, 106]
     *
     * Return the max sliding window.
     */
    public int[] maxSlidingWindowBruteForce(int[] nums, int k) { // O(n*k)
        if (nums.length < k) {
            return new int[0];
        }
        int[] res = new int[nums.length-k+1];

        for (int i=0; i+k-1<nums.length; i++) {
            int max=Integer.MIN_VALUE;
            int right=i;
            while (right < i+k) {
                if (max < nums[right]) {
                    max = nums[right];
                    res[i] = max;
                }
                right++;
            }
        }

        return res;
    }
    
}
