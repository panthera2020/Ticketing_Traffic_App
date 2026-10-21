package com.lastma.controllers;

import com.lastma.dtos.requests.FindAllVehicleOwnedByDriverRequest;
import com.lastma.dtos.requests.FindVehicleByIdRequest;
import com.lastma.dtos.requests.FindVehicleByPlateNumberRequest;
import com.lastma.dtos.requests.RegisterVehicleRequest;
import com.lastma.dtos.responses.ApiResponse;
import com.lastma.services.VehicleService;
import com.lastma.services.VehicleServiceImpl;

public class VehicleController {
    private VehicleService vehicleService = new VehicleServiceImpl();

    public ApiResponse registerVehicle(RegisterVehicleRequest request){
        try {
            return new ApiResponse(true, vehicleService.registerVehicle(request));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findVehicleById(FindVehicleByIdRequest idRequest){
        try {
            return new ApiResponse(true, vehicleService.findVehicleBy(idRequest));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findVehicleByPlateNumber(FindVehicleByPlateNumberRequest plateNumberRequest){
        try {
            return new ApiResponse(true, vehicleService.findVehicleBy(plateNumberRequest));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findAllVehiclesOwnedByDriver(FindAllVehicleOwnedByDriverRequest request){
        try {
            return new ApiResponse(true, vehicleService.findAllVehiclesOwnedBy(request));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }
}
