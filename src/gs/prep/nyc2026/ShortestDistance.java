package gs.prep.nyc2026;

import java.util.ArrayList;
import java.util.List;

public class ShortestDistance {
    private static final String DOCUMENT = "";

    /*
     * == Instructions ==
     *
     * Debug why the included test cases aren't succeeding and account for them in the code
     *
     * A description of the expected behaviour is given below
     */

    /**
     * Given two words returns the shortest distance between their two midpoints in number of characters
     * Words can appear multiple times in any order and should be case insensitive.
     *
     * E.g. for the document="This is a sample document we just made up"
     *   shortestDistance( document, "we", "just" ) == 4
     *   shortestDistance( document, "is", "a" ) == 2.5
     *   shortestDistance( document, "is", "not" ) == -1
     */
    public static double shortestDistanceN2(String document, String word1, String word2) {
        document = document.toLowerCase();
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        double minDis = document.length();
        int index1=0, index2=0;
        List<Double> words1 = new ArrayList<>();
        List<Double> words2 = new ArrayList<>();

        while ((index1 = document.indexOf(word1, index1)) != -1) {
            if (isWholeWord(document, word1, index1)) {
                double mid = index1 + (word1.length()-1) / 2d;
                words1.add(mid);
            }
            index1 += word1.length();
        }

        while ((index2 = document.indexOf(word2, index2)) != -1) {
            if (isWholeWord(document, word2, index2)) {
                double mid = index2 + (word2.length()-1) / 2d;
                words2.add(mid);
            }
            index2 += word2.length();
        }

        if (words1.isEmpty() || words2.isEmpty()) return -1;

        for (int i=0; i<words1.size(); i++) {
            for (int j=0; j<words2.size(); j++) {
                minDis = Math.min(minDis, Math.abs(words1.get(i) - words2.get(j)));
            }
        }

        return minDis;
    }

    public static double shortestDistance(String document, String word1, String word2) {
        document = document.toLowerCase();
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        // positions[0] == mid, positions[1] == 1 or 2 for word1 or word2
        List<double[]> positions = new ArrayList<>();
        int index=0;

        while ((index = document.indexOf(word1, index)) != -1) {
            if (isWholeWord(document, word1, index)) {
                positions.add(new double[]{index + (word1.length()-1)/2d, 1});
            }
            index += word1.length();
        }

        index=0;
        while ((index = document.indexOf(word2, index)) != -1) {
            if (isWholeWord(document, word2, index)) {
                positions.add(new double[]{index + (word2.length()-1)/2d, 2});
            }
            index += word2.length();
        }

        double minDis=document.length();
        double lastPos1=-1;
        double lastPos2=-1;
        for (double[] p : positions) {
            if (p[1] == 1) {
                lastPos1 = p[0];
            } else {
                lastPos2 = p[0];
            }

            if (lastPos1 != -1 && lastPos2 != -1) {
                minDis = Math.min(minDis, Math.abs(lastPos1-lastPos2));
            }
        }

        if (lastPos1 == -1 || lastPos2 == -1) return -1;

        return minDis;
    }

    private static boolean isWholeWord(String document, String word, int index) {
        if (index > 0 && Character.isLetter(document.charAt(index-1))
                || ((index + word.length()) < document.length()) && Character.isLetter(document.charAt(index + word.length()))) {
            return false;
        }
        return true;
    }


    public static boolean doTestsPass() {
        // todo: implement more tests if you'd like

        return  shortestDistance(DOCUMENT, "and", "graphic") == 6d &&
                shortestDistance(DOCUMENT, "transfer", "it") == 14d &&
                shortestDistance(DOCUMENT, "layout", "It" ) == 6d &&
                shortestDistance(DOCUMENT, "Design", "filler" ) == 25d &&
                shortestDistance(DOCUMENT, "It", "transfer") == 14d &&
                Math.abs(shortestDistance(DOCUMENT, "of", "lorem") - 4.5) < 0.000001 &&
                shortestDistance(DOCUMENT, "thiswordisnotthere", "lorem") == -1d;
    }


    public static void main(String[] args) {
        // Run the tests
        if (doTestsPass()) {
            System.out.println("All tests pass");
        } else {
            System.out.println("There are test failures");
        }
    }
}
