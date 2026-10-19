package com.lastma.services;

import com.lastma.data.models.Driver;
import com.lastma.dtos.responses.DriverServiceResponse;

import java.util.List;

public interface DriverService {
    DriverServiceResponse register(String fullName, String mail, String phoneNumber, String password);

    DriverServiceResponse findDriverBy(int driverId);

    List<DriverServiceResponse> findAllDriver();
}
