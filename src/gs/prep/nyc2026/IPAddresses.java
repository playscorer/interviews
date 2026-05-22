package gs.prep.nyc2026;

import com.sun.source.tree.Tree;

import java.util.*;
import java.util.stream.Collectors;

public class IPAddresses {

    /**
     * Given a list of logs with IP addresses in the following format:
     *
     * lines = ["10.0.0.1 - GET 2020-08-24", "10.0.0.1 - GET 2020-08-24", "10.0.0.2 - GET 2020-08-20"]
     *
     * Return the most frequent IP address from the logs.
     * The retuned IP address value must be in a string format. If multiple IP addresses have the count equal to max count,
     * then return the address as a comma-separated string with IP addresses in sorted order.
     */
    public static String mostFrequentIPs(List<String> lines) {

        TreeMap<String, Long> mapFrequencies = lines.stream()
                .map(line -> line.split(" - ")[0])
                .collect(Collectors.groupingBy(ip -> ip, TreeMap::new, Collectors.counting()));
        /*
        for (String line : lines) {
            String ip = line.split(" - ")[0];
            mapFrequencies.put(ip, mapFrequencies.getOrDefault(ip, 0) + 1);
        }
         */

        long maxFreq = Collections.max(mapFrequencies.values());
        /*
        int maxFreq = mapFrequencies.entrySet().stream()
                .max(Comparator.comparingInt(entry -> entry.getValue()))
                .map(entry -> entry.getValue())
                .orElse(0);
         */

        return mapFrequencies.entrySet().stream()
                .filter(entry -> entry.getValue().equals(maxFreq))
                .map(entry -> entry.getKey())
                .collect(Collectors.joining(","));
    }

}
