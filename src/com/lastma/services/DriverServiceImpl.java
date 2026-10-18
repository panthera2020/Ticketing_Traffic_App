package com.lastma.services;

import com.lastma.data.models.Driver;
import com.lastma.data.repositories.DriverRepository;
import com.lastma.data.repositories.DriverRepositoryImpl;

import java.util.List;

public class DriverServiceImpl implements DriverService {
    private DriverRepository driverRepository = new DriverRepositoryImpl();

    @Override
    public int register(String fullName, String mail, String phoneNumber, String password) {
        Driver driver = new Driver();
        driver.setFullName(fullName);
        driver.setEmail(mail);
        driver.setPhoneNumber(phoneNumber);
        return driverRepository.save(driver).getId();
    }

    @Override
    public Driver findDriverBy(int driverId) {
        return driverRepository.findById(driverId);
    }

    @Override
    public List<Driver> findAllDriver() {
        return driverRepository.findAll();
    }
}
