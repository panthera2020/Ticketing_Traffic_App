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
        driverRepository.deleteAll();
    }

    @Test
    public void testThatWhenISaveOneDriver_CountIsOne() {
        driverRepository.save(driver);
        assertEquals(1L,driverRepository.count());
    }

    @Test
    public void testThatWhenISaveDriver_AndISaveSameDriver_DriverIsUpdated() {
        Driver newDriver = driverRepository.save(driver);
        Driver sameDriver = driverRepository.findById(newDriver.getId());
        sameDriver.setFullName("Young John");
        driverRepository.save(sameDriver);

        assertEquals(1, driverRepository.count());
        assertEquals("Young John", driverRepository.findById(newDriver.getId()).getFullName());
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
    public void testThatWhenIFindByIncorrectId_returnsNull() {
        assertNull (driverRepository.findById(-1));
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
    public void testThatWHenIFindAll_AndNoSavedDrivers_returnsNull() {
        assertNull(driverRepository.findAll());
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
    public void testThatWhenIDeleteAllDrivers_AllDriversAreDeleted() {
        Driver driverTwo = new Driver();
        Driver driverThree = new Driver();
        driverRepository.save(driver);
        driverRepository.save(driverTwo);
        driverRepository.save(driverThree);
        driverRepository.deleteAll();
        assertEquals(0L, driverRepository.count());
        assertNull(driverRepository.findAll());
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

    @Test
    public void testThatWhenISaveTwoDrivers_IdsAreIncremented() {
        Driver driverTwo = new Driver();
        driverRepository.save(driver);
        driverRepository.save(driverTwo);

        assertEquals(1, driver.getId());
        assertEquals(2, driverTwo.getId());
    }

    @Test
    public void testThatWhenISaveDriver_SavedDriverIsReturned() {
        Driver savedDriver = driverRepository.save(driver);
        assertSame(driver, savedDriver);
    }

    @Test
    public void testThatWhenISaveSameDriverTwice_CountDoesNotIncrease() {
        driverRepository.save(driver);
        driverRepository.save(driver);

        assertEquals(1L, driverRepository.count());
        assertEquals(1, driver.getId());
    }

    @Test
    public void testThatWhenISaveDriverWithExistingId_ThatDriverIsReplaced() {
        Driver savedDriver = driverRepository.save(driver);

        Driver replacement = new Driver();
        replacement.setId(savedDriver.getId());
        replacement.setFullName("Young John");
        driverRepository.save(replacement);

        assertEquals(1L, driverRepository.count());
        assertSame(replacement, driverRepository.findById(savedDriver.getId()));
        assertEquals("Young John", driverRepository.findById(savedDriver.getId()).getFullName());
    }

    @Test
    public void testThatWhenIUpdateSecondDriver_FirstDriverIsNotAffected() {
        Driver driverTwo = new Driver();
        driver.setFullName("John Doe");
        driverRepository.save(driver);
        driverRepository.save(driverTwo);

        Driver replacement = new Driver();
        replacement.setId(driverTwo.getId());
        replacement.setFullName("Jane Smith");
        driverRepository.save(replacement);

        assertEquals(2L, driverRepository.count());
        assertEquals("John Doe", driverRepository.findById(driver.getId()).getFullName());
        assertEquals("Jane Smith", driverRepository.findById(driverTwo.getId()).getFullName());
    }

    @Test
    public void testThatWhenISaveAfterDeletingADriver_NewIdIsUnique() {
        Driver driverTwo = new Driver();
        Driver driverThree = new Driver();
        driverRepository.save(driver);
        driverRepository.save(driverTwo);
        driverRepository.delete(driver.getId());
        driverRepository.save(driverThree);

        assertNotEquals(driverTwo.getId(), driverThree.getId());
        assertEquals(2L, driverRepository.count());
    }

    @Test
    public void testThatWhenIFindById_CorrectDriverIsReturnedFromMany() {
        Driver driverTwo = new Driver();
        Driver driverThree = new Driver();
        driverRepository.save(driver);
        driverRepository.save(driverTwo);
        driverRepository.save(driverThree);

        assertSame(driverTwo, driverRepository.findById(driverTwo.getId()));
        assertSame(driverThree, driverRepository.findById(driverThree.getId()));
    }

    @Test
    public void testThatWhenIFindByIdOnEmptyRepository_returnsNull() {
        assertNull(driverRepository.findById(1));
    }

    @Test
    public void testThatWhenIFindDeletedDriver_returnsNull() {
        Driver savedDriver = driverRepository.save(driver);
        int driverId = savedDriver.getId();
        driverRepository.delete(driverId);

        assertNull(driverRepository.findById(driverId));
    }

    @Test
    public void testThatWhenIFindAll_CorrectNumberOfDriversIsReturned() {
        driverRepository.save(driver);
        driverRepository.save(new Driver());
        driverRepository.save(new Driver());

        assertEquals(3, driverRepository.findAll().size());
    }

    @Test
    public void testThatWhenIDeleteOneDriver_FindAllDoesNotContainIt() {
        Driver driverTwo = new Driver();
        driverRepository.save(driver);
        driverRepository.save(driverTwo);
        driverRepository.delete(driver.getId());

        List<Driver> foundDrivers = driverRepository.findAll();
        assertEquals(1, foundDrivers.size());
        assertFalse(foundDrivers.contains(driver));
        assertTrue(foundDrivers.contains(driverTwo));
    }

    @Test
    public void testThatWhenIDeleteTheOnlyDriver_FindAllReturnsNull() {
        Driver savedDriver = driverRepository.save(driver);
        driverRepository.delete(savedDriver.getId());

        assertNull( driverRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteOneDriver_OtherDriversRemain() {
        Driver driverTwo = new Driver();
        Driver driverThree = new Driver();
        driverRepository.save(driver);
        driverRepository.save(driverTwo);
        driverRepository.save(driverThree);

        driverRepository.delete(driverTwo.getId());

        assertEquals(2L, driverRepository.count());
        assertTrue(driverRepository.existById(driver.getId()));
        assertFalse(driverRepository.existById(driverTwo.getId()));
        assertTrue(driverRepository.existById(driverThree.getId()));
    }


    @Test
    public void testThatWhenIDeleteAllOnEmptyRepository_NoErrorIsThrown() {
        assertDoesNotThrow(() -> driverRepository.deleteAll());
        assertEquals(0L, driverRepository.count());
    }

    @Test
    public void testThatWhenISaveAfterDeleteAll_IdStartsFromOneAgain() {
        driverRepository.save(driver);
        driverRepository.save(new Driver());
        driverRepository.deleteAll();

        Driver newDriver = new Driver();
        driverRepository.save(newDriver);

        assertEquals(1, newDriver.getId());
        assertEquals(1L, driverRepository.count());
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueForEachSavedDriver() {
        Driver driverTwo = new Driver();
        Driver driverThree = new Driver();
        driverRepository.save(driver);
        driverRepository.save(driverTwo);
        driverRepository.save(driverThree);

        assertTrue(driverRepository.existById(driver.getId()));
        assertTrue(driverRepository.existById(driverTwo.getId()));
        assertTrue(driverRepository.existById(driverThree.getId()));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDelete_ReturnsFalse() {
        Driver savedDriver = driverRepository.save(driver);
        int driverId = savedDriver.getId();
        driverRepository.delete(driverId);

        assertFalse(driverRepository.existById(driverId));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDeleteAll_ReturnsFalse() {
        Driver savedDriver = driverRepository.save(driver);
        int driverId = savedDriver.getId();
        driverRepository.deleteAll();

        assertFalse(driverRepository.existById(driverId));
    }

    @Test
    public void testThatWhenIDeleteDriver_CountDecreasesByOne() {
        Driver driverTwo = new Driver();
        driverRepository.save(driver);
        driverRepository.save(driverTwo);
        driverRepository.delete(driver.getId());

        assertEquals(1L, driverRepository.count());
    }

}