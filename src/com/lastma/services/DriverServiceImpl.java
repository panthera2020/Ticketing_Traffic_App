package com.lastma.services;

import com.lastma.data.models.Driver;
import com.lastma.data.repositories.DriverRepository;
import com.lastma.data.repositories.DriverRepositoryImpl;
import com.lastma.dtos.responses.DriverServiceResponse;

import java.util.ArrayList;
import java.util.List;

public class DriverServiceImpl implements DriverService {
    private DriverRepository driverRepository = new DriverRepositoryImpl();

    @Override
    public DriverServiceResponse register(String fullName, String mail, String phoneNumber, String password) {
        Driver driver = new Driver();
        driver.setFullName(fullName);
        driver.setEmail(mail);
        driver.setPhoneNumber(phoneNumber);
        driverRepository.save(driver);
        DriverServiceResponse response = new DriverServiceResponse();
        response.setId(driver.getId());
        response.setFullName(driver.getFullName());
        response.setEmail(driver.getEmail());
        response.setPhoneNumber(driver.getPhoneNumber());
        return response;
    }

    @Override
    public DriverServiceResponse findDriverBy(int driverId) {
        Driver foundDriver = driverRepository.findById(driverId);
        DriverServiceResponse response = new DriverServiceResponse();
        response.setId(foundDriver.getId());
        response.setFullName(foundDriver.getFullName());
        response.setEmail(foundDriver.getEmail());
        response.setPhoneNumber(foundDriver.getPhoneNumber());
        return response;
    }

    @Override
    public List<DriverServiceResponse> findAllDriver() {
        List<Driver> foundDrivers = driverRepository.findAll();
        List<DriverServiceResponse> responseList = new ArrayList<>();
        for( Driver driver : foundDrivers){
            DriverServiceResponse response = new DriverServiceResponse();
            response.setId(driver.getId());
            response.setFullName(driver.getFullName());
            response.setEmail(driver.getEmail());
            response.setPhoneNumber(driver.getPhoneNumber());
            responseList.add(response);
        }
        return responseList;
    }
}
