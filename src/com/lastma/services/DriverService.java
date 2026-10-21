package com.lastma.services;

import com.lastma.dtos.requests.FindDriverRequest;
import com.lastma.dtos.requests.RegisterDriverRequest;
import com.lastma.dtos.responses.FindAllDriverResponse;
import com.lastma.dtos.responses.FindDriverResponse;
import com.lastma.dtos.responses.RegisterDriverResponse;

import java.util.List;

public interface DriverService {
    RegisterDriverResponse registerDriver(RegisterDriverRequest request);

    FindDriverResponse findDriverBy(FindDriverRequest request);

    List<FindAllDriverResponse> findAllDriver();
}
