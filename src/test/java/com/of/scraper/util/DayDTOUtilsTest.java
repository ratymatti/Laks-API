package com.of.scraper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.of.scraper.dto.DayDTO;
import com.of.scraper.entity.Fish;

public class DayDTOUtilsTest {
    
    @Test
    public void testTransformToDayDTO() {
        List<Fish> testData = new ArrayList<>();
        testData.add(new Fish("Laks", 10.0, LocalDate.of(2020, 07, 01)));
        testData.add(new Fish("Laks", 20.0, LocalDate.of(2020, 07, 01)));
        
        DayDTO result = DayDTOUtils.transformToDayDTO(testData, "07.01");
        
        assertEquals("07.01", result.getDate());
        assertEquals(2, result.getFishCount());
        assertEquals(30.0, result.getTotalWeight());
        assertEquals(15.0, result.getAverageWeight());
    }

    @Test
    public void testTransformToDayDTOList() {
        Map<String, List<Fish>> testData = new HashMap<>();
        testData.put("07.01", new ArrayList<>());
        testData.get("07.01").add(new Fish("Laks", 10.0, LocalDate.of(2020, 07, 01)));
        testData.get("07.01").add(new Fish("Laks", 20.0, LocalDate.of(2020, 07, 01)));
        testData.put("07.02", new ArrayList<>());
        testData.get("07.02").add(new Fish("Laks", 10.0, LocalDate.of(2020, 07, 02)));
        testData.get("07.02").add(new Fish("Laks", 30.0, LocalDate.of(2020, 07, 02)));

        List<DayDTO> result = DayDTOUtils.transformToDayDTOList(testData);
        
        assertEquals(2, result.size());
        assertEquals("07.01", result.get(0).getDate());
        assertEquals(2, result.get(0).getFishCount());
        assertEquals(30.0, result.get(0).getTotalWeight());
        assertEquals(15.0, result.get(0).getAverageWeight());
        assertEquals("07.02", result.get(1).getDate());
        assertEquals(2, result.get(1).getFishCount());
        assertEquals(40.0, result.get(1).getTotalWeight());
        assertEquals(20.0, result.get(1).getAverageWeight());
    }
}
