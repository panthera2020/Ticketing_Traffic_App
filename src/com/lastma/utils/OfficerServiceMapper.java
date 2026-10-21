package com.lastma.utils;

import com.lastma.data.models.Officer;
import com.lastma.dtos.requests.RegisterOfficerRequest;
import com.lastma.dtos.responses.FindAllOfficersResponse;
import com.lastma.dtos.responses.FindOfficerResponse;
import com.lastma.dtos.responses.RegisterOfficerResponse;

import java.util.ArrayList;
import java.util.List;

public class OfficerServiceMapper {
    public static Officer mapOfficer(RegisterOfficerRequest request) {
        Officer officer = new Officer();
        officer.setFullName(request.getFullName());
        officer.setOfficerNumber(request.getOfficerNumber());
        officer.setPassword(request.getPassword());
        return officer;
    }

    public static RegisterOfficerResponse mapRegistered(Officer officer) {
        RegisterOfficerResponse response = new RegisterOfficerResponse();
        response.setFullName(officer.getFullName());
        response.setOfficerNumber(officer.getOfficerNumber());
        response.setId(officer.getId());
        response.setActive(officer.isActive());
        return response;
    }

    public static FindOfficerResponse mapFound(Officer officer) {
        FindOfficerResponse response = new FindOfficerResponse();
        response.setFullName(officer.getFullName());
        response.setOfficerNumber(officer.getOfficerNumber());
        response.setId(officer.getId());
        response.setActive(officer.isActive());
        return response;
    }

    public static List<FindAllOfficersResponse> mapListOf(List<Officer> foundOfficers) {
        List<FindAllOfficersResponse> responseList = new ArrayList<>();
        for (Officer officer : foundOfficers) {
            FindAllOfficersResponse response = new FindAllOfficersResponse();
            response.setFullName(officer.getFullName());
            response.setOfficerNumber(officer.getOfficerNumber());
            response.setId(officer.getId());
            response.setActive(officer.isActive());
            responseList.add(response);
        }
        return responseList;
    }
}
