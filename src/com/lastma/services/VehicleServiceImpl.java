package com.lastma.services;

import com.lastma.data.models.Vehicle;
import com.lastma.data.models.VehicleType;
import com.lastma.data.repositories.DriverRepository;
import com.lastma.data.repositories.DriverRepositoryImpl;
import com.lastma.data.repositories.VehicleRepository;
import com.lastma.data.repositories.VehicleRepositoryImpl;
import com.lastma.dtos.responses.VehicleServiceResponse;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class VehicleServiceImpl implements  VehicleService {
    private VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
    private DriverRepository driverRepository = new DriverRepositoryImpl();

    @Override
    public VehicleServiceResponse register(String make, String model, String year, VehicleType vehicleType, int driverId, String plateNumber) {
        validate(driverId);
        validate(plateNumber);
        Vehicle newVehicle = new Vehicle();
        newVehicle.setMake(make);
        newVehicle.setModel(model);
        newVehicle.setYear(Year.parse(year));
        newVehicle.setType(vehicleType);
        newVehicle.setDriverId(driverId);
        newVehicle.setPlateNumber(plateNumber);
        vehicleRepository.save(newVehicle);
        VehicleServiceResponse response = new VehicleServiceResponse();
        response.setId(newVehicle.getId());
        response.setMake(newVehicle.getMake());
        response.setModel(newVehicle.getModel());
        response.setYear(newVehicle.getYear());
        response.setType(newVehicle.getType());
        response.setDriverId(newVehicle.getDriverId());
        response.setPlateNumber(newVehicle.getPlateNumber());
        return response;
    }



    @Override
    public VehicleServiceResponse findVehicleBy(int vehicleId) {
        Vehicle vehicleFound = vehicleRepository.findById(vehicleId);
        VehicleServiceResponse response = new VehicleServiceResponse();
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
    public VehicleServiceResponse findVehicleBy(String plateNumber) {
        List<Vehicle> registeredVehicles = vehicleRepository.findAll();
        for (Vehicle vehicle : registeredVehicles) {
            if (vehicle.getPlateNumber().equals(plateNumber)) {
                VehicleServiceResponse response = new VehicleServiceResponse();
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
    public List<VehicleServiceResponse> findAllVehiclesOwnedBy(int driverId) {
        List<VehicleServiceResponse> ownedVehicles = new ArrayList<>();
        List<Vehicle> registeredVehicles = vehicleRepository.findAll();
        for (Vehicle vehicle : registeredVehicles) {
            if (vehicle.getDriverId() == driverId) {
                VehicleServiceResponse response = new VehicleServiceResponse();
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
