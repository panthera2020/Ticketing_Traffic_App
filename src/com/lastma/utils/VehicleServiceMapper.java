package com.lastma.utils;

import com.lastma.data.models.Vehicle;
import com.lastma.dtos.requests.RegisterVehicleRequest;
import com.lastma.dtos.responses.FindAllVehicleOwnedByDriverResponse;
import com.lastma.dtos.responses.FindVehicleByIdResponse;
import com.lastma.dtos.responses.FindVehicleByPlateNumberResponse;
import com.lastma.dtos.responses.RegisterVehicleResponse;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class VehicleServiceMapper {

    public static Vehicle map(RegisterVehicleRequest request){
        Vehicle newVehicle = new Vehicle();
        newVehicle.setMake(request.getMake());
        newVehicle.setModel(request.getModel());
        newVehicle.setYear(Year.parse(request.getYear()));
        newVehicle.setType(request.getVehicleType());
        newVehicle.setDriverId(request.getDriverId());
        newVehicle.setPlateNumber(request.getPlateNumber());
        return newVehicle;
    }

    public static RegisterVehicleResponse mapRegistered(Vehicle vehicle){
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

    public static FindVehicleByIdResponse mapId(Vehicle foundVehicle){
        FindVehicleByIdResponse response = new FindVehicleByIdResponse();
        response.setId(foundVehicle.getId());
        response.setMake(foundVehicle.getMake());
        response.setModel(foundVehicle.getModel());
        response.setYear(foundVehicle.getYear());
        response.setType(foundVehicle.getType());
        response.setDriverId(foundVehicle.getDriverId());
        response.setPlateNumber(foundVehicle.getPlateNumber());
        return response;
    }

    public static FindVehicleByPlateNumberResponse mapPlateNumber(Vehicle foundVehicle){
        FindVehicleByPlateNumberResponse response = new FindVehicleByPlateNumberResponse();
        response.setId(foundVehicle.getId());
        response.setMake(foundVehicle.getMake());
        response.setModel(foundVehicle.getModel());
        response.setYear(foundVehicle.getYear());
        response.setType(foundVehicle.getType());
        response.setDriverId(foundVehicle.getDriverId());
        response.setPlateNumber(foundVehicle.getPlateNumber());
        return response;
    }

    public static List<FindAllVehicleOwnedByDriverResponse> mapListOf(List<Vehicle> registeredVehicles, int driverId){
        List<FindAllVehicleOwnedByDriverResponse> ownedVehicles = new ArrayList<>();
        for (Vehicle vehicle : registeredVehicles) {
            if (vehicle.getDriverId() == driverId) {
                FindAllVehicleOwnedByDriverResponse response = new FindAllVehicleOwnedByDriverResponse();
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
}
