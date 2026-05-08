package DSA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestoreArrayAdjPairs {

    public int[] restoreArray(int[][] adjacentPairs) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        int[] result = new int[adjacentPairs.length+1];

        for (int i=0; i<adjacentPairs.length; i++) {
            map.computeIfAbsent(adjacentPairs[i][0], k -> new ArrayList<>()).add(adjacentPairs[i][1]);
            map.computeIfAbsent(adjacentPairs[i][1], k -> new ArrayList<>()).add(adjacentPairs[i][0]);
        }

        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            if (entry.getValue().size() == 1) {
                result[0] = entry.getKey();
                result[1] = entry.getValue().get(0);
                break;
            }
        }

        for (int i=2; i<result.length; i++) {
            if (map.get(result[i-1]).get(0) == result[i-2]) {
                result[i] = map.get(result[i-1]).get(1);
            } else {
                result[i] = map.get(result[i-1]).get(0);
            }
        }

        return result;
    }
}
