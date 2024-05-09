package com.of.scraper.web;

import org.springframework.web.bind.annotation.GetMapping;
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
        Data data = scraperService.scrapeData();    
        dataService.saveData(data);
    }
    
}
