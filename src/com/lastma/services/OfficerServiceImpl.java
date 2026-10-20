package com.lastma.services;

import com.lastma.data.models.Officer;
import com.lastma.data.repositories.OfficerRepository;
import com.lastma.data.repositories.OfficerRepositoryImpl;
import com.lastma.dtos.requests.RegisterDriverRequest;
import com.lastma.dtos.requests.RegisterOfficerRequest;
import com.lastma.dtos.responses.RegisterOfficerResponse;

import java.util.ArrayList;
import java.util.List;

public class OfficerServiceImpl implements OfficerService {
    private OfficerRepository officerRepository = new OfficerRepositoryImpl();

    @Override
    public RegisterOfficerResponse registerOfficer(RegisterOfficerRequest request) {
        Officer officer = new Officer();
        officer.setFullName(request.getFullName());
        officer.setOfficerNumber(request.getOfficerNumber());
        officer.setPassword(request.getPassword());
        Officer registeredOfficer = officerRepository.save(officer);
        RegisterOfficerResponse response = new RegisterOfficerResponse();
        response.setFullName(registeredOfficer.getFullName());
        response.setOfficerNumber(registeredOfficer.getOfficerNumber());
        response.setId(registeredOfficer.getId());
        response.setActive(registeredOfficer.isActive());
        return response;
    }

    @Override
    public RegisterOfficerResponse findOfficer(int officerId) {
        validate(officerId);
        Officer foundOfficer = officerRepository.findById(officerId);
        RegisterOfficerResponse response = new RegisterOfficerResponse();
        response.setFullName(foundOfficer.getFullName());
        response.setOfficerNumber(foundOfficer.getOfficerNumber());
        response.setId(foundOfficer.getId());
        response.setActive(foundOfficer.isActive());
        return response;
    }

    @Override
    public List<RegisterOfficerResponse> findAllOfficers() {
        List<Officer> foundOfficers = officerRepository.findAll();
        List<RegisterOfficerResponse> responseList = new ArrayList<>();
        for (Officer officer : foundOfficers) {
            RegisterOfficerResponse response = new RegisterOfficerResponse();
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
        validate(officerId);
        officerRepository.findById(officerId).deactivate();
    }

    private void validate(int officerId) {
        if (officerRepository.findById(officerId) == null) throw new IllegalArgumentException("Officer with id " + officerId + " not found");
    }
}
