package com.of.scraper.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;

@Embeddable
@AllArgsConstructor
public class AverageAndMedianDTO {
    
    int average;
    int median;
}
