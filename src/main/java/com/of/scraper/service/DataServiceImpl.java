package com.of.scraper.service;

import org.springframework.stereotype.Service;

import com.of.scraper.entity.Data;
import com.of.scraper.repository.DataRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DataServiceImpl implements DataService {

    private DataRepository dataRepository;

    @Override
    public Data saveData(Data data) {
        return dataRepository.save(data);
    }
}
