package com.lastma.data.repositories;

import com.lastma.data.models.Driver;

import java.util.ArrayList;
import java.util.List;

public class DriverRepositoryImpl implements DriverRepository {
    private static List<Driver> drivers = new ArrayList<>();
    private int numberOfDrivers = 0;

    @Override
    public Driver save(Driver driver) {
        if (existById(driver.getId())) {
            update(driver);
            return driver;
        }
        driver.setId(++numberOfDrivers);
        drivers.add(driver);
        return driver;
    }

    @Override
    public long count() {
        return drivers.size();
    }

    @Override
    public Driver findById(int id) {
        for(Driver driver : drivers){
            if(driver.getId() == id){
                return driver;
            }
        }
        throw new IllegalArgumentException("Driver with id " + id + " not found");
    }

    private void update(Driver driver) {
        for (int index = 0; index < drivers.size(); index++) {
            if (drivers.get(index).getId() == driver.getId()) {
                drivers.set(index, driver);
                break;
            }
        }
    }

    @Override
    public List<Driver> findAll() {
        if(count() == 0) throw new IllegalArgumentException("No Drivers saved");
        return drivers;
    }

    @Override
    public void delete(int id) {
        Driver foundDriver = findById(id);
        drivers.remove(foundDriver);
//        numberOfDrivers--;
    }

    @Override
    public void deleteAll() {
        drivers.clear();
        numberOfDrivers = 0;
    }

    @Override
    public boolean existById(int id) {
        for(Driver driver : drivers){
            if(driver.getId() == id){
                return true;
            }
        }
        return false;
    }
}
