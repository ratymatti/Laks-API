package com.of.scraper.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.of.scraper.dto.AverageAndMedianDTO;
import com.of.scraper.entity.Fish;

public class AverageAndMedianDTOUtils {
    /**
     * Transforms daily counts from year to average and median from that specific
     * year.
     * 
     * @param dailyCounts Map where key is date in format MM/dd and value is count
     *                    from that date.
     * @return AverageAndMedianDTO with average count and median count calculated
     *         from those values.
     */

    public static AverageAndMedianDTO transformToAverageAndMedianDTO(Map<String, Integer> dailyCounts) {
        List<Integer> counts = new ArrayList<>(dailyCounts.values());

        double average = CalculationUtils
                .roundToTwoDecimals(CalculationUtils
                        .calculateAverageAmount(CalculationUtils
                                .calculateCount(counts, Integer::intValue), counts.size()));

        double median = CalculationUtils.calculateMedianAmount(counts);

        return new AverageAndMedianDTO(average, median);
    }

    /**
     * Transforms fishes from every year to AverageAndMedianDTOs
     * 
     * @param fishesByYear Map where keys represents a year and
     *                     values are Lists of all fishes from that specific year.
     * @return Map where key represents a year and value is AverageAndMedianDTO from
     *         that year.
     */

    public static Map<Integer, AverageAndMedianDTO> transformToAverageAndMedianDTOMap(
            Map<Integer, List<Fish>> fishesByYear) {
        Map<Integer, AverageAndMedianDTO> fishCountAverageAndMedian = new TreeMap<>();

        for (List<Fish> year : fishesByYear.values()) {
            AverageAndMedianDTO averageAndMedian = transformToAverageAndMedianDTO(CalculationUtils
                    .calculateDailyCounts(year));

            fishCountAverageAndMedian.put(year.get(0).getDate().getYear(),
                    averageAndMedian);
        }

        return fishCountAverageAndMedian;
    }
}
