package turing;

/**
 * Distribute Energy Packets - Similar to LeetCode 2226
 *
 * You are given a list packets, where packets[i] represents the size of the i_th energy packet.
 * Each packet can be split into any number of smaller sub-packets, but you cannot merge packets together.
 * You are also given integer agents, representing the number of agents who must each receive one packet of the same size.
 *
 * Each agent must receive their allocation from one sub-packet only, and some packets may go unused.
 *
 * Return the maximum number of units each agent can receive equally.
 *
 * Example 1:
 *
 * Input:
 * packets = [7,10,4], agents = 4
 *
 * Output:
 * 4
 *
 * Explanation:
 *
 * Divide 7 → 4 + 3
 * Divide 10 → 2 + 4 + 4
 *
 * Now have 4 packets of size 4
 * Total: [4, 3, 2, 4, 4, 4]
 *
 * Assign 4, 4, 4, 4 → to 4 agents
 * Maximum equal allocation = 4
 *
 * “What is the largest equal chunk we can carve out at least agents times?”
 */
public class DistributeEnergyPackets {

    public int maximumCandies(int[] candies, long k) {
        int maxPile=0;
        int s=0;

        for (int i=0; i<candies.length; i++) {
            maxPile = Math.max(maxPile, candies[i]);
        }

        for (int i=maxPile; i>0; i--) {
            s = 0;
            for (int j=0; j<candies.length; j++) {
                s += (candies[j] / i);
            }
            if (s >= k) {
                return i;
            }
        }

        return 0;
    }

    public int maximumCandiesBinarySearch(int[] candies, long k) {
        int maxCandiesInPile = 0;

        for (int pileIndex=0; pileIndex<candies.length; pileIndex++) {
            maxCandiesInPile = Math.max(maxCandiesInPile, candies[pileIndex]);
        }

        int left=0;
        int right=maxCandiesInPile;

        while (left < right) {
            int middle = (left + right + 1) / 2;

            if (canAllocateCandies(candies, k, middle)) {
                left = middle;
            } else {
                right = middle-1;
            }
        }

        return left;
    }

    public boolean canAllocateCandies(int[] candies, long k, int numOfCandies) {
        long maxNumOfChildren = 0;

        for (int pileIndex=0; pileIndex<candies.length; pileIndex++) {
            maxNumOfChildren += candies[pileIndex] / numOfCandies;
        }

        if (maxNumOfChildren >= k) {
            return true;
        }

        return false;
    }
}
