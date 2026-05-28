package headlands;

import java.util.*;
import java.io.*;

public class ClaudeSolution {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String header = br.readLine();
            if (header == null || !header.trim().equals("#job_id,runtime_in_seconds,next_job_id")) {
                System.out.println("Malformed Input");
                return;
            }

            Map<Integer, int[]> jobs = new LinkedHashMap<>(); // id -> [runtime, nextId]
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    System.out.println("Malformed Input");
                    return;
                }
                int id = Integer.parseInt(parts[0].trim());
                int runtime = Integer.parseInt(parts[1].trim());
                int nextId = Integer.parseInt(parts[2].trim());
                if (jobs.containsKey(id)) {
                    System.out.println("Malformed Input");
                    return;
                }
                jobs.put(id, new int[]{runtime, nextId});
            }

            // Find chain start nodes: ids that are never a next_job_id
            Set<Integer> allIds = jobs.keySet();
            Set<Integer> notStarts = new HashSet<>();
            for (int[] v : jobs.values()) {
                if (v[1] != 0) notStarts.add(v[1]);
            }

            List<int[]> chains = new ArrayList<>(); // [startId, lastId, count, totalRuntime]

            Set<Integer> visited = new HashSet<>();
            for (int id : allIds) {
                if (notStarts.contains(id)) continue;
                // Walk chain
                int startId = id;
                int lastId = id;
                int count = 0;
                int totalRuntime = 0;
                int cur = id;
                while (cur != 0) {
                    if (!jobs.containsKey(cur) || visited.contains(cur)) {
                        System.out.println("Malformed Input");
                        return;
                    }
                    visited.add(cur);
                    int[] job = jobs.get(cur);
                    totalRuntime += job[0];
                    count++;
                    lastId = cur;
                    cur = job[1];
                }
                chains.add(new int[]{startId, lastId, count, totalRuntime});
            }

            // Sort descending by total runtime
            chains.sort((a, b) -> b[3] - a[3]);

            StringBuilder sb = new StringBuilder();
            for (int[] chain : chains) {
                sb.append("-\n");
                sb.append("start_job: ").append(chain[0]).append("\n");
                sb.append("last_job: ").append(chain[1]).append("\n");
                sb.append("number_of_jobs: ").append(chain[2]).append("\n");
                sb.append("job_chain_runtime: ").append(toHMS(chain[3])).append("\n");
                sb.append("average_job_time: ").append(toHMS(chain[3] / chain[2])).append("\n");
            }
            sb.append("-");
            System.out.println(sb);

        } catch (NumberFormatException e) {
            System.out.println("Malformed Input");
        } catch (Exception e) {
            System.out.println("Malformed Input");
        }
    }

    static String toHMS(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}