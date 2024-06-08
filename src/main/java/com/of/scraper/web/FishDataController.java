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

    @GetMapping("/getBestWeeks/{species}")
    public ResponseEntity<Map<Integer, List<WeekDTO>>> getBestWeeksYearly(@PathVariable String species) {
        Map<Integer, List<WeekDTO>> data = fishDataService.getBestWeeksYearly(species);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/getBestWeeksAlltime/{species}")
    public ResponseEntity<List<WeekDTO>> getBestWeeksAlltime(@PathVariable String species) {
        List<WeekDTO> data = fishDataService.getBestWeeksAlltime(species);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/getBestBigFishWeeksAlltime/{species}/{weight}")
    public ResponseEntity<List<WeekDTO>> getBestBigFishWeeksAlltime(@PathVariable String species, @PathVariable double weight) {
        List<WeekDTO> data = fishDataService.getBestBigFishWeeksAlltime(species, weight);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/getBestBigFishWeeksYearly/{species}/{weight}")
    public ResponseEntity<Map<Integer, List<WeekDTO>>> getBestBigFishWeeksYearly(@PathVariable String species, @PathVariable double weight) {
        Map<Integer, List<WeekDTO>> data = fishDataService.getBestBigFishWeeksYearly(species, weight);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/getAnnualStatistics")
    public ResponseEntity<List<YearDTO>> getAnnualStatistics() {
        List<YearDTO> data = fishDataService.getAnnualStatistics();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/getAlltimeStatistics")
    public ResponseEntity<StatisticsDTO> getAlltimeStatistics() {
        StatisticsDTO data = fishDataService.getAlltimeStatistics();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/getAverageAndMedian")
    public ResponseEntity<Map<Integer, AverageAndMedianDTO>> getAverageAndMedian() {
        Map<Integer, AverageAndMedianDTO> data = fishDataService.getAverageAndMedian();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
