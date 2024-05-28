package com.of.scraper.service;

import java.util.List;
import java.util.Map;

import com.of.scraper.dto.AnglerDTO;
import com.of.scraper.dto.AverageAndMedianDTO;
import com.of.scraper.dto.StatisticsDTO;
import com.of.scraper.dto.WeekDTO;
import com.of.scraper.dto.YearDTO;
import com.of.scraper.entity.Data;

public interface DataService {
    List<Data> saveAll(List<Data> dataList);
    List<Data> findAll();
    List<Data> findByName(String name);
    AnglerDTO findByNameAndSpecies(String name, String species);
    Map<Integer, List<WeekDTO>> getBestWeeksYearly(String species);
    List<WeekDTO> getBestWeeksAlltime(String species);
    List<WeekDTO> getBestBigFishWeeksAlltime(String species, double weight);
    Map<Integer, List<WeekDTO>> getBestBigFishWeeksYearly(String species, double weight);
    List<YearDTO> getAnnualStatistics();
    StatisticsDTO getAlltimeStatistics();
    Map<Integer, AverageAndMedianDTO> getAverageAndMedian();
}
