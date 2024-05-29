package com.of.scraper.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.of.scraper.dto.YearDTO;
import com.of.scraper.entity.Fish;

public class YearDTOUtils {

    /**
     * Transforms a list of fish data into a YearDTO.
     * 
     * Counts and calculates total and average weights for "Laks" and "Sjøørret",
     * and counts only total amount of "Pukkellaks".
     * 
     * @param fishes List of fish data for a specific year.
     * @return YearDTO representing the aggregated fish data for that year.
     */

    public static YearDTO transformToYearDTO(List<Fish> fishes) {
        YearDTO yearDTO = new YearDTO(fishes.get(0).getDate().getYear());

        Map<String, Integer> countMap = new HashMap<>();
        Map<String, Double> weightMap = new HashMap<>();

        for (Fish fish : fishes) {
            String species = fish.getSpecies();
            countMap.put(species, countMap.getOrDefault(species, 0) + 1);

            if (!species.equals("Pukkellaks")) {
                weightMap.put(species, weightMap.getOrDefault(species, 0.0) + fish.getWeight());
            }
        }
        YearDTOUtils.handleSetCounts(yearDTO, countMap, weightMap);
        YearDTOUtils.handleSetAverages(yearDTO);

        return yearDTO;
    }

    /**
     * Sets the counts and total weights of salmon, seatrout, and pukkellaks
     * in the provided YearDTO.
     * 
     * @param yearDTO The YearDTO to update.
     * @param countMap A map containing the counts of salmon, seatrout, and pukkellaks.
     * @param weightMap A map containing the total weights of salmon and seatrout.
     */
   
    public static void handleSetCounts(YearDTO yearDTO, Map<String, Integer> countMap, Map<String, Double> weightMap) {
        yearDTO.setSalmonCount(countMap.getOrDefault("Laks", 0));
        yearDTO.setSalmonTotalWeight(weightMap.getOrDefault("Laks", 0.0));
        yearDTO.setSeatroutCount(countMap.getOrDefault("Sjøørret", 0));
        yearDTO.setSeatroutTotalWeight(weightMap.getOrDefault("Sjøørret", 0.0));
        yearDTO.setPukkellaksCount(countMap.getOrDefault("Pukkellaks", 0));
    }

    /**
     * Calculates and sets the average weights of salmon and seatrout
     * in the provided YearDTO.
     * 
     * @param yearDTO The YearDTO to update.
     */
    
    public static void handleSetAverages(YearDTO yearDTO) {
        yearDTO.setSalmonAverageWeight(CalculationUtils
                .calculateAverageWeight(
                        yearDTO.getSalmonCount(), yearDTO.getSalmonTotalWeight()));
        yearDTO.setSeatroutAverageWeight(CalculationUtils
                .calculateAverageWeight(
                        yearDTO.getSeatroutCount(), yearDTO.getSeatroutTotalWeight()));
    }
}
