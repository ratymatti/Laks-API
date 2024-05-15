package com.of.scraper.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.of.scraper.dto.AnglerDTO;
import com.of.scraper.dto.SevenDayPeriod;
import com.of.scraper.entity.Data;
import com.of.scraper.service.DataService;
import com.of.scraper.service.ScraperService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class DataController {

    DataService dataService;
    ScraperService scraperService;

    @GetMapping("/scrapeAndSave")
    public void scrapeAndSave() {
        scraperService.scrapeData();
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<Data>> getByName(@PathVariable String name) {
        List<Data> data = dataService.findByName(name);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/getAllByNameAndSpecies/{name}/{species}")
    public ResponseEntity<AnglerDTO> getAllByNameAndSpecies(@PathVariable String name, @PathVariable String species) {
        AnglerDTO angler = dataService.findByNameAndSpecies(name, species);
        return new ResponseEntity<>(angler, HttpStatus.OK);
    }

    @GetMapping("/getBestWeeks/{species}")
    public ResponseEntity<Map<Integer, List<SevenDayPeriod>>> getBestWeeks(@PathVariable String species) {
        Map<Integer, List<SevenDayPeriod>> data = dataService.getBestWeeks(species);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
