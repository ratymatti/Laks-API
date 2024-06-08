package com.of.scraper.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.of.scraper.entity.Fish;
import com.of.scraper.service.FishDataService;
import com.of.scraper.service.ScraperService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    FishDataService fishDataService;
    ScraperService scraperService;
    
    @GetMapping("/scrapeAndSave")
    public void scrapeAndSave() {
        scraperService.scrapeData();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Fish>> getAll() {
        List<Fish> data = fishDataService.findAll();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
