package com.lastma.data.repositories;

import com.lastma.data.models.Offence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OffenceRepositoryImplTest {
    private OffenceRepositoryImpl offenceRepository;
    private Offence offence;

    @BeforeEach
    public void setUp() {
        offenceRepository = new OffenceRepositoryImpl();
        offence = new Offence();
    }

    @Test
    public void testThatWhenISaveOneOffence_CountIsOne() {
        offenceRepository.save(offence);
        assertEquals(1L, offenceRepository.count());
    }

    @Test
    public void testThatWhenISaveOffence_AndISaveSameOffence_OffenceIsUpdated() {
        Offence newOffence = offenceRepository.save(offence);
        Offence sameOffence = offenceRepository.findById(newOffence.getId());
        sameOffence.setCode("OFF-001");
        offenceRepository.save(sameOffence);

        assertEquals(1, offenceRepository.count());
        assertEquals("OFF-001", offenceRepository.findById(newOffence.getId()).getCode());
    }

    @Test
    public void testThatWhenIAddTwoOffences_CountIsTwo() {
        assertEquals(0L, offenceRepository.count());
        Offence offenceTwo = new Offence();
        offenceRepository.save(offenceTwo);
        offenceRepository.save(offence);
        assertEquals(2L, offenceRepository.count());
    }

    @Test
    public void testThatWhenISaveOffence_IdIsSet() {
        offenceRepository.save(offence);
        assertEquals(1, offence.getId());
    }

    @Test
    public void testThatWhenIFindById_OffenceIsFound() {
        Offence savedOffence = offenceRepository.save(offence);
        int offenceId = savedOffence.getId();
        assertEquals(savedOffence, offenceRepository.findById(offenceId));
    }

    @Test
    public void testThatWhenIFindByIncorrectId_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> offenceRepository.findById(-1));
    }

    @Test
    public void testThatWhenIFindAll_AllOffencesAreFound() {
        Offence offenceTwo = new Offence();
        Offence offenceThree = new Offence();
        Offence[] allOffences = {offence, offenceTwo, offenceThree};
        for (Offence item : allOffences) {
            offenceRepository.save(item);
        }
        List<Offence> foundOffences = offenceRepository.findAll();
        int count = 0;
        for (Offence item : foundOffences) {
            assertEquals(item, allOffences[count]);
            count++;
        }
    }

    @Test
    public void testThatWhenIFindAll_AndNoSavedOffences_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> offenceRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteOffence_OffenceIsDeleted() {
        Offence savedOffence = offenceRepository.save(offence);
        int offenceId = savedOffence.getId();
        assertEquals(1L, offenceRepository.count());
        offenceRepository.delete(offenceId);
        assertEquals(0L, offenceRepository.count());
    }

    @Test
    public void testThatWhenIDeleteOffenceWithInvalidId_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> offenceRepository.delete(-1));
    }

    @Test
    public void testThatWhenIDeleteAllOffences_AllOffencesAreDeleted() {
        Offence offenceTwo = new Offence();
        Offence offenceThree = new Offence();
        offenceRepository.save(offence);
        offenceRepository.save(offenceTwo);
        offenceRepository.save(offenceThree);
        offenceRepository.deleteAll();
        assertEquals(0L, offenceRepository.count());
        assertThrows(IllegalArgumentException.class, () -> offenceRepository.findAll().size());
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueIfOffenceExists() {
        Offence savedOffence = offenceRepository.save(offence);
        int offenceId = savedOffence.getId();
        assertTrue(offenceRepository.existById(offenceId));
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsFalseIfOffenceDoesNotExist() {
        assertFalse(offenceRepository.existById(-10));
    }

    @Test
    public void testThatWhenISaveTwoOffences_IdsAreIncremented() {
        Offence offenceTwo = new Offence();
        offenceRepository.save(offence);
        offenceRepository.save(offenceTwo);

        assertEquals(1, offence.getId());
        assertEquals(2, offenceTwo.getId());
    }

    @Test
    public void testThatWhenISaveOffence_SavedOffenceIsReturned() {
        Offence savedOffence = offenceRepository.save(offence);
        assertSame(offence, savedOffence);
    }

    @Test
    public void testThatWhenISaveSameOffenceTwice_CountDoesNotIncrease() {
        offenceRepository.save(offence);
        offenceRepository.save(offence);

        assertEquals(1L, offenceRepository.count());
        assertEquals(1, offence.getId());
    }

    @Test
    public void testThatWhenISaveOffenceWithExistingId_ThatOffenceIsReplaced() {
        Offence savedOffence = offenceRepository.save(offence);

        Offence replacement = new Offence();
        replacement.setId(savedOffence.getId());
        replacement.setCode("OFF-001");
        offenceRepository.save(replacement);

        assertEquals(1L, offenceRepository.count());
        assertSame(replacement, offenceRepository.findById(savedOffence.getId()));
        assertEquals("OFF-001", offenceRepository.findById(savedOffence.getId()).getCode());
    }

    @Test
    public void testThatWhenIUpdateSecondOffence_FirstOffenceIsNotAffected() {
        Offence offenceTwo = new Offence();
        offence.setCode("OFF-002");
        offenceRepository.save(offence);
        offenceRepository.save(offenceTwo);

        Offence replacement = new Offence();
        replacement.setId(offenceTwo.getId());
        replacement.setCode("OFF-003");
        offenceRepository.save(replacement);

        assertEquals(2L, offenceRepository.count());
        assertEquals("OFF-002", offenceRepository.findById(offence.getId()).getCode());
        assertEquals("OFF-003", offenceRepository.findById(offenceTwo.getId()).getCode());
    }

    @Test
    public void testThatWhenISaveAfterDeletingAOffence_NewIdIsUnique() {
        Offence offenceTwo = new Offence();
        Offence offenceThree = new Offence();
        offenceRepository.save(offence);
        offenceRepository.save(offenceTwo);
        offenceRepository.delete(offence.getId());
        offenceRepository.save(offenceThree);

        assertNotEquals(offenceTwo.getId(), offenceThree.getId());
        assertEquals(2L, offenceRepository.count());
    }

    @Test
    public void testThatWhenIFindById_CorrectOffenceIsReturnedFromMany() {
        Offence offenceTwo = new Offence();
        Offence offenceThree = new Offence();
        offenceRepository.save(offence);
        offenceRepository.save(offenceTwo);
        offenceRepository.save(offenceThree);

        assertSame(offenceTwo, offenceRepository.findById(offenceTwo.getId()));
        assertSame(offenceThree, offenceRepository.findById(offenceThree.getId()));
    }

    @Test
    public void testThatWhenIFindByIdOnEmptyRepository_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> offenceRepository.findById(1));
    }

    @Test
    public void testThatWhenIFindDeletedOffence_ErrorIsThrown() {
        Offence savedOffence = offenceRepository.save(offence);
        int offenceId = savedOffence.getId();
        offenceRepository.delete(offenceId);

        assertThrows(IllegalArgumentException.class, () -> offenceRepository.findById(offenceId));
    }

    @Test
    public void testThatWhenIFindAll_CorrectNumberOfOffencesIsReturned() {
        offenceRepository.save(offence);
        offenceRepository.save(new Offence());
        offenceRepository.save(new Offence());

        assertEquals(3, offenceRepository.findAll().size());
    }

    @Test
    public void testThatWhenIDeleteOneOffence_FindAllDoesNotContainIt() {
        Offence offenceTwo = new Offence();
        offenceRepository.save(offence);
        offenceRepository.save(offenceTwo);
        offenceRepository.delete(offence.getId());

        List<Offence> foundOffences = offenceRepository.findAll();
        assertEquals(1, foundOffences.size());
        assertFalse(foundOffences.contains(offence));
        assertTrue(foundOffences.contains(offenceTwo));
    }

    @Test
    public void testThatWhenIDeleteTheOnlyOffence_FindAllThrowsError() {
        Offence savedOffence = offenceRepository.save(offence);
        offenceRepository.delete(savedOffence.getId());

        assertThrows(IllegalArgumentException.class, () -> offenceRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteOneOffence_OtherOffencesRemain() {
        Offence offenceTwo = new Offence();
        Offence offenceThree = new Offence();
        offenceRepository.save(offence);
        offenceRepository.save(offenceTwo);
        offenceRepository.save(offenceThree);

        offenceRepository.delete(offenceTwo.getId());

        assertEquals(2L, offenceRepository.count());
        assertTrue(offenceRepository.existById(offence.getId()));
        assertFalse(offenceRepository.existById(offenceTwo.getId()));
        assertTrue(offenceRepository.existById(offenceThree.getId()));
    }

    @Test
    public void testThatWhenIDeleteSameOffenceTwice_ErrorIsThrown() {
        Offence savedOffence = offenceRepository.save(offence);
        int offenceId = savedOffence.getId();
        offenceRepository.delete(offenceId);

        assertThrows(IllegalArgumentException.class, () -> offenceRepository.delete(offenceId));
        assertEquals(0L, offenceRepository.count());
    }

    @Test
    public void testThatWhenIDeleteFromEmptyRepository_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> offenceRepository.delete(1));
    }

    @Test
    public void testThatWhenIDeleteAllOnEmptyRepository_NoErrorIsThrown() {
        assertDoesNotThrow(() -> offenceRepository.deleteAll());
        assertEquals(0L, offenceRepository.count());
    }

    @Test
    public void testThatWhenISaveAfterDeleteAll_IdStartsFromOneAgain() {
        offenceRepository.save(offence);
        offenceRepository.save(new Offence());
        offenceRepository.deleteAll();

        Offence newOffence = new Offence();
        offenceRepository.save(newOffence);

        assertEquals(1, newOffence.getId());
        assertEquals(1L, offenceRepository.count());
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueForEachSavedOffence() {
        Offence offenceTwo = new Offence();
        Offence offenceThree = new Offence();
        offenceRepository.save(offence);
        offenceRepository.save(offenceTwo);
        offenceRepository.save(offenceThree);

        assertTrue(offenceRepository.existById(offence.getId()));
        assertTrue(offenceRepository.existById(offenceTwo.getId()));
        assertTrue(offenceRepository.existById(offenceThree.getId()));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDelete_ReturnsFalse() {
        Offence savedOffence = offenceRepository.save(offence);
        int offenceId = savedOffence.getId();
        offenceRepository.delete(offenceId);

        assertFalse(offenceRepository.existById(offenceId));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDeleteAll_ReturnsFalse() {
        Offence savedOffence = offenceRepository.save(offence);
        int offenceId = savedOffence.getId();
        offenceRepository.deleteAll();

        assertFalse(offenceRepository.existById(offenceId));
    }

    @Test
    public void testThatWhenIDeleteOffence_CountDecreasesByOne() {
        Offence offenceTwo = new Offence();
        offenceRepository.save(offence);
        offenceRepository.save(offenceTwo);
        offenceRepository.delete(offence.getId());

        assertEquals(1L, offenceRepository.count());
    }
}