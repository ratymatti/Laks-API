package com.of.scraper.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;
import com.of.scraper.entity.Data;
import java.util.List;


public interface DataRepository extends JpaRepository<Data, UUID>{
    List<Data> findAll(Sort sort);
    List<Data> findByName(String name);
    List<Data> findByNameAndSpecies(String name, String species);
    List<Data> findBySpecies(String species, Sort sort);

    @Query("SELECT COUNT(d) FROM Data d WHERE d.name = :name AND d.species = :species")
    int getCountByNameAndSpecies(@Param("name") String name, @Param("species") String species);

    @Query("SELECT SUM(d.weight) FROM Data d WHERE d.name = :name AND d.species = :species")
    Double getTotalWeightByNameAndSpecies(@Param("name") String name, @Param("species") String species);

    @Query("SELECT d FROM Data d WHERE d.species = :species AND d.weight >= :weight ORDER BY d.localDate")
    List<Data> findBySpeciesAndMinWeight(@Param("species") String species, @Param("weight") double weight);

    @Query("SELECT d FROM Data d WHERE d.species IN (:species) ORDER BY d.localDate")
    List<Data> findAllByMultipleSpecies(@Param("species") String[] species);
}