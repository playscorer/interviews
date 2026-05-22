package gs.prep.nyc2026;

public class FindCycleArray {

    public static int solution(int[] arr, int startIndex) {
        int slow = startIndex, fast = startIndex;

        if (startIndex >= arr.length) return -1;

        // phase 1 : find intersection to detect cycle
        do {
            slow = arr[slow];
            if (slow >= arr.length) return -1;
            if (fast >= arr.length || arr[fast] >= arr.length) return -1;
            fast = arr[arr[fast]];
        } while (slow != fast);

        // phase 2 : move on pointer from intersection until intersection
        int cnt=0;
        do {
            slow = arr[slow];
            cnt++;
        } while (slow != fast);

        return cnt;
    }
}
