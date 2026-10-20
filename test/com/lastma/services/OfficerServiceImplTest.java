package com.lastma.services;

import com.lastma.data.models.Officer;
import com.lastma.data.repositories.OfficerRepository;
import com.lastma.data.repositories.OfficerRepositoryImpl;
import com.lastma.dtos.requests.RegisterOfficerRequest;
import com.lastma.dtos.responses.RegisterOfficerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfficerServiceImplTest {
    private OfficerService officerService;
    private OfficerRepository  officerRepository = new OfficerRepositoryImpl();
    private RegisterOfficerRequest  registerRequest;
    private RegisterOfficerResponse registerOfficerResponse;

    @BeforeEach
    void setUp() {
        officerService = new OfficerServiceImpl();
        registerRequest = new RegisterOfficerRequest();
        registerRequest.setFullName("Full Name");
        registerRequest.setOfficerNumber("Officer Number");
        registerRequest.setPassword("Password");
        officerRepository.deleteAll();
    }

    @Test
    public void testThatWhenIRegisterOfficer_OfficerCountIsOne() {
        assertEquals(0,officerRepository.count());
        officerService.registerOfficer(registerRequest);
        assertEquals(1,officerRepository.count());
    }

    @Test
    public void testThatWhenIRegisterOfficer_idIsGenerated(){
        assertNotEquals(0,officerService.registerOfficer(registerRequest));
    }

    @Test
    public void testThatWhenIRegisterOfficer_iCanFindOfficerById(){
        int officerId = officerService.registerOfficer(registerRequest).getId();
        Officer savedOfficerInRepo = officerRepository.findById(officerId);
        RegisterOfficerResponse savedOfficerByService = officerService.findOfficer(officerId);
        assertEquals(savedOfficerInRepo.getId(),savedOfficerByService.getId());
    }

    @Test
    public void testThatWhenIRegisterOfficer_officerIsActive(){
        int officerId = officerService.registerOfficer(registerRequest).getId();
        RegisterOfficerResponse savedOfficer = officerService.findOfficer(officerId);
        assertTrue(savedOfficer.isActive());
    }

    @Test
    public void testThatWhenIFindOfficerByInvalidId_ErrorIsThrown(){
        assertThrows(IllegalArgumentException.class, () -> officerService.findOfficer(-1));
    }

    @Test
    public void testThatWhenIFindAllOfficer_listOfOfficers_IsReturned(){
        officerService.registerOfficer(registerRequest);
        officerService.registerOfficer(registerRequest);
        officerService.registerOfficer(registerRequest);
        assertEquals(officerRepository.findAll().size(),officerService.findAllOfficers().size());
    }

    @Test
    public void testThatWhenOfficerIsRegisteredOfficerCanBeDeactivated(){
        int officerId = officerService.registerOfficer(registerRequest).getId();
        officerService.deactivate(officerId);
        RegisterOfficerResponse savedOfficer = officerService.findOfficer(officerId);
        assertFalse(savedOfficer.isActive());
    }

}