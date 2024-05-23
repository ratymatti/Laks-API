package com.of.scraper.util;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.of.scraper.dto.StatisticsDTO;
import com.of.scraper.dto.YearDTO;

/**
 * Utility class for handling StatisticsDTO objects.
 * 
 * @methods handleStatsIncrementations, handleRoundValues,
 *          handleSetAverageValues, roundAndSet, calculateAndSetAverages
 */

public class StatisticsDTOUtils {

    /**
     * Increments the total counts and weights of salmon, seatrout, and pukkellaks
     * in the provided StatisticsDTO.
     * 
     * @param stats   The StatisticsDTO to update.
     * @param yearDTO The YearDTO to extract the data from.
     */

    public static void handleStatsIncrementations(StatisticsDTO stats, YearDTO yearDTO) {
        stats.incrementSalmonCount(yearDTO.getSalmonCount());
        stats.incrementSalmonWeight(yearDTO.getSalmonTotalWeight());
        stats.incrementSeatroutCount(yearDTO.getSeatroutCount());
        stats.incrementSeatroutWeight(yearDTO.getSeatroutTotalWeight());
        stats.incrementPukkellaksCount(yearDTO.getPukkellaksCount());
    }

    /**
     * Rounds the total salmon and seatrout weights, average weights and average counts
     * in the provided StatisticsDTO to two decimal places.
     * 
     * @param stats The StatisticsDTO to update.
     */

    public static void handleRoundValues(StatisticsDTO stats) {
        roundAndSet(
                stats,
                StatisticsDTO::getTotalSalmonWeight,
                StatisticsDTO::setTotalSalmonWeight);

        roundAndSet(
                stats,
                StatisticsDTO::getAverageSalmonWeight,
                StatisticsDTO::setAverageSalmonWeight);

        roundAndSet(
                stats,
                StatisticsDTO::getAverageSalmonCountPerSeason,
                StatisticsDTO::setAverageSalmonCountPerSeason);

        roundAndSet(
                stats,
                StatisticsDTO::getTotalSeatroutWeight,
                StatisticsDTO::setTotalSeatroutWeight);

        roundAndSet(
                stats,
                StatisticsDTO::getAverageSeatroutWeight,
                StatisticsDTO::setAverageSeatroutWeight);

        roundAndSet(
                stats,
                StatisticsDTO::getAverageSeatroutCountPerSeason,
                StatisticsDTO::setAverageSeatroutCountPerSeason);
    }

    /**
     * Rounds the provided value to two decimal places and sets it in the
     * provided StatisticsDTO.
     * 
     * @param stats  The StatisticsDTO to update.
     * @param getter The function to get the value from the StatisticsDTO.
     * @param setter The function to set the value in the StatisticsDTO.
     */

    private static void roundAndSet(
            StatisticsDTO stats,
            Function<StatisticsDTO, Double> getter,
            BiConsumer<StatisticsDTO, Double> setter) {

        double originalValue = getter.apply(stats);
        double roundedValue = CalculationUtils.roundToTwoDecimals(originalValue);
        setter.accept(stats, roundedValue);
    }

    /**
     * Sets the average weights and counts of salmon and seatrout in the provided
     * StatisticsDTO.
     * The average values are calculated based on the total weight, total count, and
     * total years provided.
     *
     * @param stats      The StatisticsDTO to update.
     * @param totalYears The total number of years in the data.
     */

    public static void handleSetAverageValues(StatisticsDTO stats, int totalYears) {
        calculateAndSetAverages(
                stats,
                StatisticsDTO::getTotalSalmonWeight,
                StatisticsDTO::getTotalSalmonCount,
                StatisticsDTO::setAverageSalmonWeight,
                StatisticsDTO::setAverageSalmonCountPerSeason,
                totalYears);

        calculateAndSetAverages(
                stats,
                StatisticsDTO::getTotalSeatroutWeight,
                StatisticsDTO::getTotalSeatroutCount,
                StatisticsDTO::setAverageSeatroutWeight,
                StatisticsDTO::setAverageSeatroutCountPerSeason,
                totalYears);
    }

    /**
     * Calculates the average weight and count for a given species and sets these
     * averages in the provided StatisticsDTO.
     * The averages are calculated based on the total weight, total count, and total
     * years provided.
     *
     * @param stats               The StatisticsDTO to update.
     * @param totalWeightGetter   A function to get the total weight for the
     *                            species.
     * @param totalCountGetter    A function to get the total count for the species.
     * @param averageWeightSetter A consumer to set the average weight for the
     *                            species.
     * @param averageCountSetter  A consumer to set the average count for the
     *                            species.
     * @param totalYears          The total number of years in the data.
     */

    private static void calculateAndSetAverages(
            StatisticsDTO stats,
            Function<StatisticsDTO, Double> totalWeightGetter,
            Function<StatisticsDTO, Integer> totalCountGetter,
            BiConsumer<StatisticsDTO, Double> averageWeightSetter,
            BiConsumer<StatisticsDTO, Double> averageCountSetter,
            int totalYears) {

        double totalWeight = totalWeightGetter.apply(stats);
        int totalCount = totalCountGetter.apply(stats);

        double averageWeight = CalculationUtils.calculateAverageWeight(totalCount, totalWeight);
        double averageCount = CalculationUtils.calculateAverageAmount(totalCount, totalYears);

        averageWeightSetter.accept(stats, averageWeight);
        averageCountSetter.accept(stats, averageCount);
    }
}
