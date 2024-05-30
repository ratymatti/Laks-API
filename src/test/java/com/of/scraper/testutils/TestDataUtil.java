package com.of.scraper.testutils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.of.scraper.dto.DayDTO;
import com.of.scraper.dto.WeekDTO;
import com.of.scraper.entity.Fish;
import com.of.scraper.util.CalculationUtils;

/**
 * Class TestDataUtil
 * 
 * Contains methods:
 * 
 * @createTestData - returns List containing necessary Data entities
 */

public class TestDataUtil {

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

    public static List<Fish> createDataListWithOffSeasonFishes() {
        List<Fish> fishData = new ArrayList<>();

        fishData.add(new Fish("Laks", 10.0, LocalDate.of(2022, 6, 18)));
        fishData.add(new Fish("Laks", 10.0, LocalDate.of(2022, 6, 18)));
        fishData.add(new Fish("Laks", 10.0, LocalDate.of(2022, 6, 18)));
        fishData.add(new Fish("Laks", 10.0, LocalDate.of(2022, 5, 19)));
        fishData.add(new Fish("Laks", 10.0, LocalDate.of(2022, 9, 20)));

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

    public static List<Fish> createTestData(int year, int numOfYears) {
        List<Fish> testData = new ArrayList<>();

        for (int i = 0; i < numOfYears; i++) {
            for (int j = 1; j <= 30; j++) {
                for (int k = 0; k < j; k++) {
                    testData.add(new Fish("Laks", 10.0, LocalDate.of(year, 7, j)));
                }
            }
            year++;
        }
        return testData;
    }

    public static List<Fish> createRandomTestData() {
        List<Fish> testData = new ArrayList<>();

        for (int i = 1; i <= 1000; i++) {
            double randomWeight = createRandomWeight();
            int randomDay = createRandomDay();
            int randomYear = createRandomYear();
            int randomMonth = createRandomMonth();
            testData.add(new Fish("Laks", randomWeight, LocalDate.of(randomYear, randomMonth, randomDay)));
        }
        return testData;
    }

    private static int createRandomMonth() {
        Random rand = new Random();
        return rand.nextInt(8 - 6 + 1) + 6;
    }

    private static int createRandomYear() {
        Random rand = new Random();
        return rand.nextInt(2020 - 2015 + 1) + 2015;
    }

    private static int createRandomDay() {
        return (int) Math.floor(Math.random() * 30) + 1;
    }

    private static double createRandomWeight() {
        return CalculationUtils.roundToTwoDecimals(Math.random() * 20);
    }

}
