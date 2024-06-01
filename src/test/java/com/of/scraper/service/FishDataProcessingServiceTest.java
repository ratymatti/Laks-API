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
import com.of.scraper.dto.AverageAndMedianDTO;
import com.of.scraper.dto.StatisticsDTO;
import com.of.scraper.dto.WeekDTO;
import com.of.scraper.dto.YearDTO;
import com.of.scraper.entity.Fish;
import com.of.scraper.repository.FishRepository;
import com.of.scraper.testutils.TestDataUtil;

@ExtendWith(MockitoExtension.class)
public class FishDataProcessingServiceTest {

    @Mock
    private FishRepository fishRepository;

    @InjectMocks
    private FishDataProcessingService dataProcessingService;

    @Test
    public void testCreateAnglerStatsDTO() {
        // Arrange
        String testName = "Test Name";
        String testSpecies = "Test Species";
        int expectedCount = 2;
        double expectedTotalWeight = 20.0;
        double expectedAverageWeight = 10.0;

        when(fishRepository.getCountByNameAndSpecies(testName, testSpecies)).thenReturn(expectedCount);
        when(fishRepository.getTotalWeightByNameAndSpecies(testName, testSpecies)).thenReturn(expectedTotalWeight);

        // Act
        AnglerStatsDTO result = dataProcessingService.createAnglerStatsDTO(testName, testSpecies);

        // Assert
        assertEquals(testName, result.getName());
        assertEquals(expectedCount, result.getCount());
        assertEquals(expectedTotalWeight, result.getTotalWeight());
        assertEquals(expectedAverageWeight, result.getAverageWeight());

        verify(fishRepository).getCountByNameAndSpecies(testName, testSpecies);
        verify(fishRepository).getTotalWeightByNameAndSpecies(testName, testSpecies);
    }

    @Test
    public void testGetBestWeeksByYear() {
        // Arrange
        List<Fish> fishData = TestDataUtil.createTestData(2020, 2);

        // Act
        Map<Integer, List<WeekDTO>> result = dataProcessingService.getBestWeeksByYear(fishData);

        // Assert
        assertEquals(10.0, result.get(2020).get(0).getAverageWeight());
        assertEquals("07.20", result.get(2020).get(0).getStartDate());
        assertEquals("07.26", result.get(2020).get(0).getEndDate());
        assertEquals(161, result.get(2020).get(0).getCount());
        assertEquals(1610, result.get(2020).get(0).getTotalWeight());
        assertEquals(10.0, result.get(2021).get(0).getAverageWeight());
        assertEquals("07.20", result.get(2021).get(0).getStartDate());
        assertEquals("07.26", result.get(2021).get(0).getEndDate());
        assertEquals(161, result.get(2021).get(0).getCount());
        assertEquals(1610, result.get(2021).get(0).getTotalWeight());
    }

    @Test
    public void testGetBestWeeksAlltime() {
        // Arrange
        List<Fish> fishData = TestDataUtil.createTestData(2020, 2);

        // Act
        List<WeekDTO> result = dataProcessingService.getBestWeeksAlltime(fishData);

        // Assert
        assertEquals(322, result.get(0).getCount());
        assertEquals("07.20", result.get(0).getStartDate());
        assertEquals("07.26", result.get(0).getEndDate());
        assertEquals(3220, result.get(0).getTotalWeight());
        assertEquals(10.0, result.get(0).getAverageWeight());
    }

    @Test
    public void testGetStatistics() {
        List<Fish> testData = TestDataUtil.createTestData(2020, 1);

        List<YearDTO> result = dataProcessingService.getStatistics(testData);

        assertEquals(2020, result.get(0).getYear());
        assertEquals(465, result.get(0).getSalmonCount());
        assertEquals(4650, result.get(0).getSalmonTotalWeight());
        assertEquals(10.0, result.get(0).getSalmonAverageWeight());
    }

    @Test 
    public void testGetAlltimeStatistics() {
        List<Fish> testData = TestDataUtil.createTestData(2020, 2);

        StatisticsDTO result = dataProcessingService.getAlltimeStatistics(testData);

        assertEquals(930, result.getTotalSalmonCount());
        assertEquals(9300, result.getTotalSalmonWeight());
        assertEquals(10.0, result.getAverageSalmonWeight());
        assertEquals(0, result.getTotalSeatroutCount());
    }
 
    @Test
    public void testGetAverageAndMedianOfFishesPerDay() {
        // Arrange
        List<Fish> testData1 = TestDataUtil.createTestData(2020, 1);
        List<Fish> testData2 = TestDataUtil.createTestData(2020, 1);
        testData2.remove(0);

        // Act
        Map<Integer, AverageAndMedianDTO> result1 = dataProcessingService.getAverageAndMedianOfFishesPerDay(testData1);
        Map<Integer, AverageAndMedianDTO> result2 = dataProcessingService.getAverageAndMedianOfFishesPerDay(testData2);

        // Assert
        assertEquals(15.5, result1.get(2020).getMedian());
        assertEquals(15.5, result1.get(2020).getAverage());
        assertEquals(16, result2.get(2020).getMedian());
        assertEquals(16, result2.get(2020).getAverage());
    }

}
