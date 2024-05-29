package com.of.scraper.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.of.scraper.dto.DayDTO;
import com.of.scraper.entity.Data;

public class DayDTOUtils {

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

}
