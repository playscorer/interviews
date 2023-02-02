package bnp;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Second round - technical interview for BNP on Feb 2nd 2023
 */
public class MaxPopulation {

    private static final int[] births = {1975, 1980, 2006};
    private static final int[] deaths = {2000, 2005, 2040};

    public static int solution1() {
        int initialYear = births[0];
        int endingYear = deaths[deaths.length-1];
        int[] population = new int[endingYear-initialYear]; // 2040 - 1975 = 65

        // We will add 1 to cells where there is someone alive
        for (int i=0; i<births.length; i++) {
            for (int year=births[i]-initialYear; year<deaths[i]-initialYear; year++) {
                population[year] += 1;
            }
        }

        //Arrays.stream(population).forEach(System.out::print);

        int max=0;

        for (int i=0; i<population.length; i++) {
            if (max < population[i]) {
                max = population[i];
            }
        }

        for (int i=0; i<population.length; i++) {
            if (max == population[i]) {
                return i + initialYear;
            }
        }

        return 0;
    }

    public static int solution2() {
        int initialYear = births[0];
        int endingYear = deaths[deaths.length-1];
        int[] population = new int[endingYear-initialYear]; // 2040 - 1975 = 65

        IntStream.range(0, births.length).forEach(i -> IntStream.range(births[i]-initialYear, deaths[i]-initialYear).forEach(year -> population[year] += 1));
        //Arrays.stream(population).forEach(System.out::print);

        int max = Arrays.stream(population).max().getAsInt();

        return IntStream.range(0, population.length).filter(x -> population[x] == max).findFirst().getAsInt() + initialYear;
    }

    public static void main(String[] args) {
        int maxYear1 = solution1();
        int maxYear2 = solution2();

        System.out.println(maxYear1);
        System.out.println(maxYear2);
    }
}
