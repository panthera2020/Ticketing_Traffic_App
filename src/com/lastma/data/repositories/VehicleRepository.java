package com.lastma.data.repositories;

import com.lastma.data.models.Vehicle;

import java.util.List;

public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);

    long count();

    Vehicle findById(int id);

    List<Vehicle> findAll();

    void delete(int vehicleId);

    void deleteAll();

    boolean existById(int vehicleId);

}
