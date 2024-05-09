package com.of.scraper.service;

import org.springframework.stereotype.Service;

import com.of.scraper.entity.Data;

@Service
public class ScraperService {

    public Data scrapeData() {
        return new Data();
    }
    
}
