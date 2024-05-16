package com.of.scraper.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.of.scraper.dto.AnglerStatsDTO;
import com.of.scraper.dto.DayDTO;
import com.of.scraper.dto.SevenDayPeriod;
import com.of.scraper.dto.WeekDTO;
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
     * This method groups a list of fishes by the year they were caught.
     *
     * @param fishes The list of fishes to group.
     * @return A map where the keys are years and the values are lists of fishes
     *         caught in that year.
     */

    public Map<Integer, List<Data>> groupFishesByYear(List<Data> fishes) {
        Map<Integer, List<Data>> fishesByYear = new HashMap<>();
        for (Data fish : fishes) {
            int year = fish.getLocalDate().getYear();
            fishesByYear.computeIfAbsent(year, k -> new ArrayList<>()).add(fish);
        }
        return fishesByYear;
    }

    /**
     * This method calculates the best weeks for each year.
     * The best weeks are determined by the number of fishes caught and their total
     * weight.
     *
     * @param fishesByYear A map where the keys are years and the values are lists
     *                     of fishes caught in that year.
     * @return A map where the keys are years and the values are lists of the best
     *         seven day periods for that year.
     */

    public Map<Integer, List<SevenDayPeriod>> getBestWeeksByYear(Map<Integer, List<Data>> fishesByYear) {
        Map<Integer, List<SevenDayPeriod>> bestWeeksByYear = new TreeMap<>();

        for (Map.Entry<Integer, List<Data>> entry : fishesByYear.entrySet()) {
            int year = entry.getKey();
            List<Data> fishes = entry.getValue();

            Map<LocalDate, List<Data>> fishesByDate = fishes.stream()
                    .collect(Collectors.groupingBy(Data::getLocalDate));

            Map<LocalDate, Double> totalWeightsByDate = calculateTotalWeightsByDate(fishes);

            List<SevenDayPeriod> periods = calculatePeriods(fishes, fishesByDate, totalWeightsByDate);

            periods.sort(Comparator.comparing(SevenDayPeriod::getCount).reversed());
            List<SevenDayPeriod> bestPeriods = periods.stream().limit(3).collect(Collectors.toList());

            bestWeeksByYear.put(year, bestPeriods);
        }

        return bestWeeksByYear;
    }

    /**
     * This method calculates the total weight of fishes caught on each date.
     *
     * @param fishes The list of fishes to consider.
     * @return A map where the keys are dates and the values are the total weight of
     *         fishes caught on that date.
     */

    private Map<LocalDate, Double> calculateTotalWeightsByDate(List<Data> fishes) {
        return fishes.stream()
                .collect(Collectors.groupingBy(Data::getLocalDate, Collectors.summingDouble(Data::getWeight)));
    }

    /**
     * This method calculates the seven day periods for a list of fishes.
     * For each period, it calculates the number of fishes caught, their total
     * weight, and their average weight.
     *
     * @param fishes             The list of fishes to consider.
     * @param fishesByDate       A map where the keys are dates and the values are
     *                           lists of fishes caught on that date.
     * @param totalWeightsByDate A map where the keys are dates and the values are
     *                           the total weight of fishes caught on that date.
     * @return A list of seven day periods.
     */

    private List<SevenDayPeriod> calculatePeriods(List<Data> fishes, Map<LocalDate, List<Data>> fishesByDate,
            Map<LocalDate, Double> totalWeightsByDate) {
        List<SevenDayPeriod> periods = new ArrayList<>();

        LocalDate startDate = fishes.get(0).getLocalDate();
        LocalDate endDate = fishes.get(fishes.size() - 1).getLocalDate();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(7)) {
            LocalDate periodEndDate = date.plusDays(6);

            int count = 0;
            double totalWeight = 0.0;
            for (LocalDate currentDate = date; !currentDate.isAfter(periodEndDate); currentDate = currentDate
                    .plusDays(1)) {
                if (fishesByDate.containsKey(currentDate)) {
                    List<Data> fishesOnDate = fishesByDate.get(currentDate);
                    count += fishesOnDate.size();
                    totalWeight += totalWeightsByDate.getOrDefault(currentDate, 0.0);
                }
            }
            double averageWeight = (count > 0) ? totalWeight / count : 0.0;
            periods.add(new SevenDayPeriod(date, periodEndDate, count, totalWeight, averageWeight));
        }

        return periods;
    }

    /**
     * Calculates and returns the top three weeks with the highest fish count from
     * the provided fish data.
     * 
     * The method first filters out off-season fishes, then groups the remaining
     * data by day and month.
     * The daily data is transformed into DayDTOs, which are then aggregated into
     * weekly statistics.
     * Finally, the top three weeks with the highest fish count are determined and
     * returned.
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

        for (Data fish : fishData) {
            String dayAndMonth = formatDateToMMddString(fish.getLocalDate());
            if (!fishDataByDayAndMonth.containsKey(dayAndMonth)) {
                fishDataByDayAndMonth.put(dayAndMonth, new ArrayList<>());
            }
            fishDataByDayAndMonth.get(dayAndMonth).add(fish);
        }
        return fishDataByDayAndMonth;
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
     * @param fishData List of Data objects, each representing a single fish's data.
     * @return DayDTO representing the aggregated fishing data for the day.
     */

    private DayDTO transformToDayDTO(List<Data> fishData) {
        String date = formatDateToMMddString(fishData.get(0).getLocalDate());
        int count = fishData.size();
        double totalWeight = fishData.stream().mapToDouble(Data::getWeight).sum();
        double averageWeight = (count > 0) ? totalWeight / count : 0.0;

        return new DayDTO(date, count, totalWeight, averageWeight);
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
     * @param fishDataInDayDTOs A list of DayDTOs, each representing a day's fish
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
            double roundedAverageWeight = Math.round(averageWeight * 100.0) / 100.0;

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
}
