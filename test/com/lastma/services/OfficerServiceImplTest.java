package com.lastma.services;

import com.lastma.data.models.Officer;
import com.lastma.data.repositories.OfficerRepository;
import com.lastma.data.repositories.OfficerRepositoryImpl;
import com.lastma.dtos.responses.OfficerServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfficerServiceImplTest {
    private OfficerService officerService;
    private OfficerRepository  officerRepository = new OfficerRepositoryImpl();

    @BeforeEach
    void setUp() {
        officerService = new OfficerServiceImpl();
        officerRepository.deleteAll();
    }

    @Test
    public void testThatWhenIRegisterOfficer_OfficerCountIsOne() {
        assertEquals(0,officerRepository.count());
        officerService.registerOfficer("Fullname","officerNumber","password");
        assertEquals(1,officerRepository.count());
    }

    @Test
    public void testThatWhenIRegisterOfficer_idIsGenerated(){
        assertNotEquals(0,officerService.registerOfficer("Fullname","officerNumber","password"));
    }

    @Test
    public void testThatWhenIRegisterOfficer_iCanFindOfficerById(){
        int officerId = officerService.registerOfficer("Fullname","officerNumber","password").getId();
        Officer savedOfficerInRepo = officerRepository.findById(officerId);
        OfficerServiceResponse savedOfficerByService = officerService.findOfficer(officerId);
        assertEquals(savedOfficerInRepo.getId(),savedOfficerByService.getId());
    }

    @Test
    public void testThatWhenIRegisterOfficer_officerIsActive(){
        int officerId = officerService.registerOfficer("Fullname", "officerNumber", "password").getId();
        OfficerServiceResponse savedOfficer = officerService.findOfficer(officerId);
        assertTrue(savedOfficer.isActive());
    }

    @Test
    public void testThatWhenIFindOfficerByInvalidId_ErrorIsThrown(){
        assertThrows(IllegalArgumentException.class, () -> officerService.findOfficer(-1));
    }

    @Test
    public void testThatWhenIFindAllOfficer_listOfOfficers_IsReturned(){
        officerService.registerOfficer("Fullname","officerNumber","password");
        officerService.registerOfficer("Fullname1","officerNumber1","password1");
        officerService.registerOfficer("Fullname2","officerNumber2","password2");
        assertEquals(officerRepository.findAll().size(),officerService.findAllOfficers().size());
    }

    @Test
    public void testThatWhenOfficerIsRegisteredOfficerCanBeDeactivated(){
        int officerId = officerService.registerOfficer("Fullname", "officerNumber", "password").getId();
        officerService.deactivate(officerId);
        OfficerServiceResponse savedOfficer = officerService.findOfficer(officerId);
        assertFalse(savedOfficer.isActive());
    }

}