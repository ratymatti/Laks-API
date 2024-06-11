package com.of.scraper.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.of.scraper.dto.AnglerDTO;
import com.of.scraper.entity.Fish;
import com.of.scraper.service.FishDataService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/angler")
public class AnglerDataController {

    FishDataService fishDataService;
    
    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<Fish>> getByName(@PathVariable String name) {
        List<Fish> data = fishDataService.findByName(name);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/getAllByNameAndSpecies/{name}/{species}")
    public ResponseEntity<AnglerDTO> getAllByNameAndSpecies(@PathVariable String name, @PathVariable String species) {
        AnglerDTO angler = fishDataService.findByNameAndSpecies(name, species);
        return new ResponseEntity<>(angler, HttpStatus.OK);
    }

    @GetMapping("/getAnglerData/{species}")
    public void getAnglerData(@PathVariable String species) {
        fishDataService.getAnglerData(species);
    }
}
