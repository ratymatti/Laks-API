package com.of.scraper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.of.scraper.dto.DayDTO;
import com.of.scraper.dto.YearDTO;
import com.of.scraper.testutils.TestDataUtil;

@ExtendWith(MockitoExtension.class)
public class CalculationUtilsTest {

    @Test
    public void testRoundYearDTOValues() {
        // Create a YearDTO with some test values
        YearDTO yearDTO = new YearDTO();
        yearDTO.setSalmonTotalWeight(123.456);
        yearDTO.setSalmonAverageWeight(123.456);
        yearDTO.setSeatroutTotalWeight(123.456);
        yearDTO.setSeatroutAverageWeight(123.456);

        // Call the method under test
        YearDTO roundedYearDTO = CalculationUtils.roundYearDTOValues(yearDTO);

        // Check that each field has been rounded correctly
        assertEquals(123.46, roundedYearDTO.getSalmonTotalWeight(), 0.01);
        assertEquals(123.46, roundedYearDTO.getSalmonAverageWeight(), 0.01);
        assertEquals(123.46, roundedYearDTO.getSeatroutTotalWeight(), 0.01);
        assertEquals(123.46, roundedYearDTO.getSeatroutAverageWeight(), 0.01);
    }

    @Test
    public void testRoundToTwoDecimals() {
        // Test with rounding down
        double testValue1 = 10.123456;
        double result1 = CalculationUtils.roundToTwoDecimals(testValue1);
        assertEquals(10.12, result1);

        // Test with rounding up
        double testValue2 = 10.126;
        double result2 = CalculationUtils.roundToTwoDecimals(testValue2);
        assertEquals(10.13, result2);

        // Test with a negative value
        double testValue3 = -10.123456;
        double result3 = CalculationUtils.roundToTwoDecimals(testValue3);
        assertEquals(-10.12, result3);

        // Test with less than two decimal places
        double testValue4 = 10.1;
        double result4 = CalculationUtils.roundToTwoDecimals(testValue4);
        assertEquals(10.1, result4);

        // Test with zero
        double testValue5 = 0;
        double result5 = CalculationUtils.roundToTwoDecimals(testValue5);
        assertEquals(0, result5);
    }

    @Test
    public void testCalculateAverageWeight() {
        // Test with normal values
        int testCount1 = 10;
        double testTotalWeight1 = 100.0;
        double expectedAverageWeight1 = 10.0;
        double result1 = CalculationUtils.calculateAverageWeight(testCount1, testTotalWeight1);
        assertEquals(expectedAverageWeight1, result1);

        // Test with count as zero
        int testCount2 = 0;
        double testTotalWeight2 = 100.0;
        double result2 = CalculationUtils.calculateAverageWeight(testCount2, testTotalWeight2);
        assertEquals(0.0, result2);

        // Test with total weight as zero
        int testCount3 = 10;
        double testTotalWeight3 = 0.0;
        double expectedAverageWeight3 = 0.0;
        double result3 = CalculationUtils.calculateAverageWeight(testCount3, testTotalWeight3);
        assertEquals(expectedAverageWeight3, result3);

        // Test with both count and total weight as zero
        int testCount4 = 0;
        double testTotalWeight4 = 0.0;
        double expectedAverageWeight4 = 0.0;
        double result4 = CalculationUtils.calculateAverageWeight(testCount4, testTotalWeight4);
        assertEquals(expectedAverageWeight4, result4);
    }

    @Test
    public void testCalculateTotalWeight() {
        // Create a list of DayDTOs with some test values
        List<DayDTO> testData1 = TestDataUtil.createDayDTOtestDataList();
        List<DayDTO> testData2 = new ArrayList<>();

        // Call the method under test
        double result1 = CalculationUtils.calculateTotalWeight(testData1, DayDTO::getTotalWeight);
        double result2 = CalculationUtils.calculateTotalWeight(testData2, DayDTO::getTotalWeight);

        // Check that the total weight has been calculated correctly
        assertEquals(600.0, result1);
        assertEquals(0.0, result2);
    }

    @Test
    public void testCalculateCount() {
        // Create a list of DayDTOs with some test values
        List<DayDTO> testData1 = TestDataUtil.createDayDTOtestDataList();
        List<DayDTO> testData2 = new ArrayList<>();

        // Call the method under test
        int result1 = CalculationUtils.calculateCount(testData1, DayDTO::getFishCount);
        int result2 = CalculationUtils.calculateCount(testData2, DayDTO::getFishCount);

        // Check that the count has been calculated correctly
        assertEquals(60, result1);
        assertEquals(0, result2);
    }

}
