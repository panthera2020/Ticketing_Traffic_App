package com.lastma.services;

import com.lastma.data.models.Driver;
import com.lastma.data.repositories.DriverRepository;
import com.lastma.data.repositories.DriverRepositoryImpl;
import com.lastma.dtos.requests.FindDriverRequest;
import com.lastma.dtos.requests.RegisterDriverRequest;
import com.lastma.dtos.responses.FindAllDriverResponse;
import com.lastma.dtos.responses.FindDriverResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DriverServiceImplTest {
    private DriverService driverService;
    private DriverRepository driverRepository = new DriverRepositoryImpl();
    private RegisterDriverRequest registerRequest;
    private FindDriverRequest findRequest;

    @BeforeEach
    public void setup() {
        driverService = new DriverServiceImpl();
        registerRequest = new RegisterDriverRequest();
        registerRequest.setFullName("Full Name");
        registerRequest.setMail("email@example.com");
        registerRequest.setPhoneNumber("12345");
        registerRequest.setPassword("password");
        findRequest = new FindDriverRequest();
        driverRepository.deleteAll();
    }

    @Test
    public void testThatWhenIRegisterDriver_CountIsOne(){
        assertEquals(0,driverRepository.count());
        driverService.registerDriver(registerRequest);
        assertEquals(1,driverRepository.count());
    }

    @Test
    public void testThatWhenDriverIsRegistered_idIsGenerated(){
        assertNotEquals(0,driverService.registerDriver(registerRequest));
    }

    @Test
    public void testThatWhenDriverIsRegistered_iCanFindDriverById(){
        int driverId = driverService.registerDriver(registerRequest).getId();

        findRequest.setDriverId(driverId);
        Driver savedDriverInRepo = driverRepository.findById(driverId);
        FindDriverResponse savedDriverByService = driverService.findDriverBy(findRequest);
        assertEquals(savedDriverInRepo.getId(),savedDriverByService.getId());
    }

    @Test
    public void testThatWhenIFindDriverByInvalidId_ErrorIsThrown(){
        findRequest.setDriverId(-1);
        assertThrows(IllegalArgumentException.class,() -> driverService.findDriverBy(findRequest));
    }

    @Test
    public void testThatWhenIFindAllDrivers_iCanFindAllDrivers(){
        driverService.registerDriver(registerRequest);
        driverService.registerDriver(registerRequest);
        driverService.registerDriver(registerRequest);
        assertEquals(driverRepository.findAll().size(),driverService.findAllDriver().size());
        List<FindAllDriverResponse> driversFromService = driverService.findAllDriver();
        List<Driver> driversFromRepo = driverRepository.findAll();
        for( int index = 0; index < driversFromRepo.size(); index++ ){
            assertEquals(driversFromRepo.get(index).getId(),driversFromService.get(index).getId());
            assertEquals(driversFromRepo.get(index).getFullName(),driversFromService.get(index).getFullName());
            assertEquals(driversFromRepo.get(index).getPhoneNumber(),driversFromService.get(index).getPhoneNumber());
        }
    }

}