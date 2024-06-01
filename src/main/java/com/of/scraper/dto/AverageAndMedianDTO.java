package com.of.scraper.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * AverageAndMedianDTO is a Data Transfer Object (DTO) class that represents the
 * average and median of the fishing data.
 * 
 * The class has the following fields:
 * 
 * @average: double - the average of the fishing data.
 * @median: double - the median of the fishing data.
 */

@Embeddable
@AllArgsConstructor
@Getter
@Setter
public class AverageAndMedianDTO {
    
    double average;
    double median;
}
