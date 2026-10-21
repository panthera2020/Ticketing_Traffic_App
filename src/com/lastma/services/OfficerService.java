package com.lastma.services;

import com.lastma.dtos.requests.FindOfficerRequest;
import com.lastma.dtos.requests.RegisterOfficerRequest;
import com.lastma.dtos.responses.FindAllOfficersResponse;
import com.lastma.dtos.responses.FindOfficerResponse;
import com.lastma.dtos.responses.RegisterOfficerResponse;

import java.util.List;

public interface OfficerService {
    RegisterOfficerResponse registerOfficer(RegisterOfficerRequest request);

    FindOfficerResponse findOfficer(FindOfficerRequest request);

    List<FindAllOfficersResponse> findAllOfficers();

    void deactivate(int officerId);
}
