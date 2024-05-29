package com.of.scraper.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.of.scraper.entity.Fish;
import com.of.scraper.repository.FishRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FishServiceImpl implements FishService {

    private FishRepository fishRepository;
    
    

}
