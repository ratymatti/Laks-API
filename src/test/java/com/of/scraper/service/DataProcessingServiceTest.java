package com.of.scraper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
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
        List<Data> fishData = createTestData();

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
        List<Data> fishData = createTestData();

        // Act
        List<WeekDTO> result = dataProcessingService.getBestWeeksAlltime(fishData);

        // Assert
        assertEquals(22, result.get(0).getCount());
        assertEquals(10.0, result.get(0).getAverageWeight());
    }

    public List<Data> createTestData() {
        List<Data> fishData = new ArrayList<>();
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 16)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 17)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 18)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 19)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 20)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 21)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 22)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 6, 22)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 1)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 2)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 3)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 4)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 5)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 6)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 7)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 1)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 2)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 3)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2022, 7, 4)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 1)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 2)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 3)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 4)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 5)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 6)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 7)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 1)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 2)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 3)));
        fishData.add(new Data("Laks", 10.0, LocalDate.of(2021, 7, 4)));
        return fishData;
    }
}
