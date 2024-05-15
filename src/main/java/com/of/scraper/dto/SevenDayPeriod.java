package com.of.scraper.dto;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SevenDayPeriod {
    
    private LocalDate startDate;
    private LocalDate endDate;
    private int count;
    private double totalWeight;
    private double averageWeight;
}
