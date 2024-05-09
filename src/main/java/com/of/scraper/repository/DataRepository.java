package com.of.scraper.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.of.scraper.entity.Data;

public interface DataRepository extends JpaRepository<Data, UUID>{
    
}
