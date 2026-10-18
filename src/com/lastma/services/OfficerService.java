package com.lastma.services;

import com.lastma.data.models.Officer;

import java.util.List;

public interface OfficerService {
    int registerOfficer(String fullName, String officerNumber, String password);

    Officer findOfficer(int officerId);

    List<Officer> findAllOfficers();

    void deactivate(int officerId);
}
