package bloomberg;

import java.util.*;

/**
 * Summary
 * Given one directional airport routes, devise a solution that can provide the path from Airport A to Airport B.
 *
 * method 1, keep track of airports and which ones are connected
 *
 * method 2, print routes between airport a and airport b
 *
 * JFK - SFO
 * JFK - MIAMI
 * MIA - SFO
 *
 * JFK - SFO
 * JFK - MIA - SFO
 *
 * JFK - MIA
 * MIA - JFK
 */
public class BuySideRound2Exo2 {
    // JFK -> SFO, MIAMI
    // MIA -> SFO
    Map<String, List<String>> airports = new HashMap<>();
    Set<String> visited = new HashSet<>();
    List<String> path = new ArrayList<>();
    int depth = 0;

    public void addRoute(String departure, String arrival) {
        airports.computeIfAbsent(departure, k -> new ArrayList<>()).add(arrival);
        airports.computeIfAbsent(arrival, k -> new ArrayList<>()).add(departure);
    }

    /**
     * Time Complexity = O(P × L)
     * P = number of valid routes (paths) between the two airports
     * L = average number of airports in a route (≤ V)
     *
     * Worst case : O(V!) (factorial growth)
     * V = number of airports
     *
     * Space Complexity = O(V)
     * visited → airports already visited → O(V)
     * path → current route → O(V)
     * recursion stack → depth of DFS → O(V)
     *
     * If routes are stored instead of printed, then:
     * Space = O(P × V)
     * which can become O(V! × V)
     */
    private void showRoutes(String departure, String arrival) {
        String indent = "  ".repeat(depth);

        // show traversal
        System.out.println(indent + "Visiting: " + departure);

        visited.add(departure);
        path.add(departure);

        // reached arrival
        if (departure.equals(arrival)) {
            // show full valid path
            System.out.println(indent + "Path found: " + String.join(" -> ", path));
        } else {
            for (String airport : airports.get(departure)) {
                if (!visited.contains(airport)) {
                    depth++;
                    showRoutes(airport, arrival);
                }
            }
        }

        // backtracking
        path.remove(path.size() - 1);
        visited.remove(departure);

        System.out.println(indent + "Backtracking from: " + departure);
    }

    /**
     * JFK - SFO
     * JFK - MIA
     * MIA - SFO
     *
     * JFK - SFO
     * JFK - MIA - SFO
     *
     * JFK - MIA
     * MIA - JFK
     */
    public static void main(String[] args) {
        BuySideRound2Exo2 init = new BuySideRound2Exo2();
        init.addRoute("JFK", "SFO");
        init.addRoute("JFK", "MIA");
        init.addRoute("MIA", "SFO");

        init.showRoutes("JFK", "SFO");
    }
}
