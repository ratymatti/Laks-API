package com.of.scraper.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        List<Data> data = scraperService.scrapeData();

    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<Data>> getByName(@PathVariable String name) {
        List<Data> data = dataService.findByName(name);
        double totalWeight = data.stream()
            .filter(Data -> Data.getSpecies().equals("Laks"))
            .mapToDouble(Data::getWeight)
            .sum();
        System.out.println("Total weight: " + totalWeight);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Data> addNew(@RequestBody Data data) {
        Data newData = dataService.save(data);
        return new ResponseEntity<>(newData, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAll() {
        dataService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
