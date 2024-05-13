package com.of.scraper.service;

import java.util.List;

import com.of.scraper.entity.Data;

public interface DataService {
    List<Data> saveAll(List<Data> dataList);
    List<Data> findByName(String name);
    List<Data> findAll();
    Data save(Data data);
    void deleteAll();
}
