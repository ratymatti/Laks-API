package com.of.scraper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.of.scraper.entity.Data;
import com.of.scraper.testutils.TestDataUtil;

@ExtendWith(MockitoExtension.class)
public class GroupingUtilsTest {
    
    @Test
    public void testGroupByDayAndMonth() {
        // Create List<Data> with test data
        List<Data> testData = TestDataUtil.createTestData(2020, 1);
        
        // Call the method under test
        Map<String, List<Data>> result = GroupingUtils.groupByDayAndMonth(testData);

        // Check that the method returns the correct number of elements
        assertEquals(1, result.get("07.01").size());
        assertEquals(2, result.get("07.02").size());
        assertEquals(30, result.get("07.30").size());
    }

    @Test
    public void testGroupByYear() {
        // Create List<Data> with test data
        List<Data> testData = TestDataUtil.createTestData(2020, 2);
        
        // Call the method under test
        Map<Integer, List<Data>> result = GroupingUtils.groupByYear(testData);

        // Check that the method returns the correct number of elements
        assertEquals(465, result.get(2020).size());
        assertEquals(465, result.get(2021).size());
    }
}
