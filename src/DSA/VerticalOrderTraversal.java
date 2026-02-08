package DSA;

import java.util.*;

public class VerticalOrderTraversal {
    public class TreeNode {
          int val;
          TreeNode left;
          TreeNode right;
          TreeNode() {}
          TreeNode(int val) { this.val = val; }
          TreeNode(int val, TreeNode left, TreeNode right) {
              this.val = val;
              this.left = left;
              this.right = right;
          }
      }
    public List<List<Integer>> verticalTraversalDfs(TreeNode root) {
        TreeMap<Integer, PriorityQueue<int[]>> treeMap = new TreeMap<>();
        dfs(root, treeMap, 0, 0);
        List<List<Integer>> list = new ArrayList<>();
        treeMap.values().forEach(pq -> {
            List<Integer> arrayList = new ArrayList<>();
            while (!pq.isEmpty()) {
                int[] node = pq.poll();
                arrayList.add(node[1]);
            }
            list.add(arrayList);
        });
        return list;
    }

    public void dfs(TreeNode root, TreeMap<Integer, PriorityQueue<int[]>> treeMap, int row, int column) {
        if (root != null) {
            treeMap.computeIfAbsent(column, k -> new PriorityQueue<>(
                    (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1])
            ).offer(new int[]{row, root.val});
            dfs(root.left, treeMap, row+1, column-1);
            dfs(root.right, treeMap, row+1, column+1);
        }
    }

    record Triple(int column, int row, TreeNode node) {}

    public List<List<Integer>> verticalTraversalBfs(TreeNode root) {
        TreeMap<Integer, PriorityQueue<int[]>> treeMap = new TreeMap<>();
        Queue<Triple> queue = new LinkedList<>();

        if (root == null) {
            return null;
        }

        queue.offer(new Triple(0, 0, root));

        while (!queue.isEmpty()) {
            Triple triple = queue.poll();

            treeMap.computeIfAbsent(triple.column, k -> new PriorityQueue<>(
                    (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1])
            ).offer(new int[] {triple.row, triple.node.val});

            if (triple.node.left != null) {
                queue.offer(new Triple(triple.column-1, triple.row+1, triple.node.left));
            }

            if (triple.node.right != null) {
                queue.offer(new Triple(triple.column+1, triple.row+1, triple.node.right));
            }
        }
        List<List<Integer>> list = new ArrayList<>();
        treeMap.values().forEach(pq -> {
            List<Integer> arrayList = new ArrayList<>();
            while (!pq.isEmpty()) {
                int[] node = pq.poll();
                arrayList.add(node[1]);
            }
            list.add(arrayList);
        });
        return list;
    }
}
