package com.of.scraper.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.of.scraper.dto.AnglerDTO;
import com.of.scraper.dto.AnglerStatsDTO;
import com.of.scraper.dto.SevenDayPeriod;
import com.of.scraper.entity.Data;
import com.of.scraper.repository.DataRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DataServiceImpl implements DataService {

    private DataRepository dataRepository;

    @Override
    public List<Data> saveAll(List<Data> dataList) {
        return dataRepository.saveAll(dataList);
    }

    @Override
    public List<Data> findAll() {
        return dataRepository.findAll();
    }

    @Override
    public List<Data> findByName(String name) {
        return dataRepository.findByName(name);
    }

    /**
     * Finds the angler data by name and species.
     *
     * @param name    the name of the angler
     * @param species the species of fish
     * @return the angler data
     */

    @Override
    public AnglerDTO findByNameAndSpecies(String name, String species) {
        AnglerStatsDTO anglerStats = createAnglerStatsDTO(name, species);
        List<Data> dataList = dataRepository.findByNameAndSpecies(name, species);

        return new AnglerDTO(name, anglerStats, dataList);
    }

    /**
     * Creates an AnglerStatsDTO for the given name and species.
     *
     * @param name    the name of the angler
     * @param species the species of fish
     * @return the angler stats
     */

    protected AnglerStatsDTO createAnglerStatsDTO(String name, String species) {
        int count = dataRepository.getCountByNameAndSpecies(name, species);
        double totalWeight = dataRepository.getTotalWeightByNameAndSpecies(name, species);
        double averageWeight = (count > 0) ? totalWeight / count : 0;

        return new AnglerStatsDTO(name, count, totalWeight, averageWeight);
    }

    /**
     * This method returns the best weeks for a given species.
     * The best weeks are determined by the number of fishes caught and their total
     * weight.
     *
     * @param species The species of fish to consider.
     * @return A map where the keys are years and the values are lists of the best
     *         seven day periods for that year.
     */

    @Override
    public Map<Integer, List<SevenDayPeriod>> getBestWeeks(String species) {
        List<Data> fishesBySpecies = dataRepository.findBySpecies(species, Sort.by("localDate"));

        Map<Integer, List<Data>> fishesByYear = groupFishesByYear(fishesBySpecies);

        return getBestWeeksByYear(fishesByYear);
    }

    /**
     * This method groups a list of fishes by the year they were caught.
     *
     * @param fishes The list of fishes to group.
     * @return A map where the keys are years and the values are lists of fishes
     *         caught in that year.
     */

    private Map<Integer, List<Data>> groupFishesByYear(List<Data> fishes) {
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

    private Map<Integer, List<SevenDayPeriod>> getBestWeeksByYear(Map<Integer, List<Data>> fishesByYear) {
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

}
