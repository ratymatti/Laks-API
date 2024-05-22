package com.of.scraper.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.of.scraper.dto.AnglerStatsDTO;
import com.of.scraper.dto.DayDTO;
import com.of.scraper.dto.WeekDTO;
import com.of.scraper.dto.YearDTO;
import com.of.scraper.entity.Data;
import com.of.scraper.repository.DataRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DataProcessingService {

    private DataRepository dataRepository;

    /**
     * Creates an AnglerStatsDTO for the given name and species.
     *
     * @param name    the name of the angler
     * @param species the species of fish
     * @return the angler stats
     */

    public AnglerStatsDTO createAnglerStatsDTO(String name, String species) {
        int count = dataRepository.getCountByNameAndSpecies(name, species);
        double totalWeight = dataRepository.getTotalWeightByNameAndSpecies(name, species);
        double averageWeight = (count > 0) ? totalWeight / count : 0;

        return new AnglerStatsDTO(name, count, totalWeight, averageWeight);
    }

    /**
     * This method calculates the best weeks for each year.
     * The best weeks are determined by the number of fishes caught.
     *
     * @param fishData A list of fishes to calculate the best weeks for.
     * 
     * @return A map where the keys are years and the values are lists of three best
     *         seven day periods for that year.
     */

    public Map<Integer, List<WeekDTO>> getBestWeeksByYear(List<Data> fishData) {
        Map<Integer, List<Data>> fishDataByYear = groupByYear(filterOutOffSeasonFishes(fishData));

        Map<Integer, List<WeekDTO>> bestWeeksByYear = new TreeMap<>();

        for (List<Data> fishesByYear : fishDataByYear.values()) {
            Map<String, List<Data>> fishDataByDayAndMonth = groupByDayAndMonth(fishesByYear);

            List<DayDTO> fishDataInDayDTOList = transformToDayDTOList(fishDataByDayAndMonth);

            bestWeeksByYear.put(
                    fishesByYear.get(0).getLocalDate().getYear(),
                    getBestWeeks(calculateWeeklyStats(fishDataInDayDTOList)));
        }

        return bestWeeksByYear;
    }

    /**
     * Calculates and returns the top three weeks with the highest fish count from
     * the provided fish data.
     * 
     * @param fishData List of Data objects, each representing a single fish's data.
     * @return List of the top three WeekDTOs, each representing a week's aggregated
     *         fish data.
     */

    public List<WeekDTO> getBestWeeksAlltime(List<Data> fishData) {
        Map<String, List<Data>> fishDataByDayAndMonth = groupByDayAndMonth(filterOutOffSeasonFishes(fishData));

        List<DayDTO> fishDataInDayDTOList = transformToDayDTOList(fishDataByDayAndMonth);

        return getBestWeeks(calculateWeeklyStats(fishDataInDayDTOList));
    }

    /**
     * Calculates and returns yearly fishing statistics as a list of YearDTO
     * objects.
     * 
     * The method groups the fish data by year using the groupByYear method. For
     * each year's data,
     * it creates a YearDTO object using the transformToYearDTO method, which
     * includes counts and
     * total weights for each species of fish.
     * 
     * The weights in the YearDTO are then rounded to one decimal place using the
     * roundYearDTOValues method.
     * The rounded YearDTO is added to the result list.
     * 
     * @param fishData List of Data objects, each representing a single fish's data.
     * @return List of YearDTO objects, each representing a year's aggregated fish
     *         data.
     */

    public List<YearDTO> getStatistics(List<Data> fishData) {
        Map<Integer, List<Data>> fishesByYear = groupByYear(fishData);

        List<YearDTO> result = new ArrayList<>();

        for (List<Data> year : fishesByYear.values()) {
            YearDTO yearDTO = transformToYearDTO(year);

            result.add(roundYearDTOValues(yearDTO));
        }

        return result;
    }

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

    private Map<String, List<Data>> groupByDayAndMonth(List<Data> fishData) {
        Map<String, List<Data>> fishDataByDayAndMonth = new TreeMap<>();

        LocalDate seasonStart = LocalDate.of(2022, 6, 15);
        LocalDate seasonEnd = LocalDate.of(2022, 8, 31);

        for (LocalDate date = seasonStart; !date.isAfter(seasonEnd); date = date.plusDays(1)) {
            String dayAndMonth = formatDateToMMddString(date);
            fishDataByDayAndMonth.put(dayAndMonth, new ArrayList<>());
        }

        for (Data fish : fishData) {
            String dayAndMonth = formatDateToMMddString(fish.getLocalDate());
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

    private Map<Integer, List<Data>> groupByYear(List<Data> fishData) {
        Map<Integer, List<Data>> fishDataByYear = new TreeMap<>();

        for (Data fish : fishData) {
            int year = fish.getLocalDate().getYear();
            fishDataByYear.computeIfAbsent(year, key -> new ArrayList<>()).add(fish);
        }
        return fishDataByYear;
    }

    /**
     * Filters out the fishes that were caught out of season.
     * Currently by the salmon fishing season is from June 15th to August 31st.
     * Could be refactored to take the season as a parameter.
     * 
     * @param fishData
     * @return A list of fish data that were caught in season.
     */

    private List<Data> filterOutOffSeasonFishes(List<Data> fishData) {
        return fishData.stream()
                .filter(fish -> {
                    int month = fish.getLocalDate().getMonthValue();
                    int day = fish.getLocalDate().getDayOfMonth();
                    return (month > 6 || (month == 6 && day >= 15)) && (month < 8 || (month == 8 && day <= 31));
                })
                .collect(Collectors.toList());
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

    private List<DayDTO> transformToDayDTOList(Map<String, List<Data>> fishDataByDayAndMonth) {
        List<DayDTO> fishDataInDayDTOList = new ArrayList<>();

        for (String date : fishDataByDayAndMonth.keySet()) {
            fishDataInDayDTOList.add(transformToDayDTO(fishDataByDayAndMonth.get(date)));
        }

        return fishDataInDayDTOList;
    }

    /**
     * Converts a list of fish data into a DayDTO.
     * 
     * DayDTO encapsulates the date, count, total weight, and average weight of
     * fishes
     * caught in a day.
     * 
     * @param fishData List of Data objects from same date, each representing a single fish's data.
     * @return DayDTO representing the aggregated fishing data for the day.
     */

    private DayDTO transformToDayDTO(List<Data> fishData) {
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
     * Transforms a list of fish data into a YearDTO.
     * 
     * Counts and calculates total and average weights for "Laks" and "Sjøørret",
     * and counts only total amount of "Pukkellaks".
     * 
     * @param fishes List of fish data for a specific year.
     * @return YearDTO representing the aggregated fish data for that year.
     */

    private YearDTO transformToYearDTO(List<Data> fishes) {
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
                yearDTO.setPukkelaksCount(yearDTO.getPukkelaksCount() + 1);
            }
        }
        yearDTO.setSalmonAverageWeight(yearDTO.getSalmonTotalWeight() / yearDTO.getSalmonCount());
        yearDTO.setSeatroutAverageWeight(yearDTO.getSeatroutTotalWeight() / yearDTO.getSeatroutCount());

        return yearDTO;
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
     * @param fishDataInDayDTOs A list of DayDTOs from same week, each representing a day's fish
     *                          data.
     * @return A list of WeekDTOs, each representing a week's aggregated fish data.
     */

    private List<WeekDTO> calculateWeeklyStats(List<DayDTO> fishDataInDayDTOs) {
        List<WeekDTO> weeklyStats = new ArrayList<>();

        for (int i = 0; i < fishDataInDayDTOs.size(); i += 7) {
            int endIndex = Math.min(i + 7, fishDataInDayDTOs.size());

            List<DayDTO> weekData = fishDataInDayDTOs.subList(i, endIndex);

            int count = weekData.stream().mapToInt(DayDTO::getFishCount).sum();
            double totalWeight = weekData.stream().mapToDouble(DayDTO::getTotalWeight).sum();
            double averageWeight = (count > 0) ? totalWeight / count : 0.0;
            double roundedAverageWeight = Math.round(averageWeight * 10.0) / 10.0;

            String startDate = weekData.get(0).getDate();
            String endDate = weekData.get(weekData.size() - 1).getDate();

            weeklyStats.add(new WeekDTO(startDate, endDate, count, totalWeight, roundedAverageWeight));
        }

        return weeklyStats;
    }

    /**
     * Returns the three best weeks from the list of weekly statistics.
     * 
     * @param weeklyStats A list of WeekDTOs.
     * @return A list of the three best weeks.
     */

    private List<WeekDTO> getBestWeeks(List<WeekDTO> weeklyStats) {
        weeklyStats.sort(Comparator.comparing(WeekDTO::getCount).reversed());
        return weeklyStats.stream().limit(3).collect(Collectors.toList());
    }

    /**
     * Formats a date to a string in the format "MM.dd".
     * 
     * @param date The date to format as LocalDate.
     * @return String in the format "MM.dd".
     */

    private String formatDateToMMddString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MM.dd"));
    }

    /**
     * Rounds the total and average weights of salmons and seatrouts in a YearDTO
     * to one decimal place.
     * 
     * @param yearDTO The YearDTO to be rounded.
     * @return The YearDTO with rounded values.
     */

    private YearDTO roundYearDTOValues(YearDTO yearDTO) {
        yearDTO.setSalmonTotalWeight(Math.round(yearDTO.getSalmonTotalWeight() * 10.0) / 10.0);
        yearDTO.setSalmonAverageWeight(Math.round(yearDTO.getSalmonAverageWeight() * 10.0) / 10.0);
        yearDTO.setSeatroutTotalWeight(Math.round(yearDTO.getSeatroutTotalWeight() * 10.0) / 10.0);
        yearDTO.setSeatroutAverageWeight(Math.round(yearDTO.getSeatroutAverageWeight() * 10.0) / 10.0);
        return yearDTO;
    }
}
