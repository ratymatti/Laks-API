package com.of.scraper.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.of.scraper.entity.Data;
import java.util.List;


public interface DataRepository extends JpaRepository<Data, UUID>{
    List<Data> findAll();
    List<Data> findByName(String name);
    List<Data> findByNameAndSpecies(String name, String species);

    @Query("SELECT COUNT(d) FROM Data d WHERE d.name = :name AND d.species = :species")
    int getCountByNameAndSpecies(@Param("name") String name, @Param("species") String species);

    @Query("SELECT SUM(d.weight) FROM Data d WHERE d.name = :name AND d.species = :species")
    Double getTotalWeightByNameAndSpecies(@Param("name") String name, @Param("species") String species);
}