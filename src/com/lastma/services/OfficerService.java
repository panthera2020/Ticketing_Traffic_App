package com.lastma.services;

import com.lastma.dtos.requests.RegisterDriverRequest;
import com.lastma.dtos.requests.RegisterOfficerRequest;
import com.lastma.dtos.responses.RegisterOfficerResponse;

import java.util.List;

public interface OfficerService {
    RegisterOfficerResponse registerOfficer(RegisterOfficerRequest request);

    RegisterOfficerResponse findOfficer(int officerId);

    List<RegisterOfficerResponse> findAllOfficers();

    void deactivate(int officerId);
}
