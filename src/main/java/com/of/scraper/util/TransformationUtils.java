package com.of.scraper.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.of.scraper.dto.DayDTO;
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
        for (Data fish : fishes) {
            if (fish.getSpecies().equals("Laks")) {
                yearDTO.setSalmonCount(yearDTO.getSalmonCount() + 1);
                yearDTO.setSalmonTotalWeight(yearDTO.getSalmonTotalWeight() + fish.getWeight());
            }
            if (fish.getSpecies().equals("Sjøørret")) {
                yearDTO.setSeatroutCount(yearDTO.getSeatroutCount() + 1);
                yearDTO.setSeatroutTotalWeight(yearDTO.getSeatroutTotalWeight() + fish.getWeight());
            }
            if (fish.getSpecies().equals("Pukkellaks")) {
                yearDTO.setPukkellaksCount(yearDTO.getPukkellaksCount() + 1);
            }
        }
        yearDTO.setSalmonAverageWeight(yearDTO.getSalmonTotalWeight() / yearDTO.getSalmonCount());
        yearDTO.setSeatroutAverageWeight(yearDTO.getSeatroutTotalWeight() / yearDTO.getSeatroutCount());

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
     * @return DayDTO representing the aggregated fishing data for the day.
     */

    public static DayDTO transformToDayDTO(List<Data> fishData) {
        if (fishData.size() > 0) {
            String date = formatDateToMMddString(fishData.get(0).getLocalDate());
            int count = fishData.size();
            double totalWeight = fishData.stream().mapToDouble(Data::getWeight).sum();
            double averageWeight = (count > 0) ? totalWeight / count : 0.0;

            return new DayDTO(date, count, totalWeight, averageWeight);
        } else {
            return new DayDTO();
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
            fishDataInDayDTOList.add(transformToDayDTO(fishDataByDayAndMonth.get(date)));
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

            int count = CalculationUtils.calculateCount(weekData);
            double totalWeight = CalculationUtils.calculateTotalWeight(weekData);
            double averageWeight = CalculationUtils
                    .roundToTwoDecimals(CalculationUtils.calculateAverageWeight(count, totalWeight));

            String startDate = weekData.get(0).getDate();
            String endDate = weekData.get(weekData.size() - 1).getDate();

            weeklyStats.add(new WeekDTO(startDate, endDate, count, totalWeight, averageWeight));
        }

        return weeklyStats;
    }

    /**
     * Formats a date to a string in the format "MM.dd".
     * 
     * @param date The date to format as LocalDate.
     * @return String in the format "MM.dd".
     */

    public static String formatDateToMMddString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MM.dd"));
    }
}
