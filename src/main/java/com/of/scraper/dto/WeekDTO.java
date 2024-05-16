package com.of.scraper.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WeekDTO is a Data Transfer Object (DTO) class that represents the fishing data
 * for a specific week.
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
 * @startDate: String - the start date of the fishing week.
 * @endDate: String - the end date of the fishing week.
 * @count: int - the number of fish caught during this week.
 * @totalWeight: double - the total weight of all fish caught during this week.
 * @averageWeight: double - the average weight of the fish caught during this week.
 */

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeekDTO {
    
    private String startDate;
    private String endDate;
    private int count;
    private double totalWeight;
    private double averageWeight;
}
