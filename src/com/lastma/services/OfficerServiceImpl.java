package com.lastma.services;

import com.lastma.data.models.Officer;
import com.lastma.data.repositories.OfficerRepository;
import com.lastma.data.repositories.OfficerRepositoryImpl;

import java.util.List;

public class OfficerServiceImpl implements OfficerService {
    private OfficerRepository officerRepository = new OfficerRepositoryImpl();

    @Override
    public int registerOfficer(String fullName, String officerNumber, String password) {
        Officer officer = new Officer();
        officer.setFullName(fullName);
        officer.setOfficerNumber(officerNumber);
        officer.activate();
        return officerRepository.save(officer).getId();
    }

    @Override
    public Officer findOfficer(int officerId) {
        return officerRepository.findById(officerId);
    }

    @Override
    public List<Officer> findAllOfficers() {
        return officerRepository.findAll();
    }

    @Override
    public void deactivate(int officerId) {
        findOfficer(officerId).deactivate();
    }
}
