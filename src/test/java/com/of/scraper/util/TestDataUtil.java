package com.of.scraper.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.of.scraper.entity.Data;

/**
 * Class TestDataUtil
 * 
 * Contains methods:
 * 
 * @createTestData - returns List containing necessary Data entities
 */

public class TestDataUtil {
    
    public static List<Data> createTestData() {
        List<Data> fishData = new ArrayList<>();
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 18)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 18)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 18)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 19)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 20)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 21)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 21)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 21)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 1)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 2)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 3)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 4)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 5)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 5)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 5)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 1)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 2)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 3)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 4)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 1)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 2)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 3)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 4)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 5)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 5)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 5)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 1)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 2)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 3)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 4)));
        return fishData;
    }
}
