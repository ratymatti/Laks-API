package com.of.scraper.util;

import java.util.List;

import com.of.scraper.dto.DayDTO;
import com.of.scraper.dto.YearDTO;

public class CalculationUtils {

    /**
     * Rounds the total and average weights of salmons and seatrouts in a YearDTO
     * to one decimal place.
     * 
     * @param yearDTO The YearDTO to be rounded.
     * @return The YearDTO with rounded values.
     */

    public static YearDTO roundYearDTOValues(YearDTO yearDTO) {
        if (yearDTO == null) {
            throw new IllegalArgumentException("YearDTO must not be null.");
        }
        yearDTO.setSalmonTotalWeight(roundToTwoDecimals(yearDTO.getSalmonTotalWeight()));
        yearDTO.setSalmonAverageWeight(roundToTwoDecimals(yearDTO.getSalmonAverageWeight()));
        yearDTO.setSeatroutTotalWeight(roundToTwoDecimals(yearDTO.getSeatroutTotalWeight()));
        yearDTO.setSeatroutAverageWeight(roundToTwoDecimals(yearDTO.getSeatroutAverageWeight()));
        return yearDTO;
    }

    /**
     * Rounds a double value to two decimal places.
     * 
     * @param value The value to be rounded.
     * @return The rounded value as double.
     */

    public static double roundToTwoDecimals(double value) {
        final int NUM_OF_DECIMALS = 2;
        return Math.round(
                value * Math.pow(10, NUM_OF_DECIMALS)) / Math.pow(10, NUM_OF_DECIMALS);
    }

    /**
     * Calculates the total weight of fish caught in a week.
     * 
     * @param weekData A list of DayDTOs from same week, each representing a day's
     *                 fish data.
     * @return The total weight of fish caught in the week as double.
     */

    public static double calculateTotalWeight(List<DayDTO> weekData) {
        return weekData.stream().mapToDouble(DayDTO::getTotalWeight).sum();
    }

    /**
     * Calculates the total fish count for a week.
     * 
     * @param weekData A list of DayDTOs from same week, each representing a day's
     *                 fish data.
     * @return The total fish count for the week as int.
     */

    public static int calculateCount(List<DayDTO> weekData) {
        return weekData.stream().mapToInt(DayDTO::getFishCount).sum();
    }

    /**
     * Calculates the average weight of fish caught in a week.
     * 
     * @param count       The total fish count for the week.
     * @param totalWeight The total weight of fish caught in the week.
     * @return The average weight of fish caught in the week as double.
     */

    public static double calculateAverageWeight(int count, double totalWeight) {
        if (count < 0 || totalWeight < 0) {
            throw new IllegalArgumentException("Total count and total seasons must be non-negative.");
        }
        return (count > 0) ? totalWeight / count : 0.0;
    }

    /**
     * Calculates the average amount of fish caught per season.
     * 
     * @param totalCount   int
     * @param totalSeasons int
     * @return average value as double
     */

    public static double calculateAverageAmount(int totalCount, int totalSeasons) {
        return (totalSeasons > 0) ? (double) totalCount / totalSeasons : 0.0;
    }

}
