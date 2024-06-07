package com.of.scraper.util;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import com.of.scraper.entity.Fish;

public class CalculationUtils {

    /**
     * Rounds a double value to two decimal places.
     * 
     * @param value The value to be rounded.
     * @return The rounded value as double.
     */

    public static double roundToTwoDecimals(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }

        final int NUM_OF_DECIMALS = 2;
        return Math.round(
                value * Math.pow(10, NUM_OF_DECIMALS)) / Math.pow(10, NUM_OF_DECIMALS);
    }

    /**
     * Calculates the total weight of items in a list.
     * 
     * @param <T>          The type of items in the list.
     * @param data         A list of items.
     * @param weightGetter A function that takes an item of type T and returns its
     *                     weight as a Double.
     * @return The total weight of items in the list as a double.
     */

    public static <T> double calculateTotalWeight(List<T> data, Function<T, Double> weightGetter) {
        return data.stream().mapToDouble(weightGetter::apply).sum();
    }

    /**
     * Calculates the total fish count for a week.
     * 
     * @param <T>         The type of items in the list.
     * @param data        A list of items.
     * @param countGetter A function that takes an item of type T and returns
     *                    its count as an Integer.
     * 
     * @return The total fish count for the week as int.
     */

    public static <T> int calculateCount(List<T> data, Function<T, Integer> countGetter) {
        return data.stream().mapToInt(countGetter::apply).sum();
    }

    /**
     * Calculates the average weight based on the count and total weight.
     *
     * @param count       the total count of items
     * @param totalWeight the total weight of items
     * @return the average weight, or 0.0 if count is 0
     * @throws IllegalArgumentException if count or totalWeight is negative
     * @throws IllegalStateException    if count is positive and totalWeight is 0,
     *                                  or count is 0 and totalWeight is positive
     */

    public static double calculateAverageWeight(int count, double totalWeight) {
        if (count < 0 || totalWeight < 0) {
            throw new IllegalArgumentException("Total count and total seasons must be non-negative.");
        }
        if ((count > 0 && totalWeight == 0) || (count == 0 && totalWeight > 0)) {
            throw new IllegalStateException("Invalid combination of count and total weight.");
        }
        return (count > 0) ? totalWeight / count : 0.0;
    }

    /**
     * Calculates the average amount based on the total count and time period.
     *
     * @param totalCount the total count of items
     * @param timePeriod the time period over which the average is calculated
     * @return the average amount, or 0.0 if totalCount is 0
     * @throws IllegalArgumentException if totalCount or timePeriod is negative
     * @throws IllegalStateException    if totalCount is positive and timePeriod is
     *                                  0,
     *                                  or totalCount is 0 and timePeriod is
     *                                  positive
     */
    
    public static double calculateAverageAmount(int totalCount, int timePeriod) {
        if (totalCount < 0 || timePeriod < 0) {
            throw new IllegalArgumentException("Total count and total seasons must be greater than 0.");
        }
        if (totalCount > 0 && timePeriod == 0) {
            throw new IllegalStateException("Invalid combination of count and time period.");
        }
        return (totalCount > 0) ? (double) totalCount / timePeriod : 0.0;
    }

    /**
     * Calculates daily counts of fish caught per season.
     * 
     * @param year List of all fishes caught in season.
     * @return Map where keys are date in format MM/dd and values are total count
     *         from that date.
     */

    public static Map<String, Integer> calculateDailyCounts(List<Fish> year) {
        if (year == null) {
            throw new IllegalArgumentException("Year cannot be null");
        }

        Map<String, Integer> fishCounts = new TreeMap<>();

        for (Fish fish : year) {
            fishCounts.put(TransformationUtils.formatDateToMMddString(fish.getDate()),
                    fishCounts.getOrDefault(TransformationUtils
                            .formatDateToMMddString(fish.getDate()), 0) + 1);
        }

        return fishCounts;
    }

    /**
     * Calculates median amount from List of Integers. If length is 0 returns 0.0.
     * If length is even, returns average of two middle values. If length is odd,
     * returns middle value.
     * 
     * @param counts List of integers
     * @return median calculated from List counts as double
     */

    public static double calculateMedianAmount(List<Integer> counts) {
        if (counts == null) {
            throw new IllegalArgumentException("Counts cannot be null");
        }

        int length = counts.size();

        if (length == 0) {
            return 0.0;
        }

        counts.sort(Integer::compareTo);

        if (length % 2 == 0) {
            return ((double) counts.get(length / 2) + counts.get(length / 2 - 1)) / 2;
        } else {
            return counts.get(length / 2);
        }
    }

}
