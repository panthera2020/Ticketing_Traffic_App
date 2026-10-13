package com.lastma.data.repositories;

import com.lastma.data.models.Driver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DriverRepositoryImplTest {
    private DriverRepositoryImpl driverRepository;
    private Driver driver;

    @BeforeEach
    public void setUp() {
        driverRepository = new DriverRepositoryImpl();
        driver = new Driver();
    }

    @Test
    public void testThatWhenISaveOneDriver_CountIsOne() {
        driverRepository.save(driver);
        assertEquals(1L,driverRepository.count());
    }

    @Test
    public void testThatWhenIAddTwoDrivers_CountIsTwo() {
        assertEquals(0L,driverRepository.count());
        Driver driverTwo = new Driver();
        driverRepository.save(driverTwo);
        driverRepository.save(driver);
        assertEquals(2L,driverRepository.count());
    }

    @Test
    public void testThatWhenISaveDriver_IdIsSet() {
        driverRepository.save(driver);
        assertEquals(1,driver.getId());
    }

    @Test
    public void testThatWhenIFindById_DriverIsFound() {
        Driver savedDriver = driverRepository.save(driver);
        int driverId = savedDriver.getId();
        assertEquals(savedDriver, driverRepository.findById(driverId));
    }

    @Test
    public void testThatWhenIFindByIncorrectId_ErrorIsThrown() {
        assertThrows (IllegalArgumentException.class ,()-> driverRepository.findById(-1));
    }

    @Test
    public void testThatWhenIFindAll_AllDriversAreFound() {
        Driver driverTwo = new Driver();
        Driver driverThree = new Driver();
        Driver [] allDrivers = {driver,driverTwo,driverThree};
        for(Driver driver : allDrivers){
            driverRepository.save(driver);
        }
        List <Driver> foundDrivers = driverRepository.findAll();
        int count = 0;
        for(Driver driver : foundDrivers){
            assertEquals(driver,allDrivers[count]);
            count++;
        }
    }

    @Test
    public void testThatWHenIFindAll_AndNoSavedDrivers_ErrorIsThrown() {
        assertThrows (IllegalArgumentException.class ,()-> driverRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteDriver_DriverIsDeleted() {
        Driver savedDriver = driverRepository.save(driver);
        int driverId = savedDriver.getId();
        assertEquals(1L,driverRepository.count());
        driverRepository.delete(driverId);
        assertEquals(0L,driverRepository.count());
    }

    @Test
    public void testThatWhenIDeleteDriverWithInvalidId_ErrorIsThrown() {
        assertThrows (IllegalArgumentException.class ,()-> driverRepository.delete(-1));
    }

    @Test
    public void testThatWhenIDeleteAllDrivers_AllDriversAreDeleted() {
        Driver driverTwo = new Driver();
        Driver driverThree = new Driver();
        driverRepository.save(driver);
        driverRepository.save(driverTwo);
        driverRepository.save(driverThree);
        driverRepository.deleteAll();
        assertEquals(0L, driverRepository.count());
        assertThrows(IllegalArgumentException.class, ()-> driverRepository.findAll().size());
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueIfDriverExists() {
        Driver savedDriver = driverRepository.save(driver);
        int driverId = savedDriver.getId();
        assertTrue(driverRepository.existById(driverId));
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsFalseIfDriverDoesNotExist() {
        assertFalse(driverRepository.existById(-10));
    }


































//    @Test
//    public void testThatWhenIFindById_DriverIsFound() {
//        Driver savedDriver = driverRepository.save(driver);
//        int driverId = savedDriver.getId();
//        assertEquals(driver,driverRepository.findById(driverId));
//    }
//
//    @Test
//    public void testThatWhenIFindByIncorrectId_ErrorIsThrown() {
//        assertThrows(IllegalArgumentException.class, () -> driverRepository.findById(-1));
//    }
//
//    @Test
//    public void testThatWhenIFindAll_AllDriversAreReturned() {
//        Driver driverTwo = new Driver();
//        Driver driverThree = new Driver();
//        Driver [] drivers = {driver,driverTwo,driverThree};
//        for(Driver driver:drivers){
//            driverRepository.save(driver);
//        }
//        int count = 0;
//        List<Driver> foundDrivers = driverRepository.findAll();
//        for(Driver driver:foundDrivers){
//            assertEquals(driver,drivers[count]);
//            count++;
//        }
//
//    }

}