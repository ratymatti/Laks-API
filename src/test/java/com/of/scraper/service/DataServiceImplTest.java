package com.of.scraper.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.of.scraper.dto.AnglerDTO;
import com.of.scraper.dto.AnglerStatsDTO;
import com.of.scraper.entity.Data;
import com.of.scraper.repository.DataRepository;

import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataServiceImplTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private DataServiceImpl dataService;

    @Test
    public void testFindByNameAndSpecies() {
        // Arrange
        String name = "John";
        String species = "Laks";
        Data mockData = new Data();
        List<Data> mockDataList = Arrays.asList(mockData);

        when(dataRepository.findByNameAndSpecies(anyString(), anyString())).thenReturn(mockDataList);

        // Act
        AnglerDTO result = dataService.findByNameAndSpecies(name, species);
        AnglerStatsDTO anglerStats = result.getAnglerStats();

        // Assert
        assertEquals(name, result.getName());
        assertNotNull(result.getAnglerStats());
        assertEquals(mockDataList, result.getData());
        assertEquals(name, anglerStats.getName());

        verify(dataRepository).findByNameAndSpecies(name, species);
    }

    @Test
    public void testCreateAnglerStatsDTO() {
        // Arrange
        String testName  = "Test Name";
        String testSpecies = "Test Species";
        int expectedCount = 2;
        double expectedTotalWeight = 20.0;
        double expectedAverageWeight = 10.0;

        when(dataRepository.getCountByNameAndSpecies(testName, testSpecies)).thenReturn(expectedCount);
        when(dataRepository.getTotalWeightByNameAndSpecies(testName, testSpecies)).thenReturn(expectedTotalWeight);
       
        // Act
        AnglerStatsDTO result = dataService.createAnglerStatsDTO(testName, testSpecies);

        // Assert
        assertEquals(testName, result.getName());
        assertEquals(expectedCount, result.getCount());
        assertEquals(expectedTotalWeight, result.getTotalWeight());
        assertEquals(expectedAverageWeight, result.getAverageWeight());

        verify(dataRepository).getCountByNameAndSpecies(testName, testSpecies);
        verify(dataRepository).getTotalWeightByNameAndSpecies(testName, testSpecies);
    }
}