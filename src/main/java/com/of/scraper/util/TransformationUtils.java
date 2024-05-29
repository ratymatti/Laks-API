package com.of.scraper.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransformationUtils {

    /**
     * Formats a date to a string in the format "MM.dd".
     * 
     * @param date The date to format as LocalDate.
     * @return String in the format "MM.dd".
     */

    public static String formatDateToMMddString(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return date.format(DateTimeFormatter.ofPattern("MM.dd"));
    }
}
