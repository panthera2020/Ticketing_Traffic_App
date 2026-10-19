package com.lastma.services;

import com.lastma.data.models.Officer;
import com.lastma.dtos.responses.OfficerServiceResponse;

import java.util.List;

public interface OfficerService {
    OfficerServiceResponse registerOfficer(String fullName, String officerNumber, String password);

    OfficerServiceResponse findOfficer(int officerId);

    List<OfficerServiceResponse> findAllOfficers();

    void deactivate(int officerId);
}
