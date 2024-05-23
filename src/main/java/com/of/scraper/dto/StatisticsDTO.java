package com.of.scraper.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for statistics.
 * 
 * @totalSalmonCount total count of salmon caught.
 * @totalSalmonWeight total weight of salmon caught.
 * @averageSalmonWeight average weight of salmon caught.
 * @totalSeatroutCount total count of seatrout caught.
 * @totalSeatroutWeight total weight of seatrout caught.
 * @averageSeatroutWeight average weight of seatrout caught.
 * @totalPukkellaksCount total count of pukkellaks caught.
 * @averageSalmonCountPerSeason average count of salmon caught per season.
 * @averageSeatroutCountPerSeason average count of seatrout caught per season.
*/

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDTO {
    
    private int totalSalmonCount = 0;
    private double totalSalmonWeight = 0;
    private double averageSalmonWeight = 0;
    private int totalSeatroutCount = 0;
    private double totalSeatroutWeight = 0;
    private double averageSeatroutWeight = 0;
    private int totalPukkellaksCount = 0;
    private double averageSalmonCountPerSeason = 0;
    private double averageSeatroutCountPerSeason = 0;

    public void incrementSalmonCount(int salmonCount) {
        this.totalSalmonCount += salmonCount;
    }

    public void incrementSalmonWeight(double salmonWeight) {
        this.totalSalmonWeight += salmonWeight;
    }

    public void incrementSeatroutCount(int seatroutCount) {
        this.totalSeatroutCount += seatroutCount;
    }

    public void incrementSeatroutWeight(double seatroutWeight) {
        this.totalSeatroutWeight += seatroutWeight;
    }

    public void incrementPukkellaksCount(int pukkellaksCount) {
        this.totalPukkellaksCount += pukkellaksCount;
    }
}
