package com.lastma.services;

import com.lastma.data.models.Officer;
import com.lastma.data.repositories.OfficerRepository;
import com.lastma.data.repositories.OfficerRepositoryImpl;
import com.lastma.dtos.requests.FindOfficerRequest;
import com.lastma.dtos.requests.RegisterOfficerRequest;
import com.lastma.dtos.responses.FindOfficerResponse;
import com.lastma.dtos.responses.RegisterOfficerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfficerServiceImplTest {
    private OfficerService officerService;
    private OfficerRepository  officerRepository = new OfficerRepositoryImpl();
    private RegisterOfficerRequest  registerRequest;
    private FindOfficerRequest  findRequest;
    private RegisterOfficerResponse registerOfficerResponse;

    @BeforeEach
    void setUp() {
        officerService = new OfficerServiceImpl();
        registerRequest = new RegisterOfficerRequest();
        registerRequest.setFullName("Full Name");
        registerRequest.setOfficerNumber("Officer Number");
        registerRequest.setPassword("Password");
        officerRepository.deleteAll();
        findRequest = new FindOfficerRequest();
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
        findRequest.setOfficerId(officerId);
        Officer savedOfficerInRepo = officerRepository.findById(officerId);
        FindOfficerResponse savedOfficerByService = officerService.findOfficer(findRequest);
        assertEquals(savedOfficerInRepo.getId(),savedOfficerByService.getId());
    }

    @Test
    public void testThatWhenIRegisterOfficer_officerIsActive(){
        int officerId = officerService.registerOfficer(registerRequest).getId();
        findRequest.setOfficerId(officerId);
        FindOfficerResponse savedOfficer = officerService.findOfficer(findRequest);
        assertTrue(savedOfficer.isActive());
    }

    @Test
    public void testThatWhenIFindOfficerByInvalidId_ErrorIsThrown(){
        findRequest.setOfficerId(-1);
        assertThrows(IllegalArgumentException.class, () -> officerService.findOfficer(findRequest));
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
        findRequest.setOfficerId(officerId);
        officerService.deactivate(officerId);
        FindOfficerResponse savedOfficer = officerService.findOfficer(findRequest);
        assertFalse(savedOfficer.isActive());
    }

}