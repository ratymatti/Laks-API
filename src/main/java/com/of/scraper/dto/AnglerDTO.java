package com.of.scraper.dto;

import java.util.List;

import com.of.scraper.entity.Fish;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AnglerDTO is a Data Transfer Object (DTO) class that represents an angler.
 * It is marked as Embeddable, indicating that instances of this class can be
 * embedded in other entities.
 * 
 * The class uses Lombok annotations for boilerplate code:
 * - Getter and Setter: to auto-generate getters and setters for all fields.
 * - NoArgsConstructor: to auto-generate a no-argument constructor.
 * - AllArgsConstructor: to auto-generate a constructor that takes all fields as arguments.
 * 
 * The class has the following fields:
 * @name: String - the name of the angler.
 * @anglerStats: AnglerStatsDTO - a reference to an AnglerStatsDTO object that holds statistical information about the angler.
 * @data: List<Fish> - a list of Fish objects associated with the angler.
 */

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnglerDTO {

    private String name;
    private AnglerStatsDTO anglerStats;
    private List<Fish> data;
}
