package capitalone;

/**
 * I tried to recreate the exercise with Claude.
 *
 * Debugger Simulator
 *
 * Given two arrays:
 * - breakpoints — a 2D array where breakpoints[i][0] is a line number and breakpoints[i][1] is 1 if enabled, 0 if disabled, sorted by line number
 * - actions — a sequence of debugger commands
 *
 * Simulate a debugger session starting at the first breakpoint and process each action in order:
 * - "next" — move to the next enabled breakpoint. If there are no more enabled breakpoints ahead, wrap around to the first enabled one.
 * - "continue" — run past all remaining breakpoints, return -1.
 * - "stop" — halt immediately, return the current line number.
 *
 * If all actions are exhausted without hitting "stop" or "continue", return the current line number.
 *
 * Example:
 *
 * breakpoints = [[3,1], [7,0], [12,1], [20,1]]
 * actions = ["next", "next", "next", "stop"]
 *
 * start at line 3 (first enabled)
 * next → line 12 (skip 7, disabled)
 * next → line 20
 * next → line 3 (wrap around)
 * stop → return 3
 */
public class Exercise2 {

    public static int solution(int[][] breakpoints, String[] actions) {
        int bp=0;
        int act=0;

        // skip disabled breakpoints
        while (bp<breakpoints.length-1 && breakpoints[bp][1] == 0) {
            bp++;
        }
        if (bp == breakpoints.length) return -1;

        while (act < actions.length) {
            switch (actions[act]) {
                case "next":
                    // enabled breakpoint -> following action
                    if (breakpoints[bp][1] == 1) {
                        act++;
                    }
                    // both cases
                    bp = (bp+1) % breakpoints.length;
                    break;
                case "continue": return -1;
                case "stop":
                    // disabled breakpoint
                    if (breakpoints[bp][1] == 0) {
                        bp = (bp+1) % breakpoints.length;
                    }
                    return breakpoints[bp][0];
            }
        }
        return breakpoints[bp][0];
    }

    public static void main(String[] args) {
        int[][] breakpoints = {{3,1}, {7,0}, {12,1}, {20,0}};
        String[] actions = {"next", "next", "next", "stop"};

        System.out.println(solution(breakpoints, actions));
    }

}
