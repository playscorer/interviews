package DSA;

/**
 * Backtracking with DFS.
 * What is important in the backtracking is to restore the state of the cell to non visited
 * once ALL directions have been visited to let this cell be part of another exploration path.
 */
public class WordSearch {
    public boolean exist(char[][] board, String word) {
        int[][] visit = new int[board.length][board[0].length];

        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                visit[i][j] = 1;
            }
        }

        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                if(visit(i, j, visit, board, 0, word)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean visit(int i, int j, int[][] visit, char[][] board, int idx, String word) {
        if (visit[i][j] == 1 && idx < word.length() && word.charAt(idx) == board[i][j]) {
            visit[i][j] = 0;
            idx+=1;
            int[][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
            for (int[] dir : directions) {
                int x = i+dir[0];
                int y = j+dir[1];
                if (x >= 0 && x < board.length && y >= 0 && y < board[0].length) {
                    if (visit(x, y, visit, board, idx, word)) {
                        return true;
                    }
                }
            }
            visit[i][j] = 1;
        }

        if (idx >= word.length()) {
            return true;
        }

        return false;
    }
}
