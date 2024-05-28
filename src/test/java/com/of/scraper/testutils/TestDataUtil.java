package com.of.scraper.testutils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.of.scraper.dto.DayDTO;
import com.of.scraper.dto.WeekDTO;
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

    public static List<DayDTO> createDayDTOtestDataList() {
        DayDTO dayDTO1 = new DayDTO("01.01", 10, 100.0, 10.0);
        DayDTO dayDTO2 = new DayDTO("01.02", 20, 200.0, 10.0);
        DayDTO dayDTO3 = new DayDTO("01.03", 30, 300.0, 10.0);

        return Arrays.asList(dayDTO1, dayDTO2, dayDTO3);
    }

    /**
     * Creates a list of Data objects with off-season fishes.
     * 
     * @return List of Data objects with two off-season fishes and three in-season
     *         fishes.
     */

    public static List<Data> createDataListWithOffSeasonFishes() {
        List<Data> fishData = new ArrayList<>();

        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 18)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 18)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 18)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 5, 19)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 9, 20)));

        return fishData;
    }

    /**
     * Creates a list of WeekDTOs with test data.
     * 
     * @param complete If true, returns five WeekDTOs with count as 50, 40, 30, 20
     *                 and 10,
     *                 if false, returns only two WeekDTOs with count as 50 and 40.
     * 
     * @return List of WeekDTOs
     */

    public static List<WeekDTO> createWeekDTOListWithTestData(boolean complete) {
        List<WeekDTO> weekDTOList = new ArrayList<>();

        weekDTOList.add(createWeekDTO(50));
        weekDTOList.add(createWeekDTO(40));

        if (!complete) {
            return weekDTOList;
        }

        weekDTOList.add(createWeekDTO(30));
        weekDTOList.add(createWeekDTO(20));
        weekDTOList.add(createWeekDTO(10));

        return weekDTOList;
    }

    private static WeekDTO createWeekDTO(int count) {
        WeekDTO weekDTO = new WeekDTO();
        weekDTO.setCount(count);
        return weekDTO;
    }

    public static List<Data> createTestDataForAverageAndMedianTest() {
        List<Data> testData = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            for (int j = 0; j < i; j++) {
                testData.add(new Data("Laks", 10.0, LocalDate.of(2020, 7, i)));
            }
        }

        return testData;
    }

    public static void main(String[] args) {
        
    }
}
