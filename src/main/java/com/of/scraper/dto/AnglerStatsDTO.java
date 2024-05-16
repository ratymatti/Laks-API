package com.of.scraper.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AnglerStatsDTO is a Data Transfer Object (DTO) class that represents the statistics of an angler.
 * It is marked as Embeddable, indicating that instances of this class can be embedded in other entities.
 * 
 * The class uses Lombok annotations for boilerplate code:
 * - Getter and Setter: to auto-generate getters and setters for all fields.
 * - NoArgsConstructor: to auto-generate a no-argument constructor.
 * - AllArgsConstructor: to auto-generate a constructor that takes all fields as arguments.
 * 
 * The class has the following fields:
 *  @name: String - the name of the angler.
 *  @count: int - the number of catches by the angler.
 *  @totalWeight: double - the total weight of all catches by the angler.
 *  @averageWeight: double - the average weight of catches by the angler.
 */

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnglerStatsDTO {

    private String name;
    private int count;
    private double totalWeight;
    private double averageWeight;
}
