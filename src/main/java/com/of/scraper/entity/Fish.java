package com.of.scraper.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Fish is an Entity class, each represents one fish.
 * It is marked as Entity, indicating that this class is a JPA entity.
 * 
 * The class uses Lombok annotations for boilerplate code:
 * - Getter and Setter: to auto-generate getters and setters for all fields.
 * - NoArgsConstructor: to auto-generate a no-argument constructor.
 * 
 * The class has the following fields:
 * 
 * @id: UUID - the unique identifier of the data.
 * @location: String - the location where the fishing took place.
 * @date: String - the date when the fishing took place.
 * @weight: double - the weight of the fish caught.
 * @species: String - the species of the fish caught.
 * @gear: String - the gear used for fishing.
 * @zone: String - the zone where the fish was caught.
 * @name: String - the name of the angler.
 * @localDate: LocalDate - the local date when the fishing took place.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "fish")
public class Fish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "location")
    private String location;

    @Column(name = "weight")
    private double weight;

    @Column(name = "species")
    private String species;

    @Column(name = "gear")
    private String gear;

    @Column(name = "zone")
    private String zone;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDate date;

    public Fish(String species, double weight, LocalDate date) {
        this.species = species;
        this.weight = weight;
        this.date = date;
    }
}
