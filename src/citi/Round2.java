package citi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
/*
We are building a program to manage a gym's membership. The gym has multiple members, each with a unique ID, name, and membership status. The program allows gym staff to add new members, update members status, and get membership statistics.

Definitions:
* A "member" is an object that represents a gym member. It has properties for the ID, name, and membership status.
* A "membership" is a class which is used for managing members in the gym.

*/

/*
We are currently updating our system to include information about workouts for our members. As part of this update, we have introduced the Workout class, which represents a single workout session for a member. Each object of the Workout class has a unique ID, as well as a start time and end time that are represented in the number of minutes spent from the start of the day. You can assume that all the Workouts are from the same day.

To implement these changes, we need to add two functions to the Membership class:

2.1) The `addWorkout` function takes a member ID and a `Workout` as inputs and associates the workout with that member. If the workout is associated successfully, the function should return `true`. If the given member does not exist while calling this function, the workout should be ignored and the function should return `false`.

2.2) The `getAverageWorkoutDurations` function should generate a map of member IDs onto their average workout durations in minutes. The returned map should include all members. If a member has no workouts, their value should be `null`.

To assist you in testing these new functions, we have provided the testAddWorkout and testGetAverageWorkoutDurations functions.
*/

enum MembershipStatus {
    /*
      Membership Status is of three types: BRONZE, SILVER and GOLD.
      BRONZE is the default membership a new member gets.
      SILVER and GOLD are paid memberships for the gym.
    */
    BRONZE,
    SILVER,
    GOLD
}

class Workout {
    /**
     * This class represents a single workout session for a member.
     * Each object of the Workout class has a unique ID, as well as
     * a start time and end time that are represented in the number
     * of minutes spent from the start of the day.
     */

    private int id;
    private int startTime;
    private int endTime;

    public Workout(int id, int startTime, int endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getDuration() {
        return endTime - startTime;
    }
}

class Member {
    /* Data about a gym member.*/
    public int memberId;
    public String name;
    public MembershipStatus membershipStatus;

    public Member(int memberId, String name, MembershipStatus membershipStatus) {
        this.memberId = memberId;
        this.name = name;
        this.membershipStatus = membershipStatus;
    }

    @Override
    public String toString() {
        return "Member ID: " + memberId + ", Name: " + name + ", Membership Status: " + membershipStatus;
    }
}

class Membership {
    /*
      Data for managing a gym membership, and methods which staff can
      use to perform any queries or updates.
    */
    public List<Member> members;

    public Map<Integer, List<Workout>> workoutsMap;

    public Membership() {
        members = new ArrayList<>();
        workoutsMap = new HashMap<>();
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public boolean addWorkout(int memberId, Workout workout) {
        boolean found = false;
        for (Member member : members) {
            if (memberId == member.memberId) {
                found = true;
                break;
            }
        }
        if (found) {
            workoutsMap.computeIfAbsent(memberId, k -> new ArrayList<>()).add(workout);
        }
        return found;
    }

    public Map<Integer, Double> getAverageWorkoutDurations() {
        Map<Integer, Double> returnMap = new HashMap<>();

        // I did not add this during the interview to pass the only failing test
        // "average for a member with no workouts should be null"
        for (Member member : members) {
            returnMap.putIfAbsent(member.memberId, null);
        }

        for (Entry<Integer, List<Workout>> entry : workoutsMap.entrySet()) {
            int durationSum = 0;
            double avgDuration = 0;
            int memberId = entry.getKey();
            List<Workout> workouts = entry.getValue();

            for (Workout workout : workouts) {
                durationSum += workout.getEndTime() - workout.getStartTime();
            }
            avgDuration = (double) durationSum / workouts.size();

            returnMap.put(memberId, avgDuration);
        }

        return returnMap;
    }

    public void updateMembership(int memberId, MembershipStatus membershipStatus) {
        for (Member member : members) {
            if (member.memberId == memberId) {
                member.membershipStatus = membershipStatus;
                break;
            }
        }
    }

    public MembershipStatistics getMembershipStatistics() {
        int totalMembers = members.size();
        int totalPaidMembers = 0;
        for (Member member : members) {
            if (member.membershipStatus == MembershipStatus.GOLD
                    || member.membershipStatus == MembershipStatus.SILVER) {
                totalPaidMembers++;
            }
        }
        double conversionRate = (totalPaidMembers / (double) totalMembers) * 100.0;
        return new MembershipStatistics(totalMembers, totalPaidMembers, conversionRate);
    }
}

class MembershipStatistics {
    /*
      Class for returning the getMembershipStatistics result
    */
    public int totalMembers;
    public int totalPaidMembers;
    public double conversionRate;

    public MembershipStatistics(int totalMembers, int totalPaidMembers, double conversionRate) {
        this.totalMembers = totalMembers;
        this.totalPaidMembers = totalPaidMembers;
        this.conversionRate = conversionRate;
    }
}

public class Round2 {
    /*
      This is not a complete test suite, but tests some basic functionality of
      the code and shows how to use it.
    */
    public static void main(String[] args) {
        testMember();
        testMembership();
        testAddWorkout();
        testGetAverageWorkoutDurations();

        System.out.println("All test cases pass!");
    }

    public static void testMember() {
        System.out.println("Running testMember");
        Member testMember = new Member(1, "John Doe", MembershipStatus.BRONZE);
        assert testMember.memberId == 1 :
                "Member ID should be 1, was " + testMember.memberId;
        assert testMember.name.equals("John Doe") :
                "Member name should be \"John Doe\", was \"" + testMember.name + "\"";
        assert testMember.membershipStatus == MembershipStatus.BRONZE :
                "Membership status should be BRONZE, was " + testMember.membershipStatus;
    }

    public static void testMembership() {
        System.out.println("Running testMembership");
        Membership testMembership = new Membership();
        Member testMember = new Member(1, "John Doe", MembershipStatus.BRONZE);
        testMembership.addMember(testMember);
        assert testMembership.members.size() == 1 :
                "Members size should be 1, was " + testMembership.members.size();
        assert testMembership.members.get(0).equals(testMember) :
                "First member should equal testMember";

        testMembership.updateMembership(1, MembershipStatus.SILVER);
        assert testMembership.members.get(0).membershipStatus == MembershipStatus.SILVER :
                "Membership status should be SILVER, was " + testMembership.members.get(0).membershipStatus;

        Member testMember2 = new Member(2, "Alex C", MembershipStatus.BRONZE);
        testMembership.addMember(testMember2);

        Member testMember3 = new Member(3, "Marie C", MembershipStatus.GOLD);
        testMembership.addMember(testMember3);

        Member testMember4 = new Member(4, "Joe D", MembershipStatus.SILVER);
        testMembership.addMember(testMember4);

        Member testMember5 = new Member(5, "June R", MembershipStatus.BRONZE);
        testMembership.addMember(testMember5);

        Member testMember6 = new Member(6, "Westley D", MembershipStatus.SILVER);
        testMembership.addMember(testMember6);

        MembershipStatistics attendanceStats = testMembership.getMembershipStatistics();
        assert attendanceStats.totalMembers == 6 :
                "Total members should be 6, was " + attendanceStats.totalMembers;
        assert attendanceStats.totalPaidMembers == 4 :
                "Total paid members should be 4, was " + attendanceStats.totalPaidMembers;
        assert Math.abs(attendanceStats.conversionRate - 66.67) < 0.1 :
                "Conversion rate should be 66.67, was " + attendanceStats.conversionRate;
    }

    public static void testAddWorkout() {
        System.out.println("Running testAddWorkout");
        Membership testMembership = new Membership();
        Member testMember1 = new Member(12, "John Doe", MembershipStatus.SILVER);
        testMembership.addMember(testMember1);

        Member testMember2 = new Member(22, "Alex Cleeve", MembershipStatus.BRONZE);
        testMembership.addMember(testMember2);

        Workout testWorkout1 = new Workout(111, 10, 20);
        Workout testWorkout2 = new Workout(112, 15, 35);
        Workout testWorkout3 = new Workout(113, 20, 25);
        Workout testWorkout99 = new Workout(999, 1, 2);

        assertTrue(testMembership.addWorkout(12, testWorkout1));
        assertTrue(testMembership.addWorkout(22, testWorkout2));
        assertTrue(testMembership.addWorkout(12, testWorkout3));
        assertFalse(testMembership.addWorkout(404, testWorkout99));
    }

    public static void testGetAverageWorkoutDurations() {
        System.out.println("Running testGetAverageWorkoutDurations");
        Membership testMembership = new Membership();
        Member testMember1 = new Member(12, "John Doe", MembershipStatus.SILVER);
        testMembership.addMember(testMember1);

        Member testMember2 = new Member(22, "Alex Cleeve", MembershipStatus.BRONZE);
        testMembership.addMember(testMember2);

        Member testMember3 = new Member(31, "Marie Cardiff", MembershipStatus.GOLD);
        testMembership.addMember(testMember3);

        Member testMember4 = new Member(37, "George Costanza", MembershipStatus.SILVER);
        testMembership.addMember(testMember4);

        Workout testWorkout1 = new Workout(101, 10, 20);
        Workout testWorkout2 = new Workout(102, 15, 35);
        Workout testWorkout3 = new Workout(103, 45, 90);
        Workout testWorkout4 = new Workout(104, 100, 155);
        Workout testWorkout5 = new Workout(105, 120, 200);
        Workout testWorkout6 = new Workout(106, 300, 400);
        Workout testWorkout7 = new Workout(107, 1000, 1010);
        Workout testWorkout8 = new Workout(108, 1010, 1045);

        testMembership.addWorkout(12, testWorkout1);
        testMembership.addWorkout(22, testWorkout2);
        testMembership.addWorkout(31, testWorkout3);
        testMembership.addWorkout(12, testWorkout4);
        testMembership.addWorkout(22, testWorkout5);
        testMembership.addWorkout(31, testWorkout6);
        testMembership.addWorkout(12, testWorkout7);
        testMembership.addWorkout(404, testWorkout8);

        Map<Integer, Double> averageDurations = testMembership.getAverageWorkoutDurations();
        assert Math.abs(averageDurations.get(12) - 25.0) < 0.1 :
                "average duration for member 12 should be 25.0, was " + averageDurations.get(12);
        assert Math.abs(averageDurations.get(22) - 50.0) < 0.1 :
                "average duration for member 22 should be 50.0, was " + averageDurations.get(22);
        assert Math.abs(averageDurations.get(31) - 72.5) < 0.1 :
                "average duration for member 31 should be 72.5, was " + averageDurations.get(31);
        assert averageDurations.containsKey(37) && averageDurations.get(37) == null : "average for a member with no workouts should be null";
        assert !averageDurations.containsKey(404) : "workout for non-existent member should not appear in averages";
    }
}