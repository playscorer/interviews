package morganstanley;

import java.util.*;

public class Exercise1 {

    /**
     * Find the top k most frequent elements.
     *
     * Example 1 : [1 1 1 2 2 3] k=2
     * Output : [1 2]
     *
     * Example 2: [1] k=1
     * Output : [1]
     */
    public static int[] topKElements(int[] array, int k) {
        int[] topK = new int[k];
        Map<Integer, Integer> countMap = new HashMap<>();
        TreeMap<Integer, Integer> sortedMap = new TreeMap<>(Comparator.reverseOrder());

        for (int i=0; i<array.length; i++) {
            countMap.put(array[i], countMap.getOrDefault(array[i], 0) + 1);
        }

        countMap.entrySet().forEach(e -> sortedMap.put(e.getValue(), e.getKey()));

        for (int i=0; i<k; i++) {
            List<Map.Entry<Integer, Integer>> list = sortedMap.entrySet().stream().toList();
            topK[i] = list.get(i).getValue();
        }

        return topK;
    }

    public static void main(String[] args) {
        int[] ex1 = {1, 1, 1, 2, 2, 3};
        System.out.println(Arrays.toString(topKElements(ex1, 2)));

        int[] ex2 = {1};
        System.out.println(Arrays.toString(topKElements(ex2, 1)));

        int[] ex3 = {1, 1, 1, 2, 2, 3, 3, 3, 3};
        System.out.println(Arrays.toString(topKElements(ex3, 2)));
    }

}
