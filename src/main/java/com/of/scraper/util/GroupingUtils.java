package com.of.scraper.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.of.scraper.entity.Data;

public class GroupingUtils {

    /**
     * Segregates fish data into groups based on the day and month of capture.
     * 
     * The method iterates over the fish data, formats the capture date to "MM.dd"
     * format, and groups the data accordingly.
     * 
     * @param fishData List of Data objects, each representing a single fish's data.
     * @return Map with keys as "MM.dd" formatted dates and values as lists of fish
     *         data for that date.
     */

    public static Map<String, List<Data>> groupByDayAndMonth(List<Data> fishData) {
        Map<String, List<Data>> fishDataByDayAndMonth = new TreeMap<>();

        LocalDate seasonStart = LocalDate.of(2022, 6, 15);
        LocalDate seasonEnd = LocalDate.of(2022, 8, 31);

        for (LocalDate date = seasonStart; !date.isAfter(seasonEnd); date = date.plusDays(1)) {
            String dayAndMonth = TransformationUtils.formatDateToMMddString(date);
            fishDataByDayAndMonth.put(dayAndMonth, new ArrayList<>());
        }

        for (Data fish : fishData) {
            String dayAndMonth = TransformationUtils.formatDateToMMddString(fish.getLocalDate());
            fishDataByDayAndMonth.get(dayAndMonth).add(fish);
        }

        return fishDataByDayAndMonth;
    }

    /**
     * This method groups a list of fishes by the year they were caught.
     *
     * @param fishes The list of fishes to group.
     * @return A map where the keys are years and the values are lists of fishes
     *         caught in that year.
     */

    public static Map<Integer, List<Data>> groupByYear(List<Data> fishData) {
        Map<Integer, List<Data>> fishDataByYear = new TreeMap<>();

        for (Data fish : fishData) {
            int year = fish.getLocalDate().getYear();
            fishDataByYear.computeIfAbsent(year, key -> new ArrayList<>()).add(fish);
        }
        return fishDataByYear;
    }
}
