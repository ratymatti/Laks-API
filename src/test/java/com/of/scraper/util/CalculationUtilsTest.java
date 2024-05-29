package com.of.scraper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.of.scraper.dto.DayDTO;
import com.of.scraper.dto.YearDTO;
import com.of.scraper.entity.Data;
import com.of.scraper.testutils.TestDataUtil;

@ExtendWith(MockitoExtension.class)
public class CalculationUtilsTest {

    private static final int VALID_INT = 10;
    private static final int INVALID_INT = -10;
    private static final int ZERO_INT = 0;
    private static final double INVALID_DOUBLE = -10.0;
    private static final double ZERO_DOUBLE = 0.0;

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
    public void testRoundToTwoDecimals() throws Exception {
        // Test with rounding down
        double testValue1 = 10.123456;
        double result1 = CalculationUtils.roundToTwoDecimals(testValue1);
        assertEquals(10.12, result1);

        // Test with rounding up
        double testValue2 = 10.126;
        double result2 = CalculationUtils.roundToTwoDecimals(testValue2);
        assertEquals(10.13, result2);

        // Test with a negative value
        assertThrows(IllegalArgumentException.class,
                () -> CalculationUtils.roundToTwoDecimals(INVALID_DOUBLE));

        // Test with less than two decimal places
        double testValue4 = 10.1;
        double result4 = CalculationUtils.roundToTwoDecimals(testValue4);
        assertEquals(10.1, result4);

        // Test with zero
        double result5 = CalculationUtils.roundToTwoDecimals(ZERO_DOUBLE);
        assertEquals(ZERO_DOUBLE, result5);
    }

    @Test
    public void testCalculateTotalWeight() throws Exception {
        // Test with valid values
        List<DayDTO> testData1 = TestDataUtil.createDayDTOtestDataList();
        double result1 = CalculationUtils.calculateTotalWeight(testData1, DayDTO::getTotalWeight);
        assertEquals(600.0, result1);

        // Test with valid List but without getter method
        assertThrows(NullPointerException.class,
                () -> CalculationUtils.calculateTotalWeight(testData1, null));

        // Test with empty List
        List<DayDTO> testData2 = new ArrayList<>();
        double result2 = CalculationUtils.calculateTotalWeight(testData2, DayDTO::getTotalWeight);
        assertEquals(ZERO_DOUBLE, result2);

        // Test with null List
        List<DayDTO> testData3 = null;
        assertThrows(NullPointerException.class,
                () -> CalculationUtils.calculateTotalWeight(testData3, DayDTO::getTotalWeight));
    }

    @Test
    public void testCalculateCount() throws Exception {
        // Test with valid values
        List<DayDTO> testData1 = TestDataUtil.createDayDTOtestDataList();
        int result1 = CalculationUtils.calculateCount(testData1, DayDTO::getFishCount);
        assertEquals(60, result1);

        // Test with valid List but without getter method
        assertThrows(NullPointerException.class,
                () -> CalculationUtils.calculateCount(testData1, null));

        // Test with empty List
        List<DayDTO> testData2 = new ArrayList<>();
        int result2 = CalculationUtils.calculateCount(testData2, DayDTO::getFishCount);
        assertEquals(ZERO_INT, result2);

        // Test with null List
        List<DayDTO> testData3 = null;
        assertThrows(NullPointerException.class,
                () -> CalculationUtils.calculateCount(testData3, DayDTO::getFishCount));
    }

    @Test
    public void testCalculateAverageWeight() throws Exception {
        // Test with normal values
        double testTotalWeight1 = 100.0;
        double expectedAverageWeight1 = 10.0;
        double result1 = CalculationUtils.calculateAverageWeight(VALID_INT, testTotalWeight1);
        assertEquals(expectedAverageWeight1, result1);

        // Test with count as zero
        int testCount2 = 0;
        double testTotalWeight2 = 100.0;
        assertThrows(IllegalStateException.class,
                () -> CalculationUtils.calculateAverageWeight(testCount2, testTotalWeight2));

        // Test with total weight as zero
        assertThrows(IllegalStateException.class,
                () -> CalculationUtils.calculateAverageWeight(VALID_INT, ZERO_DOUBLE));

        // Test with both count and total weight as zero
        double result4 = CalculationUtils.calculateAverageWeight(ZERO_INT, ZERO_DOUBLE);
        assertEquals(ZERO_DOUBLE, result4);

        // Test with negative count
        assertThrows(IllegalArgumentException.class,
                () -> CalculationUtils.calculateAverageWeight(INVALID_INT, VALID_INT));

        // Test with negative total weight
        assertThrows(IllegalArgumentException.class,
                () -> CalculationUtils.calculateAverageWeight(VALID_INT, INVALID_DOUBLE));
    }

    @Test
    public void testCalculateAverageAmount() throws Exception {
        // Test with normal values
        int testCount1 = 10;
        int testTimePeriod1 = 5;
        double expectedAverageAmount1 = 2.0;
        double result1 = CalculationUtils.calculateAverageAmount(testCount1, testTimePeriod1);
        assertEquals(expectedAverageAmount1, result1);

        // Test with count as zero
        assertThrows(IllegalStateException.class,
                () -> CalculationUtils.calculateAverageAmount(ZERO_INT, VALID_INT));

        // Test with time period as zero
        assertThrows(IllegalStateException.class,
                () -> CalculationUtils.calculateAverageAmount(VALID_INT, ZERO_INT));

        // Test with both count and time period as zero
        double result4 = CalculationUtils.calculateAverageAmount(ZERO_INT, ZERO_INT);
        assertEquals(ZERO_DOUBLE, result4);

        // Test with negative count
        assertThrows(IllegalArgumentException.class,
                () -> CalculationUtils.calculateAverageAmount(INVALID_INT, VALID_INT));

        // Test with negative time period
        assertThrows(IllegalArgumentException.class,
                () -> CalculationUtils.calculateAverageAmount(VALID_INT, INVALID_INT));
    }

    @Test
    public void testCalculateDailyCounts() throws Exception {
        // Test with valid values
        List<Data> testData1 = TestDataUtil.createTestData(2020, 1);
        Map<String, Integer> result1 = CalculationUtils.calculateDailyCounts(testData1);
        assertEquals(30, result1.size());
        assertEquals(28, result1.get("07.28"));

        // Test with empty List
        List<Data> testData2 = new ArrayList<>();
        Map<String, Integer> result2 = CalculationUtils.calculateDailyCounts(testData2);
        assertTrue(result2.isEmpty());

        // Test with null List
        List<Data> testData3 = null;
        assertThrows(IllegalArgumentException.class,
                () -> CalculationUtils.calculateDailyCounts(testData3));
    }

    @Test
    public void testCalculateMedianAmount() throws Exception {
        // Test with odd number of elements
        List<Integer> testData1 = Arrays.asList(1, 2, 3, 4, 5);
        double result1 = CalculationUtils.calculateMedianAmount(testData1);
        assertEquals(3.0, result1);

        // Test with even number of elements
        List<Integer> testData2 = Arrays.asList(1, 2, 3, 4);
        double result2 = CalculationUtils.calculateMedianAmount(testData2);
        assertEquals(2.5, result2);

        // Test with empty List
        List<Integer> testData3 = new ArrayList<>();
        double result3 = CalculationUtils.calculateMedianAmount(testData3);
        assertEquals(ZERO_DOUBLE, result3);

        // Test with null List
        List<Integer> testData4 = null;
        assertThrows(IllegalArgumentException.class,
                () -> CalculationUtils.calculateMedianAmount(testData4));
    }

}
