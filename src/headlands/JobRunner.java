package headlands;

import java.util.*;

/**
 * HackerRank Online Assessment for Headlands Tech on May 27th, 2026 at Jefferson. VA.
 *
 * Job Runner
 *
 * You work on a highly sophisticated distributed computing platform. Each day it
 * generates a daily log of the jobs run. Each job has three properties: a unique
 * integer id, the time it took to run in seconds, and the id of the job that ran
 * after it. We call a sequence of jobs a chain, and if a job represents the end of
 * the chain, its next id will be 0. Given the daily log, generate a report
 * summarizing the chains run during the day. If the input is malformed in any way
 * then an error should be reported.
 *
 * Input Format
 * Input is provided in CSV format via STDIN. The first line is a header, and each
 * subsequent line provides the properties for a given job.
 *
 *   #job_id,runtime_in_seconds,next_job_id
 *   1,60,23
 *   2,23,3
 *   3,12,0
 *   23,30,0
 *
 * Output Format
 * The resulting summary report should be emitted via STDOUT. Each section in the
 * report describes a chain, and the sections should be in descending order based on
 * the total runtime of the chain.
 *
 *   -
 *   start_job: id of the first job in the chain
 *   last_job: id of the last job in the chain
 *   number_of_jobs: number of jobs in the chain
 *   job_chain_runtime: total runtime of the chain in HH:MM:SS
 *   average_job_time: average per-job runtime in HH:MM:SS
 *   -
 *
 * Given the example input above, the summary report would look like:
 *
 *   -
 *   start_job: 1
 *   last_job: 23
 *   number_of_jobs: 2
 *   job_chain_runtime: 00:01:30
 *   average_job_time: 00:00:45
 *   -
 *   start_job: 2
 *   last_job: 3
 *   number_of_jobs: 2
 *   job_chain_runtime: 00:00:35
 *   average_job_time: 00:00:17
 *   -
 *
 * If the input is malformed in any way - for example:
 *
 *   garbage
 *
 * Then your program should exit zero and print the following error message to STDOUT:
 *
 *   Malformed Input
 *
 * Note: The examples we provide aren't exhaustive, so be sure to test your code
 * thoroughly. A human will review the code as well.
 */
public class JobRunner {
    public record Job(int jobId, long runtime, int nextJobId) {};

    public static void main(String args[]) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        Map<Integer, Job> mapJobs = new LinkedHashMap<>();

        try (Scanner scanner = new Scanner(System.in)) {
            if (!scanner.hasNextLine() || !scanner.nextLine().equals("#job_id,runtime_in_seconds,next_job_id")) {
                System.out.println("Malformed Input");
                return;
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Split row by commas
                String[] values = line.split(",");
                if (values.length != 3) {
                    System.out.println("Malformed Input");
                    return;
                }

                Job job = new Job(Integer.parseInt(values[0]), Long.parseLong(values[1]), Integer.parseInt(values[2]));
                if (mapJobs.containsKey(job.jobId())) {
                    System.out.println("Malformed Input");
                    return;
                }
                mapJobs.put(job.jobId(), job);
            }
        }

        // List of job ids
        List<long[]> reportList = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        // We could also ignore all non starters by checking in a list of nextJobIds
        // And return Malformed whenever we revisit a node (shared nodes across chains)
        for (Integer id : mapJobs.keySet()) {
            int startJobId = id;
            int lastJobId = id;
            int count = 0;
            int totalRuntime = 0;

            boolean complete = false;
            int curId=id;
            while (curId != 0) {
                Job curJob = mapJobs.get(curId);
                if (visited.contains(curId)) {
                    break;
                }
                visited.add(curId);
                lastJobId = curId;
                count++;
                totalRuntime += curJob.runtime();
                curId = curJob.nextJobId();
                if (curId == 0) complete = true;
            }
            if (complete) {
                reportList.add(new long[]{startJobId, lastJobId, count, totalRuntime});
            }
        }

        // Sort by total runtime desc
        Collections.sort(reportList, Comparator.comparingLong((long[] r) -> r[3]).reversed());

        StringBuilder sb = new StringBuilder();
        for (long[] chain : reportList) {
            System.out.println();
            sb.append("-\n");
            sb.append("start_job: ").append(chain[0]).append("\n");
            sb.append("last_job: ").append(chain[1]).append("\n");
            sb.append("number_of_jobs: ").append(chain[2]).append("\n");
            sb.append("job_chain_runtime: ").append(toHMS(chain[3])).append("\n");
            sb.append("average_job_time: ").append(toHMS(chain[3] / chain[2])).append("\n");
        }
        sb.append("-");
        System.out.println(sb);
    }

    public static String toHMS(long seconds) {
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    /*
        I messed up during the assessment. Although my tests pass, I iterated through a list of sortedJobIds by runtime desc,
        not containing the endJobs (nextId == 0).
        That means I only ignore the final jobs in my list, but I'd reprocess intermediate ones when chains are longer than 2.
        I sort initially and not at the end based on the total time.

        Problems I still see : I stress out and do not read thoroughly the description of the problem. Then I have the idea,
        but I get easily stuck in the middle of my reasoning and do not push it through by actually trying a specific case on paper.
        I should write pseudocode.
        I should exclude edge cases from the start to assume my input is correctly formed.
     */
}
