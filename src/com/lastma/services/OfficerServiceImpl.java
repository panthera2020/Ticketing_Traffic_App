package com.lastma.services;

import com.lastma.data.models.Officer;
import com.lastma.data.repositories.OfficerRepository;
import com.lastma.data.repositories.OfficerRepositoryImpl;
import com.lastma.dtos.requests.FindOfficerRequest;
import com.lastma.dtos.requests.RegisterOfficerRequest;
import com.lastma.dtos.responses.FindAllOfficersResponse;
import com.lastma.dtos.responses.FindOfficerResponse;
import com.lastma.dtos.responses.RegisterOfficerResponse;

import java.util.ArrayList;
import java.util.List;

import static com.lastma.utils.OfficerServiceMapper.*;

public class OfficerServiceImpl implements OfficerService {
    private OfficerRepository officerRepository = new OfficerRepositoryImpl();

    @Override
    public RegisterOfficerResponse registerOfficer(RegisterOfficerRequest request) {
        Officer newOfficer = mapOfficer(request);
        Officer officer = officerRepository.save(newOfficer);

        return mapRegistered(officer);
    }

    @Override
    public FindOfficerResponse findOfficer(FindOfficerRequest request) {
        validate(request.getOfficerId());
        Officer officer = officerRepository.findById(request.getOfficerId());
        return mapFound(officer);
    }

    @Override
    public List<FindAllOfficersResponse> findAllOfficers() {
        List<Officer> foundOfficers = officerRepository.findAll();

        return mapListOf(foundOfficers);
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
