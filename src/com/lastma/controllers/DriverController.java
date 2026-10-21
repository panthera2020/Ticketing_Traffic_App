package com.lastma.controllers;

import com.lastma.dtos.requests.RegisterDriverRequest;
import com.lastma.dtos.responses.ApiResponse;
import com.lastma.services.DriverService;
import com.lastma.services.DriverServiceImpl;

public class DriverController {
    private DriverService driverService = new DriverServiceImpl();

    public ApiResponse registerDriver(RegisterDriverRequest request){
        try {
            return new ApiResponse(true, driverService.registerDriver(request));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }
}
