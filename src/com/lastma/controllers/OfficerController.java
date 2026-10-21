package com.lastma.controllers;

import com.lastma.dtos.requests.RegisterOfficerRequest;
import com.lastma.dtos.responses.ApiResponse;

public class OfficerController {

    public ApiResponse registerOfficer(RegisterOfficerRequest request){
        try {
            return new ApiResponse(true, registerOfficer(request));
        }
        catch (Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }
}
