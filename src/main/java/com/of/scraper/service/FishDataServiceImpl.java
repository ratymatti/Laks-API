package com.of.scraper.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.of.scraper.dto.AnglerDTO;
import com.of.scraper.dto.AnglerStatsDTO;
import com.of.scraper.dto.AverageAndMedianDTO;
import com.of.scraper.dto.StatisticsDTO;
import com.of.scraper.dto.WeekDTO;
import com.of.scraper.dto.YearDTO;
import com.of.scraper.entity.Fish;
import com.of.scraper.repository.FishRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FishDataServiceImpl implements FishDataService {

    private FishRepository fishRepository;
    private FishDataProcessingService fishDataProcessingService;

    private final String[] SPECIES = { "Laks", "Sjøørret", "Pukkellaks" };

    /**
     * This method saves a list of fish data to the database.
     * 
     * @param dataList The list of fish data to save.
     * @return The list of fish data that was saved.
     */

    @Override
    public List<Fish> saveAll(List<Fish> dataList) {
        return fishRepository.saveAll(dataList);
    }

    /**
     * This method returns all fish data from the database.
     * 
     * @return A list of all fish data.
     */

    @Override
    public List<Fish> findAll() {
        return fishRepository.findAll(Sort.by("date"));
    }

    /**
     * This method returns all fish data for a given angler.
     * 
     * @param name String - The name of the angler.
     */

    @Override
    public List<Fish> findByName(String name) {
        return fishRepository.findByName(name);
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
        AnglerStatsDTO anglerStats = fishDataProcessingService
                .createAnglerStatsDTO(name, species);
        List<Fish> dataList = fishRepository
                .findByNameAndSpecies(name, species);

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
        List<Fish> fishesBySpecies = fishRepository
                .findBySpecies(species, Sort.by("date"));
        return fishDataProcessingService.getBestWeeksByYear(fishesBySpecies);
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
        List<Fish> fishesBySpecies = fishRepository
                .findBySpecies(species, Sort.by("date"));
        return fishDataProcessingService.getBestWeeksAlltime(fishesBySpecies);
    }

    /**
     * This method returns three best weeks alltime for a given species and minimum
     * weight.
     * The best weeks are determined by the total count of fishes caught that weigh
     * more than the specified weight.
     * 
     * @param species The species of fish to consider.
     * @param weight  The minimum weight of fish to consider.
     * @return A list of the three best weeks alltime in WeekDTO format.
     */

    @Override
    public List<WeekDTO> getBestBigFishWeeksAlltime(String species, double weight) {
        List<Fish> bigFishData = fishRepository
                .findBySpeciesAndMinWeight(species, weight);
        return fishDataProcessingService.getBestWeeksAlltime(bigFishData);
    }

    /**
     * This method returns the best weeks yearly for a given species and minimum
     * weight.
     * The best weeks are determined by the total count of fishes caught that weigh
     * more than the specified weight.
     * The results are grouped by year.
     * 
     * @param species The species of fish to consider.
     * @param weight  The minimum weight of fish to consider.
     * @return A map where the keys are years and the values are lists of the best
     *         weeks in WeekDTO format.
     */

    @Override
    public Map<Integer, List<WeekDTO>> getBestBigFishWeeksYearly(String species, double weight) {
        List<Fish> bigFishData = fishRepository
                .findBySpeciesAndMinWeight(species, weight);
        return fishDataProcessingService.getBestWeeksByYear(bigFishData);
    }

    /**
     * This method returns the annual statistics for all fish data.
     * The statistics are grouped by year.
     * 
     * @return A list of YearDTO objects representing the annual statistics.
     */

    @Override
    public List<YearDTO> getAnnualStatistics() {
        List<Fish> fishes = fishRepository
                .findAllByMultipleSpecies(SPECIES);
        return fishDataProcessingService.getStatistics(fishes);
    }

    /**
     * This method returns the all-time statistics for all fish data.
     * 
     * @return A StatisticsDTO object representing the all-time statistics.
     */

    @Override
    public StatisticsDTO getAlltimeStatistics() {
        List<Fish> fishes = fishRepository
                .findAllByMultipleSpecies(SPECIES);
        return fishDataProcessingService.getAlltimeStatistics(fishes);
    }

    @Override
    public Map<Integer, AverageAndMedianDTO> getAverageAndMedian() {
        List<Fish> fishes = fishRepository.findBySpecies("Laks", Sort.by("date"));
        return fishDataProcessingService.getAverageAndMedianOfFishesPerDay(fishes);
    }

}
