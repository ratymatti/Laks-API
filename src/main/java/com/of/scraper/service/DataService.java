package com.of.scraper.service;

import java.util.List;

import com.of.scraper.dto.AnglerDTO;
import com.of.scraper.entity.Data;

public interface DataService {
    List<Data> saveAll(List<Data> dataList);
    List<Data> findAll();
    List<Data> findByName(String name);
    AnglerDTO findByNameAndSpecies(String name, String species);
}
