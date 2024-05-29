package com.of.scraper.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.of.scraper.dto.AverageAndMedianDTO;
import com.of.scraper.dto.StatisticsDTO;
import com.of.scraper.dto.YearDTO;
import com.of.scraper.entity.Data;

public class TransformationUtils {

    

    /**
     * Aggregates yearly statistics into a single StatisticsDTO.
     * 
     * @param yearlyStatistics List of YearDTOs, each representing fish data for a
     *                         specific year.
     * @return StatisticsDTO representing the aggregated fish data for all years.
     */

    public static StatisticsDTO transformToStatisticsDTO(List<YearDTO> yearlyStatistics) {
        StatisticsDTO stats = new StatisticsDTO();
        int totalYears = yearlyStatistics.size();

        for (YearDTO yearDTO : yearlyStatistics) {
            StatisticsDTOUtils.handleStatsIncrementations(stats, yearDTO);
        }

        StatisticsDTOUtils.handleSetAverageValues(stats, totalYears);
        StatisticsDTOUtils.handleRoundValues(stats);

        return stats;
    }

    /**
     * Transforms daily counts from year to average and median from that specific
     * year.
     * 
     * @param dailyCounts Map where key is date in format MM/dd and value is count
     *                    from that date.
     * @return AverageAndMedianDTO with average count and median count calculated
     *         from those values.
     */

    public static AverageAndMedianDTO transformToAverageAndMedianDTO(Map<String, Integer> dailyCounts) {
        List<Integer> counts = new ArrayList<>(dailyCounts.values());

        double average = CalculationUtils
                .roundToTwoDecimals(CalculationUtils
                        .calculateAverageAmount(CalculationUtils
                                .calculateCount(counts, Integer::intValue), counts.size()));

        double median = CalculationUtils.calculateMedianAmount(counts);

        return new AverageAndMedianDTO(average, median);
    }

    /**
     * Transforms fishes from every year to AverageAndMedianDTOs
     * 
     * @param fishesByYear Map where keys represents a year and
     *                     values are Lists of all fishes from that specific year.
     * @return Map where key represents a year and value is AverageAndMedianDTO from
     *         that year.
     */

    public static Map<Integer, AverageAndMedianDTO> transformToAverageAndMedianDTOMap(
            Map<Integer, List<Data>> fishesByYear) {
        Map<Integer, AverageAndMedianDTO> fishCountAverageAndMedian = new TreeMap<>();

        for (List<Data> year : fishesByYear.values()) {
            AverageAndMedianDTO averageAndMedian = transformToAverageAndMedianDTO(CalculationUtils
                    .calculateDailyCounts(year));

            fishCountAverageAndMedian.put(year.get(0).getLocalDate().getYear(),
                    averageAndMedian);
        }

        return fishCountAverageAndMedian;
    }

    /**
     * Formats a date to a string in the format "MM.dd".
     * 
     * @param date The date to format as LocalDate.
     * @return String in the format "MM.dd".
     */

    public static String formatDateToMMddString(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return date.format(DateTimeFormatter.ofPattern("MM.dd"));
    }
}
