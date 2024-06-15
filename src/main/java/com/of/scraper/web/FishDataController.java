package com.of.scraper.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.of.scraper.dto.AverageAndMedianDTO;
import com.of.scraper.dto.StatisticsDTO;
import com.of.scraper.dto.WeekDTO;
import com.of.scraper.dto.YearDTO;
import com.of.scraper.service.FishDataService;
import com.of.scraper.service.ScraperService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/fish")
public class FishDataController {

    FishDataService fishDataService;
    ScraperService scraperService;

    /**
     * Retrieves the best weeks for each year for the given species. Best weeks are defined as
     * the three best weeks in the year. The best week is defined as the weeks with the highest
     * count of fish. Weeks always start on first day of the season for more accurate results.
     *
     * @param  species     the species to retrieve the best weeks for, Laks or Sjøørret basically
     * @return             a map of integers representing years to a list of WeekDTO objects
     */

    @GetMapping("/getBestWeeks/{species}")
    public ResponseEntity<Map<Integer, List<WeekDTO>>> getBestWeeksYearly(@PathVariable String species) {
        Map<Integer, List<WeekDTO>> data = fishDataService.getBestWeeksYearly(species);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Retrieves the best weeks for each year for all time. Best weeks are defined as the three best
     * weeks all time. The best weeks are defined as the weeks with the highest count of fish.
     * Weeks always start on first day of the season for more accurate results.
     * 
     * @param species the species to retrieve the best weeks for Laks or Sjøørret basically
     * @return a list of WeekDTO objects
     */

    @GetMapping("/getBestWeeksAlltime/{species}")
    public ResponseEntity<List<WeekDTO>> getBestWeeksAlltime(@PathVariable String species) {
        List<WeekDTO> data = fishDataService.getBestWeeksAlltime(species);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Retrieves the best weeks for each year for the given species and weight. Best weeks are defined as
     * the three best weeks in the year. The best week is defined as the weeks with the highest
     * count of fish with the given minimum weight. Weeks always start on first day of the season for
     * more accurate results.
     *
     * @param  species     the species to retrieve the best weeks for, Laks or Sjøørret basically
     * @param  weight      the minimum weight of fish to consider, 10kg for Laks and 5kg for Sjøørret
     *                     is preferred, but you can change that if you are interested in bait fish.
     * @return             a map of integers representing years to a list of WeekDTO objects
     */

    @GetMapping("/getBestBigFishWeeksYearly/{species}/{weight}")
    public ResponseEntity<Map<Integer, List<WeekDTO>>> getBestBigFishWeeksYearly(@PathVariable String species, @PathVariable double weight) {
        Map<Integer, List<WeekDTO>> data = fishDataService.getBestBigFishWeeksYearly(species, weight);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    /**
     * Retrieves the best weeks for each year for all time for the given species and weight. Best weeks
     * are defined as the three best weeks all time. The best weeks are defined as the weeks with the
     * highest count of fish. Weeks always start on first day of the season for more accurate results.
     * 
     * @param species the species to retrieve the best weeks for Laks or Sjøørret basically
     * @param weight  the minimum weight of fish to consider, 10kg for Laks and 5kg for Sjøørret
     *                is preferred but you can change that if you are interested in bait fish.
     * @return a list of WeekDTO objects
     */

    @GetMapping("/getBestBigFishWeeksAlltime/{species}/{weight}")
    public ResponseEntity<List<WeekDTO>> getBestBigFishWeeksAlltime(@PathVariable String species, @PathVariable double weight) {
        List<WeekDTO> data = fishDataService.getBestBigFishWeeksAlltime(species, weight);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Retrieves the annual statistics for all fish data, off season fishes are also included.
     * The statistics are grouped by year. Data contains count, total weight, average weight
     * for salmon and seatrout and count for pukkellaks. This data can be used later for
     * visualization and analysis.
     * 
     * @return a list of YearDTO objects.
     */

    @GetMapping("/getAnnualStatistics")
    public ResponseEntity<List<YearDTO>> getAnnualStatistics() {
        List<YearDTO> data = fishDataService.getAnnualStatistics();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Retrieves the all-time statistics for all fish data, off season fishes are also included
     * Data contains count, total weight, average weight for salmon and seatrout and count for pukkellaks.
     * This data can be used later for visualization and analysis.
     *  
     * @return a StatisticsDTO object
     */

    @GetMapping("/getAlltimeStatistics")
    public ResponseEntity<StatisticsDTO> getAlltimeStatistics() {
        StatisticsDTO data = fishDataService.getAlltimeStatistics();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Calculates the average and median counts of in-season salmon for each year.
     * This can be used later for analysis. 
     * 
     * @return a map of integers representing years to an AverageAndMedianDTO object.
     */

    @GetMapping("/getAverageAndMedian")
    public ResponseEntity<Map<Integer, AverageAndMedianDTO>> getAverageAndMedian() {
        Map<Integer, AverageAndMedianDTO> data = fishDataService.getAverageAndMedian();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
