package com.lastma.services;

import com.lastma.data.models.Officer;
import com.lastma.data.repositories.OfficerRepository;
import com.lastma.data.repositories.OfficerRepositoryImpl;
import com.lastma.dtos.responses.OfficerServiceResponse;

import java.util.ArrayList;
import java.util.List;

public class OfficerServiceImpl implements OfficerService {
    private OfficerRepository officerRepository = new OfficerRepositoryImpl();

    @Override
    public OfficerServiceResponse registerOfficer(String fullName, String officerNumber, String password) {
        Officer officer = new Officer();
        officer.setFullName(fullName);
        officer.setOfficerNumber(officerNumber);
        officer.setPassword(password);
        officer.activate();
        officerRepository.save(officer);
        OfficerServiceResponse response = new OfficerServiceResponse();
        response.setFullName(officer.getFullName());
        response.setOfficerNumber(officer.getOfficerNumber());
        response.setId(officer.getId());
        response.setActive(officer.isActive());
        return response;
    }

    @Override
    public OfficerServiceResponse findOfficer(int officerId) {
        Officer foundOfficer = officerRepository.findById(officerId);
        OfficerServiceResponse response = new OfficerServiceResponse();
        response.setFullName(foundOfficer.getFullName());
        response.setOfficerNumber(foundOfficer.getOfficerNumber());
        response.setId(foundOfficer.getId());
        response.setActive(foundOfficer.isActive());
        return response;
    }

    @Override
    public List<OfficerServiceResponse> findAllOfficers() {
        List<Officer> foundOfficers = officerRepository.findAll();
        List<OfficerServiceResponse> responseList = new ArrayList<>();
        for (Officer officer : foundOfficers) {
            OfficerServiceResponse response = new OfficerServiceResponse();
            response.setFullName(officer.getFullName());
            response.setOfficerNumber(officer.getOfficerNumber());
            response.setId(officer.getId());
            response.setActive(officer.isActive());
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public void deactivate(int officerId) {
        officerRepository.findById(officerId).deactivate();
    }
}
