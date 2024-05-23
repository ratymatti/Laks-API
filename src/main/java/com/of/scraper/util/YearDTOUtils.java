package com.of.scraper.util;

import java.util.Map;

import com.of.scraper.dto.YearDTO;

public class YearDTOUtils {

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
