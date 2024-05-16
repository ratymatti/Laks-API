package com.of.scraper.dto;

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
public class WeekDTO {
    
    private String startDate;
    private String endDate;
    private int count;
    private double totalWeight;
    private double averageWeight;
}
