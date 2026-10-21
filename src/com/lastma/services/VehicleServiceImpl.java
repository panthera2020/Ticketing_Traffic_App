package com.lastma.services;

import com.lastma.data.models.Driver;
import com.lastma.data.models.Vehicle;
import com.lastma.data.repositories.DriverRepository;
import com.lastma.data.repositories.DriverRepositoryImpl;
import com.lastma.data.repositories.VehicleRepository;
import com.lastma.data.repositories.VehicleRepositoryImpl;
import com.lastma.dtos.requests.FindAllVehicleOwnedByDriverRequest;
import com.lastma.dtos.requests.FindVehicleByIdRequest;
import com.lastma.dtos.requests.FindVehicleByPlateNumberRequest;
import com.lastma.dtos.requests.RegisterVehicleRequest;
import com.lastma.dtos.responses.FindAllVehicleOwnedByDriverResponse;
import com.lastma.dtos.responses.FindVehicleByIdResponse;
import com.lastma.dtos.responses.FindVehicleByPlateNumberResponse;
import com.lastma.dtos.responses.RegisterVehicleResponse;

import java.util.List;

import static com.lastma.utils.VehicleServiceMapper.*;

public class VehicleServiceImpl implements  VehicleService {
    private VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
    private DriverRepository driverRepository = new DriverRepositoryImpl();

    @Override
    public RegisterVehicleResponse registerVehicle(RegisterVehicleRequest request) {
        validate(request.getDriverId());
        validate(request.getPlateNumber());
        Vehicle newVehicle = map(request);
        Vehicle vehicle = vehicleRepository.save(newVehicle);

        return mapRegistered(vehicle);
    }



    @Override
    public FindVehicleByIdResponse findVehicleBy(FindVehicleByIdRequest idRequest) {
        validate(idRequest.getVehicleId());
        Vehicle foundVehicle = vehicleRepository.findById(idRequest.getVehicleId());

        return mapId(foundVehicle);
    }

    @Override
    public FindVehicleByPlateNumberResponse findVehicleBy(FindVehicleByPlateNumberRequest plateNumberRequest) {
        List<Vehicle> registeredVehicles = vehicleRepository.findAll();
        for (Vehicle foundVehicle : registeredVehicles) {
            if (foundVehicle.getPlateNumber().equals(plateNumberRequest.getPlateNumber()))  return mapPlateNumber(foundVehicle);

        }
        throw new IllegalArgumentException("Vehicle with" + plateNumberRequest.getPlateNumber() + " doesn't exist");
    }

    @Override
    public List<FindAllVehicleOwnedByDriverResponse> findAllVehiclesOwnedBy(FindAllVehicleOwnedByDriverRequest request) {
        validate(request.getDriverId());
        List<Vehicle> registeredVehicles = vehicleRepository.findAll();
        int driverId = request.getDriverId();

        return mapListOf(registeredVehicles, driverId);
    }

    private void validate(int driverId) {
        Driver foundDriver = driverRepository.findById(driverId);
        if(foundDriver == null) throw new IllegalArgumentException("Driver with" + driverId + " doesn't exist");
    }

    private void validate(String plateNumber) {
        if(vehicleRepository.count() > 0){
            List<Vehicle> registeredVehicles = vehicleRepository.findAll();
            for (Vehicle vehicle : registeredVehicles) {
                if (vehicle.getPlateNumber().equals(plateNumber)) throw new IllegalArgumentException("Vehicle already exists with plate number " + plateNumber);
            }
        }
    }
}
