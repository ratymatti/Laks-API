package com.of.scraper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.of.scraper.dto.YearDTO;

public class YearDTOUtilsTest {
   
    @Test
    public void testRoundYearDTOValues() {
        // Create a YearDTO with some test values
        YearDTO yearDTO = new YearDTO();
        yearDTO.setSalmonTotalWeight(123.456);
        yearDTO.setSalmonAverageWeight(123.456);
        yearDTO.setSeatroutTotalWeight(123.456);
        yearDTO.setSeatroutAverageWeight(123.456);

        // Call the method under test
        YearDTO roundedYearDTO = YearDTOUtils.roundYearDTOValues(yearDTO);

        // Check that each field has been rounded correctly
        assertEquals(123.46, roundedYearDTO.getSalmonTotalWeight(), 0.01);
        assertEquals(123.46, roundedYearDTO.getSalmonAverageWeight(), 0.01);
        assertEquals(123.46, roundedYearDTO.getSeatroutTotalWeight(), 0.01);
        assertEquals(123.46, roundedYearDTO.getSeatroutAverageWeight(), 0.01);
    }
}
