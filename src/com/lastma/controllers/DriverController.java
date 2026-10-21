package com.lastma.controllers;

import com.lastma.dtos.requests.RegisterDriverRequest;
import com.lastma.dtos.responses.ApiResponse;

public class DriverController {

    public ApiResponse registerDriver(RegisterDriverRequest request){
        try {
            return new ApiResponse(true, registerDriver(request));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }
}
