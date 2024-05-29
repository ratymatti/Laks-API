package com.of.scraper.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.of.scraper.dto.AverageAndMedianDTO;
import com.of.scraper.dto.DayDTO;
import com.of.scraper.dto.StatisticsDTO;
import com.of.scraper.dto.WeekDTO;
import com.of.scraper.dto.YearDTO;
import com.of.scraper.entity.Data;

public class TransformationUtils {

    /**
     * Transforms a list of fish data into a YearDTO.
     * 
     * Counts and calculates total and average weights for "Laks" and "Sjøørret",
     * and counts only total amount of "Pukkellaks".
     * 
     * @param fishes List of fish data for a specific year.
     * @return YearDTO representing the aggregated fish data for that year.
     */

    public static YearDTO transformToYearDTO(List<Data> fishes) {
        YearDTO yearDTO = new YearDTO(fishes.get(0).getLocalDate().getYear());

        Map<String, Integer> countMap = new HashMap<>();
        Map<String, Double> weightMap = new HashMap<>();

        for (Data fish : fishes) {
            String species = fish.getSpecies();
            countMap.put(species, countMap.getOrDefault(species, 0) + 1);

            if (!species.equals("Pukkellaks")) {
                weightMap.put(species, weightMap.getOrDefault(species, 0.0) + fish.getWeight());
            }
        }
        YearDTOUtils.handleSetCounts(yearDTO, countMap, weightMap);
        YearDTOUtils.handleSetAverages(yearDTO);

        return yearDTO;
    }

    /**
     * Converts a list of fish data into a DayDTO.
     * 
     * DayDTO encapsulates the date, count, total weight, and average weight of
     * fishes
     * caught in a day.
     * 
     * @param fishData List of Data objects from same date, each representing a
     *                 single fish's data.
     * @param date     The date of the fish data in "MM.dd" format.
     * @return DayDTO representing the aggregated fishing data for the day.
     */

    public static DayDTO transformToDayDTO(List<Data> fishData, String date) {
        if (fishData.size() > 0) {
            int count = fishData.size();
            double totalWeight = CalculationUtils
                    .calculateTotalWeight(fishData, Data::getWeight);
            double averageWeight = CalculationUtils
                    .calculateAverageWeight(count, totalWeight);

            return new DayDTO(date, count, totalWeight, averageWeight);
        } else {
            return new DayDTO(date);
        }
    }

    /**
     * Converts a map of fish data into a list of DayDTOs.
     * 
     * Each key-value pair in the map corresponds to a date and its associated fish
     * data.
     * Each date's fish data is transformed into a DayDTO and added to the list.
     * 
     * @param fishDataByDayAndMonth Map with date keys in "MM.dd" format and values
     *                              as lists of fish data.
     * @return List of DayDTOs, each representing fish data for a specific date.
     */

    public static List<DayDTO> transformToDayDTOList(Map<String, List<Data>> fishDataByDayAndMonth) {
        List<DayDTO> fishDataInDayDTOList = new ArrayList<>();

        for (String date : fishDataByDayAndMonth.keySet()) {
            fishDataInDayDTOList.add(transformToDayDTO(fishDataByDayAndMonth.get(date), date));
        }

        return fishDataInDayDTOList;
    }

    /**
     * Aggregates daily fish data into weekly statistics.
     * 
     * Iterates over the provided list of DayDTOs in 7-day chunks, calculating the
     * total and
     * average weight of fish caught in each week. Each week's data is encapsulated
     * in a WeekDTO,
     * which includes the start and end dates, fish count, total weight, and average
     * weight.
     * 
     * @param fishDataInDayDTOs A list of DayDTOs from same week, each representing
     *                          a day's fish
     *                          data.
     * @return A list of WeekDTOs, each representing a week's aggregated fish data.
     */

    public static List<WeekDTO> transformToWeekDTOList(List<DayDTO> fishDataInDayDTOs) {
        List<WeekDTO> weeklyStats = new ArrayList<>();

        for (int i = 0; i < fishDataInDayDTOs.size(); i += 7) {
            int endIndex = Math.min(i + 7, fishDataInDayDTOs.size());
            List<DayDTO> weekData = fishDataInDayDTOs.subList(i, endIndex);

            weeklyStats.add(transformToWeekDTO(weekData));
        }

        return weeklyStats;
    }

    /**
     * Aggregates daily fish data into a single WeekDTO.
     * 
     * @param weekData List of DayDTOs
     * @return WeekDTO representing the aggregated fish data for the week.
     */

    private static WeekDTO transformToWeekDTO(List<DayDTO> weekData) {
        int count = CalculationUtils
                .calculateCount(weekData, DayDTO::getFishCount);
        double totalWeight = CalculationUtils
                .calculateTotalWeight(weekData, DayDTO::getTotalWeight);
        double averageWeight = CalculationUtils
                .roundToTwoDecimals(CalculationUtils.calculateAverageWeight(count, totalWeight));

        String startDate = weekData.get(0).getDate();
        String endDate = weekData.get(weekData.size() - 1).getDate();

        return new WeekDTO(startDate, endDate, count, totalWeight, averageWeight);
    }

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
