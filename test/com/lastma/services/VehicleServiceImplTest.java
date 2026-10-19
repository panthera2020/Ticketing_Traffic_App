package com.lastma.services;

import com.lastma.data.models.Vehicle;
import com.lastma.data.models.VehicleType;
import com.lastma.data.repositories.VehicleRepository;
import com.lastma.data.repositories.VehicleRepositoryImpl;
import com.lastma.dtos.responses.VehicleServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleServiceImplTest {
    private VehicleService vehicleService;
    private DriverService driverService;
    private VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
    private int driverId;

    @BeforeEach
    void setUp() {
        vehicleService = new VehicleServiceImpl();
        driverService = new DriverServiceImpl();
        vehicleRepository.deleteAll();
        driverId = driverService.register("Fullname","email@example.com","phoneNumber","Password").getId();
    }

    @Test
    public void testThatWhenVehicleIsRegistered_countIsOne() {
        assertEquals(0, vehicleRepository.count());
        vehicleService.register("make","model", "2020", VehicleType.SUV,driverId,"PLATENUMBER");
        assertEquals(1, vehicleRepository.count());
    }

    @Test
    public void testThatWhenVehicleIsRegisteredWithInvalidDriverId_errorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> vehicleService.register("make","model", "2020", VehicleType.SUV,2,"PLATENUMBER"));
    }

    @Test
    public void testThatOneDriverIdCanRegisterTwoVehicles() {
        assertEquals(0, vehicleRepository.count());
        vehicleService.register("make","model", "2020", VehicleType.SUV,driverId,"PLATENUMBER");
        vehicleService.register("make","model", "2015", VehicleType.CAR,driverId,"PLATENUMBER1");
        vehicleService.register("make","model", "2026", VehicleType.TRUCK,driverId,"PLATENUMBER2");
        assertEquals(3, vehicleRepository.count());
    }

    @Test
    public void testThatVehiclesCanNotBeRegisteredWithTheSamePlatNumber(){
        vehicleService.register("make","model", "2020", VehicleType.SUV,driverId,"PLATENUMBER");
        assertEquals(1, vehicleRepository.count());
        assertThrows(IllegalArgumentException.class, () -> vehicleService.register("make","model", "2020", VehicleType.SUV,driverId,"PLATENUMBER"));
    }

    @Test
    public void testThatWhenVehicleIsRegistered_vehicleCanBeFoundById(){
        int vehicleId = vehicleService.register("make","model", "2020", VehicleType.SUV,driverId,"PLATENUMBER").getId();
        VehicleServiceResponse savedVehicleByService = vehicleService.findVehicleBy(vehicleId);
        Vehicle savedVehicleInRepo = vehicleRepository.findById(vehicleId);
        assertEquals(savedVehicleByService.getId(), savedVehicleInRepo.getId());
    }

    @Test
    public void testThatWhenVehicleIsIsSearchedByInvalidId_errorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> vehicleService.findVehicleBy(-1));
    }

    @Test
    public void testThatWhenRegisteredVehicleCanBeFoundByPlateNumber(){
        int vehicleId = vehicleService.register("make","model", "2020", VehicleType.SUV,driverId,"PLATENUMBER").getId();
        VehicleServiceResponse savedVehicleByService = vehicleService.findVehicleBy("PLATENUMBER");
        Vehicle savedVehicleInRepo = vehicleRepository.findById(vehicleId);
        assertEquals(savedVehicleByService.getId(), savedVehicleInRepo.getId());
    }

    @Test
    public void testThatICanGetListOfVehiclesOwnedByADriverWithId(){
        int vehicleIdOne = vehicleService.register("make","model", "2020", VehicleType.SUV,driverId,"PLATENUMBER").getId();
        int vehicleIdTwo = vehicleService.register("make","model", "2015", VehicleType.CAR,driverId,"PLATENUMBER1").getId();
        int vehicleIdThree = vehicleService.register("make","model", "2026", VehicleType.TRUCK,driverId,"PLATENUMBER2").getId();
        int [] vehicleIds = {vehicleIdOne,vehicleIdTwo,vehicleIdThree};
        List<VehicleServiceResponse> driversVehicles = vehicleService.findAllVehiclesOwnedBy(driverId);
        int count = 0;
        for(VehicleServiceResponse vehicle : driversVehicles){
            assertEquals(vehicle.getId(),vehicleIds[count]);
            count++;
        }
    }

    @Test
    public void testThatWhenIFindListOfVehiclesOwnedByDriverWithInvalidId_errorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> vehicleService.findAllVehiclesOwnedBy(-1));
    }

}