package com.lastma.services;

import com.lastma.data.models.Vehicle;
import com.lastma.data.models.VehicleType;
import com.lastma.dtos.responses.VehicleServiceResponse;

import java.util.List;


public interface VehicleService {
    VehicleServiceResponse register(String make, String model, String year, VehicleType vehicleType, int driverId, String plateNumber);

    VehicleServiceResponse findVehicleBy(int vehicleId);

    VehicleServiceResponse findVehicleBy(String plateNumber);

    List<VehicleServiceResponse> findAllVehiclesOwnedBy(int driverId);
}
