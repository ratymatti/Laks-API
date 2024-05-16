package com.of.scraper.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.of.scraper.dto.AnglerDTO;
import com.of.scraper.dto.AnglerStatsDTO;
import com.of.scraper.dto.WeekDTO;
import com.of.scraper.entity.Data;
import com.of.scraper.repository.DataRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DataServiceImpl implements DataService {

    private DataRepository dataRepository;
    private DataProcessingService dataProcessingService;

    /**
     * This method saves a list of fish data to the database.
     * 
     * @param dataList The list of fish data to save.
     * @return The list of fish data that was saved.
     */

    @Override
    public List<Data> saveAll(List<Data> dataList) {
        return dataRepository.saveAll(dataList);
    }

    /**
     * This method returns all fish data from the database.
     * 
     * @return A list of all fish data.
     */

    @Override
    public List<Data> findAll() {
        return dataRepository.findAll();
    }

    /**
     * This method returns all fish data for a given angler.
     * 
     * @param name String - The name of the angler.
     */

    @Override
    public List<Data> findByName(String name) {
        return dataRepository.findByName(name);
    }

    /**
     * This method returns the angler data by name and species
     * as AnglerDTO.
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
     * The best weeks are determined by the number of fishes caught.
     *
     * @param species The species of fish to consider.
     * @return A map where the keys are years and the values are lists of the best
     *         seven day periods for that year.
     */

    @Override
    public Map<Integer, List<WeekDTO>> getBestWeeksYearly(String species) {
        List<Data> fishesBySpecies = dataRepository.findBySpecies(species, Sort.by("localDate"));
        return dataProcessingService.getBestWeeksByYear(fishesBySpecies);
    }

    /**
     * This method returns three best weeks alltime for a given species.
     * The best weeks are determined by the total count of fishes caught.
     * 
     * @param species The species of fish to consider.
     * @return A list of the three best weeks alltime in WeekDTO format.
     */

    @Override
    public List<WeekDTO> getBestWeeksAlltime(String species) {
        List<Data> fishesBySpecies = dataRepository.findBySpecies(species, Sort.by("localDate"));
        return dataProcessingService.getBestWeeksAlltime(fishesBySpecies);
    }

}
