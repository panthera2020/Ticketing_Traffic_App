package com.lastma.data.repositories;

import com.lastma.data.models.Officer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OfficerRepositoryImplTest {
    private OfficerRepositoryImpl officerRepository;
    private Officer officer;
    private String driverName = "Young John";

    @BeforeEach
    public void setUp() {
        officerRepository = new OfficerRepositoryImpl();
        officer = new Officer();
        officerRepository.deleteAll();
    }

    @Test
    public void testThatWhenISaveOneOfficer_CountIsOne() {
        officerRepository.save(officer);
        assertEquals(1L, officerRepository.count());
    }

    @Test
    public void testThatWhenIAddTwoOfficers_CountIsTwo() {
        assertEquals(0L, officerRepository.count());
        officerRepository.save(new Officer());
        officerRepository.save(officer);
        assertEquals(2L, officerRepository.count());
    }

    @Test
    public void testThatWhenISaveOfficer_IdIsSet() {
        officerRepository.save(officer);
        assertEquals(1, officer.getId());
    }

    @Test
    public void testThatWhenISaveTwoOfficers_IdsAreIncremented() {
        Officer officerTwo = new Officer();
        officerRepository.save(officer);
        officerRepository.save(officerTwo);

        assertEquals(1, officer.getId());
        assertEquals(2, officerTwo.getId());
    }

    @Test
    public void testThatWhenISaveOfficer_SavedOfficerIsReturned() {
        Officer savedOfficer = officerRepository.save(officer);
        assertSame(officer, savedOfficer);
    }

    @Test
    public void testThatWhenISaveOfficer_AndISaveSameOfficer_OfficerIsUpdated() {
        Officer newOfficer = officerRepository.save(officer);
        Officer sameOfficer = officerRepository.findById(newOfficer.getId());
        sameOfficer.setFullName(driverName);
        officerRepository.save(sameOfficer);

        assertEquals(1, officerRepository.count());
        assertEquals(driverName, officerRepository.findById(newOfficer.getId()).getFullName());
    }

    @Test
    public void testThatWhenISaveSameOfficerTwice_CountDoesNotIncrease() {
        officerRepository.save(officer);
        officerRepository.save(officer);

        assertEquals(1L, officerRepository.count());
        assertEquals(1, officer.getId());
    }

    @Test
    public void testThatWhenISaveOfficerWithExistingId_ThatOfficerIsReplaced() {
        Officer savedOfficer = officerRepository.save(officer);

        Officer replacement = new Officer();
        replacement.setId(savedOfficer.getId());
        replacement.setFullName(driverName);
        officerRepository.save(replacement);

        assertEquals(1L, officerRepository.count());
        assertSame(replacement, officerRepository.findById(savedOfficer.getId()));
        assertEquals(driverName, officerRepository.findById(savedOfficer.getId()).getFullName());
    }

    @Test
    public void testThatWhenIUpdateSecondOfficer_FirstOfficerIsNotAffected() {
        Officer officerTwo = new Officer();
        officer.setFullName("John Doe");
        officerRepository.save(officer);
        officerRepository.save(officerTwo);

        Officer replacement = new Officer();
        replacement.setId(officerTwo.getId());
        replacement.setFullName("Jane Smith");
        officerRepository.save(replacement);

        assertEquals(2L, officerRepository.count());
        assertEquals("John Doe", officerRepository.findById(officer.getId()).getFullName());
        assertEquals("Jane Smith", officerRepository.findById(officerTwo.getId()).getFullName());
    }

    @Test
    public void testThatWhenISaveAfterDeletingAnOfficer_NewIdIsUnique() {
        Officer officerTwo = new Officer();
        Officer officerThree = new Officer();
        officerRepository.save(officer);
        officerRepository.save(officerTwo);
        officerRepository.delete(officer.getId());
        officerRepository.save(officerThree);

        assertNotEquals(officerTwo.getId(), officerThree.getId());
        assertEquals(2L, officerRepository.count());
    }

    @Test
    public void testThatWhenIFindById_OfficerIsFound() {
        Officer savedOfficer = officerRepository.save(officer);
        assertEquals(savedOfficer, officerRepository.findById(savedOfficer.getId()));
    }

    @Test
    public void testThatWhenIFindByIncorrectId_returnsNull() {
        assertNull(officerRepository.findById(-1));
    }

    @Test
    public void testThatWhenIFindById_CorrectOfficerIsReturnedFromMany() {
        Officer officerTwo = new Officer();
        Officer officerThree = new Officer();
        officerRepository.save(officer);
        officerRepository.save(officerTwo);
        officerRepository.save(officerThree);

        assertSame(officerTwo, officerRepository.findById(officerTwo.getId()));
        assertSame(officerThree, officerRepository.findById(officerThree.getId()));
    }

    @Test
    public void testThatWhenIFindByIdOnEmptyRepository_returnsNull() {
        assertNull(officerRepository.findById(1));
    }

    @Test
    public void testThatWhenIFindDeletedOfficer_returnsNull() {
        Officer savedOfficer = officerRepository.save(officer);
        int officerId = savedOfficer.getId();
        officerRepository.delete(officerId);

        assertNull( officerRepository.findById(officerId));
    }

    @Test
    public void testThatWhenIFindAll_AllOfficersAreFound() {
        Officer officerTwo = new Officer();
        Officer officerThree = new Officer();
        Officer[] allOfficers = {officer, officerTwo, officerThree};
        for (Officer eachOfficer : allOfficers) {
            officerRepository.save(eachOfficer);
        }
        List<Officer> foundOfficers = officerRepository.findAll();
        assertEquals(3, foundOfficers.size());
        for (int index = 0; index < allOfficers.length; index++) {
            assertSame(allOfficers[index], foundOfficers.get(index));
        }
    }

    @Test
    public void testThatWhenIFindAll_AndNoSavedOfficers_returnsNull() {
        assertNull(officerRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteOneOfficer_FindAllDoesNotContainIt() {
        Officer officerTwo = new Officer();
        officerRepository.save(officer);
        officerRepository.save(officerTwo);
        officerRepository.delete(officer.getId());

        List<Officer> foundOfficers = officerRepository.findAll();
        assertEquals(1, foundOfficers.size());
        assertFalse(foundOfficers.contains(officer));
        assertTrue(foundOfficers.contains(officerTwo));
    }

    @Test
    public void testThatWhenIDeleteTheOnlyOfficer_FindAllReturnsNull() {
        Officer savedOfficer = officerRepository.save(officer);
        officerRepository.delete(savedOfficer.getId());

        assertNull(officerRepository.findAll());
    }

    @Test
    public void testThatWhenIModifyListFromFindAll_RepositoryIsNotAffected() {
        officerRepository.save(officer);
        officerRepository.findAll().clear();

        assertEquals(1L, officerRepository.count());
        assertEquals(1, officerRepository.findAll().size());
    }

    @Test
    public void testThatWhenIDeleteOfficer_OfficerIsDeleted() {
        Officer savedOfficer = officerRepository.save(officer);
        assertEquals(1L, officerRepository.count());
        officerRepository.delete(savedOfficer.getId());
        assertEquals(0L, officerRepository.count());
    }

    @Test
    public void testThatWhenIDeleteOneOfficer_OtherOfficersRemain() {
        Officer officerTwo = new Officer();
        Officer officerThree = new Officer();
        officerRepository.save(officer);
        officerRepository.save(officerTwo);
        officerRepository.save(officerThree);

        officerRepository.delete(officerTwo.getId());

        assertEquals(2L, officerRepository.count());
        assertTrue(officerRepository.existById(officer.getId()));
        assertFalse(officerRepository.existById(officerTwo.getId()));
        assertTrue(officerRepository.existById(officerThree.getId()));
    }

    @Test
    public void testThatWhenIDeleteOfficer_CountDecreasesByOne() {
        Officer officerTwo = new Officer();
        officerRepository.save(officer);
        officerRepository.save(officerTwo);
        officerRepository.delete(officer.getId());

        assertEquals(1L, officerRepository.count());
    }

    @Test
    public void testThatWhenIDeleteAllOfficers_AllOfficersAreDeleted() {
        officerRepository.save(officer);
        officerRepository.save(new Officer());
        officerRepository.save(new Officer());
        officerRepository.deleteAll();

        assertEquals(0L, officerRepository.count());
        assertNull(officerRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteAllOnEmptyRepository_NoErrorIsThrown() {
        assertDoesNotThrow(() -> officerRepository.deleteAll());
        assertEquals(0L, officerRepository.count());
    }

    @Test
    public void testThatWhenISaveAfterDeleteAll_IdStartsFromOneAgain() {
        officerRepository.save(officer);
        officerRepository.save(new Officer());
        officerRepository.deleteAll();

        Officer newOfficer = new Officer();
        officerRepository.save(newOfficer);

        assertEquals(1, newOfficer.getId());
        assertEquals(1L, officerRepository.count());
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueIfOfficerExists() {
        Officer savedOfficer = officerRepository.save(officer);
        assertTrue(officerRepository.existById(savedOfficer.getId()));
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsFalseIfOfficerDoesNotExist() {
        assertFalse(officerRepository.existById(-10));
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueForEachSavedOfficer() {
        Officer officerTwo = new Officer();
        Officer officerThree = new Officer();
        officerRepository.save(officer);
        officerRepository.save(officerTwo);
        officerRepository.save(officerThree);

        assertTrue(officerRepository.existById(officer.getId()));
        assertTrue(officerRepository.existById(officerTwo.getId()));
        assertTrue(officerRepository.existById(officerThree.getId()));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDelete_ReturnsFalse() {
        Officer savedOfficer = officerRepository.save(officer);
        int officerId = savedOfficer.getId();
        officerRepository.delete(officerId);

        assertFalse(officerRepository.existById(officerId));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDeleteAll_ReturnsFalse() {
        Officer savedOfficer = officerRepository.save(officer);
        int officerId = savedOfficer.getId();
        officerRepository.deleteAll();

        assertFalse(officerRepository.existById(officerId));
    }

}