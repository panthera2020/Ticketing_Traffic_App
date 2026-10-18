package com.lastma.data.repositories;

import com.lastma.data.models.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleRepositoryImplTest {
    private VehicleRepositoryImpl vehicleRepository;
    private Vehicle vehicle;

    @BeforeEach
    public void setUp() {
        vehicleRepository = new VehicleRepositoryImpl();
        vehicle = new Vehicle();
    }

    @Test
    public void testThatWhenISaveOneVehicle_CountIsOne() {
        vehicleRepository.save(vehicle);
        assertEquals(1L, vehicleRepository.count());
    }

    @Test
    public void testThatWhenIAddTwoVehicles_CountIsTwo() {
        assertEquals(0L, vehicleRepository.count());
        vehicleRepository.save(new Vehicle());
        vehicleRepository.save(vehicle);
        assertEquals(2L, vehicleRepository.count());
    }

    @Test
    public void testThatWhenISaveVehicle_IdIsSet() {
        vehicleRepository.save(vehicle);
        assertEquals(1, vehicle.getId());
    }

    @Test
    public void testThatWhenISaveTwoVehicles_IdsAreIncremented() {
        Vehicle vehicleTwo = new Vehicle();
        vehicleRepository.save(vehicle);
        vehicleRepository.save(vehicleTwo);

        assertEquals(1, vehicle.getId());
        assertEquals(2, vehicleTwo.getId());
    }

    @Test
    public void testThatWhenISaveVehicle_SavedVehicleIsReturned() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        assertSame(vehicle, savedVehicle);
    }

    @Test
    public void testThatWhenISaveVehicle_AndISaveSameVehicle_VehicleIsUpdated() {
        Vehicle newVehicle = vehicleRepository.save(vehicle);
        Vehicle sameVehicle = vehicleRepository.findById(newVehicle.getId());
        sameVehicle.setMake("Toyota");
        vehicleRepository.save(sameVehicle);

        assertEquals(1, vehicleRepository.count());
        assertEquals("Toyota", vehicleRepository.findById(newVehicle.getId()).getMake());
    }

    @Test
    public void testThatWhenISaveSameVehicleTwice_CountDoesNotIncrease() {
        vehicleRepository.save(vehicle);
        vehicleRepository.save(vehicle);

        assertEquals(1L, vehicleRepository.count());
        assertEquals(1, vehicle.getId());
    }

    @Test
    public void testThatWhenISaveVehicleWithExistingId_ThatVehicleIsReplaced() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        Vehicle replacement = new Vehicle();
        replacement.setId(savedVehicle.getId());
        replacement.setMake("Toyota");
        vehicleRepository.save(replacement);

        assertEquals(1L, vehicleRepository.count());
        assertSame(replacement, vehicleRepository.findById(savedVehicle.getId()));
        assertEquals("Toyota", vehicleRepository.findById(savedVehicle.getId()).getMake());
    }

    @Test
    public void testThatWhenIUpdateSecondVehicle_FirstVehicleIsNotAffected() {
        Vehicle vehicleTwo = new Vehicle();
        vehicle.setMake("Honda");
        vehicleRepository.save(vehicle);
        vehicleRepository.save(vehicleTwo);

        Vehicle replacement = new Vehicle();
        replacement.setId(vehicleTwo.getId());
        replacement.setMake("Toyota");
        vehicleRepository.save(replacement);

        assertEquals(2L, vehicleRepository.count());
        assertEquals("Honda", vehicleRepository.findById(vehicle.getId()).getMake());
        assertEquals("Toyota", vehicleRepository.findById(vehicleTwo.getId()).getMake());
    }

    @Test
    public void testThatWhenISaveAfterDeletingAVehicle_NewIdIsUnique() {
        Vehicle vehicleTwo = new Vehicle();
        Vehicle vehicleThree = new Vehicle();
        vehicleRepository.save(vehicle);
        vehicleRepository.save(vehicleTwo);
        vehicleRepository.delete(vehicle.getId());
        vehicleRepository.save(vehicleThree);

        assertNotEquals(vehicleTwo.getId(), vehicleThree.getId());
        assertEquals(2L, vehicleRepository.count());
    }

    @Test
    public void testThatWhenIFindById_VehicleIsFound() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        assertEquals(savedVehicle, vehicleRepository.findById(savedVehicle.getId()));
    }

    @Test
    public void testThatWhenIFindByIncorrectId_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> vehicleRepository.findById(-1));
    }

    @Test
    public void testThatWhenIFindById_CorrectVehicleIsReturnedFromMany() {
        Vehicle vehicleTwo = new Vehicle();
        Vehicle vehicleThree = new Vehicle();
        vehicleRepository.save(vehicle);
        vehicleRepository.save(vehicleTwo);
        vehicleRepository.save(vehicleThree);

        assertSame(vehicleTwo, vehicleRepository.findById(vehicleTwo.getId()));
        assertSame(vehicleThree, vehicleRepository.findById(vehicleThree.getId()));
    }

    @Test
    public void testThatWhenIFindByIdOnEmptyRepository_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> vehicleRepository.findById(1));
    }

    @Test
    public void testThatWhenIFindDeletedVehicle_ErrorIsThrown() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        int vehicleId = savedVehicle.getId();
        vehicleRepository.delete(vehicleId);

        assertThrows(IllegalArgumentException.class, () -> vehicleRepository.findById(vehicleId));
    }

    @Test
    public void testThatWhenIFindAll_AllVehiclesAreFound() {
        Vehicle vehicleTwo = new Vehicle();
        Vehicle vehicleThree = new Vehicle();
        Vehicle[] allVehicles = {vehicle, vehicleTwo, vehicleThree};
        for (Vehicle eachVehicle : allVehicles) {
            vehicleRepository.save(eachVehicle);
        }
        List<Vehicle> foundVehicles = vehicleRepository.findAll();
        assertEquals(3, foundVehicles.size());
        for (int index = 0; index < allVehicles.length; index++) {
            assertSame(allVehicles[index], foundVehicles.get(index));
        }
    }

    @Test
    public void testThatWhenIFindAll_AndNoSavedVehicles_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> vehicleRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteOneVehicle_FindAllDoesNotContainIt() {
        Vehicle vehicleTwo = new Vehicle();
        vehicleRepository.save(vehicle);
        vehicleRepository.save(vehicleTwo);
        vehicleRepository.delete(vehicle.getId());

        List<Vehicle> foundVehicles = vehicleRepository.findAll();
        assertEquals(1, foundVehicles.size());
        assertFalse(foundVehicles.contains(vehicle));
        assertTrue(foundVehicles.contains(vehicleTwo));
    }

    @Test
    public void testThatWhenIDeleteTheOnlyVehicle_FindAllThrowsError() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        vehicleRepository.delete(savedVehicle.getId());

        assertThrows(IllegalArgumentException.class, () -> vehicleRepository.findAll());
    }

    @Test
    public void testThatWhenIModifyListFromFindAll_RepositoryIsNotAffected() {
        vehicleRepository.save(vehicle);
        vehicleRepository.findAll().clear();

        assertEquals(1L, vehicleRepository.count());
        assertEquals(1, vehicleRepository.findAll().size());
    }

    @Test
    public void testThatWhenIDeleteVehicle_VehicleIsDeleted() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        assertEquals(1L, vehicleRepository.count());
        vehicleRepository.delete(savedVehicle.getId());
        assertEquals(0L, vehicleRepository.count());
    }

    @Test
    public void testThatWhenIDeleteVehicleWithInvalidId_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> vehicleRepository.delete(-1));
    }

    @Test
    public void testThatWhenIDeleteOneVehicle_OtherVehiclesRemain() {
        Vehicle vehicleTwo = new Vehicle();
        Vehicle vehicleThree = new Vehicle();
        vehicleRepository.save(vehicle);
        vehicleRepository.save(vehicleTwo);
        vehicleRepository.save(vehicleThree);

        vehicleRepository.delete(vehicleTwo.getId());

        assertEquals(2L, vehicleRepository.count());
        assertTrue(vehicleRepository.existById(vehicle.getId()));
        assertFalse(vehicleRepository.existById(vehicleTwo.getId()));
        assertTrue(vehicleRepository.existById(vehicleThree.getId()));
    }

    @Test
    public void testThatWhenIDeleteSameVehicleTwice_ErrorIsThrown() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        int vehicleId = savedVehicle.getId();
        vehicleRepository.delete(vehicleId);

        assertThrows(IllegalArgumentException.class, () -> vehicleRepository.delete(vehicleId));
        assertEquals(0L, vehicleRepository.count());
    }

    @Test
    public void testThatWhenIDeleteFromEmptyRepository_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> vehicleRepository.delete(1));
    }

    @Test
    public void testThatWhenIDeleteVehicle_CountDecreasesByOne() {
        Vehicle vehicleTwo = new Vehicle();
        vehicleRepository.save(vehicle);
        vehicleRepository.save(vehicleTwo);
        vehicleRepository.delete(vehicle.getId());

        assertEquals(1L, vehicleRepository.count());
    }

    @Test
    public void testThatWhenIDeleteAllVehicles_AllVehiclesAreDeleted() {
        vehicleRepository.save(vehicle);
        vehicleRepository.save(new Vehicle());
        vehicleRepository.save(new Vehicle());
        vehicleRepository.deleteAll();

        assertEquals(0L, vehicleRepository.count());
        assertThrows(IllegalArgumentException.class, () -> vehicleRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteAllOnEmptyRepository_NoErrorIsThrown() {
        assertDoesNotThrow(() -> vehicleRepository.deleteAll());
        assertEquals(0L, vehicleRepository.count());
    }

    @Test
    public void testThatWhenISaveAfterDeleteAll_IdStartsFromOneAgain() {
        vehicleRepository.save(vehicle);
        vehicleRepository.save(new Vehicle());
        vehicleRepository.deleteAll();

        Vehicle newVehicle = new Vehicle();
        vehicleRepository.save(newVehicle);

        assertEquals(1, newVehicle.getId());
        assertEquals(1L, vehicleRepository.count());
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueIfVehicleExists() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        assertTrue(vehicleRepository.existById(savedVehicle.getId()));
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsFalseIfVehicleDoesNotExist() {
        assertFalse(vehicleRepository.existById(-10));
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueForEachSavedVehicle() {
        Vehicle vehicleTwo = new Vehicle();
        Vehicle vehicleThree = new Vehicle();
        vehicleRepository.save(vehicle);
        vehicleRepository.save(vehicleTwo);
        vehicleRepository.save(vehicleThree);

        assertTrue(vehicleRepository.existById(vehicle.getId()));
        assertTrue(vehicleRepository.existById(vehicleTwo.getId()));
        assertTrue(vehicleRepository.existById(vehicleThree.getId()));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDelete_ReturnsFalse() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        int vehicleId = savedVehicle.getId();
        vehicleRepository.delete(vehicleId);

        assertFalse(vehicleRepository.existById(vehicleId));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDeleteAll_ReturnsFalse() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        int vehicleId = savedVehicle.getId();
        vehicleRepository.deleteAll();

        assertFalse(vehicleRepository.existById(vehicleId));
    }
}
