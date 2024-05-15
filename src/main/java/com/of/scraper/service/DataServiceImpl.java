package com.of.scraper.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

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

    @Override
    public Map<Integer, List<SevenDayPeriod>> getBestWeeks(String species) {
        List<Data> fishesBySpecies = dataRepository.findBySpecies(species);
        fishesBySpecies.sort(Comparator.comparing(Data::getLocalDate));

        Map<Integer, List<Data>> fishesByYear = new HashMap<>();

        for (Data fish : fishesBySpecies) {
            int year = fish.getLocalDate().getYear();
            if (!fishesByYear.containsKey(year)) {
                fishesByYear.put(year, new ArrayList<>());
            }
            fishesByYear.get(year).add(fish);
        }

        Map<Integer, List<SevenDayPeriod>> bestWeeksByYear = getBestWeeksByYear(fishesByYear);
        
        return bestWeeksByYear;
    }

    private Map<Integer, List<SevenDayPeriod>> getBestWeeksByYear(Map<Integer, List<Data>> fishesByYear) {
        Map<Integer, List<SevenDayPeriod>> bestWeeksByYear = new HashMap<>();

        for (Map.Entry<Integer, List<Data>> entry : fishesByYear.entrySet()) {
            int year = entry.getKey();
            List<Data> fishes = entry.getValue();

            Map<LocalDate, List<Data>> fishesByDate = fishes.stream()
                .collect(Collectors.groupingBy(Data::getLocalDate));

            List<SevenDayPeriod> periods = new ArrayList<>();

            LocalDate startDate = fishes.get(0).getLocalDate();
            LocalDate endDate = fishes.get(fishes.size() - 1).getLocalDate();

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(7)) {
                LocalDate periodEndDate = date.plusDays(6);

                int count = 0;
                for (LocalDate currentDate = date; !currentDate.isAfter(periodEndDate); currentDate = currentDate.plusDays(1)) {
                    if (fishesByDate.containsKey(currentDate)) {
                        count += fishesByDate.get(currentDate).size();
                    }
                }
                periods.add(new SevenDayPeriod(date, periodEndDate, count, 0.0, 0.0));
            }

            periods.sort(Comparator.comparing(SevenDayPeriod::getCount).reversed());
            List<SevenDayPeriod> bestPeriods = periods.stream().limit(3).collect(Collectors.toList());

            bestWeeksByYear.put(year, bestPeriods);
        }

        return bestWeeksByYear;
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

}
