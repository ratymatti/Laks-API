package com.of.scraper.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.of.scraper.dto.AnglerDTO;
import com.of.scraper.dto.AnglerStatsDTO;
import com.of.scraper.entity.Fish;
import com.of.scraper.repository.FishRepository;

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
    private FishRepository fishRepository;

    @Mock
    private FishDataProcessingService fishDataProcessingService;

    @InjectMocks
    private FishDataServiceImpl fishDataService;

    @Test
    public void testFindByNameAndSpecies() {
        // Arrange
        String expectedName = "John";
        String species = "Laks";
        Fish mockData = new Fish();
        List<Fish> mockDataList = Arrays.asList(mockData);
        AnglerStatsDTO mockAnglerStats = new AnglerStatsDTO();
        mockAnglerStats.setName(expectedName);

        when(fishDataProcessingService.createAnglerStatsDTO(anyString(), anyString())).thenReturn(mockAnglerStats);
        when(fishRepository.findByNameAndSpecies(anyString(), anyString())).thenReturn(mockDataList);

        // Act
        AnglerDTO result = fishDataService.findByNameAndSpecies(expectedName, species);
        AnglerStatsDTO anglerStats = result.getAnglerStats();

        // Assert
        assertEquals(expectedName, result.getName());
        assertNotNull(result.getAnglerStats());
        assertEquals(mockDataList, result.getData());
        assertEquals(expectedName, anglerStats.getName());

        verify(fishRepository).findByNameAndSpecies(expectedName, species);
    }

}