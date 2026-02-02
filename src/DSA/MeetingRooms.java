package DSA;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Floripa on Jan 27th - Implemented an elegant solution in a glance
 * after a break of week, and having no idea how to solve it before!
 */
public class MeetingRooms {
    public boolean canAttendMeetings(int[][] intervals) {
        List<int[]> list = Arrays.asList(intervals);
        List<Map.Entry<Integer, Integer>> newList
                = list.stream().map(arr -> Map.entry(arr[0], arr[1])).sorted(Comparator.comparing(Map.Entry::getKey)).collect(Collectors.toList());

        for (int i=0; i<newList.size()-1; i++) {
            if (newList.get(i).getValue() > newList.get(i+1).getKey())
                return false;
        }

        return true;
    }
}
