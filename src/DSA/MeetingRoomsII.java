package DSA;

import java.util.Arrays;
import java.util.PriorityQueue;

public class MeetingRoomsII {
    public int minMeetingRooms(int[][] intervals) {
        int[] starts = new int[intervals.length];
        int[] ends = new int[intervals.length];

        for(int i=0; i<intervals.length; i++) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }
        Arrays.sort(starts);
        Arrays.sort(ends);
        int rooms = 0;
        int endsItr = 0;
        for(int i=0; i<starts.length; i++) {
            if(starts[i]<ends[endsItr])
                rooms++;
            else
                endsItr++;
        }
        return rooms;
    }

    public int minMeetingRooms2(int[][] intervals) {
        int[] starts = new int[intervals.length];
        int[] ends = new int[intervals.length];

        for(int i=0; i<intervals.length; i++) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }
        Arrays.sort(starts);
        Arrays.sort(ends);

        int i=0, j=0;
        int cores=0, maxCores=0;

        while (i < starts.length) {
            if (starts[i] < ends[j]) {
                cores += 1;
                maxCores = Math.max(maxCores, cores);
                i += 1;
            }
            else {
                cores -= 1;
                j += 1;
            }
        }

        return maxCores;
    }

    public int minMeetingRoomsPq(int[][] intervals) {
        Arrays.sort(intervals, (i1, i2) -> i1[0] - i2[0]);

        // we need the SOONEST available room
        // at each iteration we look at the rooms and if the new meeting is starting
        // afer any meeting that ended, that means that rooms is free and we remove it from the PQ
        PriorityQueue<Integer> meetingRooms = new PriorityQueue<>();
        for (int i=0; i<intervals.length; i++) {
            if (!meetingRooms.isEmpty() && meetingRooms.peek() <= intervals[i][0]) {
                meetingRooms.poll();
            }
            meetingRooms.offer(intervals[i][1]);
        }

        return meetingRooms.size();
    }
}
