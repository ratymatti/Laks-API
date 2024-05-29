package com.of.scraper.util;

import java.util.ArrayList;
import java.util.List;

import com.of.scraper.dto.DayDTO;
import com.of.scraper.dto.WeekDTO;

public class WeekDTOUtils {
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
}
