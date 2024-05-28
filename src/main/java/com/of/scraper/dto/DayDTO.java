package com.of.scraper.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * DayDTO is a Data Transfer Object (DTO) class that represents the fishing data
 * for a specific day.
 * It is marked as Embeddable, indicating that instances of this class can be
 * embedded in other entities.
 * 
 * The class uses Lombok annotations for boilerplate code:
 * - Getter and Setter: to auto-generate getters and setters for all fields.
 * - NoArgsConstructor: to auto-generate a no-argument constructor.
 * - AllArgsConstructor: to auto-generate a constructor that takes all fields as
 * arguments.
 * 
 * The class has the following fields:
 * 
 * @date: String - the date of the fishing day.
 * @fishCount: int - the number of fish caught on this day.
 * @totalWeight: double - the total weight of all fish caught on this day.
 * @averageWeight: double - the average weight of the fish caught on this day.
 */

@Embeddable
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor

public class DayDTO {

    @NonNull
    private String date;

    private int fishCount = 0;
    private double totalWeight = 0.0;
    private double averageWeight = 0.0;
}
