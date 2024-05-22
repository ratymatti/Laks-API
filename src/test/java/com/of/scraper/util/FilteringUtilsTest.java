package com.of.scraper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.of.scraper.dto.WeekDTO;
import com.of.scraper.entity.Data;
import com.of.scraper.testutils.TestDataUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FilteringUtilsTest {
    
    @Test
    public void testFilterOutOffSeasonFishes() {
        // Create List<Data> with test data
        List<Data> testData = TestDataUtil.createDataListWithOffSeasonFishes();
        
        // Call the method under test
        List<Data> result = FilteringUtils.filterOutOffSeasonFishes(testData);

        // Check that the method returns the correct number of elements
        assertEquals(3, result.size());
    }

    @Test
    public void testGetBestWeeks() {
        // Create List<WeekDTO> with test data
        List<WeekDTO> testData1 = TestDataUtil.createWeekDTOListWithTestData(true);
        List<WeekDTO> testData2 = TestDataUtil.createWeekDTOListWithTestData(false);
        List<WeekDTO> testData3 = new ArrayList<>();
        
        // Call the method under test
        List<WeekDTO> result1 = FilteringUtils.getBestWeeks(testData1);
        List<WeekDTO> result2 = FilteringUtils.getBestWeeks(testData2);
        List<WeekDTO> result3 = FilteringUtils.getBestWeeks(testData3);

        // Check that the method returns the correct number of elements
        assertEquals(3, result1.size());
        assertEquals(50, result1.get(0).getCount());
        assertEquals(40, result1.get(1).getCount());
        assertEquals(30, result1.get(2).getCount());
        assertEquals(2, result2.size());
        assertEquals(50, result2.get(0).getCount());
        assertEquals(40, result2.get(1).getCount());
        assertEquals(0, result3.size());
    }
}
