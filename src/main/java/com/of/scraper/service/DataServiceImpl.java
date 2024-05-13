package com.of.scraper.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.of.scraper.entity.Data;
import com.of.scraper.repository.DataRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DataServiceImpl implements DataService {

    private DataRepository dataRepository;

    @Override
    public List<Data> saveAll(List<Data> dataList) {
        return dataRepository.saveAll(dataList);
    }

    @Override
    public List<Data> findByName(String name) {
        return dataRepository.findByName(name);
    }

    @Override
    public List<Data> findAll() {
        return dataRepository.findAll();
    }

    @Override
    public Data save(Data data) {
        return dataRepository.save(data);
    }

    @Override
    public void deleteAll() {
        dataRepository.deleteAllInBatch();
    }
}