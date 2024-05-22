package com.of.scraper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.of.scraper.dto.AnglerStatsDTO;
import com.of.scraper.dto.WeekDTO;
import com.of.scraper.entity.Data;
import com.of.scraper.repository.DataRepository;
import com.of.scraper.util.TestDataUtil;

@ExtendWith(MockitoExtension.class)
public class DataProcessingServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private DataProcessingService dataProcessingService;

    @Test
    public void testCreateAnglerStatsDTO() {
        // Arrange
        String testName = "Test Name";
        String testSpecies = "Test Species";
        int expectedCount = 2;
        double expectedTotalWeight = 20.0;
        double expectedAverageWeight = 10.0;

        when(dataRepository.getCountByNameAndSpecies(testName, testSpecies)).thenReturn(expectedCount);
        when(dataRepository.getTotalWeightByNameAndSpecies(testName, testSpecies)).thenReturn(expectedTotalWeight);

        // Act
        AnglerStatsDTO result = dataProcessingService.createAnglerStatsDTO(testName, testSpecies);

        // Assert
        assertEquals(testName, result.getName());
        assertEquals(expectedCount, result.getCount());
        assertEquals(expectedTotalWeight, result.getTotalWeight());
        assertEquals(expectedAverageWeight, result.getAverageWeight());

        verify(dataRepository).getCountByNameAndSpecies(testName, testSpecies);
        verify(dataRepository).getTotalWeightByNameAndSpecies(testName, testSpecies);
    }

    @Test
    public void testGetBestWeeksByYear() {
        // Arrange
        List<Data> fishData = TestDataUtil.createTestData();

        // Act
        Map<Integer, List<WeekDTO>> result = dataProcessingService.getBestWeeksByYear(fishData);
        System.out.println(result.get(2022).get(0).getCount());

        // Assert
        assertEquals(10.0, result.get(2022).get(0).getAverageWeight());
        assertEquals(11, result.get(2022).get(0).getCount());
        assertEquals(10.0, result.get(2021).get(0).getAverageWeight());
        assertEquals(11, result.get(2021).get(0).getCount());
        assertEquals(8, result.get(2022).get(1).getCount());
    }

    @Test
    public void testGetBestWeeksAlltime() {
        // Arrange
        List<Data> fishData = TestDataUtil.createTestData();

        // Act
        List<WeekDTO> result = dataProcessingService.getBestWeeksAlltime(fishData);

        // Assert
        assertEquals(22, result.get(0).getCount());
        assertEquals(10.0, result.get(0).getAverageWeight());
    }

    @Test
    public void testRoundToTwoDecimals() {
        // Test with rounding down
        double testValue1 = 10.123456;
        double result1 = dataProcessingService.roundToTwoDecimals(testValue1);
        assertEquals(10.12, result1);

        // Test with rounding up
        double testValue2 = 10.126;
        double result2 = dataProcessingService.roundToTwoDecimals(testValue2);
        assertEquals(10.13, result2);

        // Test with a negative value
        double testValue3 = -10.123456;
        double result3 = dataProcessingService.roundToTwoDecimals(testValue3);
        assertEquals(-10.12, result3);

        // Test with less than two decimal places
        double testValue4 = 10.1;
        double result4 = dataProcessingService.roundToTwoDecimals(testValue4);
        assertEquals(10.1, result4);

        // Test with zero
        double testValue5 = 0;
        double result5 = dataProcessingService.roundToTwoDecimals(testValue5);
        assertEquals(0, result5);
    }

    @Test
    public void testCalculateAverageWeight() {
        // Test with normal values
        int testCount1 = 10;
        double testTotalWeight1 = 100.0;
        double expectedAverageWeight1 = 10.0;
        double result1 = dataProcessingService.calculateAverageWeight(testCount1, testTotalWeight1);
        assertEquals(expectedAverageWeight1, result1);

        // Test with count as zero
        int testCount2 = 0;
        double testTotalWeight2 = 100.0;
        double result2 = dataProcessingService.calculateAverageWeight(testCount2, testTotalWeight2);
        assertEquals(0.0, result2);

        // Test with total weight as zero
        int testCount3 = 10;
        double testTotalWeight3 = 0.0;
        double expectedAverageWeight3 = 0.0;
        double result3 = dataProcessingService.calculateAverageWeight(testCount3, testTotalWeight3);
        assertEquals(expectedAverageWeight3, result3);

        // Test with both count and total weight as zero
        int testCount4 = 0;
        double testTotalWeight4 = 0.0;
        double expectedAverageWeight4 = 0.0;
        double result4 = dataProcessingService.calculateAverageWeight(testCount4, testTotalWeight4);
        assertEquals(expectedAverageWeight4, result4);
    }

}
