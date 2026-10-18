package com.lastma.data.repositories;

import com.lastma.data.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleRepositoryImpl implements VehicleRepository {
    private static List<Vehicle> vehicles = new ArrayList<>();
    private int nextId = 0;

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (existById(vehicle.getId())) {
            update(vehicle);
            return vehicle;
        }
        vehicle.setId(++nextId);
        vehicles.add(vehicle);
        return vehicle;
    }

    private void update(Vehicle vehicle) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId() == vehicle.getId()) {
                vehicles.set(i, vehicle);
                return;
            }
        }
    }

    @Override
    public long count() {
        return vehicles.size();
    }

    @Override
    public Vehicle findById(int id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() == id) {
                return vehicle;
            }
        }
        throw new IllegalArgumentException("Vehicle with id " + id + " not found");
    }

    @Override
    public List<Vehicle> findAll() {
        if (vehicles.isEmpty()) throw new IllegalArgumentException("No Vehicles saved");
        return new ArrayList<>(vehicles);
    }

    @Override
    public void delete(int id) {
        Vehicle foundVehicle = findById(id);
        vehicles.remove(foundVehicle);
    }

    @Override
    public void deleteAll() {
        vehicles.clear();
        nextId = 0;
    }

    @Override
    public boolean existById(int id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() == id) {
                return true;
            }
        }
        return false;
    }

}
