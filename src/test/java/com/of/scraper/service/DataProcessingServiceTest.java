package com.of.scraper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.of.scraper.dto.AnglerStatsDTO;
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
        String testName  = "Test Name";
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
}