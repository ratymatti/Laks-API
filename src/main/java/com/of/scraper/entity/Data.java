package com.of.scraper.entity;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "data")
public class Data {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "location")
    private String location;

    @Column(name = "date")
    private String date;

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

    @Column(name = "local_date")
    private LocalDate localDate;
}