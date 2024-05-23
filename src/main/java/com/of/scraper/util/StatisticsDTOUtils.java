package com.of.scraper.util;

import com.of.scraper.dto.StatisticsDTO;
import com.of.scraper.dto.YearDTO;

/**
 * Utility class for handling StatisticsDTO objects.
 * 
 * @methods handleStatsIncrementations, handleRoundValues, handleSetAverageValues
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
     * Rounds the total salmon and seatrout weights in the provided StatisticsDTO
     * to two decimal places.
     * 
     * @param stats The StatisticsDTO to update.
     */

    public static void handleRoundValues(StatisticsDTO stats) {
        stats.setTotalSalmonWeight(CalculationUtils.roundToTwoDecimals(stats.getTotalSalmonWeight()));
        stats.setTotalSeatroutWeight(CalculationUtils.roundToTwoDecimals(stats.getTotalSeatroutWeight()));
    }

    /**
     * Calculates and sets the average weights and counts of salmon and
     * seatrout in the provided StatisticsDTO.
     * 
     * @param stats      The StatisticsDTO to update.
     * @param totalYears The total amount of years in the data.
     */

    public static void handleSetAverageValues(StatisticsDTO stats, int totalYears) {
        double totalSalmonWeight = stats.getTotalSalmonWeight();
        int totalSalmonCount = stats.getTotalSalmonCount();

        stats.setAverageSalmonWeight(
                CalculationUtils.calculateAndRoundAverageDouble(
                        totalSalmonCount, totalSalmonWeight));
        stats.setAverageSalmonCountPerSeason(
                CalculationUtils.calculateAndRoundAverageInt(
                        totalSalmonCount, totalYears));

        double totalSeatroutWeight = stats.getTotalSeatroutWeight();
        int totalSeatroutCount = stats.getTotalSeatroutCount();

        stats.setAverageSeatroutWeight(
                CalculationUtils.calculateAndRoundAverageDouble(
                        totalSeatroutCount, totalSeatroutWeight));
        stats.setAverageSeatroutCountPerSeason(
                CalculationUtils.calculateAndRoundAverageInt(
                        totalSeatroutCount, totalYears));
    }
}
