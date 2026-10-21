package com.lastma.controllers;

import com.lastma.dtos.requests.CreateOffenceRequest;
import com.lastma.dtos.requests.FindOffenceByCodeRequest;
import com.lastma.dtos.requests.FindOffenceByIdRequest;
import com.lastma.dtos.responses.ApiResponse;
import com.lastma.services.OffenceService;
import com.lastma.services.OffenceServiceImpl;

public class OffenceController {
    private OffenceService offenceService = new OffenceServiceImpl();

    public ApiResponse createOffenceWith(CreateOffenceRequest request){
        try {
            return new ApiResponse(true, offenceService.createOffenceWith(request));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findById(FindOffenceByIdRequest idRequest){
        try {
            return new ApiResponse(true,offenceService.findBy(idRequest));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findByCode(FindOffenceByCodeRequest codeRequest){
        try {
            return new ApiResponse(true,offenceService.findBy(codeRequest));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

}
