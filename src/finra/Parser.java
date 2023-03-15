package finra;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Given String tom,joe,"is,very,tall",man
 *
 * Write a function to translate that to String
 *  tom,joe,is_very_tall,man
 *
 *  The objective is to normalize delimiters that happen within a free text field
 */
public class Parser {

    // split str with delimiter ",'
    // ArrayList : {tom joe "is,very,tall" man}

    public static String parse(String str) {
        if (str == null || str.isBlank() || !str.contains("\"")) {
            return str;
        }
        boolean inQuote = false;
        String[] splitted = str.split(",");
        List<String> output = new ArrayList<>();
        StringJoiner joiner = null;

        for (int i=0; i<splitted.length; i++) {
            System.out.println(splitted[i]);

            if (splitted[i].contains("\"")) {
                if (!inQuote) {
                    inQuote = true;
                    joiner = new StringJoiner("_");
                    joiner.add(splitted[i].replaceAll("\"", ""));
                } else {
                    inQuote = false;
                    joiner.add(splitted[i].replaceAll("\"", ""));
                    output.add(joiner.toString());
                }
            } else {
                if (inQuote) {
                    joiner.add(splitted[i]);
                } else {
                    output.add(splitted[i]);
                }
            }
        }

        return output.stream().collect(Collectors.joining(","));
    }

    public static void main(String[] args) {
        String str = "tom,joe,\"is,very,tall\",man";
        System.out.println("input: " + str);
        System.out.println("output: " + Parser.parse(str));
    }

}
