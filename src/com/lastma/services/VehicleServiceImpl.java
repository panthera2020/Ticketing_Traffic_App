package com.lastma.services;

import com.lastma.data.models.Vehicle;
import com.lastma.data.models.VehicleType;
import com.lastma.data.repositories.DriverRepository;
import com.lastma.data.repositories.DriverRepositoryImpl;
import com.lastma.data.repositories.VehicleRepository;
import com.lastma.data.repositories.VehicleRepositoryImpl;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class VehicleServiceImpl implements  VehicleService {
    private VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
    private DriverRepository driverRepository = new DriverRepositoryImpl();

    @Override
    public int register(String make, String model, String year, VehicleType vehicleType, int driverId,String plateNumber) {
        validate(driverId);
        validate(plateNumber);
        Vehicle newVehicle = new Vehicle();
        newVehicle.setMake(make);
        newVehicle.setModel(model);
        newVehicle.setYear(Year.parse(year));
        newVehicle.setType(vehicleType);
        newVehicle.setDriverId(driverId);
        newVehicle.setPlateNumber(plateNumber);
        return vehicleRepository.save(newVehicle).getId();
    }

    @Override
    public Vehicle findVehicleBy(int vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

    @Override
    public Vehicle findVehicleBy(String plateNumber) {
        List<Vehicle> registeredVehicles = vehicleRepository.findAll();
        for (Vehicle vehicle : registeredVehicles) {
            if (vehicle.getPlateNumber().equals(plateNumber)) {
                return vehicle;
            }
        }
        throw new IllegalArgumentException("Vehicle with" + plateNumber + " doesn't exist");
    }

    @Override
    public List<Vehicle> findAllVehiclesOwnedBy(int driverId) {
        List<Vehicle> ownedVehicles = new ArrayList<>();
        List<Vehicle> registeredVehicles = vehicleRepository.findAll();
        for (Vehicle vehicle : registeredVehicles) {
            if (vehicle.getDriverId() == driverId) { ownedVehicles.add(vehicle);}
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
