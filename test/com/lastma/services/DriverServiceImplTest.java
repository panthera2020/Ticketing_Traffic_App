package com.lastma.services;

import com.lastma.data.models.Driver;
import com.lastma.data.repositories.DriverRepository;
import com.lastma.data.repositories.DriverRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DriverServiceImplTest {
    private DriverService driverService;
    private DriverRepository driverRepository = new DriverRepositoryImpl();

    @BeforeEach
    public void setup() {
        driverService = new DriverServiceImpl();
        driverRepository.deleteAll();
    }

    @Test
    public void testThatWhenIRegisterDriver_CountIsOne(){
        assertEquals(0,driverRepository.count());
        driverService.register("Full Name","email@example.com", "PhoneNumber", "Password");
        assertEquals(1,driverRepository.count());
    }

    @Test
    public void testThatWhenDriverIsRegistered_idIsGenerated(){
        assertNotEquals(0,driverService.register("Full Name","email@example.com", "PhoneNumber", "Password"));
    }

    @Test
    public void testThatWhenDriverIsRegistered_iCanFindDriverById(){
        int driverId = driverService.register("Full Name","email@example.com", "PhoneNumber", "Password");
        Driver savedDriverInRepo = driverRepository.findById(driverId);
        Driver savedDriverByService = driverService.findDriverBy(driverId);
        assertEquals(savedDriverInRepo,savedDriverByService);
    }

    @Test
    public void testThatWhenIFindDriverByInvalidId_ErrorIsThrown(){
        assertThrows(IllegalArgumentException.class,() -> driverService.findDriverBy(-1));
    }

    @Test
    public void testThatWhenIFindAllDrivers_iCanFindAllDrivers(){
        driverService.register("Full Name","email@example.com", "PhoneNumber", "Password");
        driverService.register("Full Name1","email@example.com1", "PhoneNumber1", "Password1");
        driverService.register("Full Name2","email@example.com2", "PhoneNumber2", "Password2");
        assertEquals(driverRepository.findAll(),driverService.findAllDriver());
    }

}