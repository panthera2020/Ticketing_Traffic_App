package com.lastma.data.repositories;

import com.lastma.data.models.Driver;

import java.util.List;

public interface DriverRepository {
    Driver save(Driver driver);

    long count();

    Driver findById(int id);

    List<Driver> findAll();

    void delete(int driverId);

    void deleteAll();

    boolean existById(int driverId);

}
