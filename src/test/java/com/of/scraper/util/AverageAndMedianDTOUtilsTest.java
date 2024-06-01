package com.of.scraper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import com.of.scraper.dto.AverageAndMedianDTO;
import com.of.scraper.entity.Fish;
import com.of.scraper.testutils.TestDataUtil;

public class AverageAndMedianDTOUtilsTest {

    @Test
    public void testTransformToAverageAndMedianDTO() {
        Map <String, Integer> testData = new TreeMap<>();
        testData.put("07.01", 5);
        testData.put("07.02", 10);
        testData.put("07.03", 15);

        AverageAndMedianDTO result = AverageAndMedianDTOUtils.transformToAverageAndMedianDTO(testData);

        assertEquals(10.0, result.getAverage());
        assertEquals(10.0, result.getMedian());
    }
    
    @Test
    public void testTransformToAverageAndMedianDTOMap() {
        List<Fish> year1 = TestDataUtil.createTestData(2020, 1);
        List<Fish> year2 = TestDataUtil.createTestData(2021, 1);
        Map<Integer, List<Fish>> testData = new TreeMap<>();
        testData.put(2020, year1);
        testData.put(2021, year2);
        
        Map<Integer, AverageAndMedianDTO> result = AverageAndMedianDTOUtils.transformToAverageAndMedianDTOMap(testData);
        
        assertEquals(15.5, result.get(2020).getAverage());
        assertEquals(15.5, result.get(2021).getAverage());
        assertEquals(15.5, result.get(2020).getMedian());
        assertEquals(15.5, result.get(2021).getMedian());
    }
}
