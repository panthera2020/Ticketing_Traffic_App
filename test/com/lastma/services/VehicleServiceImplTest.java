package com.lastma.services;

import com.lastma.data.models.Vehicle;
import com.lastma.data.models.VehicleType;
import com.lastma.data.repositories.VehicleRepository;
import com.lastma.data.repositories.VehicleRepositoryImpl;
import com.lastma.dtos.requests.*;
import com.lastma.dtos.responses.FindAllVehicleOwnedByDriverResponse;
import com.lastma.dtos.responses.FindVehicleByIdResponse;
import com.lastma.dtos.responses.FindVehicleByPlateNumberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleServiceImplTest {
    private VehicleService vehicleService;
    private DriverService driverService;
    private VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
    private int driverId;
    private RegisterVehicleRequest vehicleRequest;


    @BeforeEach
    void setUp() {
        vehicleService = new VehicleServiceImpl();
        driverService = new DriverServiceImpl();
        vehicleRepository.deleteAll();
        RegisterDriverRequest driverRequest = new RegisterDriverRequest();
        driverRequest.setFullName("Full Name");
        driverRequest.setPassword("password");
        driverRequest.setPhoneNumber("123456789");
        driverRequest.setMail("email@example.com");
        vehicleRequest = new RegisterVehicleRequest();
        driverId = driverService.registerDriver(driverRequest).getId();
        vehicleRequest.setDriverId(driverId);
        vehicleRequest.setMake("Make Name");
        vehicleRequest.setModel("Model Name");
        vehicleRequest.setYear("2020");
        vehicleRequest.setVehicleType(VehicleType.SUV);
        vehicleRequest.setPlateNumber("Plate Number");
    }

    @Test
    public void testThatWhenVehicleIsRegistered_countIsOne() {
        assertEquals(0, vehicleRepository.count());
        vehicleService.register(vehicleRequest);
        assertEquals(1, vehicleRepository.count());
    }

    @Test
    public void testThatWhenVehicleIsRegisteredWithInvalidDriverId_errorIsThrown() {
        vehicleRequest.setDriverId(2);
        assertThrows(IllegalArgumentException.class, () -> vehicleService.register(vehicleRequest));
    }

    @Test
    public void testThatOneDriverIdCanRegisterTwoVehicles() {
        assertEquals(0, vehicleRepository.count());
        vehicleService.register(vehicleRequest);
        vehicleRequest.setVehicleType(VehicleType.CAR);
        vehicleRequest.setPlateNumber("PLATENUMBER1");
        vehicleRequest.setYear("2015");
        vehicleService.register(vehicleRequest);
        vehicleRequest.setVehicleType(VehicleType.TRUCK);
        vehicleRequest.setPlateNumber("PLATENUMBER2");
        vehicleRequest.setYear("2026");
        vehicleService.register(vehicleRequest);
        assertEquals(3, vehicleRepository.count());
    }

    @Test
    public void testThatVehiclesCanNotBeRegisteredWithTheSamePlateNumber(){
        vehicleService.register(vehicleRequest);
        assertEquals(1, vehicleRepository.count());
        assertThrows(IllegalArgumentException.class, () -> vehicleService.register(vehicleRequest));
    }

    @Test
    public void testThatWhenVehicleIsRegistered_vehicleCanBeFoundById(){
        int vehicleId = vehicleService.register(vehicleRequest).getId();
        FindVehicleByIdRequest vehicleIdRequest = new FindVehicleByIdRequest();
        vehicleIdRequest.setVehicleId(vehicleId);
        FindVehicleByIdResponse savedVehicleByService = vehicleService.findVehicleBy(vehicleIdRequest);
        Vehicle savedVehicleInRepo = vehicleRepository.findById(vehicleId);
        assertEquals(savedVehicleByService.getId(), savedVehicleInRepo.getId());
    }

    @Test
    public void testThatWhenVehicleIsIsSearchedByInvalidId_errorIsThrown() {
        FindVehicleByIdRequest vehicleIdRequest = new FindVehicleByIdRequest();
        vehicleIdRequest.setVehicleId(-1);
        assertThrows(IllegalArgumentException.class, () -> vehicleService.findVehicleBy(vehicleIdRequest));
    }

    @Test
    public void testThatWhenRegisteredVehicleCanBeFoundByPlateNumber(){
        int vehicleId = vehicleService.register(vehicleRequest).getId();
        FindVehicleByPlateNumberRequest vehiclePlateNumberRequest = new FindVehicleByPlateNumberRequest();
        vehiclePlateNumberRequest.setPlateNumber("Plate Number");
        FindVehicleByPlateNumberResponse savedVehicleByService = vehicleService.findVehicleBy(vehiclePlateNumberRequest);
        Vehicle savedVehicleInRepo = vehicleRepository.findById(vehicleId);
        assertEquals(savedVehicleByService.getId(), savedVehicleInRepo.getId());
    }

    @Test
    public void testThatWhenIFindVehicleByUnregisteredPLateNumber_errorIsThrown() {
        vehicleService.register(vehicleRequest);
        FindVehicleByPlateNumberRequest vehiclePlateNumberRequest = new FindVehicleByPlateNumberRequest();
        vehiclePlateNumberRequest.setPlateNumber("Plate Number001");
        assertThrows(IllegalArgumentException.class, () -> vehicleService.findVehicleBy(vehiclePlateNumberRequest));
    }

    @Test
    public void testThatICanGetListOfVehiclesOwnedByADriverWithId(){
        int vehicleIdOne = vehicleService.register(vehicleRequest).getId();
        vehicleRequest.setPlateNumber(vehicleIdOne + "1");
        int vehicleIdTwo = vehicleService.register(vehicleRequest).getId();
        vehicleRequest.setPlateNumber(vehicleIdTwo + "2");
        int vehicleIdThree = vehicleService.register(vehicleRequest).getId();
        int [] vehicleIds = {vehicleIdOne,vehicleIdTwo,vehicleIdThree};
        FindAllVehicleOwnedByDriverRequest vehicleOwnedRequest = new FindAllVehicleOwnedByDriverRequest();
        vehicleOwnedRequest.setDriverId(driverId);
        List<FindAllVehicleOwnedByDriverResponse> driversVehicles = vehicleService.findAllVehiclesOwnedBy(vehicleOwnedRequest);
        int count = 0;
        for(FindAllVehicleOwnedByDriverResponse vehicle : driversVehicles){
            assertEquals(vehicle.getId(),vehicleIds[count]);
            count++;
        }
    }

    @Test
    public void testThatWhenIFindListOfVehiclesOwnedByDriverWithInvalidId_errorIsThrown() {
        FindAllVehicleOwnedByDriverRequest vehicleOwnedRequest = new FindAllVehicleOwnedByDriverRequest();
        vehicleOwnedRequest.setDriverId(-1);
        assertThrows(IllegalArgumentException.class, () -> vehicleService.findAllVehiclesOwnedBy(vehicleOwnedRequest));
    }

}