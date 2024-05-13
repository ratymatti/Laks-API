package com.of.scraper.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.of.scraper.entity.Data;
import java.util.List;


public interface DataRepository extends JpaRepository<Data, UUID>{
    List<Data> findByName(String name);
    List<Data> findAll();
    void deleteAllInBatch();
}