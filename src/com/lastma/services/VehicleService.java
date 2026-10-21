package com.lastma.services;

import com.lastma.data.models.VehicleType;
import com.lastma.dtos.responses.RegisterVehicleResponse;

import java.util.List;


public interface VehicleService {
    RegisterVehicleResponse register(String make, String model, String year, VehicleType vehicleType, int driverId, String plateNumber);

    RegisterVehicleResponse findVehicleBy(int vehicleId);

    RegisterVehicleResponse findVehicleBy(String plateNumber);

    List<RegisterVehicleResponse> findAllVehiclesOwnedBy(int driverId);
}
