package com.of.scraper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TransformationUtilsTest {

    @Test
    public void testFormatDateToMMddString() throws Exception {
        // Test with valid random date
        LocalDate date = LocalDate.of(2021, 04, 20);
        String formattedDate = TransformationUtils.formatDateToMMddString(date);
        assertEquals("04.20", formattedDate);

        // Test with null value
        assertThrows(IllegalArgumentException.class,
                () -> TransformationUtils.formatDateToMMddString(null));
    }
}
