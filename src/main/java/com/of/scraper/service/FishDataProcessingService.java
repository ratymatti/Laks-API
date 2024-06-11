package com.of.scraper.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.of.scraper.dto.AnglerStatsDTO;
import com.of.scraper.dto.AverageAndMedianDTO;
import com.of.scraper.dto.DayDTO;
import com.of.scraper.dto.StatisticsDTO;
import com.of.scraper.dto.WeekDTO;
import com.of.scraper.dto.YearDTO;
import com.of.scraper.entity.Fish;
import com.of.scraper.repository.FishRepository;
import com.of.scraper.util.AverageAndMedianDTOUtils;
import com.of.scraper.util.CalculationUtils;
import com.of.scraper.util.DayDTOUtils;
import com.of.scraper.util.FilteringUtils;
import com.of.scraper.util.GroupingUtils;
import com.of.scraper.util.StatisticsDTOUtils;
import com.of.scraper.util.WeekDTOUtils;
import com.of.scraper.util.YearDTOUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FishDataProcessingService {

    private FishRepository fishRepository;

    /**
     * Creates an AnglerStatsDTO for the given name and species.
     *
     * @param name    the name of the angler
     * @param species the species of fish
     * @return the angler stats
     */

    public AnglerStatsDTO createAnglerStatsDTO(String name, String species) {
        int count = fishRepository
                .getCountByNameAndSpecies(name, species);
        double totalWeight = fishRepository
                .getTotalWeightByNameAndSpecies(name, species);
        double averageWeight = CalculationUtils
                .calculateAverageWeight(count, totalWeight);

        return new AnglerStatsDTO(
                name,
                count,
                CalculationUtils.roundToTwoDecimals(totalWeight),
                CalculationUtils.roundToTwoDecimals(averageWeight));
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

    public Map<Integer, List<WeekDTO>> getBestWeeksByYear(List<Fish> fishData) {
        Map<Integer, List<Fish>> fishDataByYear = GroupingUtils
                .groupByYear(FilteringUtils.filterOutOffSeasonFishes(fishData));

        Map<Integer, List<WeekDTO>> bestWeeksByYear = new TreeMap<>();

        for (List<Fish> fishesByYear : fishDataByYear.values()) {
            Map<String, List<Fish>> fishDataByDayAndMonth = GroupingUtils
                    .groupByDayAndMonth(fishesByYear);

            List<DayDTO> fishDataInDayDTOList = DayDTOUtils
                    .transformToDayDTOList(fishDataByDayAndMonth);

            bestWeeksByYear.put(
                    fishesByYear.get(0).getDate().getYear(),
                    FilteringUtils.getBestWeeks(
                            WeekDTOUtils.transformToWeekDTOList(fishDataInDayDTOList)));
        }

        return bestWeeksByYear;
    }

    /**
     * Calculates and returns the top three weeks with the highest fish count from
     * the provided fish data.
     * 
     * @param fishData List of Fish objects, each representing a single fish's data.
     * @return List of the top three WeekDTOs, each representing a week's aggregated
     *         fish data.
     */

    public List<WeekDTO> getBestWeeksAlltime(List<Fish> fishData) {
        Map<String, List<Fish>> fishDataByDayAndMonth = GroupingUtils
                .groupByDayAndMonth(FilteringUtils
                        .filterOutOffSeasonFishes(fishData));

        List<DayDTO> fishDataInDayDTOList = DayDTOUtils
                .transformToDayDTOList(fishDataByDayAndMonth);

        return FilteringUtils
                .getBestWeeks(WeekDTOUtils
                        .transformToWeekDTOList(fishDataInDayDTOList));
    }

    /**
     * Calculates and returns yearly fishing statistics as a list of YearDTO
     * objects.
     * 
     * The method groups the fish data by year using the groupByYear method. For
     * each year's data, it creates a YearDTO object using the transformToYearDTO
     * method, which includes counts and total weights for each species of fish.
     * 
     * The weights in the YearDTO are then rounded to one decimal place using the
     * roundYearDTOValues method. The rounded YearDTO is added to the result list.
     * 
     * @param fishData List of Fish objects, each representing a single fish's data.
     * @return List of YearDTO objects, each representing a year's aggregated fish
     *         data.
     */

    public List<YearDTO> getStatistics(List<Fish> fishData) {
        Map<Integer, List<Fish>> fishesByYear = GroupingUtils.groupByYear(fishData);

        List<YearDTO> result = new ArrayList<>();

        for (List<Fish> year : fishesByYear.values()) {
            YearDTO yearDTO = YearDTOUtils.transformToYearDTO(year);

            result.add(YearDTOUtils.roundYearDTOValues(yearDTO));
        }

        return result;
    }

    /**
     * Calculates and returns all-time fishing statistics as a StatisticsDTO object.
     * 
     * @param fishData List of Data objects, each representing a single fish's data.
     * @return StatisticsDTO object representing all-time aggregated fish data.
     */

    public StatisticsDTO getAlltimeStatistics(List<Fish> fishData) {
        return StatisticsDTOUtils.transformToStatisticsDTO(getStatistics(fishData));
    }

    /**
     * Calculates and returns all-time in-season salmon fishing statistics as Map
     * where
     * key represents year and values are AverateAndMedianDTO from that year.
     * 
     * @param fishData List of all salmons caught
     * @return Map with AverageAndMedianDTO values
     */

    public Map<Integer, AverageAndMedianDTO> getAverageAndMedianOfFishesPerDay(List<Fish> fishData) {
        Map<Integer, List<Fish>> fishesByYear = GroupingUtils
                .groupByYear(FilteringUtils
                        .filterOutOffSeasonFishes(fishData));

        return AverageAndMedianDTOUtils.transformToAverageAndMedianDTOMap(fishesByYear);
    }

    public List<AnglerStatsDTO> getAnglerStatistics(List<Fish> fishData) {
        Map<String, AnglerStatsDTO> anglerStatsMap = new HashMap<>();

        for (Fish fish : fishData) {
            if (fish.getName().equals("N/A")) {
                continue;
            }
            if (!anglerStatsMap.containsKey(fish.getName())) {
                anglerStatsMap.put(fish.getName(), new AnglerStatsDTO());
            }
            AnglerStatsDTO updatedStats = updateAnglerStats(anglerStatsMap.get(fish.getName()), fish);
            anglerStatsMap.put(fish.getName(), updatedStats);
        }
        List<AnglerStatsDTO> anglerStatsList = new ArrayList<>();

        for (AnglerStatsDTO stats : anglerStatsMap.values()) {
            anglerStatsList.add(stats);
        }

        return anglerStatsList.stream()
                .sorted(Comparator.comparing(AnglerStatsDTO::getCount).reversed())
                .collect(Collectors.toList());
    }

    private AnglerStatsDTO updateAnglerStats(AnglerStatsDTO anglerStats, Fish fish) {
        if (anglerStats.getName() == null) {
            anglerStats.setName(fish.getName());
        }
        int currentCount = anglerStats.getCount();
        anglerStats.setCount(currentCount + 1);
        anglerStats.setTotalWeight(anglerStats.getTotalWeight() + fish.getWeight());
        anglerStats.setAverageWeight(CalculationUtils
                .roundToTwoDecimals(CalculationUtils
                        .calculateAverageWeight(++currentCount, anglerStats.getTotalWeight())));
        return anglerStats;
    }
}
