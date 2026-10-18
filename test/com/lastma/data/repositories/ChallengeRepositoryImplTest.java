package com.lastma.data.repositories;

import com.lastma.data.models.Challenge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeRepositoryImplTest {
    private ChallengeRepositoryImpl challengeRepository;
    private Challenge challenge;

    @BeforeEach
    public void setUp() {
        challengeRepository = new ChallengeRepositoryImpl();
        challenge = new Challenge();
    }

    @Test
    public void testThatWhenISaveOneChallenge_CountIsOne() {
        challengeRepository.save(challenge);
        assertEquals(1L, challengeRepository.count());
    }

    @Test
    public void testThatWhenISaveChallenge_AndISaveSameChallenge_ChallengeIsUpdated() {
        Challenge newChallenge = challengeRepository.save(challenge);
        Challenge sameChallenge = challengeRepository.findById(newChallenge.getId());
        sameChallenge.setReason("Wrong plate number");
        challengeRepository.save(sameChallenge);

        assertEquals(1, challengeRepository.count());
        assertEquals("Wrong plate number", challengeRepository.findById(newChallenge.getId()).getReason());
    }

    @Test
    public void testThatWhenIAddTwoChallenges_CountIsTwo() {
        assertEquals(0L, challengeRepository.count());
        Challenge challengeTwo = new Challenge();
        challengeRepository.save(challengeTwo);
        challengeRepository.save(challenge);
        assertEquals(2L, challengeRepository.count());
    }

    @Test
    public void testThatWhenISaveChallenge_IdIsSet() {
        challengeRepository.save(challenge);
        assertEquals(1, challenge.getId());
    }

    @Test
    public void testThatWhenIFindById_ChallengeIsFound() {
        Challenge savedChallenge = challengeRepository.save(challenge);
        int challengeId = savedChallenge.getId();
        assertEquals(savedChallenge, challengeRepository.findById(challengeId));
    }

    @Test
    public void testThatWhenIFindByIncorrectId_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> challengeRepository.findById(-1));
    }

    @Test
    public void testThatWhenIFindAll_AllChallengesAreFound() {
        Challenge challengeTwo = new Challenge();
        Challenge challengeThree = new Challenge();
        Challenge[] allChallenges = {challenge, challengeTwo, challengeThree};
        for (Challenge item : allChallenges) {
            challengeRepository.save(item);
        }
        List<Challenge> foundChallenges = challengeRepository.findAll();
        int count = 0;
        for (Challenge item : foundChallenges) {
            assertEquals(item, allChallenges[count]);
            count++;
        }
    }

    @Test
    public void testThatWhenIFindAll_AndNoSavedChallenges_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> challengeRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteChallenge_ChallengeIsDeleted() {
        Challenge savedChallenge = challengeRepository.save(challenge);
        int challengeId = savedChallenge.getId();
        assertEquals(1L, challengeRepository.count());
        challengeRepository.delete(challengeId);
        assertEquals(0L, challengeRepository.count());
    }

    @Test
    public void testThatWhenIDeleteChallengeWithInvalidId_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> challengeRepository.delete(-1));
    }

    @Test
    public void testThatWhenIDeleteAllChallenges_AllChallengesAreDeleted() {
        Challenge challengeTwo = new Challenge();
        Challenge challengeThree = new Challenge();
        challengeRepository.save(challenge);
        challengeRepository.save(challengeTwo);
        challengeRepository.save(challengeThree);
        challengeRepository.deleteAll();
        assertEquals(0L, challengeRepository.count());
        assertThrows(IllegalArgumentException.class, () -> challengeRepository.findAll().size());
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueIfChallengeExists() {
        Challenge savedChallenge = challengeRepository.save(challenge);
        int challengeId = savedChallenge.getId();
        assertTrue(challengeRepository.existById(challengeId));
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsFalseIfChallengeDoesNotExist() {
        assertFalse(challengeRepository.existById(-10));
    }

    @Test
    public void testThatWhenISaveTwoChallenges_IdsAreIncremented() {
        Challenge challengeTwo = new Challenge();
        challengeRepository.save(challenge);
        challengeRepository.save(challengeTwo);

        assertEquals(1, challenge.getId());
        assertEquals(2, challengeTwo.getId());
    }

    @Test
    public void testThatWhenISaveChallenge_SavedChallengeIsReturned() {
        Challenge savedChallenge = challengeRepository.save(challenge);
        assertSame(challenge, savedChallenge);
    }

    @Test
    public void testThatWhenISaveSameChallengeTwice_CountDoesNotIncrease() {
        challengeRepository.save(challenge);
        challengeRepository.save(challenge);

        assertEquals(1L, challengeRepository.count());
        assertEquals(1, challenge.getId());
    }

    @Test
    public void testThatWhenISaveChallengeWithExistingId_ThatChallengeIsReplaced() {
        Challenge savedChallenge = challengeRepository.save(challenge);

        Challenge replacement = new Challenge();
        replacement.setId(savedChallenge.getId());
        replacement.setReason("Wrong plate number");
        challengeRepository.save(replacement);

        assertEquals(1L, challengeRepository.count());
        assertSame(replacement, challengeRepository.findById(savedChallenge.getId()));
        assertEquals("Wrong plate number", challengeRepository.findById(savedChallenge.getId()).getReason());
    }

    @Test
    public void testThatWhenIUpdateSecondChallenge_FirstChallengeIsNotAffected() {
        Challenge challengeTwo = new Challenge();
        challenge.setReason("Faulty meter");
        challengeRepository.save(challenge);
        challengeRepository.save(challengeTwo);

        Challenge replacement = new Challenge();
        replacement.setId(challengeTwo.getId());
        replacement.setReason("Signage was missing");
        challengeRepository.save(replacement);

        assertEquals(2L, challengeRepository.count());
        assertEquals("Faulty meter", challengeRepository.findById(challenge.getId()).getReason());
        assertEquals("Signage was missing", challengeRepository.findById(challengeTwo.getId()).getReason());
    }

    @Test
    public void testThatWhenISaveAfterDeletingAChallenge_NewIdIsUnique() {
        Challenge challengeTwo = new Challenge();
        Challenge challengeThree = new Challenge();
        challengeRepository.save(challenge);
        challengeRepository.save(challengeTwo);
        challengeRepository.delete(challenge.getId());
        challengeRepository.save(challengeThree);

        assertNotEquals(challengeTwo.getId(), challengeThree.getId());
        assertEquals(2L, challengeRepository.count());
    }

    @Test
    public void testThatWhenIFindById_CorrectChallengeIsReturnedFromMany() {
        Challenge challengeTwo = new Challenge();
        Challenge challengeThree = new Challenge();
        challengeRepository.save(challenge);
        challengeRepository.save(challengeTwo);
        challengeRepository.save(challengeThree);

        assertSame(challengeTwo, challengeRepository.findById(challengeTwo.getId()));
        assertSame(challengeThree, challengeRepository.findById(challengeThree.getId()));
    }

    @Test
    public void testThatWhenIFindByIdOnEmptyRepository_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> challengeRepository.findById(1));
    }

    @Test
    public void testThatWhenIFindDeletedChallenge_ErrorIsThrown() {
        Challenge savedChallenge = challengeRepository.save(challenge);
        int challengeId = savedChallenge.getId();
        challengeRepository.delete(challengeId);

        assertThrows(IllegalArgumentException.class, () -> challengeRepository.findById(challengeId));
    }

    @Test
    public void testThatWhenIFindAll_CorrectNumberOfChallengesIsReturned() {
        challengeRepository.save(challenge);
        challengeRepository.save(new Challenge());
        challengeRepository.save(new Challenge());

        assertEquals(3, challengeRepository.findAll().size());
    }

    @Test
    public void testThatWhenIDeleteOneChallenge_FindAllDoesNotContainIt() {
        Challenge challengeTwo = new Challenge();
        challengeRepository.save(challenge);
        challengeRepository.save(challengeTwo);
        challengeRepository.delete(challenge.getId());

        List<Challenge> foundChallenges = challengeRepository.findAll();
        assertEquals(1, foundChallenges.size());
        assertFalse(foundChallenges.contains(challenge));
        assertTrue(foundChallenges.contains(challengeTwo));
    }

    @Test
    public void testThatWhenIDeleteTheOnlyChallenge_FindAllThrowsError() {
        Challenge savedChallenge = challengeRepository.save(challenge);
        challengeRepository.delete(savedChallenge.getId());

        assertThrows(IllegalArgumentException.class, () -> challengeRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteOneChallenge_OtherChallengesRemain() {
        Challenge challengeTwo = new Challenge();
        Challenge challengeThree = new Challenge();
        challengeRepository.save(challenge);
        challengeRepository.save(challengeTwo);
        challengeRepository.save(challengeThree);

        challengeRepository.delete(challengeTwo.getId());

        assertEquals(2L, challengeRepository.count());
        assertTrue(challengeRepository.existById(challenge.getId()));
        assertFalse(challengeRepository.existById(challengeTwo.getId()));
        assertTrue(challengeRepository.existById(challengeThree.getId()));
    }

    @Test
    public void testThatWhenIDeleteSameChallengeTwice_ErrorIsThrown() {
        Challenge savedChallenge = challengeRepository.save(challenge);
        int challengeId = savedChallenge.getId();
        challengeRepository.delete(challengeId);

        assertThrows(IllegalArgumentException.class, () -> challengeRepository.delete(challengeId));
        assertEquals(0L, challengeRepository.count());
    }

    @Test
    public void testThatWhenIDeleteFromEmptyRepository_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> challengeRepository.delete(1));
    }

    @Test
    public void testThatWhenIDeleteAllOnEmptyRepository_NoErrorIsThrown() {
        assertDoesNotThrow(() -> challengeRepository.deleteAll());
        assertEquals(0L, challengeRepository.count());
    }

    @Test
    public void testThatWhenISaveAfterDeleteAll_IdStartsFromOneAgain() {
        challengeRepository.save(challenge);
        challengeRepository.save(new Challenge());
        challengeRepository.deleteAll();

        Challenge newChallenge = new Challenge();
        challengeRepository.save(newChallenge);

        assertEquals(1, newChallenge.getId());
        assertEquals(1L, challengeRepository.count());
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueForEachSavedChallenge() {
        Challenge challengeTwo = new Challenge();
        Challenge challengeThree = new Challenge();
        challengeRepository.save(challenge);
        challengeRepository.save(challengeTwo);
        challengeRepository.save(challengeThree);

        assertTrue(challengeRepository.existById(challenge.getId()));
        assertTrue(challengeRepository.existById(challengeTwo.getId()));
        assertTrue(challengeRepository.existById(challengeThree.getId()));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDelete_ReturnsFalse() {
        Challenge savedChallenge = challengeRepository.save(challenge);
        int challengeId = savedChallenge.getId();
        challengeRepository.delete(challengeId);

        assertFalse(challengeRepository.existById(challengeId));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDeleteAll_ReturnsFalse() {
        Challenge savedChallenge = challengeRepository.save(challenge);
        int challengeId = savedChallenge.getId();
        challengeRepository.deleteAll();

        assertFalse(challengeRepository.existById(challengeId));
    }

    @Test
    public void testThatWhenIDeleteChallenge_CountDecreasesByOne() {
        Challenge challengeTwo = new Challenge();
        challengeRepository.save(challenge);
        challengeRepository.save(challengeTwo);
        challengeRepository.delete(challenge.getId());

        assertEquals(1L, challengeRepository.count());
    }
}