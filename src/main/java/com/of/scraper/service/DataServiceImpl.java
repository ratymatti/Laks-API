package com.of.scraper.service;

import java.util.List;
import java.util.Map;
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
    private DataProcessingService dataProcessingService;

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
        AnglerStatsDTO anglerStats = dataProcessingService.createAnglerStatsDTO(name, species);
        List<Data> dataList = dataRepository.findByNameAndSpecies(name, species);

        return new AnglerDTO(name, anglerStats, dataList);
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
        
        Map<Integer, List<Data>> fishesByYear = dataProcessingService.groupFishesByYear(fishesBySpecies);

        return dataProcessingService.getBestWeeksByYear(fishesByYear);
    }

}
