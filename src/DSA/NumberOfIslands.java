package DSA;

import java.util.LinkedList;
import java.util.Queue;

public class NumberOfIslands {
    public static int numIslands(char[][] grid) {
        int count = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    bfs(i, j, grid);
                }
            }
        }
        return count;
    }

    public static void bfs(int i, int j, char[][] grid) {
        Queue<int[]> neighbors = new LinkedList<>();
        neighbors.offer(new int[]{i, j});

        int[][] directions = new int[][]{{0, 1}, {0, -1}, {-1, 0}, {1, 0}};

        while (!neighbors.isEmpty()) {
            int[] pt = neighbors.poll();

            for (int[] dir : directions) {
                int x = pt[0] + dir[0];
                int y = pt[1] + dir[1];

                if (0 <= x && x < grid.length && 0 <= y && y < grid[0].length && grid[x][y] == '1') {
                    grid[x][y] = '0';
                    neighbors.offer(new int[]{x, y});
                }
            }
        }
    }

    public static void display(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        char[][] grid = new char[][]{
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '1'},
                {'0', '0', '0', '1', '1'}
        };

        display(grid);
        System.out.println(numIslands(grid));
        display(grid);
    }
}
