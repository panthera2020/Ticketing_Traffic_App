package com.lastma.services;

import com.lastma.data.models.Vehicle;
import com.lastma.data.models.VehicleType;
import com.lastma.data.repositories.DriverRepository;
import com.lastma.data.repositories.DriverRepositoryImpl;
import com.lastma.data.repositories.VehicleRepository;
import com.lastma.data.repositories.VehicleRepositoryImpl;
import com.lastma.dtos.responses.RegisterVehicleResponse;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class VehicleServiceImpl implements  VehicleService {
    private VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
    private DriverRepository driverRepository = new DriverRepositoryImpl();

    @Override
    public RegisterVehicleResponse register(String make, String model, String year, VehicleType vehicleType, int driverId, String plateNumber) {
        validate(driverId);
        validate(plateNumber);
        Vehicle newVehicle = new Vehicle();
        newVehicle.setMake(make);
        newVehicle.setModel(model);
        newVehicle.setYear(Year.parse(year));
        newVehicle.setType(vehicleType);
        newVehicle.setDriverId(driverId);
        newVehicle.setPlateNumber(plateNumber);
        Vehicle savedVehicle = vehicleRepository.save(newVehicle);
        RegisterVehicleResponse response = new RegisterVehicleResponse();
        response.setId(savedVehicle.getId());
        response.setMake(savedVehicle.getMake());
        response.setModel(savedVehicle.getModel());
        response.setYear(savedVehicle.getYear());
        response.setType(savedVehicle.getType());
        response.setDriverId(savedVehicle.getDriverId());
        response.setPlateNumber(savedVehicle.getPlateNumber());
        return response;
    }



    @Override
    public RegisterVehicleResponse findVehicleBy(int vehicleId) {
        Vehicle vehicleFound = vehicleRepository.findById(vehicleId);
        RegisterVehicleResponse response = new RegisterVehicleResponse();
        response.setId(vehicleFound.getId());
        response.setMake(vehicleFound.getMake());
        response.setModel(vehicleFound.getModel());
        response.setYear(vehicleFound.getYear());
        response.setType(vehicleFound.getType());
        response.setDriverId(vehicleFound.getDriverId());
        response.setPlateNumber(vehicleFound.getPlateNumber());
        return response;
    }

    @Override
    public RegisterVehicleResponse findVehicleBy(String plateNumber) {
        List<Vehicle> registeredVehicles = vehicleRepository.findAll();
        for (Vehicle vehicle : registeredVehicles) {
            if (vehicle.getPlateNumber().equals(plateNumber)) {
                RegisterVehicleResponse response = new RegisterVehicleResponse();
                response.setId(vehicle.getId());
                response.setMake(vehicle.getMake());
                response.setModel(vehicle.getModel());
                response.setYear(vehicle.getYear());
                response.setType(vehicle.getType());
                response.setDriverId(vehicle.getDriverId());
                response.setPlateNumber(vehicle.getPlateNumber());
                return response;
            }
        }
        throw new IllegalArgumentException("Vehicle with" + plateNumber + " doesn't exist");
    }

    @Override
    public List<RegisterVehicleResponse> findAllVehiclesOwnedBy(int driverId) {
        List<RegisterVehicleResponse> ownedVehicles = new ArrayList<>();
        List<Vehicle> registeredVehicles = vehicleRepository.findAll();
        for (Vehicle vehicle : registeredVehicles) {
            if (vehicle.getDriverId() == driverId) {
                RegisterVehicleResponse response = new RegisterVehicleResponse();
                response.setId(vehicle.getId());
                response.setMake(vehicle.getMake());
                response.setModel(vehicle.getModel());
                response.setYear(vehicle.getYear());
                response.setType(vehicle.getType());
                response.setDriverId(vehicle.getDriverId());
                response.setPlateNumber(vehicle.getPlateNumber());
                ownedVehicles.add(response);
            }
        }
        return ownedVehicles;
    }

    private void validate(int driverId) {
        driverRepository.findById(driverId);
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
