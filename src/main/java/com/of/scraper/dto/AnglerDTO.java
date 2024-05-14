package com.of.scraper.dto;

import java.util.List;

import com.of.scraper.entity.Data;

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
public class AnglerDTO {

    private String name;
    private AnglerStatsDTO anglerStats;
    private List<Data> data;
}
