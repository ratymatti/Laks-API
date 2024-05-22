package com.of.scraper.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The YearDTO class represents a data transfer object for a year's worth of fishing data.
 * It is marked as Embeddable, which means it can be embedded in other entities.
 * 
* The class uses Lombok annotations for boilerplate code:
 * - Getter and Setter: Create getter and setter methods for all fields.
 * - NoArgsConstructor: Create a no-argument constructor.
 * - AllArgsConstructor: Create a constructor that takes an argument for every field.
 * 
 * The class contains the following fields:
 * @year: The year the data pertains to.
 * @salmonCount: The number of salmons caught in the year.
 * @salmonTotalWeight: The total weight of all salmons caught in the year.
 * @salmonAverageWeight: The average weight of a salmon caught in the year.
 * @seatroutCount: The number of seatrouts caught in the year.
 * @seatroutTotalWeight: The total weight of all seatrouts caught in the year.
 * @seatroutAverageWeight: The average weight of a seatrout caught in the year.
 */

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YearDTO {

    private int year;
    private int salmonCount = 0;
    private double salmonTotalWeight = 0;
    private double salmonAverageWeight = 0;
    private int seatroutCount = 0;
    private double seatroutTotalWeight = 0;
    private double seatroutAverageWeight = 0;
    private int pukkellaksCount = 0;

    public YearDTO(int year) {
        this.year = year;
    }
}
