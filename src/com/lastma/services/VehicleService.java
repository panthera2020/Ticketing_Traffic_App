package com.lastma.services;

import com.lastma.data.models.Vehicle;
import com.lastma.data.models.VehicleType;

import java.util.List;


public interface VehicleService {
    int register(String make, String model, String year, VehicleType vehicleType, int driverId,String plateNumber);

    Vehicle findVehicleBy(int vehicleId);

    Vehicle findVehicleBy(String plateNumber);

    List<Vehicle> findAllVehiclesOwnedBy(int driverId);
}
