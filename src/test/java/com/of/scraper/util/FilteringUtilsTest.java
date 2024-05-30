package com.of.scraper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.of.scraper.dto.WeekDTO;
import com.of.scraper.entity.Fish;
import com.of.scraper.testutils.TestDataUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FilteringUtilsTest {
    
    @Test
    public void testFilterOutOffSeasonFishes() {
        // Test with valid data containing 5 entities with 2 off-season entities
        List<Fish> testData = TestDataUtil.createDataListWithOffSeasonFishes();
        List<Fish> result = FilteringUtils.filterOutOffSeasonFishes(testData);
        assertEquals(3, result.size());

        // Test with empty list
        List<Fish> testData2 = new ArrayList<>();
        List<Fish> result2 = FilteringUtils.filterOutOffSeasonFishes(testData2);
        assertEquals(0, result2.size());
    }

    @Test
    public void testGetBestWeeks() {
        // Test with 5 entities
        List<WeekDTO> testData1 = TestDataUtil.createWeekDTOListWithTestData(true);
        List<WeekDTO> result1 = FilteringUtils.getBestWeeks(testData1);
        assertEquals(3, result1.size());
        assertEquals(50, result1.get(0).getCount());
        assertEquals(40, result1.get(1).getCount());
        assertEquals(30, result1.get(2).getCount());
        
        // Test with 2 entities
        List<WeekDTO> testData2 = TestDataUtil.createWeekDTOListWithTestData(false);
        List<WeekDTO> result2 = FilteringUtils.getBestWeeks(testData2);
        assertEquals(2, result2.size());
        assertEquals(50, result2.get(0).getCount());
        assertEquals(40, result2.get(1).getCount());

        // Test with empty list
        List<WeekDTO> testData3 = new ArrayList<>();
        List<WeekDTO> result3 = FilteringUtils.getBestWeeks(testData3);
        assertEquals(0, result3.size());
    }
}
