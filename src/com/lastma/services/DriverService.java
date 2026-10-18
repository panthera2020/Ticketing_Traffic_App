package com.lastma.services;

import com.lastma.data.models.Driver;

import java.util.List;

public interface DriverService {
    int register(String fullName, String mail, String phoneNumber, String password);

    Driver findDriverBy(int driverId);

    List<Driver> findAllDriver();
}
