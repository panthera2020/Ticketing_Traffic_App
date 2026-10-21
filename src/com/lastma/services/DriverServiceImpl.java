package com.lastma.services;

import com.lastma.data.models.Driver;
import com.lastma.data.repositories.DriverRepository;
import com.lastma.data.repositories.DriverRepositoryImpl;
import com.lastma.dtos.requests.FindDriverRequest;
import com.lastma.dtos.requests.RegisterDriverRequest;
import com.lastma.dtos.responses.FindAllDriverResponse;
import com.lastma.dtos.responses.FindDriverResponse;
import com.lastma.dtos.responses.RegisterDriverResponse;

import java.util.List;

import static com.lastma.utils.DriverServiceMapper.*;

public class DriverServiceImpl implements DriverService {
    private DriverRepository driverRepository = new DriverRepositoryImpl();

    @Override
    public RegisterDriverResponse registerDriver(RegisterDriverRequest driverRequest) {
        Driver newDriver = map(driverRequest);
        Driver driver = driverRepository.save(newDriver);

        return mapRegistered(driver);
    }

    @Override
    public FindDriverResponse findDriverBy(FindDriverRequest request) {
        Driver driver = driverRepository.findById(request.getDriverId());
        validate(driver);

        return mapFound(driver);
    }

    @Override
    public List<FindAllDriverResponse> findAllDriver() {
        List<Driver> foundDrivers = driverRepository.findAll();

        return mapListOf(foundDrivers);
    }

    private void validate(Driver driver) {if(driver == null) throw new IllegalArgumentException("Driver is null");}
}
