package com.of.scraper.repository;

import com.of.scraper.entity.Fish;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface FishRepository extends JpaRepository<Fish, UUID>{
    List<Fish> findAll(Sort sort);
    List<Fish> findByName(String name);
    List<Fish> findByNameAndSpecies(String name, String species);
    List<Fish> findBySpecies(String species, Sort sort);

    @Query("SELECT COUNT(d) FROM Data d WHERE d.name = :name AND d.species = :species")
    int getCountByNameAndSpecies(@Param("name") String name, @Param("species") String species);

    @Query("SELECT SUM(d.weight) FROM Data d WHERE d.name = :name AND d.species = :species")
    Double getTotalWeightByNameAndSpecies(@Param("name") String name, @Param("species") String species);

    @Query("SELECT d FROM Data d WHERE d.species = :species AND d.weight >= :weight ORDER BY d.localDate")
    List<Fish> findBySpeciesAndMinWeight(@Param("species") String species, @Param("weight") double weight);

    @Query("SELECT d FROM Data d WHERE d.species IN (:species) ORDER BY d.localDate")
    List<Fish> findAllByMultipleSpecies(@Param("species") String[] species);
}
